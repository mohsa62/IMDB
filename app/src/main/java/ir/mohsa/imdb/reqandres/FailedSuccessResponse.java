package ir.mohsa.imdb.reqandres;

/**
 * Created by 3s801261697 on 21/08/2017.s
 */

public class FailedSuccessResponse {
    private boolean success;
    private String message;
    private int code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}