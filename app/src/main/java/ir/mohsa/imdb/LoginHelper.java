package ir.mohsa.imdb;

import android.content.Context;
import android.content.SharedPreferences;

import ir.mohsa.imdb.reqandres.LoginResponse;
import ir.mohsa.imdb.reqandres.userResponse;

/**
 * Created by 3801261697 on 21/08/2017.sd
 */

public class LoginHelper {
    private final static String SharedPrefName = "login";
    private final static String TokenKey = "token";
    private final static String TokenTypeKey = "type";
    private final static String EmailKey = "email";
    private final static String ImageUri = "https://robohash.org/5a12876ee4b0fffc0ffc2ac5";

    public static void saveLoginData(Context context, LoginResponse loginResponse, String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TokenKey, loginResponse.getAccessToken());
        editor.putString(TokenTypeKey, loginResponse.getTokenType());
        editor.putString(EmailKey, email);
        editor.apply();
    }

    public static String getEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(EmailKey, null);
    }

    public static String getAccessToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(TokenKey, null);
    }

    public static String getTokenType(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(TokenTypeKey, null);
    }

    public static String getImageUri(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(ImageUri,null);
    }

    public static void saveLoginDataExtra(Context context, userResponse my_user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ImageUri, my_user.getUserInfo().getImageUri());
        editor.apply();
    }
}
