package de.fhe.adapters;

import java.util.ArrayList;

import de.fhe.activities.R;
import de.fhe.data.Favorite;
import de.fhe.data.FavoriteEntry;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoritesListAdapter extends BaseExpandableListAdapter 
{	
	private static final int NUM_OF_CHARS_LOCATION_TITLE_PORT = 35;
	private static final int NUM_OF_CHARS_LOCATION_TITLE_LAND = 60;
	
	private static final int NUM_OF_CHARS_MOVIE_TITLE_PORT = 25;
	private static final int NUM_OF_CHARS_MOVIE_TITLE_LAND = 50;
	
	private Context context;
	private ArrayList<Favorite> favoriteList;
	
	public FavoritesListAdapter(Context context, ArrayList<Favorite> favoriteList)
	{
		super();
		this.context = context;
		this.favoriteList = favoriteList;
	}

	public Object getChild(int groupPosition, int childPosition) 
	{
		return favoriteList.get(groupPosition).getEntryAt(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		View view;
		
		if (convertView == null)
		{		
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = (LinearLayout)inflater.inflate(R.layout.favorites_entry, null);
		}
		else
			view = convertView;
			
		TextView movie = (TextView)view.findViewById(R.id.favorites_entry_movie_textView);		
		TextView location = (TextView)view.findViewById(R.id.favorites_entry_location_textView);
		TextView showTimes = (TextView)view.findViewById(R.id.favorites_entry_showtimes_textView);
		
		FavoriteEntry entry = (FavoriteEntry)getChild(groupPosition, childPosition);
		
		Point size = new Point();
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		display.getSize(size);
		int dpToPx = context.getResources().getDisplayMetrics().densityDpi / 160;
		
		// Filmtitel
		movie.setText(entry.getMovie());
		
		// Höhe Filmtitel berechnen
		int numOfLinesMovie = 1;
		
		if (size.x > size.y) 	// landscape
		{
			if (movie.length() % NUM_OF_CHARS_MOVIE_TITLE_LAND == 0)
				numOfLinesMovie = movie.length() / NUM_OF_CHARS_MOVIE_TITLE_LAND;
			else
				numOfLinesMovie = movie.length() / NUM_OF_CHARS_MOVIE_TITLE_LAND + 1;
		}
		else					// portrait
		{
			if (movie.length() % NUM_OF_CHARS_MOVIE_TITLE_PORT == 0)
				numOfLinesMovie = movie.length() / NUM_OF_CHARS_MOVIE_TITLE_PORT;
			else
				numOfLinesMovie = movie.length() / NUM_OF_CHARS_MOVIE_TITLE_PORT + 1;
		}
		
		// 18 sp in px umrechnen
		int lineHeightTitle = (int)(18 * context.getResources().getDisplayMetrics().scaledDensity);
		int paddingBetweenLines = (numOfLinesMovie - 1) * 7 * dpToPx;
		
		int heightMovie = numOfLinesMovie * lineHeightTitle + paddingBetweenLines;
		
		
		// Kino und Ort
		location.setText(entry.getCinema() + ", " + entry.getCity());
		
		
		// Höhe Ort und Kino berechnen
		int numOfLinesLocation = 1;
		
		if (size.x > size.y) 	// landscape
		{
			if (location.length() % NUM_OF_CHARS_LOCATION_TITLE_LAND == 0)
				numOfLinesLocation = location.length() / NUM_OF_CHARS_LOCATION_TITLE_LAND;
			else
				numOfLinesLocation = location.length() / NUM_OF_CHARS_LOCATION_TITLE_LAND + 1;
		}
		else					// portrait
		{
			if (location.length() % NUM_OF_CHARS_LOCATION_TITLE_PORT == 0)
				numOfLinesLocation = location.length() / NUM_OF_CHARS_LOCATION_TITLE_PORT;
			else
				numOfLinesLocation = location.length() / NUM_OF_CHARS_LOCATION_TITLE_PORT + 1;
		}
		
		// 14 sp in px umrechnen
		int lineHeightLocation = (int)(14 * context.getResources().getDisplayMetrics().scaledDensity);
		int paddingBetweenLinesLocation = (numOfLinesLocation - 1) * 5 * dpToPx;
		
		int heightLocation = numOfLinesLocation * lineHeightLocation + paddingBetweenLinesLocation;
		
		// Spielzeiten
		String times = new String();
		int numOfTimesPerLine = (size.x > size.y) ? 8 : 4;	
		int numOfLinesTimes = 1;
		
		for (int i = 0; i < entry.getDateShowTimes().size(); i++)
		{
			times += entry.getDateShowTimesAt(i).getDate() + ":\t";
			int numOfTimes = entry.getDateShowTimesAt(i).getShowTimes().size();
			
			for (int j = 1; j <= numOfTimes; j++)
			{
				times += entry.getDateShowTimesAt(i).getShowTimeAt(j-1);
				
				if (j != numOfTimes)
					times += ", ";
				
				if (j % numOfTimesPerLine == 0 && j != numOfTimes)
				{
					times += "\n\t\t\t\t";		
					numOfLinesTimes++;
				}
			}
			
			if (i != entry.getDateShowTimes().size() - 1)
			{
				times += "\n";
				numOfLinesTimes++;
			}
		}
		
		showTimes.setText(times);
		
		// Höhe Vorführungen berechnen		
		// 14 sp in px umrechnen
		int lineHeightTimes = (int)(14 * context.getResources().getDisplayMetrics().scaledDensity);
		int paddingBetweenLinesTimes = (numOfLinesTimes) * 5 * dpToPx;		
		int heightTimes = numOfLinesTimes * lineHeightTimes + paddingBetweenLinesTimes;
		
		// Gesamthöhe berechnen
		int padding = ((3 * 10) + 25) * dpToPx;
		int height = heightMovie + heightLocation + heightTimes + padding;
		
		// Höhe an Hintergrundmuster anpassen
		if(height % 36 != 0)
			view.setMinimumHeight(height + (36 - (height % 36)));
		else
			view.setMinimumHeight(height);
		
		return view;
	}

	public int getChildrenCount(int groupPosition) 
	{
		return favoriteList.get(groupPosition).getEntries().size();
	}

	public Object getGroup(int groupPosition) 
	{
		return favoriteList.get(groupPosition);
	}

	public int getGroupCount() 
	{
		return favoriteList.size();
	}

	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		View view = null;
		
		if (convertView == null)
		{		
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.favorites_date, null);
		}
		else
			view = convertView;
		
		TextView date = (TextView)view.findViewById(R.id.favorites_date_textView);
		date.setText(getGroup(groupPosition).toString());
		
		view.setMinimumHeight(2 * 36);
		
		return view;
	}

	public boolean hasStableIds() 
	{
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
}
