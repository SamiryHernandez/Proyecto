package com.uth.proyecto.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class MeResponseModel {
    private final boolean status;
    private final UserResponseModel data;

    public MeResponseModel(boolean status, UserResponseModel data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public UserResponseModel getData() {
        return data;
    }

    @Nullable
    public static MeResponseModel fromJson(@NonNull JSONObject json) {
        try {
            boolean status = json.getBoolean("status");
            JSONObject jsonData = json.getJSONObject("data");
            UserResponseModel data = UserResponseModel.fromJson(jsonData);

            return new MeResponseModel(status, data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
