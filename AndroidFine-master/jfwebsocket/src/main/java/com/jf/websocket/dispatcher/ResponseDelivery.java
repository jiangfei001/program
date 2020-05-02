package com.jf.websocket.dispatcher;

import com.jf.websocket.SocketListener;

public interface ResponseDelivery extends SocketListener {

    void addListener(SocketListener listener);

    void removeListener(SocketListener listener);

    void clear();

    boolean isEmpty();
}
