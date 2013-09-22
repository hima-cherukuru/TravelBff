package emirates.travel.travelbff;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RankInterestsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_interests);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rank_interests, menu);
		return true;
	}

}
