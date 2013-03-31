package de.fhe.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fhe.activities.R;
import de.fhe.data.CinemaProgram;
import de.fhe.data.MovieShowTimes;

/**
 * Adapter für die Liste der Kinos mit den dort aufgeführten Filmen.
 * 
 * @author Senta und Samuel
 *
 */
public class CinemaListAdapter extends BaseExpandableListAdapter
{
	private static final int[] RATINGS = { 	R.drawable.rating_very_bad,
											R.drawable.rating_bad,
											R.drawable.rating_medium,
											R.drawable.rating_good,
											R.drawable.rating_very_good  };
	
	private static final int NUM_OF_CHARS_CINEMA_TITLE_PORT = 28;
	private static final int NUM_OF_CHARS_CINEMA_TITLE_LAND = 50;
	
	private static final int NUM_OF_CHARS_MOVIE_TITLE_PORT = 25;
	private static final int NUM_OF_CHARS_MOVIE_TITLE_LAND = 50;
	
	private Context context = null;
	private ArrayList<CinemaProgram> cinemaProgramList = null;
	
	public CinemaListAdapter(Context context, ArrayList<CinemaProgram> cinemaProgramList)
	{
		this.context = context;
		this.cinemaProgramList = cinemaProgramList;
	}

	public Object getChild(int groupPosition, int childPosition) 
	{
		return cinemaProgramList.get(groupPosition).getMovieShowTimes().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	/**
	 * Baut den View für die Film-Elemente (Kind) der Liste zusammen und passt dessen Höhe an das Hintergrundbild an.
	 */
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		View childView = null;
		
		if (convertView == null)
		{			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			childView = inflater.inflate(R.layout.search_result_movie, null);
		}
		else
			childView = convertView;
		
		TextView title = (TextView)childView.findViewById(R.id.search_result_cinema_movie_textView);
		TextView showTimes = (TextView)childView.findViewById(R.id.search_result_cinema_showTimes_textView);		
		ImageView ratingImage = (ImageView)childView.findViewById(R.id.search_result_cinema_rating_imageView);

		Point size = new Point();
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		display.getSize(size);
		int dpToPx = context.getResources().getDisplayMetrics().densityDpi / 160;
		
		// Daten zum Film in View eintragen
		MovieShowTimes data = (MovieShowTimes)getChild(groupPosition, childPosition);
		
		// Bild für Bewertung setzen
		if (data.getRating() != -1)
		{
			int index = data.getRating() / 25;
			ratingImage.setImageResource(RATINGS[index]);
		}
		// Bild löschen (wichtig für den Fall, dass View wiederverwendet wird)
		else
		{
			ratingImage.setImageResource(0);
		}
		
		// Filmtitel
		title.setText(data.getMovie());
		
		// Höhe Filmtitel berechnen
		int numOfLinesTitle = 1;
		int heightTitle = 0;
		
		if (size.x > size.y) 	// landscape
		{
			if (title.length() % NUM_OF_CHARS_MOVIE_TITLE_LAND == 0)
				numOfLinesTitle = title.length() / NUM_OF_CHARS_MOVIE_TITLE_LAND;
			else
				numOfLinesTitle = title.length() / NUM_OF_CHARS_MOVIE_TITLE_LAND + 1;
		}
		else					// portrait
		{
			if (title.length() % NUM_OF_CHARS_MOVIE_TITLE_PORT == 0)
				numOfLinesTitle = title.length() / NUM_OF_CHARS_MOVIE_TITLE_PORT;
			else
				numOfLinesTitle = title.length() / NUM_OF_CHARS_MOVIE_TITLE_PORT + 1;
		}
		
		// 18 sp in px umrechnen
		int lineHeightTitle = (int)(18 * context.getResources().getDisplayMetrics().scaledDensity);
		int paddingBetweenLines = (numOfLinesTitle - 1) * 7 * dpToPx;
		
		heightTitle = numOfLinesTitle * lineHeightTitle + paddingBetweenLines;
		
		// Vorführungen
		String allTimes = "";
		
		int numOfLinesSub = 1;		
		int numOfTimesPerLine = (size.x > size.y) ? 8 : 3;			
		
		for(int i = 0; i < data.getDateShowTimes().size(); i++)
		{
			// Vorführzeiten formatieren
			String date = data.getDateShowTimes().get(i).getDate();			
			
			String showTimesOfDate = "";
			int numOfTimes = data.getDateShowTimesAt(i).getShowTimes().size();
			
			for(int j = 1; j <= numOfTimes; j++)
			{
				showTimesOfDate += data.getDateShowTimesAt(i).getShowTimeAt(j - 1);
				
				if(j != numOfTimes)
					showTimesOfDate += ", ";
				
				if(j % numOfTimesPerLine == 0 && j != numOfTimes)
				{
					showTimesOfDate += "\n\t\t\t\t";
					numOfLinesSub++; // Zeilenanzahl Vorführungen berechnen
				}
			}
			
			if(!allTimes.equals(""))
			{
				allTimes += "\n"; // neue Zeile für neues Datum
				numOfLinesSub++;
			}
			
			allTimes += date + ":\t" + showTimesOfDate;
		}
		
		showTimes.setText(allTimes);
		
		// Höhe Vorführungen berechnen		
		// 14 sp in px umrechnen
		int lineHeightSub = (int)(14 * context.getResources().getDisplayMetrics().scaledDensity);
		int paddingBetweenLinesSub = (numOfLinesSub) * 5 * dpToPx;		
		int heightSub = numOfLinesSub * lineHeightSub + paddingBetweenLinesSub;
		
		// Gesamthöhe berechnen
		int padding = 3 * 10 * dpToPx;
		int height = heightTitle + heightSub + padding;
		
		// Höhe an Hintergrundmuster anpassen
		if(height % 36 != 0)
		{
			childView.setMinimumHeight(height + (36 - (height % 36)));
		}
		else
		{
			childView.setMinimumHeight(height);
		}
		
		return childView;
	}

	public int getChildrenCount(int groupPosition) 
	{
		return cinemaProgramList.get(groupPosition).getMovieShowTimes().size();
	}

	public Object getGroup(int groupPosition) 
	{
		return cinemaProgramList.get(groupPosition).getCinema();
	}

	public int getGroupCount() 
	{
		return cinemaProgramList.size();
	}

	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	/**
	 * Baut den View für die Kino-Elemente (Gruppe) der Liste zusammen und passt dessen Höhe an das Hintergrundbild an.
	 */
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{			
		View groupView = null;
				
		if (convertView == null)
		{				
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			groupView = (View)inflater.inflate(R.layout.search_result_cinema, null);
		}
		else
			groupView = (View)convertView;
			
		String title = getGroup(groupPosition).toString();
		TextView groupTitle = (TextView)groupView.findViewById(R.id.search_result_cinema_title_textView);
		groupTitle.setText(title);	
		
		Point size = new Point();
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		display.getSize(size);
		
		// Höhe berechnen
		int numOfLinesTitle = 1;
		int heightTitle = 0;
		
		if (size.x > size.y) 	// landscape
		{
			if (title.length() % NUM_OF_CHARS_CINEMA_TITLE_LAND == 0)
				numOfLinesTitle = title.length() / NUM_OF_CHARS_CINEMA_TITLE_LAND;
			else
				numOfLinesTitle = title.length() / NUM_OF_CHARS_CINEMA_TITLE_LAND + 1;
		}
		else					// portrait
		{			
			if (title.length() % NUM_OF_CHARS_CINEMA_TITLE_PORT == 0)
				numOfLinesTitle = title.length() / NUM_OF_CHARS_CINEMA_TITLE_PORT;
			else
				numOfLinesTitle = title.length() / NUM_OF_CHARS_CINEMA_TITLE_PORT + 1;
		}
		
		// 18 sp in px umrechnen
		int lineHeightTitle = (int)(18 * context.getResources().getDisplayMetrics().scaledDensity);
		
		int dpToPx = context.getResources().getDisplayMetrics().densityDpi / 160;
		int paddingTitle = 2 * 10 * dpToPx;	
		int paddingBetweenLines = (numOfLinesTitle - 1) * 5 * dpToPx;
		
		heightTitle = numOfLinesTitle * lineHeightTitle + paddingTitle + paddingBetweenLines;
		
		// Höhe an Hintergrundmuster anpassen
		if(heightTitle % 36 != 0)
		{
			groupView.setMinimumHeight(heightTitle + (36 - (heightTitle % 36)));
		}
		else
		{
			groupView.setMinimumHeight(heightTitle);
		}
		
		return groupView;
	}

	public boolean hasStableIds() 
	{
		return true;
	}

	/**
	 * Grundsätzlich sind alle Filme auswählbar, denn auch Filme ohne Bewertung müssen sich zu den Favoriten hinzufügen lassen.
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}		
}