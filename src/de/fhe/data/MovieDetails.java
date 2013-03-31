package de.fhe.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenstruktur für die Detailansicht eines Films.
 * 
 * @author Samuel und Senta
 *
 */
public class MovieDetails implements Serializable
{	
	private static final long serialVersionUID = -5225746887231004294L;
	
	private String title = null;
	private String subtitle = null;
	private String imageUrl = null;
	private String videoUrl = null;
	
	private int ratingDemand = 0;
	private int ratingTension = 0;
	private int ratingAction = 0;
	private int ratingHumor = 0;
	private int ratingEroticism = 0;
	
	private String summary = null;
	private String description = null;
	private String conclusion = null;
	
	private ArrayList<MovieDetailsPair> additionals = null;
	private ArrayList<MovieDetailsPair> actors = null;
	
	public MovieDetails()
	{
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getSubtitle()
	{
		return subtitle;
	}
	
	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}
	
	public String getImageUrl()
	{
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	
	public String getVideoUrl()
	{
		return videoUrl;
	}
	
	public void setVideoUrl(String videoUrl)
	{
		this.videoUrl = videoUrl;
	}
	
	public int getRatingDemand()
	{
		return ratingDemand;
	}
	
	public void setRatingDemand(int ratingDemand)
	{
		this.ratingDemand = ratingDemand;
	}
	
	public int getRatingTension()
	{
		return ratingTension;
	}
	
	public void setRatingTension(int ratingTension)
	{
		this.ratingTension = ratingTension;
	}
	
	public int getRatingAction()
	{
		return ratingAction;
	}
	
	public void setRatingAction(int ratingAction)
	{
		this.ratingAction = ratingAction;
	}
	
	public int getRatingHumor()
	{
		return ratingHumor;
	}
	
	public void setRatingHumor(int ratingHumor)
	{
		this.ratingHumor = ratingHumor;
	}
	
	public int getRatingEroticism()
	{
		return ratingEroticism;
	}
	
	public void setRatingEroticism(int ratingEroticism)
	{
		this.ratingEroticism = ratingEroticism;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getConclusion()
	{
		return conclusion;
	}
	
	public void setConclusion(String conclusion)
	{
		this.conclusion = conclusion;
	}
	
	public ArrayList<MovieDetailsPair> getAdditionals()
	{
		return additionals;
	}
	
	public void setAdditionals(ArrayList<MovieDetailsPair> additionals)
	{
		this.additionals = additionals;
	}
	
	public void addAdditional(MovieDetailsPair value)
	{
		additionals.add(value);
	}
	
	public ArrayList<MovieDetailsPair> getActors()
	{
		return actors;
	}
	
	public void setActors(ArrayList<MovieDetailsPair> actors)
	{
		this.actors = actors;
	}
	
	public void addActor(MovieDetailsPair value)
	{
		actors.add(value);
	}
}
