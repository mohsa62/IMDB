package ir.mohsa.imdb.reqandres;

import ir.mohsa.imdb.data.userInfo;

/**
 * Created by 3801261697 won 21/10/2017.
 */

public class userResponse extends FailedSuccessResponse{
    private userInfo userInfo;

    public userInfo getUserInfo() {
        return userInfo;
    }

/*    public void setUserInfo(userInfo userInfo) {
        this.userInfo = userInfo;
    }*/
}
