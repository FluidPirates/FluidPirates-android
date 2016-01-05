package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

public class Lazy {
    public static class Ex {
        public static String getStackTrace(java.lang.Exception e) {
            StringWriter sWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(sWriter));
            return sWriter.getBuffer().toString();
        }
    }

    public static class Str {
        public static String urlEncode(String value, boolean trim) {
            return java.net.URLEncoder.encode(value.trim());
        }

        public static String urlEncode(String value) {
            return urlEncode(value, true);
        }

        public static String urlReplace(String url, String... keys) {
            Hashtable<String, String> params = Lazy.Hash.h(keys);

            for (Hashtable.Entry<String, String> param : params.entrySet()) {
                url = url.replaceAll(param.getKey(), param.getValue());
            }

            return url;
        }
    }

    public static class Hash {
        public static Hashtable<String, String> h(String... keys_and_values) {
            Hashtable<String, String> hash = new Hashtable<String, String>();
            for(int i = 0; i < keys_and_values.length / 2; i += 1) {
                hash.put(keys_and_values[i / 2], keys_and_values[i / 2 + 1]);
            }
            return hash;
        }
    }
}
