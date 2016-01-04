package utils;

import java.net.SocketTimeoutException;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class GetJsonArrayAsync extends AsyncTask<String, Void, JSONArray> {
	private final static String TAG = "GetJsonArrayAsync";
	private ProgressDialog progressDialog = null;
	protected Context context = null;

	public GetJsonArrayAsync(Context context) {
		this.context = context;	
	}
	
	@Override
	protected JSONArray doInBackground(String... urls) {
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
					GetJsonArrayAsync.this.cancel(true);
				}
			}
		);
	}
	
	@Override
	protected void onPostExecute(JSONArray json) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}
	
	protected JSONArray queryUrlForJson(String url) {
		JSONArray json = new JSONArray();

		try {
			json = JsonHelper.getJsonArrayFromUrl(url);
		} catch (SocketTimeoutException e) {
			Log.e(TAG, Lazy.Ex.getStackTrace(e));
		} catch (Exception e) {
			Log.e(TAG, Lazy.Ex.getStackTrace(e));
		}

		return json;
	}
}
