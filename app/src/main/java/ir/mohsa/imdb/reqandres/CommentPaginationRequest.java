package ir.mohsa.imdb.reqandres;

/**f
 * Created by 3801261697 on 05/11/2017.
 */

public class CommentPaginationRequest extends PaginationRequest {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
