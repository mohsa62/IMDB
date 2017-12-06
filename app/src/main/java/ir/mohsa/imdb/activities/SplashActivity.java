package ir.mohsa.imdb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import ir.mohsa.imdb.LoginHelper;

/**f
 * Created by 3801261697 on 20/11/2017.
 */

public class SplashActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userName = getUserName();
        if (userName != null) {
//            Log.e("login information:Token",LoginHelper.getAccessToken(this));
//            Log.e("login information:Type",LoginHelper.getTokenType(this));
            Intent intent = new Intent(this,HomeActivityIMDB.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,ActivityLogin.class);
            startActivity(intent);
        }
        finish();
    }

    private String getUserName() {
        return LoginHelper.getEmail(this);
    }
}
