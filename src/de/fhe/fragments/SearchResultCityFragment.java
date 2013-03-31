package de.fhe.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import de.fhe.activities.R;
import de.fhe.activities.SearchActivity;
import de.fhe.adapters.CityListAdapter;
import de.fhe.data.CityProgram;
import de.fhe.data.SearchOptions;
import de.fhe.util.DialogFactory;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Fragment der <code>SearchActivity</code>. Lädt das Layout und damit die Ergebnisse der letzten Suche.
 * 
 * @author Senta und Samuel
 *
 */
public class SearchResultCityFragment extends Fragment
	implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, OnItemClickListener
{	
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;
	
	private ListView cityList = null;
	
	private RestProgramTask restTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		dialogFactory = new DialogFactory(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		View view = null;
			
		if (getSearchRequested() && getSearchOptions() != null && getSearchOptions().getLocation() != null && !getSearchOptions().equals(getSavedSearchOptions()))
		{
			// Neue Suche angestoßen, Ergebnisse laden, Anzeige überschreiben
			view = inflater.inflate(R.layout.search_result_city_list, null);
			cityList = (ListView)view.findViewById(R.id.search_result_city_list);
			cityList.setOnItemClickListener(this);
			
			if (getCityProgramList() != null)
				cityList.setAdapter(new CityListAdapter<CityProgram>(getActivity(), R.layout.search_result_city, getCityProgramList()));
			
			progressDialog = ProgressDialog.show(getActivity(), getString(R.string.search_result_progress), getString(R.string.please_wait), false, true, this);
			loadCinemaProgramInBackground();
		}
		else if(getCityProgramList() != null)
		{
			// Anzeige im Hintergrund: vorherige Suchergebnisse
			view = inflater.inflate(R.layout.search_result_city_list, null);
			cityList = (ListView)view.findViewById(R.id.search_result_city_list);
			cityList.setOnItemClickListener(this);
			cityList.setAdapter(new CityListAdapter<CityProgram>(getActivity(), R.layout.search_result_city, getCityProgramList()));			
		}
		else
		{
			// Anzeige im Hintergrund: keine Ergebnisse
			view = inflater.inflate(R.layout.search_result_city_empty, null);
		}
		
		return view;
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

	private boolean getSearchRequested()
	{
		return ((SearchActivity)getActivity()).getSearchRequested();
	}
	
	private void setSearchRequested(boolean searchRequested)
	{
		((SearchActivity)getActivity()).setSearchRequested(searchRequested);
	}
	
	private SearchOptions getSearchOptions()
	{
		return ((SearchActivity)getActivity()).getSearchOptions();
	}
	
	private SearchOptions getSavedSearchOptions()
	{
		return ((SearchActivity)getActivity()).getSavedSearchOptions();
	}
	
	private void setSavedSearchOptions(SearchOptions savedSearchOptions)
	{
		((SearchActivity)getActivity()).setSavedSearchOptions(savedSearchOptions);
	}

	private ArrayList<CityProgram> getCityProgramList()
	{
		return ((SearchActivity)getActivity()).getCityProgramList();
	}
	
	private void setCityProgramList(ArrayList<CityProgram> cityProgramList)
	{
		((SearchActivity)getActivity()).setCityProgramList(cityProgramList);
	}
	
	private void setSelectedCity(int selectedCity)
	{
		((SearchActivity)getActivity()).setSelectedCity(selectedCity);
	}
	
	private void selectSecondFragment()
	{
		((SearchActivity)getActivity()).getTabListenerMulti().selectFragment(MultiTabListener.FRAGMENT_2);
	}

	private void loadCinemaProgramInBackground()
    {
		SearchOptions options = getSearchOptions();
		
		// URL mit Suchoptionen
		String url = new String(getString(R.string.app_base_url) + "/showtime/programsearch?" +
			"location=" + options.getLocation().replace(" ", "+") + "&" +
			"radius=" + options.getRadius() + "&" +
			"date=" + options.getDate() + "&" +
			"time=" + options.getTime() + "&" +
			"movie=" + options.getMovieId());
				
		if (options.getLocationLat() != 0 && options.getLocationLng() != 0)
		{
			url += "latitude=" + options.getLocationLat() + "&" +
				   "longitude=" + options.getLocationLng();
		}
		
		restTask = new RestProgramTask();
		restTask.execute(url);
    }
	
	/**
	 * Callback für den OK-Button der Dialoge. Beendet die Activity.
	 */
	public void onClick(DialogInterface dialog, int which)
	{
		getActivity().getActionBar().setSelectedNavigationItem(0);
	}
	
	/**
	 * Callback für Abbrechen des Progress-Dialogs. Laden / Parsen wird abgebrochen.
	 */
	public void onCancel(DialogInterface dialog) 
	{
		setSearchRequested(false);
		
		if (restTask != null)
			restTask.cancel(true);
		
		getActivity().getActionBar().setSelectedNavigationItem(0);
	}
	
	/**
	 * Parsen des Inhalts der Website und Extraktion der Informationen zum Kinoprogramm.
	 * 
	 * @author Samuel und Senta
	 *
	 */
	private class RestProgramTask extends AsyncTask<String, Void, ArrayList<CityProgram>>
	{
		private boolean noInternetConnection = false;
		private boolean serviceNotRunning = false;
		private boolean badRequestError = false;
		private boolean parsingError = false;
		private boolean serviceUnavailable = false;
		
		@Override
		protected ArrayList<CityProgram> doInBackground(String... url)
		{
			ArrayList<CityProgram> result = null;
			
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				CityProgram[] programList = restTemplate.getForObject(url[0], CityProgram[].class);
				result = new ArrayList<CityProgram>(Arrays.asList(programList));
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
			
			return result;
		}
		
		/**
		 * @param result ist null, wenn ein Fehler beim Parsen aufgetreten ist, <br>
		 * ist leer, wenn das Parsen erfolgreich war, aber kein Kinoprogramm gefunden wurde.
		 */
		@Override
		protected void onPostExecute(ArrayList<CityProgram> result)
		{
			setSearchRequested(false);
			progressDialog.dismiss();
			
			// erfolgreich geparst, Kinoprogramm gefunden
			if (result != null && result.size() > 0)
			{
				setCityProgramList(result);
				setSavedSearchOptions(getSearchOptions());
				
				cityList.setAdapter(new CityListAdapter<CityProgram>(getActivity(), R.layout.search_result_city, getCityProgramList()));
			}
			// erfolgreich geparst, kein Kinoprogramm gefunden
			else if (result != null && result.size() == 0)
			{
				dialogFactory.createDialog(R.string.search_result_noPogramm, R.string.search_result_noProgrammText, SearchResultCityFragment.this).show();
			}
			// Fehlerbehandlung
			else
			{
				if (noInternetConnection)
				{
					dialogFactory.createDialog(R.string.no_connection, R.string.search_result_noConnectionText, SearchResultCityFragment.this).show();
				}
				else if (serviceNotRunning || serviceUnavailable)
				{
					dialogFactory.createDialog(R.string.app_service_unavailable, R.string.app_service_unavailableText, SearchResultCityFragment.this).show();
				}
				else if (badRequestError)
				{
					dialogFactory.createDialog(R.string.app_bad_request, R.string.app_bad_requestText, SearchResultCityFragment.this).show();
				}
				else if (parsingError)
				{
					dialogFactory.createDialog(R.string.parsing_error, R.string.search_result_parsingErrorText, SearchResultCityFragment.this).show();
				}
			}	
		}
	}

	/**
	 * Callback für die Auswahl eines Elements in der Liste.
	 */
	public void onItemClick(AdapterView<?> l, View v, int position, long id)
	{
		if (getCityProgramList() != null)
		{
			setSelectedCity(position);
			selectSecondFragment();
		}
	}
}
