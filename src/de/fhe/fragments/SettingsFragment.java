package de.fhe.fragments;

import de.fhe.activities.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Fragment der <code>SettingsActivity</code>. Lädt das Layout der Einstellungen.
 * 
 * @author Samuel und Senta
 *
 */
public class SettingsFragment extends PreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
	}
}
