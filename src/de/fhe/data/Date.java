package de.fhe.data;

/**
 * Datenstruktur für den Spinner (Datum) des Suchformulars.
 * 
 * @author Senta und Samuel
 *
 */
public class Date
{
	private String dateDisplay = null;
	private String dateId = null;
	
	public Date(String dateDisplay, String dateId)
	{
		this.dateDisplay = dateDisplay;
		this.dateId = dateId;
	}

	public String getDateDisplay()
	{
		return dateDisplay;
	}

	public String getDateId()
	{
		return dateId;
	}
	
	/**
	 * Wird durch den <code>ArrayAdapter</code> des Spinners der
	 * <code>SearchActivity</code> zur Anzeige der Daten benötigt.
	 * 
	 * @return Datum
	 */
	@Override
	public String toString()
	{
		return dateDisplay;
	}
}
