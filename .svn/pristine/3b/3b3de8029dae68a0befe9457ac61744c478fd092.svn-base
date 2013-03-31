package de.fhe.fragments;

import java.util.ArrayList;
import java.util.Date;

import de.fhe.activities.CinemaDetailsActivity;
import de.fhe.activities.MovieDetailsActivity;
import de.fhe.activities.R;
import de.fhe.activities.SearchActivity;
import de.fhe.adapters.CinemaListAdapter;
import de.fhe.data.CinemaProgram;
import de.fhe.data.CityProgram;
import de.fhe.db.DBShowtime;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

/**
 * Zeigt eine ausklappbare Liste an, die die Kinos und die in denen gezeigten Filme (mit Spielzeiten und Bewertung) anzeigt.
 * Bei Auswahl eines Films werden dessen Detailinformationen angezeigt, falls vorhanden.
 * 
 * @author Senta und Samuel
 *
 */
public class SearchResultCinemaFragment extends Fragment implements OnChildClickListener, OnItemLongClickListener
{	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.search_result_cinema_list, container, false);
		ExpandableListView listView = (ExpandableListView)view.findViewById(R.id.search_result_cinema_expandList);
		ExpandableListAdapter adapter = new CinemaListAdapter(getActivity(), getCinemaProgramList());
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(this);
		listView.setOnItemLongClickListener(this);
		
		return view;
	}
	
	private ArrayList<CityProgram> getCityProgramList()
	{
		return ((SearchActivity)getActivity()).getCityProgramList();
	}
	
	private int getSelectedCity()
	{
		return ((SearchActivity)getActivity()).getSelectedCity();
	}
	
	private ArrayList<CinemaProgram> getCinemaProgramList()
	{
		return getCityProgramList().get(getSelectedCity()).getCinemaPrograms();
	}

	/**
	 * Callback für die Auswahl eines Films in der Liste. Ruft die Details zum Film auf.
	 */
	public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) 
	{
		String url = getCinemaProgramList().get(groupPosition).getMovieShowTimes().get(childPosition).getMovieDetailsUrl();
		
		if (url != null)
		{	
			Intent i = new Intent(getActivity(), MovieDetailsActivity.class);
			i.putExtra("movieDetailsUrl", getString(R.string.app_base_url) + url);
			startActivity(i);
		}
		else
		{
			Toast.makeText(getActivity(), getString(R.string.search_result_cinema_noMovieDetails), Toast.LENGTH_SHORT).show();
		}
		
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.actionitems_cinemas, menu);
		menu.findItem(R.id.actionitems_itemCinemas).setActionProvider(new CinemaActionProvider(getActivity()));
	}
	
	/**
	 * Erstellt das Menü für die Detailinformationen zu den Kinos und behandelt die Auswahl eines Eintrags.
	 * 
	 * @author Senta und Samuel
	 *
	 */
	private class CinemaActionProvider extends ActionProvider implements OnMenuItemClickListener
	{		
		public CinemaActionProvider(Context context)
		{
			super(context);
		}

		@Override
		public View onCreateActionView()
		{
			return null;
		}

		@Override
		public boolean hasSubMenu()
		{
			return true;
		}
		
		@Override
		public void onPrepareSubMenu(SubMenu subMenu)
		{
			subMenu.clear();
			int numOfEntries = 0;
			
			if (getCinemaProgramList() != null)
			{
				for (int i = 0; i < getCinemaProgramList().size(); i++)
				{
					if (getCinemaProgramList().get(i).getCinemaDetailsUrl() != null)
					{
						MenuItem item = subMenu.add(0, i, i, getCinemaProgramList().get(i).getCinema());
						item.setOnMenuItemClickListener(this);
						numOfEntries++;
					}
				}
			}
			
			if(numOfEntries == 0)
			{
				Toast.makeText(getActivity(), R.string.search_result_cinema_noCinemaDetails, Toast.LENGTH_SHORT).show();
			}
		}

		public boolean onMenuItemClick(MenuItem item)
		{
			Intent i = new Intent(getActivity(), CinemaDetailsActivity.class);
			i.putExtra("cinemaDetailsUrl", getString(R.string.app_base_url) + getCinemaProgramList().get(item.getItemId()).getCinemaDetailsUrl());
			startActivity(i);
			
			return true;
		}
	}

	/**
	 * Speichert den ausgewählten Film in der Datenbank unter Favoriten.
	 */
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
		{
			int groupIndex = ExpandableListView.getPackedPositionGroup(id);
			int childIndex = ExpandableListView.getPackedPositionChild(id);
			
			DBShowtime db = ((SearchActivity)getActivity()).getDatabase();
			db.openWrite();
			
			int result = db.insertFavoritesEntry(new Date(), 
					getCinemaProgramList().get(groupIndex).getMovieShowTimes().get(childIndex), 
					getCityProgramList().get(getSelectedCity()).getCity(), 
					getCinemaProgramList().get(groupIndex).getCinema());
			
			if (result == DBShowtime.ERROR_DUPLICATE)
				Toast.makeText(getActivity(), R.string.search_result_cinema_duplicateFav, Toast.LENGTH_SHORT).show();
			else if (result == DBShowtime.ERROR_NOT_OPENED)
				Toast.makeText(getActivity(), R.string.search_result_cinema_errorFav, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getActivity(), R.string.search_result_cinema_insertFav, Toast.LENGTH_SHORT).show();
			
			db.close();
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
