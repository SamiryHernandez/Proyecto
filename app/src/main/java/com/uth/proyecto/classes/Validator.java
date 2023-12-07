package com.uth.proyecto.classes;

import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Validator {
    public Validator() {
    }

    public boolean isEmpty(@NonNull EditText editText) {
        return (editText.getText().toString().trim()).isEmpty();
    }

    public boolean isValidEmail(@NonNull EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
    }

    public boolean isNumber(@NonNull EditText editText) {
        return isNumber(editText, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public boolean isNumber(@NonNull EditText editText, double minValue, double maxValue) {
        try {
            String inputText = editText.getText().toString().trim();
            double number = Double.parseDouble(inputText);

            return number >= minValue && number <= maxValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
