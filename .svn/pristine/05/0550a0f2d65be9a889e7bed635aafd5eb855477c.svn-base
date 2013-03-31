package de.fhe.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		setContentView(R.layout.main_menu);
		
		((Button)findViewById(R.id.main_menu_buttonSearch)).setOnClickListener(this);
		((Button)findViewById(R.id.main_menu_buttonPreview)).setOnClickListener(this);
		((Button)findViewById(R.id.main_menu_buttonFavorites)).setOnClickListener(this);
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

	public void onClick(View view)
	{
		if (view.getId() == R.id.main_menu_buttonSearch)
		{
			Intent search = new Intent(this, SearchActivity.class);
			startActivity(search);
		}
		else if (view.getId() == R.id.main_menu_buttonPreview)
		{
			Intent preview = new Intent(this, PreviewActivity.class);
			startActivity(preview);
		}
		else if (view.getId() == R.id.main_menu_buttonFavorites)
		{
			Intent favorite = new Intent(this, FavoritesActivity.class);
			startActivity(favorite);
		}		
	}
}
