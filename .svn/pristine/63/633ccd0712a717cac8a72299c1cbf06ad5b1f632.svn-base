package de.fhe.adapters;

import java.util.ArrayList;
import java.util.List;

import de.fhe.data.MovieOption;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

/**
 * Adapter für das Befüllen des ListViews der <code>MovieListActivity</code>.
 * Dazu wird intern ein ArrayAdapter verwendet, der als SectionIndexer arbeitet.
 * Das bedeutet, dass sehr schnell per Index durch die sehr große Filmliste navigiert werden kann.
 * 
 * @author Samuel und Senta
 *
 */
public class MovieOptionIndexedArrayAdapter extends ArrayAdapter<MovieOption> implements SectionIndexer
{
	private Object[] sections = null;
	
	/**
	 * Verwendet den internen ArrayAdapter.
	 * 
	 * @param context aktueller Kontext
	 * @param textViewResourceId Resource-ID eines Layouts für die Elemente
	 * @param objects Objekte für die Elemente des ListViews
	 */
	public MovieOptionIndexedArrayAdapter(Context context, int textViewResourceId, List<MovieOption> objects, boolean isSubList)
	{
		super(context, textViewResourceId, objects);
		
		if (objects.size() > 0)
		{
			// Sektionen aus der Liste der Filme generieren
			ArrayList<String> tempSections = new ArrayList<String>();
			String prevBegin = null;
			int startIndex = 0;
			
			if (!isSubList)
			{				
				prevBegin = new String("Alle");
				tempSections.add(prevBegin);
				startIndex = 1;
			}
			else
			{
				prevBegin = objects.get(0).getMovieTitle().substring(0, 1);	
				tempSections.add(prevBegin);
			}
				
			for (int i = startIndex; i < objects.size(); i++)
			{
				String begin = objects.get(i).getMovieTitle().substring(0, 1);				
				if (begin.matches("\\w") && !prevBegin.equals(begin))	//"\\w" --> nur Sektionen, die mit Buchstaben oder Zahlen beginnen
				{
					tempSections.add(begin);
					prevBegin = begin;
				}
			}
			
			sections = tempSections.toArray();
		}
		else
			sections = new Object[0];
	}

	/**
	 * Zu einer gegebenen Section (Index) wird die Position
	 * des ersten Elementes in der Liste ermittelt.
	 */
	public int getPositionForSection(int sectionIndex)
	{
		int position = 0;
		
		// wenn sectionIndex außerhalb der Grenzen von sections,
		// setze auf letzte Sektion
		if (sectionIndex >= sections.length)
		{
			sectionIndex--;
		}
		String section = (String)sections[sectionIndex];
		
		// Sonderfall: alle Filme
		if (sectionIndex == 0)
			return position;
		
		while (position == 0)
		{
			// suche nach erstem Element der Sektion
			for (int i = 1; i < getCount(); i++)
			{
				if (getItem(i).getMovieTitle().startsWith(section))
				{
					position = i;
					break;
				}
			}
			
			if (position == 0)
			{
				sectionIndex++;
				
				if (sectionIndex >= sections.length)
				{
					sectionIndex--;
				}
				
				section = (String)sections[sectionIndex];
			}
		}
		
		return position;
	}

	/**
	 * Zu einer gegebenen Position eines Elementes in der Liste
	 * wird die zugehörige Section (Index) ermittelt.
	 */
	public int getSectionForPosition(int position)
	{
		int sectionIndex = 0;
		
		// wenn position außerhalb der Grenzen von movieList,
		// setze auf letztes Element
		if (position >= getCount())
		{
			position--;
		}
		
		// Sonderfall: alle Filme
		if (position == 0)
			return sectionIndex;
		
		String section = getItem(position).getMovieTitle().substring(0, 1);
		
		for (int i = 0; i < sections.length; i++)
		{
			if (sections[i].equals(section))
			{
				sectionIndex = i;
				break;
			}
		}
		
		return sectionIndex;
	}

	/**
	 * Gibt die Liste der Sektionen (Indizes) zurück.
	 */
	public Object[] getSections()
	{
		return sections;
	}
}
