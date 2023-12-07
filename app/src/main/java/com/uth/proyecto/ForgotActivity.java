package com.uth.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uth.proyecto.classes.Utils;
import com.uth.proyecto.classes.Validator;
import com.uth.proyecto.config.ApiConfig;
import com.uth.proyecto.models.SimpleResponseModel;

import org.json.JSONObject;

public class ForgotActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    Validator validator;
    Utils utils;
    Button btnForgot, btnLogin;
    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        init();

        btnLogin.setOnClickListener(this::onClickLogin);
        btnForgot.setOnClickListener(this::onClickForgot);
    }

    private void onClickForgot(View v) {
        if (validator.isEmpty(txtEmail)) {
            utils.message(v, getString(R.string.empty_email));

            return;
        }

        if (!validator.isValidEmail(txtEmail)) {
            utils.message(v, getString(R.string.invalid_email));

            return;
        }

        String email = txtEmail.getText().toString().trim();

        JSONObject object = utils.createJson("email", email);

        makeRequestApi(v, object);
    }

    protected void makeRequestApi(@NonNull View v, JSONObject object) {
        Context ctx = v.getContext();

        requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiConfig.endpointForgot, object, response -> {

            SimpleResponseModel srm = SimpleResponseModel.fromJson(response);

            if (srm != null) {
                boolean status = srm.isStatus();
                String message = srm.getMessage();

                Log.e("Log:L fun => makeRequestApi", response.toString());

                if (!status) {
                    utils.clearInputs(getWindow().getDecorView().findViewById(android.R.id.content));

                    utils.message(v, message);

                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    utils.showNotification(ctx, "Correo de recuperación", message);
                } else utils.simpleNotification(ctx, message);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, error -> {
            utils.message(v, "Al parecer se ha producido un error.");
            Log.e("Volley Error", error.toString());
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void onClickLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    protected void init() {
        // Classes
        validator = new Validator();
        utils = new Utils();

        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnForgot = findViewById(R.id.btnForgot);

        // EditText
        txtEmail = findViewById(R.id.txtEmail);
    }
}