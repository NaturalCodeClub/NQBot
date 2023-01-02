package org.ncc.github.nqbot.data;

import com.google.gson.Gson;
import net.mamoe.mirai.utils.BotConfiguration;

/**
 * 机器人配置文件的类
 */
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

    /**
     * 机器人密码
     * @return 机器人密码
     */
    public String getPassword(){
        return this.password;
    }

    /**
     * 机器人的QQID
     * @return QQID
     */
    public long getQid() {
        return this.qid;
    }

    /**
     * 登录机器人使用的协议
     * @return 协议
     */
    public BotConfiguration.MiraiProtocol getProtocol(){
        return this.protocol;
    }

    /**
     * 转换为json字符串
     * @return
     */
    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
