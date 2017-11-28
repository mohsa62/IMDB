package ir.mohsa.imdb.reqandres;

import java.util.List;

import ir.mohsa.imdb.data.SharedMovie;

/**r
 * Created by 3801261697 on 02/10/2017.
 */

public class MovieListResponse extends FailedSuccessResponse {

    private List<SharedMovie> movies;

    public List<SharedMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<SharedMovie> movies) {
        this.movies = movies;
    }
}
