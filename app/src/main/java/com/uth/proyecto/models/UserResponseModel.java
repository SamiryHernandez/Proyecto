package com.uth.proyecto.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserResponseModel {
    private final int id;
    private final String email;
    private final ProfileResponseModel profile;

    public UserResponseModel(int id, String email, ProfileResponseModel profile) {
        this.id = id;
        this.email = email;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ProfileResponseModel getProfile() {
        return profile;
    }

    @Nullable
    public static UserResponseModel fromJson(@NonNull JSONObject json) {
        try {
            int id = json.getInt("id");
            String email = json.getString("email");
            JSONObject jsonProfile = json.getJSONObject("profile");
            ProfileResponseModel profile = ProfileResponseModel.fromJson(jsonProfile);

            return new UserResponseModel(id, email, profile);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
