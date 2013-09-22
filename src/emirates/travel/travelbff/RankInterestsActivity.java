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
import android.widget.ArrayAdapter;

public class RankInterestsActivity extends Activity {
	RankInterestsActivity instance;
	String[] cityDateDurationInput;
	int budget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_interests);
		
		Intent intent = getIntent();
		instance = this;
		cityDateDurationInput = intent.getStringArrayExtra("START_CITY_DATE_DURATION");
		budget = intent.getIntExtra("BUDGET", 0);
		
		final DragSortListView listview = (DragSortListView) findViewById(R.id.interestList);
		String[] values = new String[] { "People Culture", "Music", "Adventure", 
				"History", "Entertainment", "Food", "Nature"};
		
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
		      list.add(values[i]);
		    }
		    final StableArrayAdapter adapter = new StableArrayAdapter(this,
		        android.R.layout.simple_list_item_1, list);
		    listview.setAdapter(adapter);

//		    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//		      @Override
//		      public void onItemClick(AdapterView<?> parent, final View view,
//		          int position, long id) {
//		        final String item = (String) parent.getItemAtPosition(position);
//		        view.animate().setDuration(2000).alpha(0)
//		            .withEndAction(new Runnable() {
//		              @Override
//		              public void run() {
//		                list.remove(item);
//		                adapter.notifyDataSetChanged();
//		                view.setAlpha(1);
//		              }
//		            });
//		      }
//
//		    });
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
