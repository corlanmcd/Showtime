package de.fhe.activities;

import de.fhe.fragments.SettingsFragment;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Activity für die Einstellungen. Startet das zugehörige <code>SettingsFragment</code>.
 * 
 * @author Samuel und Senta
 *
 */
public class SettingsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.settings_title));
		getActionBar().setDisplayUseLogoEnabled(false);
		getListView().setBackgroundColor(Color.rgb(162, 209, 242));
		
		// starte Fragment für Einstellungen
		PreferenceFragment settingsFragment = new SettingsFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(android.R.id.content, settingsFragment);
		ft.commit();
	}
}