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
import java.net.ProtocolException;
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

    public static JSONObject postJsonObjectFromUrl(String url, Params params) {
        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
            return jsonError("MalformedURLException");
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urlObject.openConnection();
        } catch (IOException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
            return jsonError("IOException can't open connection");
        }
        conn.setDoOutput(true);
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
            return jsonError("ProtocolException");
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(conn.getOutputStream());
        } catch (IOException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
            return jsonError("IOException can't get output stream");
        }
        out.print(params.toUrlParams());
        out.close();

        try {
            int responseCode = conn.getResponseCode();
        } catch (IOException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
            return jsonError("IOException can't get response code");
        }

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

    private static JSONObject jsonError(String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
