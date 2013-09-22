package emirates.travel.travelbff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobeta.android.dslv.*;
import com.mobeta.android.demodslv.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				Intent intent = new Intent(getBaseContext(), ReccomendationsActivity.class);
				intent.putExtra("START_CITY_DATE_DURATION", cityDateDurationInput);
				intent.putExtra("BUDGET", budget);
				
				final ArrayList<String> finalList = new ArrayList<String>();
				for (int i = 0; i < 6; ++i) {
					finalList.add((String) listview.getItemAtPosition(i));
				}
				intent.putExtra("RANKINGS", finalList);
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
}
