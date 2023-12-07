package com.uth.proyecto.classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.uth.proyecto.config.SQLiteConfig;

public class SQLiteConnection extends SQLiteOpenHelper {
    public SQLiteConnection(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, SQLiteConfig.dbName, factory, version);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SQLiteConfig.createTblUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL(SQLiteConfig.dropTblUser);
            onCreate(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
