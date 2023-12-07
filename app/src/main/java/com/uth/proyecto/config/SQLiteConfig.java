package com.uth.proyecto.config;

import com.uth.proyecto.classes.Utils;

public class SQLiteConfig {
    protected static Utils utils = new Utils();

    public static final String dbName = "dbgpro001";
    public static final String tblUser = "user";

    public static final String id = "id";
    public static final String firstName = "first_name";
    public static final String lastName = "last_name";
    public static final String weight = "weight";
    public static final String email = "email";
    public static final String countryId = "country_id";
    public static final String photo = "photo";
    public static final String token = "token";

    public static final String createTblUser = utils.concat("CREATE TABLE IF NOT EXISTS", tblUser, "(", id, "INTEGER PRIMARY KEY AUTOINCREMENT,", firstName, "TEXT,", lastName, "TEXT,", weight, "FLOAT,", email, "TEXT,", countryId, "TEXT,", photo, "TEXT NULL,", token, "TEXT NULL)");

    public static final String dropTblUser = utils.concat("DROP TABLE IF EXISTS", tblUser);

    public static final String selectAll = utils.concat("SELECT COUNT(*) FROM", SQLiteConfig.tblUser);

    public static final String selectToken = utils.concat("SELECT", token, "FROM", tblUser);

}
