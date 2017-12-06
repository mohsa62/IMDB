package ir.mohsa.imdb.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.LoginHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.reqandres.LoginRequest;
import ir.mohsa.imdb.reqandres.LoginResponse;
import ir.mohsa.imdb.reqandres.userResponse;
import ir.mohsa.imdb.data.userInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 3801261697 on 06/08/2017.sd
 */

public class ActivityLogin extends Activity{

    private EditText loginEmail;
    private EditText loginPassword;
    public static userInfo loggedInUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_constraint);
        Button loginBtn = (Button) findViewById(R.id.login_constraint_login);
        Button signUpButton = (Button) findViewById(R.id.login_constraint_register);
        loginEmail = (EditText) findViewById(R.id.login_constraint_email);
        loginPassword = (EditText) findViewById(R.id.login_constraint_password);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivityIntent = new Intent(ActivityLogin.this, SignUpActivityIMDB.class);
                finish();
                startActivity(loginActivityIntent);
            }
        });

    }
    private void loginUser(){
        final String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();
        LoginRequest requestJson = new LoginRequest();
        requestJson.setUsername(email);
        requestJson.setPassword(password);
        RequestBody body = RequestBody.create(HttpHelper.JSON, new Gson().toJson(requestJson));
        Request request = new Request.Builder()
                .post(body)
                .url(HttpAddresses.LoginAddress).build();
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Loading. Please wait...", true);
        //OkHttpClient test_client = new OkHttpClient();
        //test_client.newCall(request).enqueue();
        HttpHelper.getInstance().getClient().newCall(request).enqueue(new Callback() { //
            @Override
            public void onFailure(Call call, IOException e) {
                ActivityLogin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityLogin.this, "Login Failed", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                LoginResponse res = new Gson().fromJson(responseString, LoginResponse.class);
                if(res.isSuccess()){
                    //save login data
                    LoginHelper.saveLoginData(ActivityLogin.this, res, email);

                    //get profile information
                    RequestBody profile_request_body = RequestBody.create(HttpHelper.JSON,"");
                    Request profile_request = new Request.Builder()
                            .addHeader("Authorization",res.getTokenType() + " " + res.getAccessToken())
                            .post(profile_request_body)
                            .url(HttpAddresses.getProfile).build();
/*                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityLogin.this,"start getting the profile NOW",Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    HttpHelper.getInstance().getClient().newCall(profile_request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ActivityLogin.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityLogin.this,"profile failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String profile_response = response.body().string();
                            final userResponse my_user = new Gson().fromJson(profile_response,userResponse.class);
//                            Log.e("getProfile response",profile_response);
                            if (response.isSuccessful()){
//                                Log.e("getProfile_isSuccessful", String.valueOf(my_user.isSuccess()));
                                if (my_user.getUserInfo() != null) {
                                    LoginHelper.saveLoginDataExtra(ActivityLogin.this,my_user);
                                    loggedInUser = my_user.getUserInfo();
//                                    Log.e("getProfile public var",loggedInUser.toString());
                                }
                            }
                        }
                    });
                    //go to home page
                    Intent loginActivityIntent = new Intent(ActivityLogin.this, HomeActivityIMDB.class);
                    finish();
                    startActivity(loginActivityIntent);
                    dialog.dismiss();
                }else{
                    ActivityLogin.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityLogin.this, "Login Failed", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }
}
