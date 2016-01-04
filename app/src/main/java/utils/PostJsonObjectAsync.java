package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import models.Params;

public class PostJsonObjectAsync extends AsyncTask<String, Void, JSONObject> {
	private final static String TAG = "PostJsonObjectAsync";
	private ProgressDialog progressDialog = null;
	protected Context context = null;
	protected Params params = null;


	public PostJsonObjectAsync(Context context, Params params) {
		this.context = context;
		this.params = params;
	}
	
	@Override
	protected JSONObject doInBackground(String... args) {
		return this.queryUrlForJson(args[0], params);
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
					PostJsonObjectAsync.this.cancel(true);
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
	
	protected JSONObject queryUrlForJson(String url, Params params) {
		JSONObject json = new JSONObject();

		try {
			json = JsonHelper.postJsonObjectFromUrl(url, params);
		} catch (SocketTimeoutException e) {
			Log.e(TAG, Lazy.Ex.getStackTrace(e));
		} catch (Exception e) {
			Log.e(TAG, Lazy.Ex.getStackTrace(e));
		}

		return json;
	}
}
