package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

public class GetJsonObjectAsync extends AsyncTask<String, Void, JSONObject> {
    private final static String TAG = "GetJsonArrayAsync";
    private ProgressDialog progressDialog = null;
    protected Context context = null;

    public GetJsonObjectAsync(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        return this.queryUrlForJson(urls[0]);
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(
                this.context,
                "Loading",
                "Just a second...",
                true,
                true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        GetJsonObjectAsync.this.cancel(true);
                    }
                }
        );
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    protected JSONObject queryUrlForJson(String url) {
        JSONObject json = new JSONObject();

        try {
            json = JsonHelper.getJsonObjectFromUrl(url);
        } catch (SocketTimeoutException e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
        } catch (Exception e) {
            Log.e(TAG, Lazy.Ex.getStackTrace(e));
        }

        return json;
    }
}
