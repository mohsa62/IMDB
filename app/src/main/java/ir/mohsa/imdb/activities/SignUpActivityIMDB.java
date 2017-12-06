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
import ir.mohsa.imdb.reqandres.LoginResponse;
import ir.mohsa.imdb.reqandres.SignupRequest;
import ir.mohsa.imdb.reqandres.userResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static ir.mohsa.imdb.activities.ActivityLogin.loggedInUser;

/**f
 * Created by 3801261697 on 21/08/2017.
 */

public class SignUpActivityIMDB extends Activity {
    Button loginButton;
    Button signUpButton;
    EditText email;
    EditText fullName;
    EditText profile_description;
    EditText password;
    EditText passwordRepeat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpButton = (Button) findViewById(R.id.signup_submit);
        loginButton = (Button) findViewById(R.id.signup_login);
        email = (EditText) findViewById(R.id.signup_email);
        fullName = (EditText) findViewById(R.id.signup_name);
        profile_description = (EditText) findViewById(R.id.signup_description);
        password = (EditText) findViewById(R.id.signup_password);
        passwordRepeat = (EditText) findViewById(R.id.signnup_password_repeat);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginActivityIntent = new Intent(SignUpActivityIMDB.this, ActivityLogin.class);
                finish();
                startActivity(LoginActivityIntent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });
    }

    private void signupUser() {
        final String passwords = password.getText().toString();
        final String passwordsrepeat = passwordRepeat.getText().toString();
        final String emails = email.getText().toString();
        final String fullNames = fullName.getText().toString();
        final String prof_description = profile_description.getText().toString();

        if(!passwords.equals(passwordsrepeat)){
            Toast.makeText(this,"PasswordMismatch",Toast.LENGTH_SHORT).show();
            return;
        }
        SignupRequest requestJson = new SignupRequest();
        requestJson.setEmail(emails);
        requestJson.setFullName(fullNames);
        requestJson.setDescription(prof_description);
        requestJson.setPassword(passwords);
        RequestBody body = RequestBody.create(HttpHelper.JSON,new Gson().toJson(requestJson));
        Request request = new Request.Builder().url(HttpAddresses.RegisterAddress).post(body).build();
//        Log.e("Sending2...", body.toString());
        final ProgressDialog dialog = ProgressDialog.show(this,"sending data...","Please wait...",true);
        HttpHelper.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SignUpActivityIMDB.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignUpActivityIMDB.this,"Connection Error",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseString = response.body().string();
//                Log.e("Error",ResponseString);
                final LoginResponse theResponse = new Gson().fromJson(ResponseString,LoginResponse.class);
                if (theResponse.isSuccess()){
                    LoginHelper.saveLoginData(SignUpActivityIMDB.this,theResponse,emails);
                    RequestBody profile_request_body = RequestBody.create(HttpHelper.JSON,"");
                    Request profile_request = new Request.Builder()
                            .addHeader("Authorization",theResponse.getTokenType() + " " + theResponse.getAccessToken())
                            .post(profile_request_body)
                            .url(HttpAddresses.getProfile).build();
                    HttpHelper.getInstance().getClient().newCall(profile_request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            SignUpActivityIMDB.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUpActivityIMDB.this,"profile failed",Toast.LENGTH_SHORT).show();
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
                                    LoginHelper.saveLoginDataExtra(SignUpActivityIMDB.this,my_user);
                                    loggedInUser = my_user.getUserInfo();
//                                    Log.e("getProfile public var",loggedInUser.toString());
                                }
                            }
                        }
                    });
                    Intent SignupActivityIntent = new Intent(SignUpActivityIMDB.this,HomeActivityIMDB.class);
                    finish();
                    startActivity(SignupActivityIntent);
                    dialog.dismiss();
                }
                else {
//                    Log.e("sign up response;",ResponseString);
                    SignUpActivityIMDB.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUpActivityIMDB.this,"the with error Sign Up Failed!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}