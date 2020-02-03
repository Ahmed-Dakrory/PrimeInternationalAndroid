package dakrory.a7med.cargomarine.helpers;
/**
 * Created by Belal on 10/5/2017.
 */

public class MyResponse {
    boolean error;
    String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}