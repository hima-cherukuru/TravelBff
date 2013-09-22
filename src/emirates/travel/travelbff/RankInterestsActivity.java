package emirates.travel.travelbff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;

import com.mobeta.android.dslv.*;
import com.mobeta.android.demodslv.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class RankInterestsActivity extends Activity {
	RankInterestsActivity instance;
	String[] cityDateDurationInput;
	int budget;
	Button nextStep;
	ArrayList<String> ranking = new ArrayList<String>();
	String[] cities = { "Miami", "SanFrancisco", "Istanbul", "Hyderabad",
			"Salvador" };
	HashMap<Integer, Integer> interestMap = new HashMap<Integer, Integer>();
	TreeMap<Integer, String> finalScoreMap = new TreeMap<Integer, String>();
	int score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		interestMap.put(1, 30);
		interestMap.put(2, 20);
		interestMap.put(3, 15);
		interestMap.put(4, 10);
		interestMap.put(5, 10);
		interestMap.put(6, 5);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_interests);

		Intent intent = getIntent();
		instance = this;
		cityDateDurationInput = intent
				.getStringArrayExtra("START_CITY_DATE_DURATION");
		budget = intent.getIntExtra("BUDGET", 0);

		final DragSortListView listview = (DragSortListView) findViewById(R.id.interestList);
		String[] values = new String[] { "People Culture", "Music",
				"Adventure", "History", "Entertainment", "Food", "Nature" };

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setBackgroundColor(0xFFFFFF);
		listview.setAdapter(adapter);

		nextStep = (Button) findViewById(R.id.recommendations);
		nextStep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				for (int i = 0; i < 6; ++i) {
					ranking.add((String) listview.getItemAtPosition(i));
				}

				AsyncTaskRunner runner = new AsyncTaskRunner();
				String sleepTime = "200000";
				runner.execute(sleepTime);

				Intent intent = new Intent(getBaseContext(),
						ReccomendationsActivity.class);
				intent.putExtra("START_CITY_DATE_DURATION",
						cityDateDurationInput);
				intent.putExtra("BUDGET", budget);
				intent.putExtra("RECOMMENDATIONS", finalScoreMap);
				intent.putExtra("RANKINGS", ranking);
				startActivity(intent);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rank_interests, menu);
		return true;
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Searching for your next destination..."); // Calls
																		// onProgressUpdate()
			try {
				// Do your long operations here and return the result
				for (int i = 0; i <= 5; i++) {
					boolean isWithinBudget = calculateBudget(
							cityDateDurationInput[0], cities[i],
							cityDateDurationInput[1], cityDateDurationInput[2]);
					if (isWithinBudget) {
						finalScoreMap.put(
								calculateInterestScore(cities[i], ranking),
								cities[i]);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return resp;
		}

		@Override
		protected void onProgressUpdate(String... text) {
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
			// Intent progressIntent = new
			// Intent(getBaseContext(),ProgressActivity.class);
			// startActivity(progressIntent);
			setContentView(R.layout.activity_progress);
		}

		@Override
		protected void onPostExecute(String result) {
			Intent progressIntent = new Intent(getBaseContext(),
					ReccomendationsActivity.class);
			startActivity(progressIntent);
		}
	}

	public String connect(String url) {
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Response", response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				// now you have the string representation of the HTML request
				instream.close();
				return result;
			} else
				return "0";
		} catch (Exception e) {
			return "0";
		}
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
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

	public boolean calculateBudget(String sourceCity, String destCity,
			String startDate, String duration) {
		String getParameters = "&startdate=" + startDate + "&duration="
				+ duration + "&origin=" + sourceCity + "&dest=" + destCity;
		String hotelResponse = connect("http://api.hotwire.com/v1/deal/hotel?apikey=t4x5cvtpbzhd235hpk4c24gf"
				+ getParameters
				+ "&limit=1&sort=price&sortOrder=asc&starrating=3~*");
		int minHotelPrice = Integer.parseInt(Jsoup.parse("hotelResponse")
				.select("Price").first().text());

		String flightResponse = connect("http://api.hotwire.com/v1/tripstarter/air?apikey=t4x5cvtpbzhd235hpk4c24gf"
				+ getParameters
				+ "&limit=1&startdate=5/1/2010&duration=4&origin=Seattle&dest=Chicago&sort=price&sortOrder=asc");
		int minFlightPrice = Integer.parseInt(Jsoup.parse("flightResponse")
				.select("AveragePrice").first().text());
		int minBudgetRequired = minHotelPrice + minFlightPrice;

		if (minBudgetRequired <= budget) {
			return true;
		} else
			return false;
	}

	public int calculateInterestScore(String cityName,
			List<String> interestRanking) {
		int finalScore = 0;
		for (int i = 0; i < 6; i++) {
			finalScore = finalScore
					+ queryScoreForInterest(cityName, interestRanking.get(i),
							interestMap.get(i + 1));
		}
		return finalScore;
	}

	public int queryScoreForInterest(String cityName, String interestName,
			final int interestWeight) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
		query.whereEqualTo("City", cityName);
		query.whereEqualTo("Interest", interestName);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					System.out.println("Error retrieving parse objects");
				} else {
					score = scoreList.get(1).getInt("Score") * interestWeight;
					System.out
							.println("Successfully retrieved parse object for Miami "
									+ scoreList.get(1).getInt("Score"));
				}
			}

		});
		return score;
	}
}
