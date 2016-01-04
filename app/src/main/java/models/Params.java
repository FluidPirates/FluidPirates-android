package models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;

public class Params extends Hashtable<String, String> {
    public String toUrlParams() {
        Iterator it = entrySet().iterator();

        StringBuilder postData = new StringBuilder();
        for (Hashtable.Entry<String, String> param : entrySet()) {
            if (postData.length() > 0) postData.append('&');
            try {
                postData.append(param.getKey());
                postData.append('=');
                postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return postData.toString();
    }
}
