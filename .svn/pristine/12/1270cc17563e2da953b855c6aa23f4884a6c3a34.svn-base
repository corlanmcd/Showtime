package de.fhe.data;

import java.io.Serializable;

/**
 * Enthält die Suchoptionen für eine neue Suche nach dem spezifizierten Filmprogramm.
 * Dazu werden Ort, Film, Datum/Zeit gespeichert. Wird von <code>NewSearchFragment</code> verwendet.
 * 
 * @author Samuel und Senta
 *
 */
public class SearchOptions implements Serializable
{
	private static final long serialVersionUID = 3255856982660695859L;
	
	private String location = null;
	private double locationLat = 0;
	private double locationLng = 0;
	private int radius = 0;
	private String date = null;
	private String time = null;
	private String movieId = null;
	
	public SearchOptions()
	{
		// Standardkonstruktor
	}
	
	public SearchOptions(SearchOptions other)
	{
		if (other != null)
		{
			this.location = other.location;
			this.locationLat = other.locationLat;
			this.locationLng = other.locationLng;
			this.radius = other.radius;
			this.date = other.date;
			this.time = other.time;
			this.movieId = other.movieId;
		}
	}

	public String getLocation() 
	{
		return location;
	}
	
	public void setLocation(String location) 
	{
		this.location = location;
	}

	public double getLocationLat()
	{
		return locationLat;
	}

	public void setLocationLat(double locationLat)
	{
		this.locationLat = locationLat;
	}

	public double getLocationLng()
	{
		return locationLng;
	}

	public void setLocationLng(double locationLng)
	{
		this.locationLng = locationLng;
	}

	public int getRadius() 
	{
		return radius;
	}

	public void setRadius(int radius) 
	{
		this.radius = radius;
	}
	
	public String getDate() 
	{
		return date;
	}
	
	public void setDate(String date) 
	{
		this.date = date;
	}
	
	public String getTime() 
	{
		return time;
	}
	
	public void setTime(String time) 
	{
		this.time = time;
	}

	public String getMovieId()
	{
		return movieId;
	}
	
	public void setMovieId(String movieId) 
	{
		this.movieId = movieId;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		SearchOptions other = (SearchOptions) obj;
		
		if (location == null)
		{
			if (other.location != null)
				return false;
		}
		else if (!location.equals(other.location))
		{
			return false;
		}
		
		if (radius != other.radius)
			return false;
		
		if (date == null)
		{
			if (other.date != null)
				return false;
		}
		else if (!date.equals(other.date))
		{
			return false;
		}
		
		if (time == null)
		{
			if (other.time != null)
				return false;
		}
		else if (!time.equals(other.time))
		{
			return false;
		}
		
		if (movieId == null)
		{
			if (other.movieId != null)
				return false;
		}
		else if (!movieId.equals(other.movieId))
		{
			return false;
		}
		
		return true;
	}
}
