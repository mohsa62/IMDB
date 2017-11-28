package ir.mohsa.imdb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.reqandres.MovieDetailRequest;
import ir.mohsa.imdb.reqandres.MovieDetailResponse;
import ir.mohsa.imdb.data.SharedMovie;
import ir.mohsa.imdb.data.userInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**f
 * Created by 3801261697 on 10/10/2017.
 */

public class MovieDetailsFragment extends Fragment {
    public static final String DATA_NAME = "data" ;
    public static final String USER_DATA = "user";

//    private SharedMovie data;
    private SharedMovie data;
    private userInfo user;
//    private TextView Comments;
    private ImageView ProfilePicture;
    private TextView profileName;
    private TextView movieName;
    private TextView releaseDate;
    private ImageView poster;
    private TextView directorName;
    private TextView producerName;
    private TextView writerName;
    private TextView cast;
    private TextView Score;
    private TextView Plot;
    private TextView Country;
    private TextView boxOffice;
    private TextView Time;
    private TextView Genre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(getArguments() != null && getArguments().getSerializable(DATA_NAME) != null ) {
            data = (SharedMovie) getArguments().getSerializable(DATA_NAME);
        }
        if(getArguments() != null && getArguments().getSerializable(USER_DATA) != null) {
            user = (userInfo) getArguments().getSerializable(USER_DATA);
        }
        View theView = inflater.inflate(R.layout.movie_details_relative,container,false);
//        Comments = (TextView) theView.findViewById(R.id.comments);

        ProfilePicture = (ImageView) theView.findViewById(R.id.profile_image_movie_detail);
        profileName = (TextView) theView.findViewById(R.id.profile_name_movie_detail);

        movieName = (TextView) theView.findViewById(R.id.Detail_Movie_Name);
        releaseDate = (TextView) theView.findViewById(R.id.Detail_Release_Date);
        poster = (ImageView) theView.findViewById(R.id.Poster);
        directorName = (TextView) theView.findViewById(R.id.Detail_Director);
        producerName = (TextView) theView.findViewById(R.id.Detail_Producer);
        writerName = (TextView) theView.findViewById(R.id.Detail_Writer);
        cast = (TextView) theView.findViewById(R.id.Movie_Player1);
        Score = (TextView) theView.findViewById(R.id.Detail_Score);
        Plot = (TextView) theView.findViewById(R.id.Detail_Plot);
        Country = (TextView) theView.findViewById(R.id.Detail_Country);
        boxOffice = (TextView) theView.findViewById(R.id.Detail_boxOffice);
        Time = (TextView) theView.findViewById(R.id.Detail_Time);
        Genre = (TextView) theView.findViewById(R.id.Detail_Genre);
        updateData();
        MovieDetailRequest DetailRequestJson = new MovieDetailRequest();
//        Log.e("data",data);
        DetailRequestJson.setId(data.getId());
        final RequestBody MovieDetailBody = RequestBody.create(HttpHelper.JSON,new Gson().toJson(DetailRequestJson));
        Request MovieDetailRequest = new Request.Builder()
                .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                .post(MovieDetailBody)
                .url(HttpAddresses.getMovie).build();
        HttpHelper.getInstance().getClient().newCall(MovieDetailRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Cannot Get Movie Details",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String MovieDetailsResponseString = response.body().string();
                MovieDetailResponse DetailRes = new Gson().fromJson(MovieDetailsResponseString,MovieDetailResponse.class);
                updateDetailedData(DetailRes);
            }
        });
        return theView;
    }

    private void updateDetailedData(final MovieDetailResponse detailData) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                producerName.setText(detailData.getProduction());
                writerName.setText(detailData.getWriter());
//                cast.setText((CharSequence) detailData.getActors());
//                Score.setText(detailData.getTotalStars());
                Plot.setText(detailData.getPlot());
//                Log.e("plot",detailData.getPlot());
                Country.setText(detailData.getCountry());
                boxOffice.setText(detailData.getBoxOffice());
                Time.setText(detailData.getRunTime());
                Genre.setText(detailData.getGenre());
                movieName.setText(detailData.getName());
                releaseDate.setText(detailData.getReleaseDate());
                Glide.with(getContext()).load(detailData.getPosterUrl())
                        .placeholder(R.drawable.dogville32x)
                        .fitCenter()
                        .into(poster);
                directorName.setText(detailData.getDirector());
            }
        });

    }

    public void updateData() {
//        Log.e("data", String.valueOf(data));
        if (data == null){
            return;
        }
//        Log.e("profile inside fragment",ActivityLogin.loggedInUser.toString());
//        if (ActivityLogin.loggedInUser != null) {
        if (user != null) {
//            Glide.with(this).load(ActivityLogin.loggedInUser.getImageUri())
            Glide.with(this).load(user.getImageUri())
                    .placeholder(R.drawable.profile_place_holder)
                    .into(ProfilePicture);
            profileName.setText(user.getFullName());
        }
//        profileName.setText(ActivityLogin.loggedInUser.getFullName());
    }

}
