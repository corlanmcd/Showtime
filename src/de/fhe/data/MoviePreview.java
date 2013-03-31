package de.fhe.data;

import java.io.Serializable;

/**
 * @author Corlan McDonald
 * 
 * This class is a container for each item in the RSS Feed for the
 * "New Starts" under the Cinema.de
 */

public class MoviePreview implements Serializable
{
	private static final long serialVersionUID = 8032364584544070540L;
	
	private String title;
	private String movieURL;
	private String imageURL;
	private int redaktionRating;


	public MoviePreview()
	{
		setTitle(null);
		setMovieURL(null);
		setImageURL(null);
		setRedaktionRating(-1);
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMovieURL()
	{
		return movieURL;
	}

	public void setMovieURL(String movieURL)
	{
		this.movieURL = movieURL;
	}

	public String getImageURL()
	{
		return imageURL;
	}

	public void setImageURL(String imageURL)
	{
		this.imageURL = imageURL;
	}

	public int getRedaktionRating()
	{
		return redaktionRating;
	}

	public void setRedaktionRating(int redaktionRating)
	{
		this.redaktionRating = redaktionRating;
	}
}
