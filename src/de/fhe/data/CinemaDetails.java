package de.fhe.data;

import java.io.Serializable;


/**
 * Datenstruktur für die Detailansicht eines Kinos.
 * 
 * @author Senta und Samuel
 *
 */
public class CinemaDetails implements Serializable
{
	private static final long serialVersionUID = -3736478732344624111L;
	
	private String title = null;
	private String address = null;
	
	private String prices = null;
	private String specials = null;
	
	private String phone = null;
	private String fax = null;
	private String web = null;
	private String email = null;
	
	private String direction = null;
	
	public CinemaDetails()
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
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getPrices()
	{
		return prices;
	}
	
	public void setPrices(String prices)
	{
		this.prices = prices;
	}
	
	public String getSpecials()
	{
		return specials;
	}
	
	public void setSpecials(String specials)
	{
		this.specials = specials;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public String getFax()
	{
		return fax;
	}
	
	public void setFax(String fax)
	{
		this.fax = fax;
	}
	
	public String getWeb()
	{
		return web;
	}
	
	public void setWeb(String web)
	{
		this.web = web;
	}
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getDirection()
	{
		return direction;
	}
	
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
}
