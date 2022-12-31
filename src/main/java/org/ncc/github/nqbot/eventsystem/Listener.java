package org.ncc.github.nqbot.eventsystem;

import net.mamoe.mirai.event.Event;

public interface Listener {
    void onEvent(Event botEvent);
    String getListenerName();
}
