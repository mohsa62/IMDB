package ir.mohsa.imdb.data;

import java.util.List;

/**f
 * Created by 3801261697 on 05/11/2017.
 */

public class Comments {
    private userInfo user;
    private int rate;
    private String text;

    public userInfo getUser() {
        return user;
    }

    public void setUser(userInfo user) {
        this.user = user;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
