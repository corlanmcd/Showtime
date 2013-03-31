package de.fhe.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Favorite 
{
	private String searchDate = null;
	private ArrayList<FavoriteEntry> entries = null;
	
	private static final SimpleDateFormat rawFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.GERMANY);
	private static final SimpleDateFormat viewFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
	
	public Favorite(String searchDate) 
	{
		this.searchDate = searchDate;
		this.entries = new ArrayList<FavoriteEntry>();
	}

	public String getSearchDate() 
	{
		return searchDate;
	}
	
	public void setSearchDate(String searchDate) 
	{
		this.searchDate = searchDate;
	}
	
	public ArrayList<FavoriteEntry> getEntries() 
	{
		return entries;
	}
	
	public void setEntries(ArrayList<FavoriteEntry> entries) 
	{
		this.entries = entries;
	}
	
	public FavoriteEntry getEntryAt(int index)
	{
		return entries.get(index);
	}

	@Override
	public String toString()
	{
		try
		{
			return viewFormat.format(rawFormat.parse(searchDate));
		}
		catch (ParseException e)
		{
			return searchDate;
		}
	}
}
