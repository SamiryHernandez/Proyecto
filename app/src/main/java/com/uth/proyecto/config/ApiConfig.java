package com.uth.proyecto.config;


import static com.uth.proyecto.config.SQLiteConfig.selectToken;
import com.uth.proyecto.classes.Utils;

public class ApiConfig {
    private static final Utils utils = new Utils();
    public static String http = "http://";
    public static final String domain = "192.168.94.173:8000/";

    public static final String path = "api/";
    public static final String login = "login/";
    public static final String register = "register/";
    public static final String forgot = "forgot-password/";
    public static final String me = "me/";
    public static final String countries = "countries/";
    public static final String friend = "potencial-friends/";
    public static final String logout = "logout/";
    public static final String updateprofile = "update-profile/";
    public static final String password = "update-password/";
    public static final String endpointLogin = utils.contactWithoutSpace(http, domain, path, login);
    public static final String endpointRegister = utils.contactWithoutSpace(http, domain, path, register);
    public static final String endpointForgot = utils.contactWithoutSpace(http, domain, path, forgot);
    public static final String endpointCountries = utils.contactWithoutSpace(http, domain, path, countries);
    public static final String endpointMe = buildEndpoint(http, domain, path, me);
    public static final String endpointPotencial = buildEndpoint(http, domain, path, friend);
    public static final String endpointlogout = buildEndpoint(http, domain, path, logout);
    public static final String endpointpassword = utils.contactWithoutSpace(http, domain, path, password);
    public static final String endpointupdate = utils.contactWithoutSpace(http, domain, path, updateprofile);


    private static String buildEndpoint(String... components) {
        StringBuilder builder = new StringBuilder();
        for (String component : components) {
            if (component != null) {
                builder.append(component);
            } else {
                // Handle the case where a component is null
                // You might want to log an error or throw an exception
            }
        }
        return builder.toString();
    }
}