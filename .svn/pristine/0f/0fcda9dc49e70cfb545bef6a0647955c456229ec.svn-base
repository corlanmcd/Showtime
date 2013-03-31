package de.fhe.activities;

import java.util.ArrayList;

import de.fhe.adapters.FavoritesListAdapter;
import de.fhe.data.Favorite;
import de.fhe.db.DBShowtime;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class FavoritesActivity extends Activity 
{
	private DBShowtime database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
	    database = new DBShowtime(this);	    
	    database.openWrite();
	    ArrayList<Favorite> favorites = database.fetchFavoriteEntries();
	    
	    if (favorites.size() == 0)
	    	setContentView(R.layout.favorites_empty);
	    else
	    {
	    	setContentView(R.layout.favorites_list);
		
			ExpandableListView listView = (ExpandableListView)findViewById(R.id.favorites_expandList);
			
			ExpandableListAdapter adapter = new FavoritesListAdapter(this, favorites);
			listView.setAdapter(adapter);
	    }
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);
	}
	
	@Override
	protected void onDestroy() 
	{
		database.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// füge action items zur action bar hinzu
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionitems_settings, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.actionitems_itemSettings)
		{
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);
		}
		
		return true;
	}
}
