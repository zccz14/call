package com.zccz14.call.event;

import java.util.EventListener;

/**
 * Created by zccz14 on 17-5-1.
 */
public interface ReceiveMessageListener extends EventListener {
    void onMessage(ReceiveMessage message);
}
