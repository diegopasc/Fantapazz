package it.fantapazz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class MyTask extends AsyncTask {

	private ProgressDialog dialog;
	protected Context applicationContext;

	@Override
	protected Object doInBackground(Object... params) {
		return IFantapazzActivity.getTimeStampFromYahooService();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		this.dialog.cancel();
//		String timestamp = IFantapazzActivity.parseJSONResponse(result);
//		timestamp = IFantapazzActivity.UnixTimeStampToDateTime(timestamp);
//		IFantapazzActivity.this.getTxtTime().setText(timestamp);
	}

	@Override
	protected void onPreExecute() {
		this.dialog = ProgressDialog.show(applicationContext, "Calling", "Time Service...", true);
	}

}
