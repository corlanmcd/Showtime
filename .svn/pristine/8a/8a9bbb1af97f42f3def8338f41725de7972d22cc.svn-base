package de.fhe.activities;

import java.util.Arrays;
import java.util.LinkedList;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import de.fhe.adapters.MovieOptionIndexedArrayAdapter;
import de.fhe.data.MovieOption;
import de.fhe.util.DialogFactory;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;

/**
 * Activity mit eingebettetem ListView für das Laden, Verarbeiten und Darstellen der Filmliste.
 * Die Filmliste wird für eine spezielle Suche nach einem Film benötigt.
 * Der ausgewählte Film wird mit Namen und ID zurückgegeben.
 * Sollte keine Internetverbindung bestehen, weist ein Dialog darauf hin.
 * 
 * @author Samuel und Senta
 *
 */
public class MovieListActivity extends ListActivity implements OnItemClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;
	
	private RestMovieListTask restTask = null;
	
	private LinkedList<MovieOption> movieList = null;
	private LinkedList<MovieOption> movieSubList = null;
	private MovieOption result = null;
	private SearchView searchView = null;
	private boolean showSubMovieList = false;
	
	/**
	 * Initialisiert den View der Activity. Dazu wird der Titel gesetzt,
	 * sowie diverse Optionen für den ListView gesetzt. Danach wird in
	 * einem separaten Task die Filmliste geladen und verarbeitet. Kann
	 * die Filmliste nicht geladen werden, wird ein Dialog angezeigt.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.movielist_title));
		getActionBar().setDisplayUseLogoEnabled(false);
		
		dialogFactory = new DialogFactory(this);
		
		getListView().setOnItemClickListener(this);
		getListView().setFastScrollEnabled(true);
		getListView().setBackgroundColor(Color.rgb(162, 209, 242));
		
		// Filmliste wurde beim Kippen des Gerätes gesichert
		if (savedInstanceState != null && savedInstanceState.getSerializable("movieOptionList") != null)
		{
			movieList = (LinkedList<MovieOption>)savedInstanceState.getSerializable("movieOptionList");
			movieSubList = (LinkedList<MovieOption>)savedInstanceState.getSerializable("movieOptionSubList");
			showSubMovieList = savedInstanceState.getBoolean("listToShow");
			
			if (showSubMovieList)
				setAdapter(movieSubList, true);
			else
				setAdapter(movieList, false);
		}
		// Filmliste muss neu geladen werden
		else
		{
			progressDialog = ProgressDialog.show(this, getString(R.string.movielist_progress), getString(R.string.please_wait), false, true, this);
			loadMovieListInBackground();
		}
	}	
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionitems_movielist, menu);
        searchView = (SearchView) menu.findItem(R.id.actionitems_itemSearch).getActionView();
        searchView.setQueryHint(getString(R.string.movielist_searchHint));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        return true;
	 }
	
	/**
	 * Speichert den Zustand.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("movieOptionList", movieList);
		outState.putSerializable("movieOptionSubList", movieSubList);
		outState.putBoolean("listToShow", showSubMovieList);
		
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
	 * Beendet die Activity und gibt Filmtitel sowie ID zurück.
	 * Wurde noch kein Film ausgewählt, wird der Standardwert
	 * "Alle Filme" mit ID = 0 zurückgegeben.
	 */
	@Override
	public void finish()
	{
		Intent resultData = new Intent();
		resultData.putExtra("movieOption", result);
		setResult(RESULT_OK, resultData);
		
		super.finish();
	}

	/**
	 * Callback für die Auswahl eines Films aus der Filmliste.
	 * Der Filmtitel und die ID werden lokal gespeichert und danach zurückgegeben.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		result = (MovieOption)parent.getAdapter().getItem(position);
		
		finish();
	}
	
	/**
	 * Lädt die Filmliste: Anfragen bei app_base_url nach Filmliste.
	 * Tritt dabei ein Fehler auf (keine Internetverbindung, Server nicht erreichbar, Parsen fehlgeschlagen),
	 * wird eine ungültige Referenz zurückgegeben. 
	 */
	private void loadMovieListInBackground()
	{	
		restTask = new RestMovieListTask();
		restTask.execute(getString(R.string.app_base_url) + "/showtime/movielist/");
	}
	
	/**
	 * Setzt den ListAdapter für den ListView.
	 * 
	 * @param movieList Inhalt des ListViews
	 */
	private void setAdapter(LinkedList<MovieOption> movieList, boolean showSubMovieList)
	{
		this.showSubMovieList = showSubMovieList;
		getListView().setFastScrollAlwaysVisible(false);
		MovieOptionIndexedArrayAdapter adapter = new MovieOptionIndexedArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, movieList, showSubMovieList);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		getListView().setFastScrollAlwaysVisible(true);
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
	private class RestMovieListTask extends AsyncTask<String, Void, LinkedList<MovieOption>>
	{
		private boolean noInternetConnection = false;
		private boolean serviceNotRunning = false;
		private boolean parsingError = false;
		private boolean serviceUnavailable = false;
		
		@Override
		protected LinkedList<MovieOption> doInBackground(String... url)
		{
			LinkedList<MovieOption> result = null;
			
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				MovieOption[] movies = restTemplate.getForObject(url[0], MovieOption[].class);
				result = new LinkedList<MovieOption>(Arrays.asList(movies));				
			}
			catch (ResourceAccessException ex)
			{
				// keine Internetverbindung
				noInternetConnection = true;
			}
			catch (HttpClientErrorException ex)
			{
				// Service läuft nicht - HTTP 404
				serviceNotRunning = true;
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
			
			return result;
		}

		@Override
		protected void onPostExecute(LinkedList<MovieOption> resultList)
		{
			// erfolgreich geparst, Filmliste gefunden
			if (resultList != null && resultList.size() > 0)
			{
				movieList = resultList;
				progressDialog.dismiss();
				setAdapter(movieList, false);
			}
			// Fehlerbehandlung
			else
			{
				movieList = null;
				
				if (noInternetConnection)
				{
					dialogFactory.createDialog(R.string.no_connection, R.string.movielist_noConnectionText, MovieListActivity.this).show();
				}
				else if (serviceNotRunning || serviceUnavailable)
				{
					dialogFactory.createDialog(R.string.app_service_unavailable, R.string.app_service_unavailableText, MovieListActivity.this).show();
				}
				else if (parsingError)
				{
					dialogFactory.createDialog(R.string.parsing_error, R.string.movielist_parsingErrorText, MovieListActivity.this).show();
				}
			}
		}
	}

	public boolean onQueryTextChange(String query)
	{
		if (query.equals(""))
		{
			setAdapter(movieList, false);
			return true;
		}
		else
			return false;
	}

	public boolean onQueryTextSubmit(String query)
	{
		if (movieSubList == null)
		{
			movieSubList = new LinkedList<MovieOption>();
		}
		else
		{
			movieSubList.clear();
		}
		
		if (movieList != null)
		{			
			for (int i = 0; i < movieList.size(); i++)
			{
				MovieOption movie = movieList.get(i);
				
				if (movie.getMovieTitle().toLowerCase().contains(query.toLowerCase()))
				{
					movieSubList.add(movie);
				}
			}
			
			setAdapter(movieSubList, true);
			
			if (movieSubList.size() == 0)
				getListView().setFastScrollAlwaysVisible(false);
			
			return true;
		}
		else
			return false;
	}

	public boolean onClose()
	{
		setAdapter(movieList, false);
		return false;
	}
}
