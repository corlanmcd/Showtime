package de.fhe.fragments;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhe.activities.MovieListActivity;
import de.fhe.activities.R;
import de.fhe.activities.SearchActivity;
import de.fhe.data.Date;
import de.fhe.data.MovieOption;
import de.fhe.data.SearchOptions;
import de.fhe.util.DialogFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


/**
 * Fragment der <code>SearchActivity</code>. Lädt das Layout für eine neue Suche.
 * Das Suchformular enthält vier EditText-Felder für Ort, Datum, Uhrzeit und Film.
 * Nur der Ort wird über die Tastatur eingegeben. Die anderen 3 Parameter werden über
 * entsprechende Dialoge bzw. eine eigene Activity abgefragt und sind bei Programmstart
 * schon mit Standardwerten belegt. Wird kein Ort angegeben, wird das durch einen Dialog
 * angezeigt.
 * 
 * @author Senta und Samuel
 *
 */
public class SearchNewFragment extends Fragment
	implements OnTimeSetListener, View.OnClickListener, DialogInterface.OnClickListener, OnItemSelectedListener, OnSeekBarChangeListener, OnCancelListener
{
	public static final int MOVIE_LIST_CODE = 1;
	public static final int N_DATE_CHOICES = 4;
	public static final int RADIUS_MAX = 10;
	public static final int KM_PER_RADIUS = 5;
	
	private Calendar calendar = null;
	
	private GpsToCityTask geoCodeTask = null;
	private boolean gpsLocationOn = false;
	
	private Date[] dateChoices = null;
	private int selectedDateChoice = 0;
	
	private String[] locationChoices = null;
	private int selectedLocationChoice = 0;
	
	private EditText location = null;
	private ImageButton history = null;
	private TextView radius = null;
	private SeekBar radiusBar = null;
	private EditText date = null;
	private EditText time = null;
	private EditText movie = null;
	private Button search = null;
	
	private DialogFactory dialogFactory = null;
	private AlertDialog dateDialog = null;
	private AlertDialog historyDialog = null;
	private ProgressDialog progressDialog = null;
	
	/**
	 * Instanziiert einen Kalender.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		gpsLocationOn = prefs.getBoolean(getString(R.string.settings_gpsLocation_key), false);		
		
		dialogFactory = new DialogFactory(getActivity());	
	}
	
	/**
	 * Lädt den View für das Suchformular, setzt die benötigten Listener und initialisiert die Felder.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		calendar = Calendar.getInstance();
		
		View view = inflater.inflate(R.layout.search_new, container, false);
		
		// EditText (für Ort)
		location = (EditText)view.findViewById(R.id.search_new_editTextLocation);
		
		// Button (für Verlauf)
		history = (ImageButton)view.findViewById(R.id.search_new_buttonHistory);
		history.setOnClickListener(this);
		
		// TextView und SeekBar (für Umkreis)
		radius = (TextView)view.findViewById(R.id.search_new_textViewRadius);
		radiusBar = (SeekBar)view.findViewById(R.id.search_new_seekBarRadius);
		radiusBar.setOnSeekBarChangeListener(this);
		radiusBar.setMax(RADIUS_MAX);
		
		// Textfelder (für Datum, Zeit & Film)
		date = (EditText)view.findViewById(R.id.search_new_editTextDate);
		date.setOnClickListener(this);
		
		time = (EditText)view.findViewById(R.id.search_new_editTextTime);
		time.setOnClickListener(this);
		
		movie = (EditText)view.findViewById(R.id.search_new_editTextMovie);
		movie.setOnClickListener(this);
		
		// Button (zum Starten der Suche)
		search = (Button)view.findViewById(R.id.search_new_buttonSearch);
		search.setOnClickListener(this);
		
		if (getSearchOptions() != null)
		{
			restoreSearchOptions();
		}
		else
		{
			initializeSearchOptions();
		}
		
		return view;
	}
	
	private SearchOptions getSearchOptions()
	{
		return ((SearchActivity)getActivity()).getSearchOptions();
	}
	
	private void setSearchOptions(SearchOptions searchOptions)
	{
		((SearchActivity)getActivity()).setSearchOptions(searchOptions);
	}
	
	private void setSearchRequested(boolean searchRequested)
	{
		((SearchActivity)getActivity()).setSearchRequested(searchRequested);
	}
	
	private int getSelectedFragment()
	{
		return ((SearchActivity)getActivity()).getTabListenerMulti().getSelectedFragement();
	}
	
	private void selectFirstFragment()
	{
		((SearchActivity)getActivity()).getTabListenerMulti().selectFragment(MultiTabListener.FRAGMENT_1);
	}
	
	/**
	 * Initialisiert das Suchformular für den ersten Start.
	 */
	private void initializeSearchOptions()
	{
		setSearchOptions(new SearchOptions());
		
		// Ort
		selectedLocationChoice = 0;
		
		// Radius setzen
		radiusBar.setProgress(1);
		radius.setText(getString(R.string.search_new_radius) + " " + radiusBar.getProgress() * KM_PER_RADIUS + " km");
		getSearchOptions().setRadius(radiusBar.getProgress() * KM_PER_RADIUS);
		
		// Datum mit Einträgen füllen und setzen		
		dateChoices = createDateChoices();
		selectedDateChoice = 0;
		date.setText(dateChoices[selectedDateChoice].getDateDisplay());
		getSearchOptions().setDate(dateChoices[selectedDateChoice].getDateId());
		
		// Zeit setzen
		time.setText(formatTime());
		getSearchOptions().setTime(formatTime());
		
		// Film setzen
		getSearchOptions().setMovieId("0");
	}
	
	/**
	 * Stellt die Suchoptionen des Suchformulars nach einem Neustart des Fragmentes (das Gerät wurde gekippt) wieder her.
	 */
	private void restoreSearchOptions()
	{		
		// Radius laden
		radiusBar.setProgress(getSearchOptions().getRadius() / KM_PER_RADIUS);
		radius.setText(getString(R.string.search_new_radius) + " " + radiusBar.getProgress() * KM_PER_RADIUS + " km");
		
		// Datum mit Einträgen füllen und Auswahl laden
		dateChoices = createDateChoices();
		
		for (int i = 0; i < dateChoices.length; i++)
		{
			if (dateChoices[i].getDateId().equals(getSearchOptions().getDate()))
			{
				selectedDateChoice = i;
				date.setText(dateChoices[selectedDateChoice].getDateDisplay());
				break;
			}
		}
	}
	
	/**
	 * Erzeugt die Optionen für die Datumsauswahl.
	 * 
	 * @return Liste der Datumsoptionen
	 */
	private Date[] createDateChoices()
	{		
		Date[] dateChoices = new Date[N_DATE_CHOICES];
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd.MM.yyyy", Locale.GERMANY);
		SimpleDateFormat dateIdFormat = new SimpleDateFormat("yyyyMMdd", Locale.GERMANY);
		
		for(int i = 0; i < N_DATE_CHOICES - 1; i++)
		{
			dateChoices[i] = new Date(dateFormat.format(calendar.getTime()), dateIdFormat.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		dateChoices[N_DATE_CHOICES - 1] = new Date("diese Woche", "week");
		
		return dateChoices;
	}

	/**
	 * Lädt die Einstellungen der Anwendung.
	 */
	@Override
	public void onResume() 
	{
		super.onResume();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		gpsLocationOn = prefs.getBoolean(getString(R.string.settings_gpsLocation_key), false);
		
		if (gpsLocationOn && location.getText().toString().equals(""))
			getGpsPosition();
	}
	
	/**
	 * Beendet den eventuell offenen Progress-Dialog und alle laufenden AsyncTasks.
	 */
	@Override
	public void onDestroy()
	{
		if (progressDialog != null)
			progressDialog.dismiss();
		
		if (geoCodeTask != null)
			geoCodeTask.cancel(true);
		
		super.onDestroy();
	}
	
	/**
	 * Aktualisiert den Kalender mit den Daten des Zeit-Dialoges.
	 * 
	 * @param hour Stunde
	 * @param minute Minute
	 */
	private void updateTime(int hour, int minute)
	{
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
	}
	
	/**
	 * Formatiert die Zeit aus dem Kalender in einen String HH/mm
	 * 
	 * @return formatierte Zeit
	 */
	private String formatTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	 * Callback für Aktualisierung der Zeit
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		updateTime(hourOfDay, minute);
		time.setText(formatTime());
		getSearchOptions().setTime(formatTime());
	}
	
	/**
	 * Callback für OnClick-Events der EditText-Felder und des Suchen-Buttons
	 * Startet die Dialoge für Datums- und Zeiteingabe sowie Activities für die Filmauswahl und die Suche.
	 * Wurde kein Ort für die Suche eingegeben, wird ein Dialog gestartet, der zur Eingabe auffordert.
	 */
	public void onClick(View v)
	{
		if (v.getId() == R.id.search_new_buttonHistory)
		{
			((SearchActivity)getActivity()).getDatabase().openRead();
			locationChoices = ((SearchActivity)getActivity()).getDatabase().fetchHistoryEntries();
			((SearchActivity)getActivity()).getDatabase().close();
			
			if (locationChoices != null && locationChoices.length > 0)
			{
				for (int i = 0; i < locationChoices.length; i++)
				{
					if (locationChoices[i].toLowerCase().equals(location.getText().toString().toLowerCase()))
					{
						selectedLocationChoice = i;
						break;
					}
				}				
				
				historyDialog = dialogFactory.createDialog(R.string.search_new_history, locationChoices, selectedLocationChoice, this);
				historyDialog.show();
			}
			else
			{
				Toast.makeText(getActivity(), R.string.search_new_noHistory, Toast.LENGTH_SHORT).show();
				((SearchActivity)getActivity()).getDatabase().close();
			}
		}
		else if (v.getId() == R.id.search_new_editTextDate)
		{
			String[] entries = new String[dateChoices.length];
			for (int i = 0; i < dateChoices.length; i++)
			{
				entries[i] = dateChoices[i].getDateDisplay();
			}
			
			dateDialog = dialogFactory.createDialog(R.string.search_new_date, entries, selectedDateChoice, this);
			dateDialog.show();
		}
		else if (v.getId() == R.id.search_new_editTextTime)
		{
			TimePickerDialog time = new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
			time.show();
		}
		else if (v.getId() == R.id.search_new_editTextMovie)
		{
			Intent i = new Intent(getActivity(), MovieListActivity.class);
			startActivityForResult(i, MOVIE_LIST_CODE);
		}
		else if (v.getId() == R.id.search_new_buttonSearch)
		{
			getSearchOptions().setLocation(location.getText().toString());
			if (!getSearchOptions().getLocation().equals(""))
			{
				//Ort in DB speichern
				((SearchActivity)getActivity()).getDatabase().openWrite();
				((SearchActivity)getActivity()).getDatabase().insertHistoryEntry(new java.util.Date(), getSearchOptions().getLocation());
				((SearchActivity)getActivity()).getDatabase().close();
				
				setSearchRequested(true);
				getActivity().getActionBar().setSelectedNavigationItem(1);
				
				if (getSelectedFragment() == MultiTabListener.FRAGMENT_2)
				{
					selectFirstFragment();
				}
			}
			else
			{
				dialogFactory.createDialog(R.string.search_new_noLocation, R.string.search_new_noLocationText, this).show();
			}
		}
	}

	/**
	 * Callback für den OK-Button des Dialoges. Dialog wird beendet.
	 */
	public void onClick(DialogInterface dialog, int which)
	{
		if (dialog == dateDialog)
		{
			selectedDateChoice = which;
			date.setText(dateChoices[selectedDateChoice].getDateDisplay());
			getSearchOptions().setDate(dateChoices[selectedDateChoice].getDateId());
			dateDialog.dismiss();
		}
		else if (dialog == historyDialog)
		{
			selectedLocationChoice = which;				
			location.setText(locationChoices[selectedLocationChoice]);
			historyDialog.dismiss();
		}
	}
	
	/**
	 * Callback für Abbrechen des Progress-Dialogs. Geocoding wird abgebrochen.
	 */
	public void onCancel(DialogInterface dialog)
	{
		if (geoCodeTask != null)
			geoCodeTask.cancel(true);
	}
	
	/**
	 * Callback für den Date-Spinner.
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
		getSearchOptions().setDate(dateChoices[position].getDateId());
	}

	/**
	 * Callback für den Date-Spinner.
	 */
	public void onNothingSelected(AdapterView<?> parent) 
	{
		// ignorieren		
	}
	
	/**
	 * Callback für die SeekBar. Der eingestellte Radius wird im TextView und  in den Suchoptionen aktualisiert.
	 */
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
	{
		if (fromUser)
		{
			radius.setText(getString(R.string.search_new_radius) + " " + progress * KM_PER_RADIUS + " km");
			getSearchOptions().setRadius(progress * KM_PER_RADIUS);
		}
	}

	/**
	 * Callback für die SeekBar.
	 */
	public void onStartTrackingTouch(SeekBar seekBar) 
	{
		// ignorieren		
	}

	/**
	 * Callback für die SeekBar.
	 */
	public void onStopTrackingTouch(SeekBar seekBar) 
	{
		// ignorieren		
	}

	/**
	 * Callback für das Ergebnis der Filmauswahl. Der Filmtitel wird in das
	 * EditText-Feld eingetragen und die ID wird in den Suchoptionen gespeichert.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == MOVIE_LIST_CODE && resultCode == Activity.RESULT_OK
			&& data.getExtras().getSerializable("movieOption") != null)
		{
			MovieOption result = (MovieOption)data.getExtras().getSerializable("movieOption");
			movie.setText(result.getMovieTitle());
			getSearchOptions().setMovieId(result.getMovieOptionId());
		}
	}
	
	private void getGpsPosition()
	{
		LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (location != null)
		{
			progressDialog = ProgressDialog.show(getActivity(), getString(R.string.search_new_progress), getString(R.string.please_wait), false, true, this);
			geoCodeTask = new GpsToCityTask();
			geoCodeTask.execute(getString(R.string.city_url) + location.getLatitude() + "," + location.getLongitude());
		}
		else
		{
			Toast.makeText(getActivity(), R.string.search_new_noGpsLocation, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Findet zu einer gegebenen GPS-Position den zugehörigen Namen der Stadt.
	 * 
	 * @author Senta und Samuel
	 *
	 */
	private class GpsToCityTask extends AsyncTask<String, Void, String>
	{
    	protected String doInBackground(String... url)
    	{
        	HttpClient httpClient = new DefaultHttpClient();
    		String result = null;
        	
        	try
        	{
        		result = httpClient.execute(new HttpGet(url[0]), new BasicResponseHandler());
        	}
        	catch (ClientProtocolException e)
        	{
        		Log.e(getClass().getSimpleName(), "HTTP protocol error.", e);
        	}
        	catch (IOException e)
        	{
        		Log.e(getClass().getSimpleName(), "Connection was aborted.", e);
        	}
        	
        	httpClient.getConnectionManager().shutdown();
        	return result;
        }
    	
    	protected void onPostExecute(String result)
    	{
    		if (result != null)
    		{
	    		try
	    		{
	    			JSONObject jsonResult = new JSONObject(result);
					JSONArray results = jsonResult.getJSONArray("results");
					JSONObject resultsIdx0 = results.getJSONObject(0);
					JSONArray components = resultsIdx0.getJSONArray("address_components");
					
					for (int i = 0; i < components.length(); i++)
					{
						JSONObject actual = components.getJSONObject(i);
						JSONArray types = actual.getJSONArray("types");
						if (types.getString(0).equals("locality"))
						{
							location.setText(actual.getString("long_name"));
							break;
						}
					}
					
					progressDialog.dismiss();
					
					if (location.getText().toString().equals(""))
					{
		    			Toast.makeText(getActivity(), R.string.search_new_noGpsLocation, Toast.LENGTH_SHORT).show();
					}
				}
	    		catch (JSONException e)
	    		{
					Log.e(getClass().getSimpleName(), "JSON Result could not be parsed.", e);
				}
    		}
    		else
    		{
    			progressDialog.dismiss();
    			Toast.makeText(getActivity(), R.string.search_new_noGpsLocation, Toast.LENGTH_SHORT).show();
    		}
    	}
    }
}