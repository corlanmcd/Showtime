package de.fhe.data;

import java.util.ArrayList;

public class FavoriteEntry 
{
	private String movie = null;
	private String cinema = null;
	private String city = null;
	private String url = null;
	private ArrayList<ShowTimes> dateShowTimes = null;
	
	public FavoriteEntry(String movie, String cinema, String city, String url)
	{
		this.movie = movie;
		this.cinema = cinema;
		this.city = city;
		this.url = url;
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
	
	public String getCinema() 
	{
		return cinema;
	}
	
	public void setCinema(String cinema) 
	{
		this.cinema = cinema;
	}
	
	public String getCity() 
	{
		return city;
	}
	
	public void setCity(String city) 
	{
		this.city = city;
	}
	
	public String getUrl() 
	{
		return url;
	}
	
	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	public ArrayList<ShowTimes> getDateShowTimes() 
	{
		return dateShowTimes;
	}
	
	public void setDateShowTimes(ArrayList<ShowTimes> dateShowTimes) 
	{
		this.dateShowTimes = dateShowTimes;
	}
	
	public ShowTimes getDateShowTimesAt(int index)
	{
		return dateShowTimes.get(index);
	}
}
