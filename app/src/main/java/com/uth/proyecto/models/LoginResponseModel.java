package com.uth.proyecto.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponseModel {
    private final boolean status;
    private final String message;
    private final String accessToken;

    public LoginResponseModel(boolean status, String message, String accessToken) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Nullable
    public static LoginResponseModel fromJson(@NonNull JSONObject json) {
        try {
            boolean status = json.getBoolean("status");
            String message = json.getString("message");
            String accessToken = json.getString("access_token");

            return new LoginResponseModel(status, message, accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
