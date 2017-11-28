package ir.mohsa.imdb.data;

import java.io.Serializable;

/**
 * Created by 3801261697 on 28/09/2017.
 */

public class SharedMovie implements Serializable {
    private String id;
    private String name;
    private String posterUrl;
    private String director;
    private int totalStars;
    private int rateCount;
    private String releaseDate;
    private boolean isInFavorite;

    public boolean isInFavorite() {
        return isInFavorite;
    }

    public void setInFavorite(boolean inFavorite) {
        isInFavorite = inFavorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
