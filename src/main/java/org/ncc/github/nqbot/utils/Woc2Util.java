package org.ncc.github.nqbot.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Woc2Util {
    public static List<String> getNewWocPicList(int page){
        try {
            String json = new String(Utils.getBytes("https://yingtall.com/wp-json/wp/v2/posts?page="+page));
            String[] split = json.split("src=");
            List<String> done = new ArrayList<>();
            int counter = 0;
            for (String singlePart :split){
                if (counter > 0){
                    String removeHead = singlePart.substring(2);
                    final String retainArg = removeHead.substring(0,removeHead.indexOf("\\\""));
                    done.add(URLDecoder.decode(retainArg,"UTF-8").replace("\\/\\/","//").replace("\\/","/"));
                }
                ++counter;
            }
            return done;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
