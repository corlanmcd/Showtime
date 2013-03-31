package de.fhe.activities;

import java.util.ArrayList;


import de.fhe.data.CityProgram;
import de.fhe.data.SearchOptions;
import de.fhe.db.DBShowtime;
import de.fhe.fragments.MultiTabListener;
import de.fhe.fragments.SearchResultCinemaFragment;
import de.fhe.fragments.SearchResultCityFragment;
import de.fhe.fragments.SearchNewFragment;
import de.fhe.fragments.TabListener;
import de.fhe.util.ObjectUtil;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Start-Activity. Enthält 2 Tabs mit einem <code>NewSearchFragment</code>
 * und einem <code>SearchResultFragment</code> sowie ein ActionItem für
 * den Start der <code>SettingsActivity</code>.
 *  
 * @author Samuel und Senta
 *
 */
public class SearchActivity extends Activity
{
	private TabListener<SearchNewFragment> tabListenerSingle = null;
	private MultiTabListener<SearchResultCityFragment, SearchResultCinemaFragment> tabListenerMulti = null;
	
	private SearchOptions searchOptions = null;
	private SearchOptions savedSearchOptions = null;
	private ArrayList<CityProgram> cityProgramList = null;
	private int selectedCity = 0;
	private boolean searchRequested = false;
	
	private DBShowtime database = null;
	private int historySize = 0;
	
	private int selectedTab = 0;
	private int selectedFragment = MultiTabListener.FRAGMENT_1;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		historySize = Integer.parseInt(prefs.getString(getString(R.string.settings_history_key), "5"));
		database = new DBShowtime(this);	
		database.setMaxHistoryEntries(historySize);
		
		if (savedInstanceState != null)
		{
			searchOptions = (SearchOptions)savedInstanceState.getSerializable("searchOptions");
			savedSearchOptions = (SearchOptions)savedInstanceState.getSerializable("savedSearchOptions");
			cityProgramList = (ArrayList<CityProgram>)savedInstanceState.getSerializable("cityProgramList");
			selectedCity = savedInstanceState.getInt("selectedCity");
			searchRequested = savedInstanceState.getBoolean("searchRequested");
			
			selectedTab = savedInstanceState.getInt("selectedTab", 0);
			selectedFragment = savedInstanceState.getInt("selectedFragment", MultiTabListener.FRAGMENT_1);
		}
		else
		{
			// Eventuell vorhandene letzte Suchoptionen & Ergebnisse aus Datei laden
			savedSearchOptions = (SearchOptions)ObjectUtil.loadObjectFromFile(this, "savedSearchOptions"); 
			cityProgramList = (ArrayList<CityProgram>)ObjectUtil.loadObjectFromFile(this, "cityProgramList");
		}
		
		// Einrichten der Tabs der action bar
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayUseLogoEnabled(true);
	    actionBar.setDisplayShowTitleEnabled(false);
	    
	    Tab tab;
	    
	    tabListenerSingle = new TabListener<SearchNewFragment>(this, getString(R.string.search_new_id), SearchNewFragment.class);
	    tab = actionBar.newTab();
	    tab.setText(getString(R.string.search_new_title));
        tab.setTabListener(tabListenerSingle);
	    actionBar.addTab(tab);
	    
	    tabListenerMulti = new MultiTabListener<SearchResultCityFragment, SearchResultCinemaFragment> (this,
	    	 getString(R.string.search_result_city_id), SearchResultCityFragment.class,
	    	 getString(R.string.search_result_cinema_id), SearchResultCinemaFragment.class,
	    	 selectedFragment);
	    
	    tab = actionBar.newTab();
	    tab.setText(getString(R.string.search_result_title));
	    tab.setTabListener(tabListenerMulti);
	    actionBar.addTab(tab);
	    
	    if (selectedTab != 0)
	    {
	    	getActionBar().setSelectedNavigationItem(selectedTab);
	    }
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		historySize = Integer.parseInt(prefs.getString(getString(R.string.settings_history_key), "5"));
		database.setMaxHistoryEntries(historySize);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("searchOptions", searchOptions);
		outState.putSerializable("savedSearchOptions", savedSearchOptions);
		outState.putSerializable("cityProgramList", cityProgramList);
		outState.putInt("selectedCity", selectedCity);
		outState.putBoolean("searchRequested", searchRequested);
		
		outState.putInt("selectedTab", getActionBar().getSelectedNavigationIndex());
		outState.putInt("selectedFragment", getTabListenerMulti().getSelectedFragement());
		
		super.onSaveInstanceState(outState);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Wechsel des Fragmentes im Suchergebnis-Tab bei Drücken von "zurück" 
		if ((keyCode == KeyEvent.KEYCODE_BACK)
			&& getActionBar().getSelectedTab().getText().equals(getString(R.string.search_result_title))
			&& tabListenerMulti.getSelectedFragement() == MultiTabListener.FRAGMENT_2)
		{
			tabListenerMulti.selectFragment(MultiTabListener.FRAGMENT_1);
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	public DBShowtime getDatabase() 
	{
		return database;
	}

	public void setDatabase(DBShowtime database) 
	{
		this.database = database;
	}

	public MultiTabListener<SearchResultCityFragment, SearchResultCinemaFragment> getTabListenerMulti()
	{
		return tabListenerMulti;
	}

	public SearchOptions getSearchOptions()
	{
		return searchOptions;
	}

	public void setSearchOptions(SearchOptions searchOptions)
	{
		this.searchOptions = searchOptions;
	}

	public SearchOptions getSavedSearchOptions()
	{
		return savedSearchOptions;
	}

	public void setSavedSearchOptions(SearchOptions savedSearchOptions)
	{
		this.savedSearchOptions = new SearchOptions(savedSearchOptions);
		ObjectUtil.saveObjectToFile(this, "savedSearchOptions", savedSearchOptions);
	}

	public ArrayList<CityProgram> getCityProgramList()
	{
		return cityProgramList;
	}

	public void setCityProgramList(ArrayList<CityProgram> cityProgramList)
	{
		this.cityProgramList = cityProgramList;
		ObjectUtil.saveObjectToFile(this, "cityProgramList", cityProgramList);
	}

	public int getSelectedCity()
	{
		return selectedCity;
	}

	public void setSelectedCity(int selectedCity)
	{
		this.selectedCity = selectedCity;
	}

	public boolean getSearchRequested()
	{
		return searchRequested;
	}

	public void setSearchRequested(boolean searchRequested)
	{
		this.searchRequested = searchRequested;
	}
}
