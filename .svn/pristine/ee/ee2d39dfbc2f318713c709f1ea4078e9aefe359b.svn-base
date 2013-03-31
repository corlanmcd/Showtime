package de.fhe.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenstruktur für das Programm eines Kinos.
 * 
 * @author Senta und Samuel
 *
 */
public class CinemaProgram implements Serializable
{
	private static final long serialVersionUID = 915013315108699640L;
	
	private String cinema = null;
	private String cinemaDetailsUrl = null;
	private ArrayList<MovieShowTimes> movieShowTimes = null;
	
	public CinemaProgram()
	{
	}
	
	public CinemaProgram(String cinema, String cinemaDetailsUrl)
	{
		this.cinema = cinema;
		this.cinemaDetailsUrl = cinemaDetailsUrl;
		this.movieShowTimes = new ArrayList<MovieShowTimes>();
	}

	public String getCinema()
	{
		return cinema;
	}
	
	public void setCinema(String cinema)
	{
		this.cinema = cinema;
	}

	public String getCinemaDetailsUrl()
	{
		return cinemaDetailsUrl;
	}
	
	public void setCinemaDetailsUrl(String cinemaDetailsUrl)
	{
		this.cinemaDetailsUrl = cinemaDetailsUrl;
	}

	public ArrayList<MovieShowTimes> getMovieShowTimes()
	{
		return movieShowTimes;
	}
	
	public void setMovieShowTimes(ArrayList<MovieShowTimes> movieShowTimes)
	{
		this.movieShowTimes = movieShowTimes;
	}
	
	public void addMovieShowTimes(MovieShowTimes showTimes)
	{
		movieShowTimes.add(showTimes);
	}
	
	public MovieShowTimes getMovieShowTimesAt(int index)
	{
		return movieShowTimes.get(index);
	}
}
