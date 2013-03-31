package de.fhe.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Managed einen Tabs mit zwei Fragmenten. Das Fragment wird zu Programmbeginn geladen
 * und je nachdem, ob der Tab ausgewählt ist oder nicht zur Laufzeit an- und abgedockt.
 * 
 * http://developer.android.com/guide/topics/ui/actionbar.html (10.05.2012)
 * 
 * @author Samuel und Senta
 *
 * @param <Type1> 1. Fragment zur Anzeige
 * @param <Type2> 2. Fragment zur Anzeige
 */
public class MultiTabListener<Type1 extends Fragment, Type2 extends Fragment> implements ActionBar.TabListener
{
	public static final int FRAGMENT_1 = 1;
	public static final int FRAGMENT_2 = 2;
	
    private final Activity hostActivity;
    private Fragment tabFragment1;
    private Fragment tabFragment2;
    private final String tabName1;
    private final String tabName2;
    private final Class<Type1> tabType1;
    private final Class<Type2> tabType2;
    private int selectedFragment;

    /** Konstruktor, der benutzt wird, wenn ein neuer Tab erzeugt wird.
      * @param activity	Host Activity
      * @param name1	Name des 1. Fragments
      * @param type1	Klasse des 1. Fragments
      * @param name2	Name des 2. Fragments
      * @param type2	Klasse des 2. Fragments
      */
    public MultiTabListener(Activity activity, String name1, Class<Type1> type1, String name2, Class<Type2> type2, int startFragment)
    {
        hostActivity = activity;
        tabName1 = name1;
        tabType1 = type1;
        tabName2 = name2;
        tabType2 = type2;
        selectedFragment = startFragment;
    }

	public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
    	if (selectedFragment == FRAGMENT_2)
    	{
	        // prüfen, ob Fragment schon existiert
	    	tabFragment2 = hostActivity.getFragmentManager().findFragmentByTag(tabName2);
	    	if (tabFragment2 == null)
	        {
	            // wenn nicht, Fragment instanziieren und der Activity hinzufügen
	            tabFragment2 = Fragment.instantiate(hostActivity, tabType2.getName());
	            ft.add(android.R.id.content, tabFragment2, tabName2);
	        }
	        else
	        {
	            // wenn ja, Fragment anhängen und anzeigen
	            ft.attach(tabFragment2);
	        }
    	}
    	else
    	{
    		// prüfen, ob Fragment schon existiert
	    	tabFragment1 = hostActivity.getFragmentManager().findFragmentByTag(tabName1);
	        if (tabFragment1 == null)
	        {
	            // wenn nicht, Fragment instanziieren und der Activity hinzufügen
	            tabFragment1 = Fragment.instantiate(hostActivity, tabType1.getName());
	            ft.add(android.R.id.content, tabFragment1, tabName1);
	        }
	        else
	        {
	            // wenn ja, Fragment anhängen und anzeigen
	            ft.attach(tabFragment1);
	        }
    	}
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {
    	if (selectedFragment == FRAGMENT_2 && tabFragment2 != null)
    	{
    		// Fragment abhängen
            ft.detach(tabFragment2);
    	}
    	else if (selectedFragment == FRAGMENT_1 && tabFragment1 != null)
        {
            // Fragment abhängen
            ft.detach(tabFragment1);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {
        // ignorieren
    }
    
    public void selectFragment(int selectedFragmentIndex)
    {
    	if (selectedFragmentIndex == FRAGMENT_1 || selectedFragmentIndex == FRAGMENT_2
    		&& selectedFragmentIndex != selectedFragment)
    	{
    		selectedFragment = selectedFragmentIndex;
    		if (selectedFragment == FRAGMENT_2)
    		{
    			// prüfen, ob Fragment schon existiert
    			tabFragment2 = hostActivity.getFragmentManager().findFragmentByTag(tabName2);
    	        if (tabFragment2 == null)
    	        {
    	            // wenn nicht, Fragment instanziieren
    	            tabFragment2 = Fragment.instantiate(hostActivity, tabType2.getName());
    	        }
    			FragmentTransaction transaction = hostActivity.getFragmentManager().beginTransaction();
    			transaction.replace(android.R.id.content, tabFragment2, tabName2);
    			transaction.commit();
    		}
    		else
    		{
    			// prüfen, ob Fragment schon existiert
    			tabFragment1 = hostActivity.getFragmentManager().findFragmentByTag(tabName1);
    	        if (tabFragment1 == null)
    	        {
    	            // wenn nicht, Fragment instanziieren
    	            tabFragment1 = Fragment.instantiate(hostActivity, tabType1.getName());
    	        }
    			FragmentTransaction transaction = hostActivity.getFragmentManager().beginTransaction();
    			transaction.replace(android.R.id.content, tabFragment1, tabName1);
    			transaction.commit();
    		}
    	}
    }
    
    public int getSelectedFragement()
    {
    	return selectedFragment;
    }
}
