package ir.mohsa.imdb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.list.EndlessAdapter;
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
    class MoviesViewHolder extends RecyclerView.ViewHolder {

        public MoviesViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView profileName;
        private TextView profileDescription;
        private TextView profileDate;

        public ProfileViewHolder(View itemView) {
            super(itemView);

        }

        private void findViewItems() {
            profilePic = (ImageView) itemView.findViewById(R.id.profile_picture);
            profileName = (TextView) itemView.findViewById(R.id.name);
            profileDescription = (TextView) itemView.findViewById(R.id.description);
            profileDate = (TextView) itemView.findViewById(R.id.CreatedAt);
        }

        private void setContent() {

        }
    }

    class EndlessProfileAdapter extends EndlessAdapter<RecyclerView.ViewHolder> {

        @Override
        public int getDataItemsCount() {
            return 0;
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


        }

        @Override
        protected int getLoadingViewType() {
            return R.layout.imdb_view_type_loading;
        }

        @Override
        protected void loadMore(int position, LoadMoreCallback callback) {
            PaginationRequest requestJson = new PaginationRequest();
            requestJson.setLimit(5);
            requestJson.setSkip(position);
            RequestBody body = RequestBody.create(HttpHelper.JSON,new Gson().toJson(requestJson));
            Request request = new Request.Builder()
                    .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                    .post(body)
                    .url(HttpAddresses.getUserFavoriteMovies)
                    .build();
            HttpHelper.getInstance().getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

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
