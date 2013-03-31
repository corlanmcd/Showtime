package de.fhe.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenstruktur für die Spielzeiten eines Films.
 * 
 * @author Senta und Samuel
 *
 */
public class MovieShowTimes implements Serializable
{
	private static final long serialVersionUID = -5670113386179846091L;
	
	private String movie = null;
	private int rating = 0;
	private String movieDetailsUrl = null;
	private ArrayList<ShowTimes> dateShowTimes = null;
	
	public MovieShowTimes()
	{
	}

	public MovieShowTimes(String movieTitle, int rating, String movieDetailsUrl)
	{
		this.movie = movieTitle;
		this.rating = rating;
		this.movieDetailsUrl = movieDetailsUrl;
		this.dateShowTimes = new ArrayList<ShowTimes>();
	}

	public String getMovie()
	{
		return movie;
	}
	
	public void setMovie(String movie) 
	{
		this.movie = movie;
	}

	public int getRating()
	{
		return rating;
	}
	
	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getMovieDetailsUrl()
	{
		return movieDetailsUrl;
	}
	
	public void setMovieDetailsUrl(String movieDetailsUrl) 
	{
		this.movieDetailsUrl = movieDetailsUrl;
	}

	public ArrayList<ShowTimes> getDateShowTimes()
	{
		return dateShowTimes;
	}
	
	public void setDateShowTimes(ArrayList<ShowTimes> dateShowTimes) 
	{
		this.dateShowTimes = dateShowTimes;
	}
	
	public void addDateShowTimes(ShowTimes showTime)
	{
		dateShowTimes.add(showTime);
	}
	
	public ShowTimes getDateShowTimesAt(int index)
	{
		return dateShowTimes.get(index);
	}
}
