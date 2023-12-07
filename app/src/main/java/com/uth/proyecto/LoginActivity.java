package com.uth.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uth.proyecto.classes.SQLiteConnection;
import com.uth.proyecto.classes.Utils;
import com.uth.proyecto.classes.Validator;
import com.uth.proyecto.config.ApiConfig;
import com.uth.proyecto.config.SQLiteConfig;
import com.uth.proyecto.models.LoginResponseModel;
import com.uth.proyecto.models.MeResponseModel;
import com.uth.proyecto.models.UserResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    Button btnForgot, btnLogin, btnRegister;
    EditText txtEmail, txtPassword;
    Validator validator;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isDataAvailable()) {
            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        init();

        btnLogin.setOnClickListener(this::onClickLogin);
        btnRegister.setOnClickListener(this::onClickRegister);
        btnForgot.setOnClickListener(this::onClickForgot);
    }

    public boolean isDataAvailable() {
        try (SQLiteConnection connection = new SQLiteConnection(this, null, 1);
             SQLiteDatabase db = connection.getReadableDatabase()) {

            if (isTableExists(db, SQLiteConfig.tblUser)) {
                Cursor cursor = db.rawQuery(SQLiteConfig.selectAll, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int count = cursor.getInt(0);

                    return count > 0;
                }

                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isTableExists(@NonNull SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        if (cursor != null) {
            boolean tableExists = cursor.getCount() > 0;
            cursor.close();
            return tableExists;
        }
        return false;
    }

    private void onClickForgot(View v) {
        Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
        startActivity(intent);
    }

    private void onClickRegister(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void onClickLogin(View v) {
        if (validator.isEmpty(txtEmail)) {
            utils.message(v, getString(R.string.empty_email));

            return;
        }

        if (!validator.isValidEmail(txtEmail)) {
            utils.message(v, getString(R.string.invalid_email));

            return;
        }

        if (validator.isEmpty(txtPassword)) {
            utils.message(v, getString(R.string.empty_password));

            return;
        }

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        JSONObject object = utils.createJson("email", email, "password", password);

        makeRequestApi(v, object);
    }

    protected void makeRequestApi(@NonNull View v, JSONObject object) {
        Context ctx = v.getContext();

        requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiConfig.endpointLogin, object, response -> {
            try {
                if (!response.getBoolean("status")) {
                    utils.message(v, response.getString("message"));
                    utils.clearInputs(getWindow().getDecorView().findViewById(android.R.id.content));
                    return;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LoginResponseModel loginResponseModel = LoginResponseModel.fromJson(response);
            utils.clearInputs(getWindow().getDecorView().findViewById(android.R.id.content));

            if (loginResponseModel != null) {
                boolean status = loginResponseModel.isStatus();
                String message = loginResponseModel.getMessage();
                String accessToken = loginResponseModel.getAccessToken();

                utils.message(v, message);

                if (status) {
                    getUserData(v, accessToken);
                }
            }
        }, error -> {
            utils.message(v, "Al parecer se ha producido un error.");
            Log.e("Volley Error", error.toString());
        });

        requestQueue.add(jsonObjectRequest);
    }

    protected void init() {
        // Classes
        validator = new Validator();
        utils = new Utils();

        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgot = findViewById(R.id.btnForgot);

        // EditText
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
    }

    protected void getUserData(View v, String accessToken) {
        JsonObjectRequest secondJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ApiConfig.endpointMe, null,
                response -> {
                    try {
                        if (!response.getBoolean("status")) {
                            utils.message(v, response.getString("message"));
                            utils.clearInputs(getWindow().getDecorView().findViewById(android.R.id.content));
                            return;
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    MeResponseModel meResponseModel = MeResponseModel.fromJson(response);
                    if (meResponseModel != null) {
                        boolean status = meResponseModel.isStatus();
                        UserResponseModel meDataModel = meResponseModel.getData();

                        if (status) {
                            saveData(meDataModel, accessToken);
                        }

                        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                error -> {
                    utils.message(v, "Error al obtener datos del perfil.");
                    Log.e("Volley Error", error.toString());
                }) {
            @NonNull
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        requestQueue.add(secondJsonObjectRequest);
    }

    private void saveData(@NonNull UserResponseModel meDataModel, String accessToken) {
        SQLiteConnection connection = new SQLiteConnection(getApplicationContext(), null, 1);
        SQLiteDatabase db = connection.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SQLiteConfig.id, meDataModel.getId());
        cv.put(SQLiteConfig.email, meDataModel.getEmail());
        cv.put(SQLiteConfig.token, accessToken);
        cv.put(SQLiteConfig.firstName, meDataModel.getProfile().getFirstName());
        cv.put(SQLiteConfig.lastName, meDataModel.getProfile().getLastName());
        cv.put(SQLiteConfig.countryId, meDataModel.getProfile().getCountryId());
        cv.put(SQLiteConfig.weight, meDataModel.getProfile().getWeight());
        cv.put(SQLiteConfig.photo, meDataModel.getProfile().getPhotoUrl());

        Long res = db.insert(SQLiteConfig.tblUser, SQLiteConfig.id, cv);

        Log.e("Log => Fun(saveData)", utils.concat("Registro exitoso cid:", res.toString()));

        db.close();
    }
}