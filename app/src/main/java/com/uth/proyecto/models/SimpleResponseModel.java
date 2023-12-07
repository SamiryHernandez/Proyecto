package com.uth.proyecto.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleResponseModel {
    private boolean status;
    private String message;

    public SimpleResponseModel(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public static SimpleResponseModel fromJson(@NonNull JSONObject json) {
        try {
            boolean status = json.getBoolean("status");
            String message = json.getString("message");

            return new SimpleResponseModel(status, message);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
