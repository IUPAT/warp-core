package com.wideplay.warp.widgets.routing;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
class EventDispatchException extends RuntimeException {
    public EventDispatchException(String msg, Exception e) {
        super(msg, e);
    }
}
