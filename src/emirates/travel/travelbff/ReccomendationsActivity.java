package emirates.travel.travelbff;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
 
public class ReccomendationsActivity extends Activity {
	ReccomendationsActivity instance;
	HashMap<Integer, String> finalScoreMap = new HashMap<Integer, String>();
	
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
 
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reccomendations);
        
        Intent intent = getIntent();
		instance = this;
		finalScoreMap = (HashMap<Integer, String>) intent.getExtras().get("RECOMMENDATIONS");
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @SuppressLint("NewApi")
			@Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                        listDataHeader.get(groupPosition)).get(
//                                        childPosition), Toast.LENGTH_SHORT)
//                        .show();
            	Intent intent = new Intent(getBaseContext(),
						DisplayCityInfoActivity.class);
				startActivity(intent);
                return false;
            }
        });
        
        expListView.setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) {
				Intent intent = new Intent(getBaseContext(),
						DisplayCityInfoActivity.class);
				startActivity(intent);
				return false;
			}
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Chicago, USA");
        listDataHeader.add("Tokyo, Japan");
        listDataHeader.add("Salvador, Brazil");
 
        // Adding child data
        List<String> city1 = new ArrayList<String>();
        city1.add("People Culture");
        city1.add("Music");
        city1.add("Adventure");
        city1.add("History");
        city1.add("Entertainment");
        city1.add("Food");
        city1.add("Nature");
 
        List<String> city2 = new ArrayList<String>();
        city2.add("People Culture");
        city2.add("Music");
        city2.add("Adventure");
        city2.add("History");
        city2.add("Entertainment");
        city2.add("Food");
        city2.add("Nature");
 
        List<String> city3 = new ArrayList<String>();
        city3.add("People Culture");
        city3.add("Music");
        city3.add("Adventure");
        city3.add("History");
        city3.add("Entertainment");
        city3.add("Food");
        city3.add("Nature");
 
        listDataChild.put(listDataHeader.get(0), city1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), city2);
        listDataChild.put(listDataHeader.get(2), city3);
    }
}