package de.fhe.activities;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import de.fhe.data.CinemaDetails;
import de.fhe.util.DialogFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activity mit eingebettetem ListView für das Laden, Verarbeiten und Darstellen der Kinoinfos.
 * Sollte keine Internetverbindung bestehen, weist ein Dialog darauf hin.
 * 
 * @author Samuel und Senta
 *
 */
public class CinemaDetailsActivity extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener
{	
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;
	
	private RestCinemaDetailsTask restTask = null;
	
	private String detailsUrl = null;
	private CinemaDetails cinemaData = null;
	
	/**
	 * Initialisiert den View der Activity. Dazu wird der Titel gesetzt.
	 * Danach werden in einem separaten Task die Kinoinfos geladen und verarbeitet.
	 * Die Ergebnisse werden in den View geladen. Können die Kinoinfos nicht geladen werden, wird ein Dialog angezeigt.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.cinema_details_title));
		getActionBar().setDisplayUseLogoEnabled(false);
		setContentView(R.layout.start_background_film_reel_light);
		
		dialogFactory = new DialogFactory(this);
		
		detailsUrl = getIntent().getExtras().getString("cinemaDetailsUrl");
		
		if (detailsUrl != null)
		{
			// Filminfos wurden beim Kippen des Gerätes gesichert
			if (savedInstanceState != null && savedInstanceState.getSerializable("cinemaData") != null)
			{
				cinemaData = (CinemaDetails)savedInstanceState.getSerializable("cinemaData");
				loadCinemaDetailsIntoView(cinemaData);
			}
			// Filminfos müssen neu geladen werden
			else
			{
				progressDialog = ProgressDialog.show(this, getString(R.string.cinema_details_progress), getString(R.string.please_wait), false, true, this);
				loadCinemaDetailsInBackground();
			}
		}
		// keine URL übergeben
		else
		{
			Toast.makeText(this, R.string.cinema_details_urlErrorText, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Lädt das Optionsmenü und fügt dafür in die ActionBar ein ActionItem mit ActionProvider ein.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// füge action item mit action provider zur action bar hinzu
		getMenuInflater().inflate(R.menu.actionitems_cinema_details, menu);
		menu.findItem(R.id.actionitems_itemCinemaDetails).setActionProvider(new CinemaDetailsActionProvider(this));
	    
	    return true;
	}

	/**
	 * Speichert den Zustand.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("cinemaData", cinemaData);
		
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Beendet den eventuell offenen Progress-Dialog und alle laufenden AsyncTasks.
	 */
	@Override
	public void onDestroy()
	{
		if (progressDialog != null)
			progressDialog.dismiss();
		
		if (restTask != null)
			restTask.cancel(true);
		
		super.onDestroy();
	}

	/**
	 * Lädt die Kinoinfos. Anfragen bei details_url nach Kinoinfos.
	 * Tritt dabei ein Fehler auf (keine Internetverbindung, Server nicht erreichbar, Parsen fehlgeschlagen),
	 * wird eine ungültige Referenz zurückgegeben.
	 * 
	 */
	private void loadCinemaDetailsInBackground()
    {
		restTask = new RestCinemaDetailsTask();
		restTask.execute(detailsUrl);
    }
	
	/**
	 * Lädt die geladenen Informationen zum Film in den View. Skaliert das Bild in Landscape-Orientierung entsprechend.
	 * 
	 * @param movieData Informationen zum Film
	 */
	private void loadCinemaDetailsIntoView(CinemaDetails cinemaData)
	{
		setContentView(R.layout.cinema_details);
		
		if (cinemaData.getTitle() != null)
			((TextView)findViewById(R.id.cinema_details_textViewTitle)).setText(cinemaData.getTitle());
		if (cinemaData.getAddress() != null)
			((TextView)findViewById(R.id.cinema_details_textViewAddressRight)).setText(cinemaData.getAddress());
		if (cinemaData.getPrices() != null)
			((TextView)findViewById(R.id.cinema_details_textViewPricesRight)).setText(cinemaData.getPrices());
		if (cinemaData.getSpecials() != null)
			((TextView)findViewById(R.id.cinema_details_textViewSpecialsRight)).setText(cinemaData.getSpecials());
		if (cinemaData.getPhone() != null)
			((TextView)findViewById(R.id.cinema_details_textViewPhoneRight)).setText(cinemaData.getPhone());
		if (cinemaData.getFax() != null)
			((TextView)findViewById(R.id.cinema_details_textViewFaxRight)).setText(cinemaData.getFax());
		if (cinemaData.getWeb() != null)
			((TextView)findViewById(R.id.cinema_details_textViewWebRight)).setText(cinemaData.getWeb());
		if (cinemaData.getEmail() != null)
			((TextView)findViewById(R.id.cinema_details_textViewEmailRight)).setText(cinemaData.getEmail());
		if (cinemaData.getDirection() != null)
			((TextView)findViewById(R.id.cinema_details_textViewDirectionRight)).setText(cinemaData.getDirection());
	}
	
	/**
	 * Callback für den OK-Button der Dialoge. Beendet die Activity.
	 */
	public void onClick(DialogInterface dialog, int which)
	{
		finish();
	}
	
	/**
	 * Callback für Abbrechen des Progress-Dialogs.
	 */
	public void onCancel(DialogInterface dialog)
	{
		if (restTask != null)
			restTask.cancel(true);
		
		finish();
	}
	
	/**
	 * Asynchroner Task für das Senden eines HTTP-Requests an die angegebene URL.
	 * Gibt den HTTP-Response (u. a. Inhalt der Website) zurück.
	 * 
	 * @author Senta und Samuel
	 *
	 */
	private class RestCinemaDetailsTask extends AsyncTask<String, Void, CinemaDetails>
	{
		private boolean noInternetConnection = false;
		private boolean serviceNotRunning = false;
		private boolean badRequestError = false;
		private boolean parsingError = false;
		private boolean serviceUnavailable = false;
		
		@Override
		protected CinemaDetails doInBackground(String... url)
		{
			CinemaDetails cinemaData = null;
			
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				cinemaData = restTemplate.getForObject(url[0], CinemaDetails.class);
			}
			catch (ResourceAccessException ex)
			{
				// keine Internetverbindung
				noInternetConnection = true;
			}
			catch (HttpClientErrorException ex)
			{
				// Service läuft nicht - HTTP 404
				if (ex.getMessage().equals("404 Not Found"))
				{
					serviceNotRunning = true;
				}
				// falscher Parameter id - HTTP 400
				else if (ex.getMessage().equals("400 Bad Request"))
				{
					badRequestError = true;
				}
			}
			catch (HttpServerErrorException ex)
			{
				// Parsen - HTTP 500
				if (ex.getMessage().equals("500 Internal Server Error"))
				{
					parsingError = true;
				}
				// cinema.de nicht verfügbar - HTTP 503
				else if (ex.getMessage().equals("503 Service Unavailable"))
				{
					serviceUnavailable = true;
				}
			}
			
			return cinemaData;
		}
		
		@Override
		protected void onPostExecute(CinemaDetails result)
		{
			if (result != null)
			{
				cinemaData = result;
				progressDialog.dismiss();
				loadCinemaDetailsIntoView(cinemaData);
			}
			// Fehlerbehandlung
			else
			{
				if (noInternetConnection)
				{
					dialogFactory.createDialog(R.string.no_connection, R.string.cinema_details_noConnectionText, CinemaDetailsActivity.this).show();
				}
				else if (serviceNotRunning || serviceUnavailable)
				{
					dialogFactory.createDialog(R.string.app_service_unavailable, R.string.app_service_unavailableText, CinemaDetailsActivity.this).show();
				}
				else if (badRequestError)
				{
					dialogFactory.createDialog(R.string.app_bad_request, R.string.app_bad_requestText, CinemaDetailsActivity.this).show();
				}
				else if (parsingError)
				{
					dialogFactory.createDialog(R.string.parsing_error, R.string.cinema_details_parsingErrorText, CinemaDetailsActivity.this).show();
				}
			}
		}
	}
	
	/**
	 * Erstellt Menü und behandelt Auswahl eines Menüeintrags: Kino auf Karte anzeigen, Website öffnen, Anrufen.
	 * 
	 * @author Senta und Samuel
	 *
	 */
	private class CinemaDetailsActionProvider extends ActionProvider implements OnMenuItemClickListener
	{
		public static final int ADDRESS_ITEM = 1;
		public static final int WEB_ITEM = 2;
		public static final int PHONE_ITEM = 3;
		
		public CinemaDetailsActionProvider(Context context)
		{
			super(context);
		}

		@Override
		public View onCreateActionView()
		{
			return null;
		}

		@Override
		public boolean hasSubMenu()
		{
			return true;
		}
		
		@Override
		public void onPrepareSubMenu(SubMenu subMenu)
		{
			subMenu.clear();
			
			if (cinemaData != null)
			{
				if (cinemaData.getAddress() != null)
				{
					MenuItem item = subMenu.add(0, ADDRESS_ITEM, 1, R.string.cinema_details_showOnMap);
					item.setIcon(R.drawable.ic_map);
					item.setOnMenuItemClickListener(this);
				}
				if (cinemaData.getWeb() != null)
				{
					MenuItem item = subMenu.add(0, WEB_ITEM, 2, R.string.cinema_details_showWebsite);
					item.setIcon(R.drawable.ic_website);
					item.setOnMenuItemClickListener(this);
				}
				if (cinemaData.getPhone() != null)
				{
					MenuItem item = subMenu.add(0, PHONE_ITEM, 3, R.string.cinema_details_call);
					item.setIcon(R.drawable.ic_call);
					item.setOnMenuItemClickListener(this);
				}
			}
		}
		
		public boolean onMenuItemClick(MenuItem item)
		{
			if (item.getItemId() == ADDRESS_ITEM)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + cinemaData.getAddress()));
				i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(i);
			}
			else if (item.getItemId() == WEB_ITEM)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + cinemaData.getWeb()));
				startActivity(i);
			}
			else if (item.getItemId() == PHONE_ITEM)
			{
				Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cinemaData.getPhone()));
				startActivity(i);
			}
			
			return true;
		}
	}
}
