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

import ir.mohsa.imdb.activities.ActivityLogin;
import ir.mohsa.imdb.reqandres.MovieListResponse;
import ir.mohsa.imdb.reqandres.PaginationRequest;
import ir.mohsa.imdb.list.EndlessAdapter;
import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.reqandres.MovieDetailRequest;
import ir.mohsa.imdb.activities.MovieDetailsActivity;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.data.SharedMovie;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 3801261697 on 26/09/2017.sd
 */

public class IMDBimageListFragment extends Fragment{
    class IMDBItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView PosterVar;
        private TextView Movie_NameVar;
        private TextView Director_NameVar;
        private TextView Movie_ScoreVar;
        private ImageView StarVar;
        private ImageView HeartVar;
        private TextView Production_YearVar;
        private SharedMovie classData;




        public IMDBItemViewHolder(View itemView) {
            super(itemView);
            //findViewItems(); //you can have a function to to that too
            PosterVar = (ImageView) itemView.findViewById(R.id.Poster);
            Movie_NameVar = (TextView) itemView.findViewById(R.id.Movie_Name);
            Director_NameVar = (TextView) itemView.findViewById(R.id.Director_Name);
            Movie_ScoreVar = (TextView) itemView.findViewById(R.id.Movie_Score);
            StarVar = (ImageView) itemView.findViewById(R.id.Star);
            HeartVar = (ImageView) itemView.findViewById(R.id.Heart);
            Production_YearVar = (TextView) itemView.findViewById(R.id.Production_Year);
        }

        private void setContent(SharedMovie data){
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

            Glide.with(IMDBimageListFragment.this).load(classData.getPosterUrl())
                    .placeholder(R.drawable.dogville32x).into(PosterVar);
        }

        private void setListeners(){
            View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent MovieDetailIntent = new Intent(getActivity(),MovieDetailsActivity.class);
//                    SharedPreferences MovieIDPref = getActivity().getSharedPreferences("MovieDATA", HomeActivityIMDB.MODE_PRIVATE);
//                    SharedPreferences.Editor MIDedit = MovieIDPref.edit();
//                    MIDedit.putString("MovieID",classData.getId());
//                    MIDedit.commit();
//                    MovieDetailIntent.putExtra("MovieID",classData.getId());
                    MovieDetailIntent.putExtra("Movie",classData);
//                    MovieDetailIntent.putExtra("UserID",ActivityLogin.loggedInUser.getUserId());
                    MovieDetailIntent.putExtra("User",ActivityLogin.loggedInUser);
                    startActivity(MovieDetailIntent);
/*                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = new MovieDetailsFragment();
                    Bundle extra = new Bundle();
                    extra.putSerializable(MovieDetailsFragment.DATA_NAME, classData);
                    fragment.setArguments(extra);
                    transaction.replace(R.id.Home_Fragment_ContainerIMDB,fragment);
                    transaction.addToBackStack("MovieDetail");
                    transaction.commit();*/
                }
            };
            View.OnClickListener favioriteListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDetailRequest addtofavjson = new MovieDetailRequest();
                    addtofavjson.setId(classData.getId());
                    RequestBody addtofavbody = RequestBody.create(HttpHelper.JSON,new Gson().toJson(addtofavjson));
                    if (StarVar.getTag().equals("regular")) {
                        StarVar.setImageResource(R.drawable.image_pressed);
                        StarVar.setTag("pressed");
                        Request addtofavreq = new Request.Builder()
                                .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                                .post(addtofavbody)
                                .url(HttpAddresses.addToFavorite).build();
                        HttpHelper.getInstance().getClient().newCall(addtofavreq).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"Failed to add to Favorites",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("add to favorites",response.toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"added to Favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    else if (StarVar.getTag().equals("pressed")) {
                        StarVar.setImageResource(R.drawable.image_regular);
                        StarVar.setTag("regular");
                        Request addtofavreq = new Request.Builder()
                                .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                                .post(addtofavbody)
                                .url(HttpAddresses.removeFromFavorite).build();
                        HttpHelper.getInstance().getClient().newCall(addtofavreq).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"Failed to remove from Favorites",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("add to favorites",response.toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"removed from Favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                }
            };
            PosterVar.setOnClickListener(listener);
            Movie_NameVar.setOnClickListener(listener);
            Director_NameVar.setOnClickListener(listener);
            Production_YearVar.setOnClickListener(listener);
            Movie_ScoreVar.setOnClickListener(listener);
            StarVar.setOnClickListener(favioriteListener);
        }
    }

    class EndlessIMDBAdapter extends EndlessAdapter<IMDBItemViewHolder>{
        List<SharedMovie> items = new ArrayList<>();

        @Override
        public int getDataItemsCount() {
            return items.size();
        }

        @Override
        public IMDBItemViewHolder onDataCreateViewHolder(ViewGroup parent, int viewType) {
            View theView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_view_for_recyclerview,parent,false);
            return new IMDBItemViewHolder(theView);
        }

        @Override
        protected int getLoadingViewType() {
            return R.layout.imdb_view_type_loading;
        }

        @Override
        public void onDataBindViewHolder(IMDBItemViewHolder holder, int position) {
            holder.setContent(items.get(position));
            holder.setListeners();
        }

        @Override
        protected void loadMore(int position, final LoadMoreCallback callback) {
            PaginationRequest paginJsonRequest = new PaginationRequest();
            paginJsonRequest.setLimit(5);
            paginJsonRequest.setSkip(position);
            RequestBody paginReqBody = RequestBody.create(HttpHelper.JSON,new Gson().toJson(paginJsonRequest));
            Request paginRequest = new Request.Builder()
                    .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                    .post(paginReqBody)
                    .url(HttpAddresses.movieList).build();
            Log.e("in load more header:",HttpHelper.getInstance().getLoginHeader(getContext()));
            HttpHelper.getInstance().getClient().newCall(paginRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Unknown Problem in load more",Toast.LENGTH_SHORT).show();
                            callback.done(0);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
//                    Log.e("response",response.code()+responseString);
                    final MovieListResponse response_to_class = new Gson()
                            .fromJson(responseString,MovieListResponse.class);
                    //&& response_to_class.isSuccess()
                    if(response.isSuccessful()){
/*                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),String.valueOf(response_to_class.isSuccess()),Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        if(response_to_class.getMovies() != null) {
                            for (SharedMovie movie : response_to_class.getMovies()) {
                                items.add(movie);
                            }
                            callback.done(response_to_class.getMovies().size());
                        }else{
                            callback.done(0);
                        }
                    }else if(response.code() == 401 || response_to_class.getCode() == 401){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Login Required",Toast.LENGTH_SHORT).show();
                                Intent loginActivityIntent = new Intent(getActivity(), ActivityLogin.class);
                                getActivity().finish();
                                getActivity().startActivity(loginActivityIntent);
                            }
                        });
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Unknown Problem",Toast.LENGTH_SHORT).show();
                            }
                        });
                        callback.done(0);
                    }
                }
            });
        }
    }

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 01-define layout to inflate
        View toReturn = inflater.inflate(R.layout.fragment_list_recyclerview,container,false);
        // 02-define recycler view
        this.recyclerView = (RecyclerView) toReturn.findViewById(R.id.recycler_view_to_inflate);
        // 03-define layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setLayoutManager(layoutManager);
        // 04-define adapter
        adapter = new EndlessIMDBAdapter();
        this.recyclerView.setAdapter(adapter);
        // 05-return
        return toReturn;
    }
}
