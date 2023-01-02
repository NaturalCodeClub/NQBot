package org.ncc.github.nqbot.eventsystem;

import net.mamoe.mirai.event.Event;
import org.bukkit.Bukkit;
import org.ncc.github.nqbot.utils.CoreUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件主线
 * 这里的相似事件会因为多个机器人而被多次调用，请自行回避多次调用的情况
 */
public class EventHub {
    private static final Set<Listener> eventListeners = ConcurrentHashMap.newKeySet();

    /**
     * 传入一个事件
     * @param event 事件
     */
    public static void broadcastEvent(Event event){
        for (Listener listener : eventListeners){
            Bukkit.getScheduler().runTaskAsynchronously(CoreUtils.getPlugin(),()-> listener.onEvent(event));
        }
    }

    /**
     * 通过名字获取一个监听器
     * @param name 名字
     * @return 指定监听器
     */
    public static Listener getListener(String name){
        for (Listener listener : eventListeners){
            if (listener.getListenerName().equals(name)) {
                return listener;
            }
        }
        return null;
    }

    /**
     * 注册监听器
     * @param listener 要被注册的监听器
     */
    public static void registerEventListener(Listener listener){
        eventListeners.add(listener);
    }

    /**
     * 删除监听器
     * @param listener 指定监听器
     */
    public static void removeListener(Listener listener){
        eventListeners.remove(listener);
    }

    /**
     * 通过名字删除监听器
     * @param name 指定监听器的名字
     */
    public static void removeListener(String name){
        removeListener(getListener(name));
    }
}
