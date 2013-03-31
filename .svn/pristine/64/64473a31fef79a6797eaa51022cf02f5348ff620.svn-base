package de.fhe.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.fhe.data.Favorite;
import de.fhe.data.FavoriteEntry;
import de.fhe.data.MovieShowTimes;
import de.fhe.data.ShowTimes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBShowtime 
{
	public static final int NO_ERROR = 0;
	public static final int ERROR_NOT_OPENED = -1;
	public static final int ERROR_DUPLICATE = -2;
	
	private static final String DB_NAME = "showtime";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_FAV_DATES = "favDates";
    private static final String TABLE_FAV_TIMES = "favTimes";
    
	private static final String KEY_HISTORY_ID = "_id"; 
    public static final String KEY_HISTORY_LOCATION = "location";
    private static final String KEY_HISTORY_TIMESTAMP = "timestamp";
    private static final int INDEX_HISTORY_ID = 0;
    private static final int INDEX_HISTORY_LOCATION = 1;
    
    private static final String KEY_FAVORITES_ID = "_id";
    private static final String KEY_FAVORITES_SEARCH = "searchDate";
    private static final String KEY_FAVORITES_MOVIE = "movieTitle";
    private static final String KEY_FAVORITES_URL = "movieUrl";
    private static final String KEY_FAVORITES_CITY = "city";
    private static final String KEY_FAVORITES_CINEMA = "cinema";
    
    private static final String KEY_FAV_DATES_ID = "_id";
    private static final String KEY_FAV_DATES_DATE = "date";
    private static final String KEY_FAV_DATES_FK = "fkFavorite";
    
    private static final String KEY_FAV_TIMES_ID = "_id";
    private static final String KEY_FAV_TIMES_TIME = "time";
    private static final String KEY_FAV_TIMES_FK = "fkDate";

    private static final String HISTORY_CREATE = "create table " + TABLE_HISTORY + " (" + 
    							KEY_HISTORY_ID + " integer primary key autoincrement, " + 
    							KEY_HISTORY_TIMESTAMP + " varchar not null, " + 
    							KEY_HISTORY_LOCATION + " varchar not null);";
    
    private static final String FAVORITES_CREATE = "create table " + TABLE_FAVORITES + " (" +
    							KEY_FAVORITES_ID + " integer primary key, " +
    							KEY_FAVORITES_SEARCH + " varchar not null, " +
    							KEY_FAVORITES_MOVIE + " varchar not null, " +
    							KEY_FAVORITES_URL + " varchar null, " +
    							KEY_FAVORITES_CITY + " varchar not null, " +
    							KEY_FAVORITES_CINEMA + " varchar not null);";
    
    private static final String FAV_DATES_CREATE = "create table " + TABLE_FAV_DATES + " (" +
    							KEY_FAV_DATES_ID + " integer primary key autoincrement, " +
    							KEY_FAV_DATES_DATE + " varchar not null, " +
    							KEY_FAV_DATES_FK + " integer not null, " + 
    							"foreign key (" + KEY_FAV_DATES_FK + ") references " + TABLE_FAVORITES + " (" + KEY_FAVORITES_ID + "));";
    
    private static final String FAV_TIMES_CREATE = "create table " + TABLE_FAV_TIMES + " (" +
								KEY_FAV_TIMES_ID + " integer primary key autoincrement, " +
								KEY_FAV_TIMES_TIME + " varchar not null, " +
								KEY_FAV_TIMES_FK + " integer not null, " + 
								"foreign key (" + KEY_FAV_TIMES_FK + ") references " + TABLE_FAV_DATES + " (" + KEY_FAV_DATES_ID + "));";							
    
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dataBase; 
    private SimpleDateFormat formatDateTime;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatShowtimeMMdd;
    private SimpleDateFormat formatShowtimeddMM;
    
    private int maxHistoryEntries; 
    
    public DBShowtime(Context context)
    {
        this.context = context;
        this.dbHelper = new DatabaseHelper(this.context);
        this.formatDateTime = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss", Locale.GERMANY);
        this.formatDate = new SimpleDateFormat("yyyy/MM/dd", Locale.GERMANY);
        this.formatShowtimeddMM = new SimpleDateFormat("dd.MM.", Locale.GERMANY);
        this.formatShowtimeMMdd = new SimpleDateFormat("MM/dd", Locale.GERMANY);
        this.maxHistoryEntries = 5;
    }
    
    public void setMaxHistoryEntries(int maxHistoryEntries)
    {
    	this.maxHistoryEntries = maxHistoryEntries;
    }
    
    public void openWrite()
    {
    	if (dataBase != null && dataBase.isOpen())
    		close();
    	
        dataBase = dbHelper.getWritableDatabase();
    }
    
    public void openRead()
    {
    	if (dataBase != null && dataBase.isOpen())
    		close();
    	
    	dataBase = dbHelper.getReadableDatabase();
    }

    public void close()
    {
    	dbHelper.close();
    }
    
    public void clear()
    {
    	dbHelper.onUpgrade(dataBase, 0, 0);
    }

    public int insertHistoryEntry(Date timestamp, String location)
    {
    	if (dataBase != null && dataBase.isOpen())
    	{
    		boolean contains = false;
    		
    		String[] locations = fetchHistoryEntries();
    		
    		for (int i = 0; i < locations.length; i++)
    		{    			
    			if (location.toLowerCase().equals(locations[i].toLowerCase()))
    			{
    				contains = true;
    				break;
    			}
    		}
    		
	        ContentValues values = new ContentValues();        
	        values.put(KEY_HISTORY_TIMESTAMP, formatDateTime.format(timestamp));
	        values.put(KEY_HISTORY_LOCATION, location);
	        
	        if (contains)
	        {
	        	dataBase.update(TABLE_HISTORY, values, KEY_HISTORY_LOCATION + "='" + location + "'", null);
	        }
	        else
	        {
	        	dataBase.insert(TABLE_HISTORY, null, values);
	        	limitHistoryEntries();
	        }
	        
	        return NO_ERROR;
    	}
    	else
    	{
    		return ERROR_NOT_OPENED;
    	}
    }
    
    public String[] fetchHistoryEntries()
    {
    	limitHistoryEntries();
    	
    	Cursor cursor = selectHistoryEntries();    	
    	String[] result = new String[cursor.getCount()];
    	
    	for (int i = 0; i < cursor.getCount(); i++)
    	{
    		cursor.moveToPosition(i);
    		result[i] = cursor.getString(INDEX_HISTORY_LOCATION);
    	}
    	
    	return result;
    }

	private void limitHistoryEntries() 
	{
		Cursor locationsDel = selectHistoryEntries();
		
		// Anzahl Einträge in Verlauf begrenzen
		while (locationsDel.getCount() > maxHistoryEntries)
		{
			locationsDel.moveToLast();
			
			int id = locationsDel.getInt(INDEX_HISTORY_ID);			
			dataBase.delete(TABLE_HISTORY, KEY_HISTORY_ID + "=" + id, null);	
			locationsDel = selectHistoryEntries();
		}
	}
    
    private Cursor selectHistoryEntries()
    {
    	if (dataBase != null && dataBase.isOpen())
    		return dataBase.rawQuery("select " + KEY_HISTORY_ID + "," + KEY_HISTORY_LOCATION + " from " + TABLE_HISTORY + " order by " + KEY_HISTORY_TIMESTAMP + " desc", null);
    	else
    		return null;
    }
    
    public int insertFavoritesEntry(Date timestamp, MovieShowTimes movie, String city, String cinema)
    {
    	if (dataBase != null && dataBase.isOpen())
    	{
    		String dateString = new String();
    		for(ShowTimes showTime : movie.getDateShowTimes())
	        {
    			dateString += showTime.getDate();
	        }
    		
    		Integer id = Integer.valueOf(new String(movie.getMovie() + city + cinema + dateString).hashCode());	
    		
    		ContentValues values = new ContentValues();  
    		values.put(KEY_FAVORITES_ID, id);
    		values.put(KEY_FAVORITES_SEARCH, formatDate.format(timestamp));
	        values.put(KEY_FAVORITES_MOVIE, movie.getMovie());
	        values.put(KEY_FAVORITES_URL, movie.getMovieDetailsUrl());
	        values.put(KEY_FAVORITES_CITY, city);
	        values.put(KEY_FAVORITES_CINEMA, cinema);
    		
        	long favId = dataBase.insert(TABLE_FAVORITES, null, values);
        	
        	if (favId != -1)
        	{
		        values.clear();
		        
		        for(ShowTimes showTime : movie.getDateShowTimes())
		        {
		        	Date date = null;
		        	
		        	try
					{
						date = formatShowtimeddMM.parse(showTime.getDate());						
					} 
		        	catch (ParseException e)
					{
		        		date = new Date();
					}
		        	
		        	values.put(KEY_FAV_DATES_DATE, formatShowtimeMMdd.format(date));
		        	values.put(KEY_FAV_DATES_FK, favId);
		        	
		        	long dateId = dataBase.insert(TABLE_FAV_DATES, null, values);
			        values.clear();
			        
			        for(String time : showTime.getShowTimes())
			        {
			        	values.put(KEY_FAV_TIMES_TIME, time);
			        	values.put(KEY_FAV_TIMES_FK, dateId);
			        	
			        	dataBase.insert(TABLE_FAV_TIMES, null, values);
				        values.clear();
			        }
		        }
	        }
        	else
	        {
	        	return ERROR_DUPLICATE;
	        }
	        
    		return NO_ERROR;
    	}
    	else
    		return ERROR_NOT_OPENED;
    }
    
    public ArrayList<Favorite> fetchFavoriteEntries()
    {
    	if (dataBase != null && dataBase.isOpen())
    	{
    		limitFavoriteEntries();	
    		
    		ArrayList<Favorite> favList = new ArrayList<Favorite>();
    		
    		Cursor curSearch = dataBase.rawQuery("select " + 		
					 KEY_FAVORITES_ID + ", " + 
					 KEY_FAVORITES_SEARCH + ", " + 
		 			 KEY_FAVORITES_MOVIE + ", " +
					 KEY_FAVORITES_URL + ", " +
					 KEY_FAVORITES_CITY + ", " +
					 KEY_FAVORITES_CINEMA +
					 " from " + TABLE_FAVORITES + " order by " + KEY_FAVORITES_SEARCH + " desc", null);
    		
    		curSearch.moveToFirst();
    		
    		while(!curSearch.isAfterLast())
    		{
				int movieIndex = curSearch.getColumnIndex(KEY_FAVORITES_MOVIE);
				int cinemaIndex = curSearch.getColumnIndex(KEY_FAVORITES_CINEMA);
				int cityIndex = curSearch.getColumnIndex(KEY_FAVORITES_CITY);
				int urlIndex = curSearch.getColumnIndex(KEY_FAVORITES_URL);
				
				int actualId = curSearch.getInt(0);
				String actualSearchDate = curSearch.getString(1);
    			
				FavoriteEntry entry = new FavoriteEntry(
						curSearch.getString(movieIndex), 
						curSearch.getString(cinemaIndex), 
						curSearch.getString(cityIndex), 
						curSearch.getString(urlIndex));
    			
    			if (favList.size() > 0 && favList.get(favList.size() - 1).getSearchDate().equals(actualSearchDate))
    			{
    				// Suchdatum vorhanden
    				favList.get(favList.size() - 1).getEntries().add(entry);  	
    			}
    			else
    			{
    				// Suchdatum nicht vorhanden
    				Favorite fav = new Favorite(actualSearchDate); 
	    			fav.getEntries().add(entry);
	    			favList.add(fav);
    			}
    			
    			// Spielzeiten einfügen    			
    			Cursor curDate = dataBase.rawQuery("select " +
						 KEY_FAV_DATES_DATE + ", " +
						 KEY_FAV_TIMES_TIME + 
						 " from " + TABLE_FAVORITES + " f" + 
						 " inner join " + TABLE_FAV_DATES + " d on f." + KEY_FAVORITES_ID + "=d." + KEY_FAV_DATES_FK + 
						 " inner join " + TABLE_FAV_TIMES + " t on d." + KEY_FAV_DATES_ID + "=t." + KEY_FAV_TIMES_FK +
						 " where " + "f." + KEY_FAVORITES_ID + "=" + actualId +
						 " order by " + KEY_FAV_DATES_DATE + " asc, " + KEY_FAV_TIMES_TIME + " asc", null);
    			
    			curDate.moveToFirst();
    			
    			while(!curDate.isAfterLast())
    			{
	    			String actualDate = curDate.getString(0);
	    			String actualTime = curDate.getString(1);
	    			Date date = null;
	    			
	    			try
					{
						date = formatShowtimeMMdd.parse(actualDate);
					} 
	    			catch (ParseException e)
					{
						date = new Date();
					}
	    			
	    			if (entry.getDateShowTimes().size() > 0 && entry.getDateShowTimesAt(entry.getDateShowTimes().size() - 1).getDate().equals(formatShowtimeddMM.format(date)))
	    			{
	    				// Datum vorhanden
	    				entry.getDateShowTimesAt(entry.getDateShowTimes().size() - 1).addShowTime(actualTime);
	    			}
	    			else
	    			{
	    				// Datum nicht vorhanden
	    				ShowTimes showTimes = new ShowTimes(formatShowtimeddMM.format(date));
	    				showTimes.addShowTime(actualTime);
	    				entry.getDateShowTimes().add(showTimes);
	    			}
	    			
	    			curDate.moveToNext();
    			}
    			
    			curSearch.moveToNext();
    		}
    		
    		return favList;
    	}
    	else
    	{
    		return null;
    	}
    }
    
    private void limitFavoriteEntries()
    {    	
    	String now = formatShowtimeMMdd.format(new Date());
    	
    	// alle vergangenen Datums und Zeiten auswählen
		Cursor pastDates = dataBase.rawQuery("select " +
				 "f." + KEY_FAVORITES_ID + ", " + 
				 "d." + KEY_FAV_DATES_ID +
				 " from " + TABLE_FAVORITES + " f" + 
				 " inner join " + TABLE_FAV_DATES + " d on f." + KEY_FAVORITES_ID + "=d." + KEY_FAV_DATES_FK + 
				 " where " + KEY_FAV_DATES_DATE + "<'" + now + "'", null);
		
		pastDates.moveToFirst();
		
		while(!pastDates.isAfterLast())
		{
			int currDateId = pastDates.getInt(1);
			
			// alle vergangenen Datums und Zeiten löschen
			dataBase.delete(TABLE_FAV_TIMES, KEY_FAV_TIMES_FK + "=" + currDateId, null);				
			dataBase.delete(TABLE_FAV_DATES, KEY_FAV_DATES_ID + "=" + currDateId, null);
			
			pastDates.moveToNext();
		}
		
		// Filme ohne Vorführungen löschen
		dataBase.delete(TABLE_FAVORITES, KEY_FAVORITES_ID + " not in (select " + KEY_FAV_DATES_FK + " from " + TABLE_FAV_DATES + ")" , null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
	{
	    DatabaseHelper(Context context)
	    {
	    	super(context, DB_NAME, null, DATABASE_VERSION);
	    }
	
	    @Override
	    public void onCreate(SQLiteDatabase db)
	    {
	        db.execSQL(HISTORY_CREATE);
	        db.execSQL(FAVORITES_CREATE);
	        db.execSQL(FAV_DATES_CREATE);
	        db.execSQL(FAV_TIMES_CREATE);
	    }
	
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	    {
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_TIMES);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_DATES);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
	        onCreate(db);
	    }
	}
}
