package ir.mohsa.imdb.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import ir.mohsa.imdb.fragments.MovieDetailsCommentsFragment;
import ir.mohsa.imdb.fragments.MovieDetailsFragment;
import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.reqandres.FailedSuccessResponse;
import ir.mohsa.imdb.data.SharedMovie;
import ir.mohsa.imdb.data.myRate;
import ir.mohsa.imdb.data.userInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**f
 * Created by 3801261697 on 31/10/2017.
 */

public class MovieDetailsActivity extends FragmentActivity {
    private EditText myComment;
    private ImageView off;
    private ImageView quarter;
    private ImageView half;
    private ImageView threequarter;
    private ImageView full;
    private int chosenRate = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
//        SharedPreferences MovIDPref = this.getSharedPreferences("MovieDATA",MODE_PRIVATE);
        final SharedMovie MovieID = (SharedMovie) getIntent().getSerializableExtra("Movie");
        userInfo UserID = (userInfo) getIntent().getSerializableExtra("User");

        FragmentTransaction MovieDetailsTransaction = getSupportFragmentManager().beginTransaction();
        Fragment movDetailsFrag = new MovieDetailsFragment();
        Bundle extra = new Bundle();
//        extra.putSerializable(MovieDetailsFragment.DATA_NAME,MovIDPref.getString("MovieID",null));
        extra.putSerializable(MovieDetailsFragment.DATA_NAME,MovieID);
        extra.putSerializable(MovieDetailsFragment.USER_DATA,UserID);
        movDetailsFrag.setArguments(extra);
        MovieDetailsTransaction.replace(R.id.MovieDetails,movDetailsFrag);
        MovieDetailsTransaction.commit();

//        comments recycler view
        FragmentTransaction MovieDetailsCommentsTransaction = getSupportFragmentManager().beginTransaction();
        Fragment movDetailsCommentsFrag = new MovieDetailsCommentsFragment();
        Bundle extraComments = new Bundle();
        extraComments.putString("movieidcom",MovieID.getId());
        movDetailsCommentsFrag.setArguments(extraComments);
        MovieDetailsCommentsTransaction.replace(R.id.MovieDetailsComments,movDetailsCommentsFrag);
        MovieDetailsCommentsTransaction.commit();
        myComment = (EditText) findViewById(R.id.myComment);
        Button sendBtn = (Button) findViewById(R.id.SendButton);
        off = (ImageView) findViewById(R.id.off);
        quarter = (ImageView) findViewById(R.id.quarter);
        half = (ImageView) findViewById(R.id.half);
        threequarter = (ImageView) findViewById(R.id.threequarter);
        full = (ImageView) findViewById(R.id.full);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRate myRate = new myRate();
                myRate.setMovieId(MovieID.getId());
                myRate.setText(myComment.getText().toString());
                myRate.setRate(chosenRate);
                RequestBody myRateBody = RequestBody.create(HttpHelper.JSON,new Gson().toJson(myRate));
                Request myRateRequest = new Request.Builder()
                        .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(MovieDetailsActivity.this))
                        .post(myRateBody)
                        .url(HttpAddresses.addRating).build();
                HttpHelper.getInstance().getClient().newCall(myRateRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MovieDetailsActivity.this,"Cannot send the rating",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String sendRateResponse = response.body().string();
                        FailedSuccessResponse sendRateResponseJson = new Gson().fromJson(sendRateResponse,FailedSuccessResponse.class);
                        if (sendRateResponseJson.isSuccess()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MovieDetailsActivity.this, "your rate and comment sent", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }
                });
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRate = 1;
                quarter.setVisibility(View.INVISIBLE);
                half.setVisibility(View.INVISIBLE);
                threequarter.setVisibility(View.INVISIBLE);
                full.setVisibility(View.INVISIBLE);
            }
        });
        quarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRate = 2;
                off.setVisibility(View.INVISIBLE);
                half.setVisibility(View.INVISIBLE);
                threequarter.setVisibility(View.INVISIBLE);
                full.setVisibility(View.INVISIBLE);
            }
        });
        half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRate = 3;
                off.setVisibility(View.INVISIBLE);
                quarter.setVisibility(View.INVISIBLE);
                threequarter.setVisibility(View.INVISIBLE);
                full.setVisibility(View.INVISIBLE);
            }
        });
        threequarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRate = 4;
                off.setVisibility(View.INVISIBLE);
                half.setVisibility(View.INVISIBLE);
                quarter.setVisibility(View.INVISIBLE);
                full.setVisibility(View.INVISIBLE);
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRate = 5;
                off.setVisibility(View.INVISIBLE);
                half.setVisibility(View.INVISIBLE);
                threequarter.setVisibility(View.INVISIBLE);
                quarter.setVisibility(View.INVISIBLE);
            }
        });
    }
}
