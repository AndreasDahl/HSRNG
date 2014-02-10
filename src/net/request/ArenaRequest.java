package net.request;

/**
 * Created by Andreas on 06-02-14.
 */
public class ArenaRequest {
    public RequestType type;
    public Object argument;

    public ArenaRequest() {

    }

    public ArenaRequest(RequestType type, Object arg) {
        this.type = type;
        this.argument = arg;
    }

    public ArenaRequest(RequestType type) {
        if (type == RequestType.PICK || type == RequestType.BAN)
            throw new IllegalArgumentException("Request type must take an argument");
        this.type = type;
    }

    public enum RequestType {
        PICK,
        BAN,
        READY,
        UPDATE
    }
}
