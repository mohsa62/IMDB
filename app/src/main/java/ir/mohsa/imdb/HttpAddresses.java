package ir.mohsa.imdb;

/**
 * Created by 3801261697 on 21/08/2017.s
 */

public class HttpAddresses {

        public final static String CloudCodeId = "59f6fc86e4b0edcca35ac141";
        public final static String StorageId = "59f6fcade4b03ffa034b018f";

        public final static String BacktoryAddress = "http://api.backtory.com/lambda/";
        public final static String UploadAddress = "http://storage.backtory.com/files";

        public final static String BaseAddress = BacktoryAddress + CloudCodeId + "/";

        public final static String RegisterAddress = BaseAddress + "registerUser";
        public final static String LoginAddress = BaseAddress + "login";
        public final static String NewImage = BaseAddress + "new";
        public final static String movieList = BaseAddress + "movieList";
        public final static String ProfileImages = BaseAddress + "userResponse.pictures";
        public final static String getProfile = BaseAddress + "getProfile";
        public final static String getMovie = BaseAddress + "getMovie";
        public final static String addToFavorite = BaseAddress + "addToFavorite";
        public final static String removeFromFavorite = BaseAddress + "removeFromFavorite";
        public final static String getMovieRatings = BaseAddress + "getMovieRatings";
        public final static String addRating = BaseAddress + "addRating";
        public final static String getUserFavoriteMovies = BaseAddress + "getUserFavoriteMovies";
}
