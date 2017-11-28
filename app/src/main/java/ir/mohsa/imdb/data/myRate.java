package ir.mohsa.imdb.data;

/**
 * Created by 3801261697 on 07/11/2017.
 */

public class myRate {
    private String movieId;
    private String text;
    private int rate;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
