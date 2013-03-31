package de.fhe.activities;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import de.fhe.data.MovieDetails;
import de.fhe.data.MovieDetailsPair;
import de.fhe.util.DialogFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activity mit eingebettetem ListView für das Laden, Verarbeiten und Darstellen der Filminfos.
 * Sollte keine Internetverbindung bestehen, weist ein Dialog darauf hin.
 * 
 * @author Samuel und Senta
 *
 */
public class MovieDetailsActivity extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, OnClickListener
{
	private static final int[] RATINGS = { 	R.drawable.rating_bar_0,
											R.drawable.rating_bar_1,
											R.drawable.rating_bar_2,
											R.drawable.rating_bar_3,
											R.drawable.rating_bar_4,
											R.drawable.rating_bar_5  };
	
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;
	
	private RestMovieDetailsTask restTask = null;
	
	private String detailsUrl = null;
	private MovieDetails movieData = null;
	private Bitmap movieImage = null;
	private double imageRatio = 0;
	
	/**
	 * Initialisiert den View der Activity. Dazu wird der Titel gesetzt.
	 * Danach werden in einem separaten Task die Filminfos geladen und verarbeitet.
	 * Die Ergebnisse werden in den View geladen. Können die Filminfos nicht geladen werden, wird ein Dialog angezeigt.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.movie_details_title));
		getActionBar().setDisplayUseLogoEnabled(false);
		setContentView(R.layout.start_background_film_reel_light);
		
		dialogFactory = new DialogFactory(this);
		
		detailsUrl = getIntent().getExtras().getString("movieDetailsUrl");
		
		if (detailsUrl != null)
		{
			// Filminfos wurden beim Kippen des Gerätes gesichert
			if (savedInstanceState != null
				&& savedInstanceState.getSerializable("movieData") != null
				&& savedInstanceState.getParcelable("movieImage") != null
				&& savedInstanceState.getDouble("imageRatio") != 0)
			{
				movieData = (MovieDetails)savedInstanceState.getSerializable("movieData");
				movieImage = (Bitmap)savedInstanceState.getParcelable("movieImage");
				imageRatio = savedInstanceState.getDouble("imageRatio");
				
				loadMovieDetailsIntoView(movieData);
			}
			// Filminfos müssen neu geladen werden
			else
			{
				progressDialog = ProgressDialog.show(this, getString(R.string.movie_details_progress), getString(R.string.please_wait), false, true, this);
				loadMovieDetailsInBackground();
			}
		}
		// keine URL übergeben
		else
		{
			Toast.makeText(this, R.string.movie_details_urlErrorText, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Speichert den Zustand 
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("movieData", movieData);
		outState.putParcelable("movieImage", movieImage);
		outState.putDouble("imageRatio", imageRatio);
		
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Beendet den eventuell offenen Progress-Dialog und alle laufenden AsyncTasks.
	 */
	@Override
	protected void onDestroy()
	{
		if (progressDialog != null)
			progressDialog.dismiss();
		
		if (restTask != null)
			restTask.cancel(true);
		
		super.onDestroy();
	}
	
	/**
	 * Callback für den Click auf das Bild.
	 */
	public void onClick(View view)
	{
		if (movieData.getVideoUrl() != null)
		{
			Intent playVideo = new Intent(this, MovieVideoActivity.class);
			playVideo.putExtra("videoUrl", movieData.getVideoUrl());
			startActivity(playVideo);
		}
		else
		{
			Toast.makeText(this, R.string.movie_details_noVideo, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Lädt die Filminfos. Dazu wird in folgenden Schritten vorgegangen:
	 * 1. Anfragen bei details_url nach Filminfos.
	 * 2. Nachladen eines Bildes mit der URL aus den Filmdaten.
	 * Tritt dabei ein Fehler auf (keine Internetverbindung, Server nicht erreichbar, Parsen fehlgeschlagen),
	 * wird eine ungültige Referenz zurückgegeben.
	 * 
	 */
	private void loadMovieDetailsInBackground()
    {
		restTask = new RestMovieDetailsTask();
		restTask.execute(detailsUrl);
    }
	
	/**
	 * Lädt die geladenen Informationen zum Film in den View. Skaliert das Bild in Landscape-Orientierung entsprechend.
	 * 
	 * @param movieData Informationen zum Film
	 */
	private void loadMovieDetailsIntoView(MovieDetails movieData)
	{
		setContentView(R.layout.movie_details);
		
		if (movieData.getTitle() != null)
			((TextView)findViewById(R.id.movie_details_textViewTitle)).setText(movieData.getTitle());
		if (movieData.getSubtitle() != null)
			((TextView)findViewById(R.id.movie_details_textViewSubTitle)).setText(movieData.getSubtitle());
		
		if (movieImage != null)
		{
			Point size = new Point();
			Display display = getWindowManager().getDefaultDisplay();
			display.getSize(size);
			
			// Skalierung des Bildes an Breite des Displays
			int scaledWidth = size.x;
			int scaledHeight = Double.valueOf(size.x / imageRatio).intValue();
			Bitmap scaledImage = Bitmap.createScaledBitmap(movieImage, scaledWidth, scaledHeight, true);
			((ImageView)findViewById(R.id.movie_details_imageViewMovie)).setImageBitmap(scaledImage);
			
			if (movieData.getVideoUrl() != null)
				findViewById(R.id.movie_details_imageViewMovie).setOnClickListener(this);
			else
				findViewById(R.id.movie_details_imageViewPlay).setVisibility(View.GONE);
		}
		else
		{
			findViewById(R.id.movie_details_relativeLayoutImage).setVisibility(View.GONE);
			findViewById(R.id.movie_details_divider2).setVisibility(View.GONE);
		}
		
		if (!(movieData.getRatingDemand() == 0
			&& movieData.getRatingTension() == 0
			&& movieData.getRatingAction() == 0
			&& movieData.getRatingHumor() == 0
			&& movieData.getRatingEroticism() == 0))
		{
			((ImageView)findViewById(R.id.movie_details_ratingBarDemand)).setImageResource(RATINGS[movieData.getRatingDemand()]);
			((ImageView)findViewById(R.id.movie_details_ratingBarTension)).setImageResource(RATINGS[movieData.getRatingTension()]);
			((ImageView)findViewById(R.id.movie_details_ratingBarAction)).setImageResource(RATINGS[movieData.getRatingAction()]);
			((ImageView)findViewById(R.id.movie_details_ratingBarHumor)).setImageResource(RATINGS[movieData.getRatingHumor()]);
			((ImageView)findViewById(R.id.movie_details_ratingBarEroticism)).setImageResource(RATINGS[movieData.getRatingEroticism()]);
		}
		else
		{
			findViewById(R.id.movie_details_gridRatings).setVisibility(View.GONE);
			findViewById(R.id.movie_details_divider3).setVisibility(View.GONE);
		}
		
		if (movieData.getSummary() != null)
			((TextView)findViewById(R.id.movie_details_textViewSummary)).setText(movieData.getSummary());
		else
			((TextView)findViewById(R.id.movie_details_textViewSummary)).setVisibility(View.GONE);
		
		if (movieData.getDescription() != null)
			((TextView)findViewById(R.id.movie_details_textViewDescription)).setText(movieData.getDescription());
		
		if (movieData.getConclusion() != null)
			((TextView)findViewById(R.id.movie_details_textViewConclusion)).setText(movieData.getConclusion());
		else
			((TextView)findViewById(R.id.movie_details_textViewConclusion)).setVisibility(View.GONE);
		
		// View für Zusatzinfos dynamisch bauen (Anzahl unbekannt)
		GridLayout gridAdditional = ((GridLayout)findViewById(R.id.movie_details_gridAdditional));
		for (int i = 0; i < movieData.getAdditionals().size(); i++)
		{
			MovieDetailsPair additional = movieData.getAdditionals().get(i);
			gridAdditional.addView(createGridEntryLeft(additional.getFirst()));
			gridAdditional.addView(createGridEntryRight(additional.getSecond()));
		}
		if (gridAdditional.getChildCount() == 0)
		{
			gridAdditional.addView(createGridEntryLeft(getString(R.string.no_info)));
		}
		
		// View für Schauspieler dynamisch bauen (Anzahl unbekannt)
		GridLayout gridActors = ((GridLayout)findViewById(R.id.movie_details_gridActors));
		for (int i = 0; i < movieData.getActors().size(); i++)
		{
			MovieDetailsPair actor = movieData.getActors().get(i);
			gridActors.addView(createGridEntryLeft(actor.getFirst()));
			gridActors.addView(createGridEntryRight(actor.getSecond()));
		}
		if (gridActors.getChildCount() == 0)
		{
			gridActors.addView(createGridEntryLeft(getString(R.string.no_info)));
		}
	}

	/**
	 * Dynamisches Erzeugen eines TextViews für einen GridView mit Style textAppearanceMedium und 5 dp Padding rechts.
	 * 
	 * @param Text für den View
	 * @return erzeugter View
	 */
	private TextView createGridEntryLeft(String text)
	{
		int dpToPx = getResources().getDisplayMetrics().densityDpi / 160;
		Point size = new Point();
		Display display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.width = (size.x / 2) - 36 - (15 * dpToPx);
		
		TextView textView = new TextView(this);
		textView.setLayoutParams(params);
		textView.setPadding(0, 0, 5 * dpToPx, 0);
		textView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		textView.setText(text);
		
		return textView;
	}
	
	/**
	 * Dynamisches Erzeugen eines TextViews für einen GridView mit Style textAppearanceMedium und 5 dp Padding links.
	 * 
	 * @param Text für den View
	 * @return erzeugter View
	 */
	private TextView createGridEntryRight(String text)
	{
		int dpToPx = getResources().getDisplayMetrics().densityDpi / 160;
		Point size = new Point();
		Display display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.width = (size.x / 2) - 36 - (15 * dpToPx);
		
		TextView textView = new TextView(this);
		textView.setLayoutParams(params);
		textView.setPadding(5 * dpToPx, 0, 0, 0);
		textView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		textView.setText(text);
		
		return textView;
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
	private class RestMovieDetailsTask extends AsyncTask<String, Void, MovieDetails>
	{
		private boolean noInternetConnection = false;
		private boolean serviceNotRunning = false;
		private boolean badRequestError = false;
		private boolean parsingError = false;
		private boolean serviceUnavailable = false;
		
		@Override
		protected MovieDetails doInBackground(String... url)
		{
			MovieDetails movieData = null;
			
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				movieData = restTemplate.getForObject(url[0], MovieDetails.class);
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
			
			return movieData;
		}
		
		@Override
		protected void onPostExecute(MovieDetails result)
		{
			if (result != null)
			{
				// Filminfos speichern, Bild nachladen
				movieData = result;
				
				ImageLoaderTask imageTask = new ImageLoaderTask();
				imageTask.execute(getString(R.string.app_base_url) + movieData.getImageUrl());
			}
			// Fehlerbehandlung
			else
			{
				if (noInternetConnection)
				{
					dialogFactory.createDialog(R.string.no_connection, R.string.movie_details_noConnectionText, MovieDetailsActivity.this).show();
				}
				else if (serviceNotRunning || serviceUnavailable)
				{
					dialogFactory.createDialog(R.string.app_service_unavailable, R.string.app_service_unavailableText, MovieDetailsActivity.this).show();
				}
				else if (badRequestError)
				{
					dialogFactory.createDialog(R.string.app_bad_request, R.string.app_bad_requestText, MovieDetailsActivity.this).show();
				}
				else if (parsingError)
				{
					dialogFactory.createDialog(R.string.parsing_error, R.string.movie_details_parsingErrorText, MovieDetailsActivity.this).show();
				}
			}
		}
	}
	
	/**
	 * Nachladen des Titelbildes des Films mit der zuvor ermittelten URL.
	 * 
	 * @author Samuel und Senta
	 *
	 */
	private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... url)
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			Bitmap image = null;
			
			if (url[0] != null)
			{
		        try
		        {
					response = httpClient.execute(new HttpGet(url[0]));
				}
		        catch (ClientProtocolException e)
		        {
		        	Log.e(getClass().getSimpleName(), "HTTP protocol error.", e);
				}
		        catch (IOException e)
				{
		        	Log.e(getClass().getSimpleName(), "Connection was aborted.", e);
				}
		        
		        if (response != null)
		        {	        	
		        	try
		        	{
		        		Options options = new Options();
						image = BitmapFactory.decodeStream(response.getEntity().getContent(), null, options);
						imageRatio = options.outWidth / (double)options.outHeight;
					}
		        	catch (IllegalStateException e)
		        	{
		        		Log.e(getClass().getSimpleName(), "Response entity is not repeatable.", e);
					}
		        	catch (IOException e)
					{
		        		Log.e(getClass().getSimpleName(), "Response entity input stream could not be created.", e);
					}
		        }
			}
	        
			return image;
		}
		
		@Override
		protected void onPostExecute(Bitmap result)
		{
			if (result == null)
			{
				Toast.makeText(MovieDetailsActivity.this, R.string.movie_details_imageErrorText, Toast.LENGTH_SHORT).show();
			}
			
			// Daten laden, auch wenn Bild nicht geladen werden konnte
			progressDialog.dismiss();
			movieImage = result;
			loadMovieDetailsIntoView(movieData);
		}
	}
}
