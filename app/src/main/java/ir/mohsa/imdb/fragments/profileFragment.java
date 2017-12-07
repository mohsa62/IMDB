package ir.mohsa.imdb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.LoginHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.activities.ActivityLogin;
import ir.mohsa.imdb.data.SharedMovie;
import ir.mohsa.imdb.list.EndlessAdapter;
import ir.mohsa.imdb.reqandres.MovieListResponse;
import ir.mohsa.imdb.reqandres.PaginationRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * dCreated by 3801261697 on 26/11/2017.
 */

public class profileFragment extends Fragment{
    private class MoviesViewHolder extends RecyclerView.ViewHolder {

        private SharedMovie classData;
        private TextView Movie_NameVar;
        private TextView Director_NameVar;
        private TextView Production_YearVar;
        private ImageView StarVar;
        private ImageView PosterVar;

        MoviesViewHolder(View itemView) {
            super(itemView);
            Movie_NameVar = (TextView) itemView.findViewById(R.id.Movie_Name);
            Director_NameVar = (TextView) itemView.findViewById(R.id.Director_Name);
            Production_YearVar = (TextView) itemView.findViewById(R.id.Production_Year);
            StarVar = (ImageView) itemView.findViewById(R.id.Star);
            PosterVar = (ImageView) itemView.findViewById(R.id.Poster);
        }

        private  void setContent (SharedMovie data) {
            classData = data;

            Movie_NameVar.setText(classData.getName());
            Director_NameVar.setText(classData.getDirector());
            Production_YearVar.setText(classData.getReleaseDate());
            if (classData.isInFavorite()){
                StarVar.setImageResource(R.drawable.image_pressed);
                StarVar.setTag("pressed");
            }
            else {
                StarVar.setImageResource(R.drawable.image_regular);
                StarVar.setTag("regular");
            }

            Glide.with(profileFragment.this).load(classData.getPosterUrl())
                    .placeholder(R.drawable.dogville32x).into(PosterVar);
        }

    }

    private class ProfileViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView profileName;
        private TextView profileDescription;
        private TextView profileDate;

        ProfileViewHolder(View itemView) {
            super(itemView);
            findViewItems();
        }

        private void findViewItems() {
            profilePic = (ImageView) itemView.findViewById(R.id.profile_picture);
            profileName = (TextView) itemView.findViewById(R.id.name);
            profileDescription = (TextView) itemView.findViewById(R.id.description);
            profileDate = (TextView) itemView.findViewById(R.id.CreatedAt);
        }

        private void setContent() {
            Glide.with(getActivity()).load(LoginHelper.getImageUri(getActivity()))
                    .into(profilePic);
            profileName.setText(LoginHelper.getUserName(getContext()));
            profileDescription.setText(LoginHelper.getUserDescription(getContext()));
            profileDate.setText(LoginHelper.getMyCreationDate(getContext()));
        }
    }

    private class EndlessProfileAdapter extends EndlessAdapter<RecyclerView.ViewHolder> {
        List<SharedMovie> items = new ArrayList<>();

        @Override
        public int getDataItemsCount() {
            return items.size();
        }

        @Override
        public RecyclerView.ViewHolder onDataCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.my_profile_info,parent,false);
                return new ProfileViewHolder(view);
            } else {
                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.single_view_for_recyclerview_b,parent,false);
                return new MoviesViewHolder(view);
            }
        }

        @Override
        public void onDataBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if ( holder.getItemViewType() == 0) {
                ((ProfileViewHolder)holder).setContent();
            } else if (holder.getItemViewType() == 1) {
                ((MoviesViewHolder)holder).setContent(items.get(position - 1));
            }
        }

        @Override
        protected int getLoadingViewType() {
            return R.layout.imdb_view_type_loading;
        }

        @Override
        protected void loadMore(int position, final LoadMoreCallback callback) {
            PaginationRequest requestJson = new PaginationRequest();
            requestJson.setLimit(1);
            requestJson.setSkip(position - 1);
            RequestBody body = RequestBody.create(HttpHelper.JSON,new Gson().toJson(requestJson));
            Request request = new Request.Builder()
                    .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                    .post(body)
                    .url(HttpAddresses.getUserFavoriteMovies)
                    .build();
            HttpHelper.getInstance().getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"failed to get user fav movies",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.body() != null) {
                        String responseString = response.body().string();
                        Log.e("my user fav movies",responseString);
                        final MovieListResponse userMovieList = new Gson().fromJson(responseString, MovieListResponse.class);
                        if (response.isSuccessful()) {
                            if (userMovieList.getMovies() != null) {
                                for (SharedMovie movie : userMovieList.getMovies()) {
                                    items.add(movie);
                                }
                                callback.done(userMovieList.getMovies().size());
                            } else {
                                callback.done(0);
                            }
                        } else if (response.code() == 401 || userMovieList.getCode() == 401) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Login Required", Toast.LENGTH_SHORT).show();
                                    Intent loginActivityIntent = new Intent(getActivity(), ActivityLogin.class);
                                    getActivity().finish();
                                    getActivity().startActivity(loginActivityIntent);
                                }
                            });
                            callback.done(0);
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Unknown Problem", Toast.LENGTH_SHORT).show();
                                }
                            });
                            callback.done(0);
                        }
                    }
                }
            });
        }

        @Override
        public int getDataItemViewType(int position) {
            if (position == 0) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    EndlessProfileAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 01-define layout to inflate, یک ریسایکلر ویو تنها
        View toReturn = inflater.inflate(R.layout.profile_recyclerview,container,false);
        // 02-define recycler view, همون لی اوت بالایی این بار با آی دی
        recyclerView = (RecyclerView) toReturn.findViewById(R.id.Profile_RecyclerView_toInflate);
        // 03-define layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // 04-define adapter
        adapter = new EndlessProfileAdapter();
        recyclerView.setAdapter(adapter);
        // 05-return
        return toReturn;
    }
}
