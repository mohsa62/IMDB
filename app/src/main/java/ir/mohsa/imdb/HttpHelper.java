package ir.mohsa.imdb;

import android.content.Context;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by 3801261697 on 06/08/2017.s
 */

public class HttpHelper {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static HttpHelper _instance;

    private OkHttpClient client;
    private HttpHelper(){
        client = new OkHttpClient();
    }
    public static HttpHelper getInstance(){
        if(_instance == null){
            _instance = new HttpHelper();
        }
        return _instance;
    }

    public OkHttpClient getClient(){
        return client;
    }

    public String getLoginHeader(Context context){
        return LoginHelper.getTokenType(context) + " " + LoginHelper.getAccessToken(context);
    }

}
