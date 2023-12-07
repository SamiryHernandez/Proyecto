package com.uth.proyecto.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.uth.proyecto.config.SQLiteConfig;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbgpro001";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de usuarios al inicializar la base de datos
        db.execSQL(SQLiteConfig.createTblUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejar la actualización de la base de datos si es necesario
        db.execSQL(SQLiteConfig.dropTblUser);
        onCreate(db);
    }

    public List<com.uth.proyecto.classes.ListElement> getAllUsers() {
        List<com.uth.proyecto.classes.ListElement> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteConfig.tblUser, null);


        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(SQLiteConfig.firstName);
            int lastNameIndex = cursor.getColumnIndex(SQLiteConfig.lastName);
            int countryIndex = cursor.getColumnIndex(SQLiteConfig.countryId);

            if (firstNameIndex >= 0 && lastNameIndex >= 0 && countryIndex >= 0) {
                do {
                    com.uth.proyecto.classes.ListElement user = new com.uth.proyecto.classes.ListElement();
                    user.setFirstName(cursor.getString(firstNameIndex));
                    user.setLastName(cursor.getString(lastNameIndex));
                    user.setCountry(cursor.getString(countryIndex));
                    // Agregar otros campos según sea necesario
                    userList.add(user);
                } while (cursor.moveToNext());
            }

            // Cerrar el cursor antes de salir del método
            cursor.close();
        }

        // Cerrar la base de datos antes de salir del método
        db.close();

        return userList;
    }
}

    /*public List<ListElement> getAllUsers() {
        List<ListElement> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener datos de usuarios y nombres de países mediante JOIN
        String query = "SELECT u.*, c." + SQLiteConfig.countryName +
                " FROM " + SQLiteConfig.tblUser + " u " +
                " JOIN " + SQLiteConfig.tblCountry + " c ON u." + SQLiteConfig.countryId + " = c." + SQLiteConfig.countryId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(SQLiteConfig.firstName);
            int lastNameIndex = cursor.getColumnIndex(SQLiteConfig.lastName);
            int countryNameIndex = cursor.getColumnIndex(SQLiteConfig.countryName);

            if (firstNameIndex >= 0 && lastNameIndex >= 0 && countryNameIndex >= 0) {
                do {
                    ListElement user = new ListElement();
                    user.setFirstName(cursor.getString(firstNameIndex));
                    user.setLastName(cursor.getString(lastNameIndex));
                    user.setCountry(cursor.getString(countryNameIndex)); // Utilizar el nombre del país
                    // Agregar otros campos según sea necesario
                    userList.add(user);
                } while (cursor.moveToNext());
            }

            // Cerrar el cursor antes de salir del método
            cursor.close();
        }

        // Cerrar la base de datos antes de salir del método
        db.close();

        return userList;
    }
}*/
