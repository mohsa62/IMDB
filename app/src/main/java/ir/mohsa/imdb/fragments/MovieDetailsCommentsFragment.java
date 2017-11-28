package ir.mohsa.imdb.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mohsa.imdb.activities.ActivityLogin;
import ir.mohsa.imdb.HttpAddresses;
import ir.mohsa.imdb.HttpHelper;
import ir.mohsa.imdb.R;
import ir.mohsa.imdb.reqandres.CommentPaginationRequest;
import ir.mohsa.imdb.reqandres.RatesResponse;
import ir.mohsa.imdb.data.Comments;
import ir.mohsa.imdb.list.EndlessAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**3
 * Created by 3801261697 on 05/11/2017.
 */

public class MovieDetailsCommentsFragment extends Fragment{
    public RecyclerView comentsRecyclerView;
    public RecyclerView.LayoutManager commentsLayoutManager;
    public RecyclerView.Adapter commentsAdapter;

    private String commentsMovieID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null && getArguments().getString("movieidcom") != null ) {
            commentsMovieID = getArguments().getString("movieidcom");
        }
        View toReturn = inflater.inflate(R.layout.comments_recyclerview,container,false);
        this.comentsRecyclerView = (RecyclerView) toReturn.findViewById(R.id.Comments_RecyclerView_toInflate);
        commentsLayoutManager = new LinearLayoutManager(this.getContext());
        this.comentsRecyclerView.setLayoutManager(commentsLayoutManager);
        commentsAdapter = new EndlessCommentsAdapter();
        this.comentsRecyclerView.setAdapter(commentsAdapter);
        return toReturn;
    }

    private class EndlessCommentsAdapter extends EndlessAdapter<CommentsItemViewHolder> {
        List<Comments> commentItems = new ArrayList<>();
        @Override
        public int getDataItemsCount() {
            return commentItems.size();
        }

        @Override
        public CommentsItemViewHolder onDataCreateViewHolder(ViewGroup parent, int viewType) {
            View theCommetnsView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_view_comments_recyclerview,parent,false);
            return new CommentsItemViewHolder(theCommetnsView);
        }

        @Override
        public void onDataBindViewHolder(CommentsItemViewHolder holder, int position) {
            holder.setContent(commentItems.get(position));
        }

        @Override
        protected int getLoadingViewType() {
            return R.layout.imdb_view_type_loading;
        }

        @Override
        protected void loadMore(int position, final LoadMoreCallback callback) {
            CommentPaginationRequest pageCommentsJsonReq = new CommentPaginationRequest();
            pageCommentsJsonReq.setLimit(5);
            pageCommentsJsonReq.setSkip(position);
            pageCommentsJsonReq.setId(commentsMovieID);
            RequestBody paginCommentsReqBody = RequestBody.create(HttpHelper.JSON,new Gson().toJson(pageCommentsJsonReq));
            Request paginRequest = new Request.Builder()
                    .addHeader("Authorization",HttpHelper.getInstance().getLoginHeader(getContext()))
                    .post(paginCommentsReqBody)
                    .url(HttpAddresses.getMovieRatings).build();
            HttpHelper.getInstance().getClient().newCall(paginRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"failed to get comments",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String commentsResponseString = response.body().string();
                    RatesResponse CommentsRes_toClass = new Gson()
                            .fromJson(commentsResponseString,RatesResponse.class);
                    if(response.isSuccessful()){
                        if (CommentsRes_toClass.getRates() != null) {
                            for (Comments Comment : CommentsRes_toClass.getRates()) {
                                commentItems.add(Comment);
                            }
                            callback.done(CommentsRes_toClass.getRates().size());
                        }else{
                            callback.done(0);
                        }
                    }else if(response.code() == 401 || CommentsRes_toClass.getCode() == 401) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Login Required",Toast.LENGTH_SHORT).show();
                                Intent loginActivityIntent = new Intent(getActivity(),ActivityLogin.class);
                                getActivity().finish();
                                getActivity().startActivity(loginActivityIntent);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Unknown Problem", Toast.LENGTH_SHORT).show();
                            }
                        });
                        callback.done(0);
                    }
                }
            });
        }
    }

    private class CommentsItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private ImageView heartRating;
        private TextView userComment;

        private Comments classData;

        public CommentsItemViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.profile_image_comments);
            userName = (TextView) itemView.findViewById(R.id.profile_name_comments);
            heartRating = (ImageView) itemView.findViewById(R.id.Heart_Rating_comments);
            userComment = (TextView) itemView.findViewById(R.id.comments_commentsRecyclerView);


        }
        private void setContent(Comments data) {
            classData = data;

            Glide.with(MovieDetailsCommentsFragment.this).load(classData.getUser().getImageUri())
                    .placeholder(R.drawable.profile_place_holder).into(userImage);
            userName.setText(classData.getUser().getFullName());
            userComment.setText(classData.getText());
            switch (classData.getRate()) {
                case 1:
                    heartRating.setImageResource(R.drawable.heart_off);
                    break;
                case 2:
                    heartRating.setImageResource(R.drawable.heart_quarter_icon);
                    break;
                case 3:
                    heartRating.setImageResource(R.drawable.heart_half_icon);
                    break;
                case 4:
                    heartRating.setImageResource(R.drawable.heart_icon_3quarter);
                    break;
                case 5:
                    heartRating.setImageResource(R.drawable.heart_icon);
                    break;
                default:
                    heartRating.setImageResource(R.drawable.heart_off);
            }
        }
    }
}
