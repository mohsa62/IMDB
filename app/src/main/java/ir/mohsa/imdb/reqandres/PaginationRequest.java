package ir.mohsa.imdb.reqandres;

/**d
 * Created by 3801261697 on 01/10/2017.
 */

public class PaginationRequest {
    private int skip;
    private int limit;

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
