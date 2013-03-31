package de.fhe.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Managed einen Tabs mit einem Fragment. Das Fragment wird zu Programmbeginn geladen
 * und je nachdem, ob der Tab ausgewählt ist oder nicht zur Laufzeit an- und abgedockt.
 * 
 * http://developer.android.com/guide/topics/ui/actionbar.html (10.05.2012)
 * 
 * @author Samuel und Senta
 *
 * @param <T> Fragment zur Anzeige
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener
{
    private Fragment tabFragment;
    private final Activity hostActivity;
    private final String tabName;
    private final Class<T> tabType;

    /** Konstruktor, der benutzt wird, wenn ein neuer Tab erzeugt wird.
      * @param activity	Host Activity
      * @param name		Name des Fragments
      * @param type		Klasse des Fragments
      */
    public TabListener(Activity activity, String name, Class<T> type)
    {
        hostActivity = activity;
        tabName = name;
        tabType = type;
    }
    
	public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
        // prüfen, ob Fragment schon existiert
    	tabFragment = hostActivity.getFragmentManager().findFragmentByTag(tabName);
        if (tabFragment == null)
        {
            // wenn nicht, Fragment instanziieren und der Activity hinzufügen
            tabFragment = Fragment.instantiate(hostActivity, tabType.getName());
            ft.add(android.R.id.content, tabFragment, tabName);
        }
        else
        {
            // wenn ja, Fragment anhängen und anzeigen
            ft.attach(tabFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {
        if (tabFragment != null)
        {
            // Fragment abhängen
            ft.detach(tabFragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {
        // ignorieren
    }
}
