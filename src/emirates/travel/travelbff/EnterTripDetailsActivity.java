package emirates.travel.travelbff;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

@SuppressLint("NewApi")
public class EnterTripDetailsActivity extends Activity {

	EditText originCity;
	Button startDate;
	DatePicker datePicker;
	DateDialogFragment frag;
	Button button;
    Calendar now;
	EditText duration;
	SeekBar budget;
	Button nextStep;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_trip_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		originCity = (EditText) findViewById(R.id.origin_city);
		originCity.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		            handled = true;
		        }
		        return handled;
		    }
		});
		
		now = Calendar.getInstance();
		startDate = (Button) findViewById(R.id.prompt_date);
		startDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog();
			}
		});
		
		duration = (EditText) findViewById(R.id.trip_duration);
		budget = (SeekBar) findViewById(R.id.budget_bar);
		
		nextStep = (Button) findViewById(R.id.button1);
		nextStep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getBaseContext(), RankInterestsActivity.class);
				intent.putExtra("START_CITY", originCity.getText().toString());
				intent.putExtra("START_DATE", startDate.getText());
				intent.putExtra("TRIP_DURATION", duration.getText().toString());
				intent.putExtra("BUDGET", budget.getProgress());
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_trip_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void showDialog() {
    	FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
    	frag = DateDialogFragment.newInstance(this, new DateDialogFragmentListener(){
    		public void updateChangedDate(int year, int month, int day){
    			startDate.setText(String.valueOf(month+1)+"-"+String.valueOf(day)+"-"+String.valueOf(year));
    			now.set(year, month, day);
    		}
    	}, now);
    	
    	frag.show(ft, "DateDialogFragment");
    	
    }
	
	public interface DateDialogFragmentListener{
    	//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
    	public void updateChangedDate(int year, int month, int day);
    }

}
