package de.fhe.activities;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import de.fhe.adapters.MoviePreviewsAdapter;
import de.fhe.data.MoviePreview;
import de.fhe.data.MoviePreviewsRSSFeed;
import de.fhe.util.DialogFactory;

/**
 * @author Corlan McDonald
 * 
 * This class defines the PreviewActivity for 'Showtime.'
 */
public class PreviewActivity extends ListActivity implements OnCancelListener, OnClickListener
{
	public static final String PREVIEW_BASE_URL = "/showtime/preview/";
	
	private Bitmap images []= null;
	
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;
	
	private MoviePreviewsAdapter moviePreviewsAdapter = null;
	private MoviePreviewsRSSFeed previewDetails = null;
	
	private RestPreviewTask restTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.preview);
		
		dialogFactory = new DialogFactory(this);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);
	       
	    progressDialog = ProgressDialog.show(this, getString(R.string.preview_progress),
	    		getString(R.string.please_wait), false, true, this);
	    
	    if(savedInstanceState == null)
		{
	    	establishConnection();
		}
	    else
	    {
	    	images = (Bitmap[]) savedInstanceState.getParcelableArray("imageResults");
	    	previewDetails = (MoviePreviewsRSSFeed) savedInstanceState.getSerializable("parseResults");
	    	
	    	if (previewDetails != null)
	    		loadPreviewDetailsIntoView(previewDetails);
	    	else
	    		establishConnection();	
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  if (previewDetails != null)
		  savedInstanceState.putSerializable("parseResults", previewDetails);
	  if (moviePreviewsAdapter != null)
		  savedInstanceState.putParcelableArray("imageResults", moviePreviewsAdapter.getImages());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  images = (Bitmap[]) savedInstanceState.getParcelableArray("imageResults");
	  previewDetails = (MoviePreviewsRSSFeed) savedInstanceState.getSerializable("parseResults");
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// füge action items zur action bar hinzu
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionitems_settings, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.actionitems_itemSettings)
		{
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);
		}
		
		return true;
	}
		
	@Override
	public void onDestroy()
	{
		if (progressDialog != null)
			progressDialog.dismiss();

		if (restTask != null)
			restTask.cancel(true);
		
		super.onDestroy();
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id)
	{
	    MoviePreview preview = (MoviePreview) list.getItemAtPosition(position);
	    String url = preview.getMovieURL();
	    
	    if (url != null)
	    {
	    	Intent intent = new Intent(this, MovieDetailsActivity.class);
	    	intent.putExtra("movieDetailsUrl", getString(R.string.app_base_url) + url);
	    	startActivity(intent);
	    }
	    else
	    {
	    	Toast.makeText(this, getString(R.string.search_result_cinema_noMovieDetails), Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void onCancel(DialogInterface arg0) 
	{
		finish();
	}
	
	/**
	 * @author Corlan McDonald
	 * 
	 * This method begins the process of establishing an HTTP connection.
	 * It sets the URL, and then calls HttpRequestTask with said URL passed-as-parameter.
	 *
	 * @param URL url
	 * @return N/A
	 */
	private void establishConnection()
	{
		String previewUrl = getString(R.string.app_base_url) + PREVIEW_BASE_URL;
		
		restTask = new RestPreviewTask();
		restTask.execute(previewUrl);
	}
	
	private class RestPreviewTask extends AsyncTask<String, Void, MoviePreviewsRSSFeed>
	{
		private boolean noInternetConnection = false;
		private boolean serviceNotRunning = false;
		private boolean parsingError = false;
		private boolean serviceUnavailable = false;
		
		@Override
		protected MoviePreviewsRSSFeed doInBackground(String... url)
		{
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				previewDetails = restTemplate.getForObject(url[0], MoviePreviewsRSSFeed.class);
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
			
			return previewDetails;
		}
		
		@Override
		protected void onPostExecute(MoviePreviewsRSSFeed result)
		{
			if (result != null)
			{
				// Filminfos speichern, Bild nachladen
				MoviePreviewsRSSFeed previewDetails = result;
					
				loadPreviewDetailsIntoView(previewDetails);
				
			}
			// Fehlerbehandlung
			else
			{
				if (noInternetConnection)
				{
					dialogFactory.createDialog(R.string.no_connection, R.string.preview_noConnectionText, PreviewActivity.this).show();
				}
				else if (serviceNotRunning || serviceUnavailable)
				{
					dialogFactory.createDialog(R.string.app_service_unavailable, R.string.app_service_unavailableText, PreviewActivity.this).show();
				}
				else if (parsingError)
				{
					dialogFactory.createDialog(R.string.parsing_error, R.string.preview_parsingErrorText, PreviewActivity.this).show();
				}
			}
		}
	}
	
	/**
	 * @author Corlan McDonald
	 * 
	 * This method updates the data on the PreviewActivity's listView with the data from our parsed information
	 * contained in previewDetails.
	 *
	 * @param MoviePreviewsRSSFeed previewDetails
	 * @return N/A
	 */
	private void loadPreviewDetailsIntoView(MoviePreviewsRSSFeed previewDetails)
	{		
		TextView textView = (TextView) findViewById(R.id.previews_start_week_header);
		textView.setText(previewDetails.getTitle());
		
		// Retrieve listView from preview.xml
		ListView listView = getListView();
		
		// Create new Adapter and set as adapter of listView
		// However, check to see if images have been saved beforehand
		moviePreviewsAdapter = new MoviePreviewsAdapter(this, previewDetails.getItems());
		
		if(images != null)
		{
			moviePreviewsAdapter.setImages(images);
		}
		
		listView.setAdapter(moviePreviewsAdapter);	

		progressDialog.dismiss();
	}

	public void onClick(DialogInterface dialog, int which)
	{
		finish();		
	}
}
