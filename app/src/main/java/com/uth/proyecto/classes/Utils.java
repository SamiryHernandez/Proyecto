package com.uth.proyecto.classes;

import static com.uth.proyecto.config.SQLiteConfig.token;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;
import com.uth.proyecto.R;
import com.uth.proyecto.config.SQLiteConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public Utils() {
    }

    public void message(View view, String msj) {
        Snackbar.make(view, msj, Snackbar.LENGTH_SHORT).show();
    }

    public String concat(@NonNull Object... args) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            result.append(args[i]);
            if (i < args.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    public String contactWithoutSpace(@NonNull Object... args) {
        StringBuilder result = new StringBuilder();

        for (Object arg : args) {
            result.append(arg);
        }

        return result.toString();
    }

    public void clearInputs(@NonNull ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                clearInputs((ViewGroup) childView);
            } else if (childView instanceof EditText) {
                ((EditText) childView).setText("");
            }
        }
    }

    public JSONObject createJson(@NonNull Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("La cantidad de argumentos debe ser par.");
        }

        JSONObject jsonBody = new JSONObject();

        try {
            for (int i = 0; i < args.length; i += 2) {
                String key = String.valueOf(args[i]);
                Object value = args[i + 1];

                if (value instanceof String || value instanceof Integer || value instanceof Double || value instanceof Boolean) {
                    jsonBody.put(key, value);
                } else {
                    throw new IllegalArgumentException("Tipo de dato no admitido: " + value.getClass());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = "Application";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(context, "1").setContentTitle(title).setContentText(message).setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, builder.build());
    }

    public void simpleNotification(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void clearToken(Context context) {
        String accesstoken = SQLiteConfig.selectToken;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(accesstoken); // assuming "accessToken" is the key used to store the token
        editor.apply();
    }


    public String getToken(Context ctx) {
        String token = "";

        try (SQLiteConnection connection = new SQLiteConnection(ctx.getApplicationContext(), null, 1);
             SQLiteDatabase db = connection.getReadableDatabase()) {

            Cursor cursor = db.rawQuery(SQLiteConfig.selectToken, null);

            if (cursor != null && cursor.moveToFirst()) {
                token = cursor.getString(0);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }



}
