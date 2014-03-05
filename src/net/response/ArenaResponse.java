package net.response;

/**
 * @author Andreas
 * @since 09-02-14
 */
public class ArenaResponse {
    public ResponseType type;
    public Object argument;

    public ArenaResponse() {

    }

    public ArenaResponse(ResponseType type, Object arg) {
        this.type = type;
        this.argument = arg;
    }

    public ArenaResponse(ResponseType type) {
        if (type == ResponseType.PICK || type == ResponseType.CHOICES || type == ResponseType.ERROR)
            throw new IllegalArgumentException("Request type '" + type + "' must take an argument");
        this.type = type;
    }

    public enum ResponseType {
        PICK,
        CHOICES,
        START,
        STOP,
        ERROR
    }
}


//    PICK(IPickable.class);
//
//    private final Class clss;
//
//    private ArenaResponse(final Class clss) {
//        this.clss = clss;
//    }