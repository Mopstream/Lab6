package com.mopstream.common.interaction;
import java.io.Serializable;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private static final long serialVersionUID = 3L;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    /**
     * @return Response сode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Response body.
     */
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
