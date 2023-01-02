package org.ncc.github.nqbot.data;

import com.google.gson.Gson;

public class BotConfigEntryArray {
    private static final Gson GSON = new Gson();
    public BotConfigEntry[] entries;

    public String toJson(){
        return GSON.toJson(this);
    }

    public static BotConfigEntryArray fromJson(String jsonIn){
        return GSON.fromJson(jsonIn,BotConfigEntryArray.class);
    }
}
