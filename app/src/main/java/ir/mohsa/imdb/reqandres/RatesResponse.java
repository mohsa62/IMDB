package ir.mohsa.imdb.reqandres;

import java.util.List;

import ir.mohsa.imdb.data.Comments;

/**r
 * Created by 3801261697 on 06/11/2017.
 */

public class RatesResponse extends FailedSuccessResponse {
    private List<Comments> rates;

    public List<Comments> getRates() {
        return rates;
    }

    public void setRates(List<Comments> rates) {
        this.rates = rates;
    }
}
