package com.uth.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uth.proyecto.adapters.CountryAdapter;
import com.uth.proyecto.classes.Utils;
import com.uth.proyecto.classes.Validator;
import com.uth.proyecto.config.ApiConfig;
import com.uth.proyecto.models.CountryResponseModel;
import com.uth.proyecto.models.SimpleResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    Validator validator;
    Utils utils;
    Button btnLogin, btnRegister;
    EditText txtFirstName, txtLastName, txtWeight, txtEmail, txtPassword;
    AutoCompleteTextView txtCountry;
    List<CountryResponseModel.Country> countriesList = new ArrayList<>();
    private CountryResponseModel.Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        btnLogin.setOnClickListener(this::onClickLogin);
        btnRegister.setOnClickListener(this::onClickRegister);

        getCountries();

        txtCountry.setOnItemClickListener(this::onClickItemCountry);
    }

    private void onClickItemCountry(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCountry = countriesList.get(i);
    }

    private void onClickRegister(View v) {
        if (validator.isEmpty(txtFirstName)) {
            utils.message(v, getString(R.string.empty_firstname));

            return;
        }

        if (validator.isEmpty(txtLastName)) {
            utils.message(v, getString(R.string.empty_lastname));

            return;
        }

        if (selectedCountry == null) {
            utils.message(v, getString(R.string.empty_country));

            return;
        }

        if (validator.isEmpty(txtWeight)) {
            utils.message(v, getString(R.string.empty_weight));

            return;
        }

        if (!validator.isNumber(txtWeight, 0, 300)) {
            utils.message(v, getString(R.string.invalid_weight));

            return;
        }

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

        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String weight = txtWeight.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        int country = selectedCountry.getId();

        JSONObject object = utils.createJson("first_name", firstName, "last_name", lastName, "country_id", country, "weight", weight, "email", email, "password", password);

        makeRequestApi(v, object);
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
        btnRegister = findViewById(R.id.btnRegister);

        // EditText
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtWeight = findViewById(R.id.txtWeight);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        // AutoCompleteTextView
        txtCountry = findViewById(R.id.txtCountry);
    }

    protected void makeRequestApi(@NonNull View v, JSONObject object) {
        Context ctx = v.getContext();

        requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiConfig.endpointRegister, object, response -> {

            SimpleResponseModel srm = SimpleResponseModel.fromJson(response);

            if (srm != null) {
                boolean status = srm.isStatus();
                String message = srm.getMessage();

                Log.e("Log:L fun => makeRequestApi", response.toString());

                if (!status) {
                    utils.clearInputs(getWindow().getDecorView().findViewById(android.R.id.content));
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    utils.showNotification(ctx, "Registro exitoso", message);
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

    protected void getCountries() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiConfig.endpointCountries, null, response -> {

            CountryResponseModel crm = CountryResponseModel.fromJson(response);

            if (crm != null) {
                countriesList = crm.getData();
                CountryAdapter countryAdapter = new CountryAdapter(RegisterActivity.this, countriesList);
                AutoCompleteTextView autoCompleteTextView = findViewById(R.id.txtCountry);
                autoCompleteTextView.setAdapter(countryAdapter);
            }
        }, error -> {
            Log.e("Volley Error", error.toString());
        });

        requestQueue.add(jsonObjectRequest);
    }
}