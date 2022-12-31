package org.ncc.github.nqbot.bot;

import com.google.gson.Gson;
import net.mamoe.mirai.utils.BotConfiguration;

public class BotConfigEntry {
    private static final Gson GSON = new Gson();
    private final long qid;
    private final String password;
    private final BotConfiguration.MiraiProtocol protocol;

    public BotConfigEntry(long qid,String password,BotConfiguration.MiraiProtocol protocol){
        this.qid = qid;
        this.password = password;
        this.protocol = protocol;
    }

    public String getPassword(){
        return this.password;
    }

    public long getQid() {
        return this.qid;
    }

    public BotConfiguration.MiraiProtocol getProtocol(){
        return this.protocol;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
