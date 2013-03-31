package de.fhe.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenstruktur für die Spielzeiten eines Tages.
 * 
 * @author Senta und Samuel
 *
 */
public class ShowTimes implements Serializable
{
	private static final long serialVersionUID = 7540513690580947941L;
	
	private String date = null;
	private ArrayList<String> showTimes = null;
	
	public ShowTimes()
	{
	}
	
	public ShowTimes(String date) 
	{
		this.date = date;
		this.showTimes = new ArrayList<String>();
	}

	public String getDate() 
	{
		return date;
	}
	
	public void setDate(String date) 
	{
		this.date = date;
	}

	public ArrayList<String> getShowTimes() 
	{
		return showTimes;
	}
	
	public void setShowTimes(ArrayList<String> showTimes)
	{
		this.showTimes = showTimes;
	}

	public void addShowTime(String time)
	{
		showTimes.add(time);		
	}
	
	public String getShowTimeAt(int index)
	{
		return showTimes.get(index);
	}
}
