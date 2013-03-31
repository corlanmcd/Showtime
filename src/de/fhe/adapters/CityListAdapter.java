package de.fhe.adapters;

import java.util.List;

import de.fhe.activities.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter für die Liste der Städte.
 * 
 * @author Senta und Samuel
 *
 * @param <T>
 */
public class CityListAdapter<T> extends ArrayAdapter<T> 
{
	private Context context;
	private int resourceId;

	public CityListAdapter(Context context, int textViewResourceId) 
	{
		super(context, textViewResourceId);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	public CityListAdapter(Context context, int resource, int textViewResourceId, List<T> objects) 
	{
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	public CityListAdapter(Context context, int resource, int textViewResourceId, T[] objects) 
	{
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	public CityListAdapter(Context context, int resource, int textViewResourceId) 
	{
		super(context, resource, textViewResourceId);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	public CityListAdapter(Context context, int textViewResourceId, List<T> objects) 
	{
		super(context, textViewResourceId, objects);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	public CityListAdapter(Context context, int textViewResourceId, T[] objects) 
	{
		super(context, textViewResourceId, objects);
		this.context = context;
		this.resourceId = textViewResourceId;
	}

	/**
	 * Baut den View für die Einträge zusammen und passt deren Höhe an das Hintergrundbild an.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = null;
		
		if (convertView == null)
		{		
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resourceId, null);
		}
		else
			view = convertView;
		
		String title = getItem(position).toString();
		TextView textView = (TextView)view.findViewById(R.id.search_result_city_title_textView);	
		textView.setText(title);
		
		Point size = new Point();
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		display.getSize(size);
		
		// Höhe berechnen
		int numOfLinesTitle = 1;
		int heightTitle = 0;
		
		if (size.x > size.y) 	// landscape
		{
			numOfLinesTitle = title.length() / 51;
		}
		else					// portrait
		{
			numOfLinesTitle = title.length() / 23;
		}
		
		numOfLinesTitle++;
		
		// 18 sp in px umrechnen
		int lineHeightTitle = (int)(18 * context.getResources().getDisplayMetrics().scaledDensity);
		
		int dpToPx = context.getResources().getDisplayMetrics().densityDpi / 160;
		int paddingTitle = 2 * 10 * dpToPx;	
		int paddingBetweenLines = (numOfLinesTitle - 1) * 5 * dpToPx;
		
		heightTitle = numOfLinesTitle * lineHeightTitle + paddingTitle + paddingBetweenLines;
		
		// Höhe an Hintergrundmuster anpassen
		if(heightTitle % 36 != 0)
		{
			textView.setMinHeight(heightTitle + (36 - (heightTitle % 36)));
		}
		else
		{
			textView.setMinHeight(heightTitle);
		}
		
		return view;
	}
}
