package org.ncc.github.nqbot.utils;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.ncc.github.nqbot.utils.Utils.getRandomUserAgent;

public class FunUtils {
    private static final String GET_OUT_API = "http://api.tangdouz.com/wz/pa.php?q=";

    public static ByteArrayInputStream getGetPic(long qid){
        try{
            final URL url = new URL(GET_OUT_API +qid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", getRandomUserAgent());
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.connect();
            try{
                if (connection.getResponseCode() == 200){
                    return Utils.readInputStream(connection.getInputStream());
                }
            }finally {
                connection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
