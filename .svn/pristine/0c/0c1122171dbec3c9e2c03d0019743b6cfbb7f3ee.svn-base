package de.fhe.data;

import java.io.Serializable;

/**
 * Beinhaltet die Daten eines Films aus der Filmliste <code>MovieListActivity</code>.
 * Es werden Titel und ID gespeichert.
 * 
 * @author Samuel und Senta
 *
 */
public class MovieOption implements Serializable
{
	private static final long serialVersionUID = -130062486071623789L;
	
	private String movieTitle = null;
	private String movieOptionId = null;
	
	public MovieOption()
	{
	}

	public MovieOption(String movieTitle, String movieOptionId)
	{
		this.movieTitle = movieTitle;
		this.movieOptionId = movieOptionId;
	}

	public String getMovieTitle()
	{
		return movieTitle;
	}
	
	public void setMovieTitle(String movieTitle)
	{
		this.movieTitle = movieTitle;
	}

	public String getMovieOptionId()
	{
		return movieOptionId;
	}

	public void setMovieOptionId(String movieOptionId)
	{
		this.movieOptionId = movieOptionId;
	}

	/**
	 * Wird durch den <code>IndexedArrayAdapter</code> der
	 * <code>MovieListActivity</code> zur Anzeige der Filmtitel benötigt.
	 * 
	 * @return Titel des Films
	 */
	@Override
	public String toString()
	{
		return movieTitle;
	}
}
