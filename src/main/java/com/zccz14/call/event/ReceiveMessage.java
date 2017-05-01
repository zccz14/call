package com.zccz14.call.event;

import java.util.EventObject;

/**
 * Server Receive Message
 */
public class ReceiveMessage extends EventObject {
    private byte[] message;

    public ReceiveMessage(Object sender, byte[] message) {
        super(sender);
        this.message = message;
    }

    public byte[] getMessage() {
        return message;
    }
}
