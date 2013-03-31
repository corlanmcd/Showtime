package de.fhe.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenstruktur für das Kinoprogramm einer Stadt.
 * 
 * @author Senta und Samuel
 *
 */
public class CityProgram implements Serializable
{
	private static final long serialVersionUID = 6890199936118024447L;
	
	private String city = null;
	private ArrayList<CinemaProgram> cinemaPrograms = null;
	
	public CityProgram()
	{
	}

	public CityProgram(String city)
	{
		this.city = city;
		cinemaPrograms = new ArrayList<CinemaProgram>();
	}

	public String getCity()
	{
		return city;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}

	public ArrayList<CinemaProgram> getCinemaPrograms()
	{
		return cinemaPrograms;
	}
	
	public void setCinemaPrograms(ArrayList<CinemaProgram> cinemaPrograms)
	{
		this.cinemaPrograms = cinemaPrograms;
	}
	
	public void addCinemaProgram(CinemaProgram program)
	{
		cinemaPrograms.add(program);
	}
	
	public CinemaProgram getCinemaProgramAt(int index)
	{
		return cinemaPrograms.get(index);
	}

	@Override
	public String toString()
	{
		return city;
	}
}
