package org.ncc.github.nqbot.eventsystem;

import net.mamoe.mirai.event.Event;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.utils.Utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventHub {
    private static final Set<Listener> eventListeners = ConcurrentHashMap.newKeySet();

    public static void broadcastEvent(Event event){
        for (Listener listener : eventListeners){
            Bukkit.getScheduler().runTaskAsynchronously(Utils.getPlugin(),()-> listener.onEvent(event));
        }
    }

    public static Listener getListener(String name){
        for (Listener listener : eventListeners){
            if (listener.getListenerName().equals(name)) {
                return listener;
            }
        }
        return null;
    }

    public static void registerEventListener(Listener listener){
        eventListeners.add(listener);
    }

    public static void removeListener(Listener listener){
        eventListeners.remove(listener);
    }

    public static void removeListener(String name){
        removeListener(getListener(name));
    }
}
