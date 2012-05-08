package it.fantapazz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IFantapazzActivity extends Activity {
	
	private TextView txtTime;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.txtTime = (TextView) findViewById(R.id.textView);
		
		setContentView(R.layout.main);

		final Button btnCallWebService = (Button) findViewById(R.id.button1);

		btnCallWebService.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyTask task = new MyTask();
				task.applicationContext = IFantapazzActivity.this;
				task.execute();
			}

		});

	}

	public static String getTimeStampFromYahooService() {
		// String url = "http://localhost/pippo.json";
		String url = "http://www.beta.fantapazz.com/servizi/login?user=antonio&pass=ciccio80&token=69222d343f634e55796a54687d553c786f637e7a5a55757d6f72293328";
        RestClient.connect(url);
		return null;
	}
	
	
}