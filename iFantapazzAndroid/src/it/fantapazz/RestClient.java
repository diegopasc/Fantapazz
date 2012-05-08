package it.fantapazz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RestClient {

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}


	/* This is a test function which will connects to a given
	 * rest service and prints it's response to Android Log with
	 * labels "Praeda".
	 */
	public static JSONObject connect(String url)
	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
			    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
			    new UsernamePasswordCredentials("micmastr", "apriti80"));
		
		httpclient.setCredentialsProvider(credsProvider);
		
		// Prepare a request object
		HttpGet httpget = new HttpGet(url); 
		
		// Execute the request
		HttpResponse response;
		try {
			
			response = httpclient.execute(httpget);
			
			// Examine the response status
			Log.i("Praeda",response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				
				String result = convertStreamToString(instream);
				
				// A Simple JSONObject Creation
				JSONObject json = new MyJSONObject(new JSONObject(result));
				
				// Closing the input stream will trigger connection release
				instream.close();

				return json;
				
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
