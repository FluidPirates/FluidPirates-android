package utils;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import models.Params;

public class JsonHelper {
    static final private String TAG = "JsonHelper";

    public static JSONArray getJsonArrayFromResource(Context context, int resourceId) throws JSONException, IOException {
        InputStream is = context.getResources().openRawResource(resourceId);
        return new JSONArray(getStringFromInputStream(is));
    }

    public static JSONObject getJsonObjectFromResource(Context context, int resourceId) throws JSONException, IOException {
        InputStream is = context.getResources().openRawResource(resourceId);
        return new JSONObject(getStringFromInputStream(is));
    }

    public static JSONArray getJsonArrayFromUrl(String url) throws MalformedURLException, JSONException, IOException {
        return getJsonArrayFromUrl(url);
    }

    public static JSONObject getJsonObjectFromUrl(String url) throws MalformedURLException, JSONException, IOException {
        return getJsonObjectFromUrl(url);
    }

    public static JSONObject postJsonObjectFromUrl(String url, Params params) throws IOException {
        URL urlObject = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(params.toUrlParams());
        out.close();

        int responseCode = conn.getResponseCode();

        String jsonString = "";

        try {
            jsonString = getStringFromInputStream(conn.getInputStream());
        } catch (Exception e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
        } finally {
            conn.disconnect();
        }

        return new JSONObject();
    }

    static private String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static String getStringFromUrl(String url) throws MalformedURLException, JSONException, IOException {
        URL urlObject = new URL(url);
        HttpURLConnection urlConn = (HttpURLConnection) urlObject.openConnection();

        String jsonString = "";

        try {
            jsonString = getStringFromInputStream(urlConn.getInputStream());
        } finally {
            urlConn.disconnect();
        }

        return jsonString;
    }
}
