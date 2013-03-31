package de.fhe.util;

import de.fhe.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.widget.TextView;

/**
 * Kapselt die Erstellung eines Dialogs.
 * 
 * @author Senta und Samuel
 *
 */
public class DialogFactory
{
	private Activity hostActivity;

	public DialogFactory(Activity hostActivity)
	{
		this.hostActivity = hostActivity;
	}
	
	/**
	 * Erzeugt einen Dialog (Titel, Meldung, OK-Button).
	 * 
	 * @param title Titel des Dialogs
	 * @param message Nachricht des Dialogs
	 * @param listener OnClickListener des Dialogs
	 * 
	 * @return erzeugter Dialog
	 */
	public AlertDialog createDialog(int titleId, int messageId, OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity);
		builder.setTitle(hostActivity.getString(titleId));
		
		int padding = hostActivity.getResources().getDisplayMetrics().densityDpi / 160 * 25;	// dp to px
		
		TextView content = new TextView(hostActivity);
		content.setText(hostActivity.getString(messageId));
		content.setPadding(padding, padding, padding, padding);
		content.setTextAppearance(hostActivity, android.R.style.TextAppearance_Small);
		builder.setView(content);
		
		AlertDialog dialog = builder.create();
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, hostActivity.getString(R.string.ok), listener);
		
		return dialog;
	}
	
	public AlertDialog createDialog(int titleId, String[] entries, int selected, OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity);
		builder.setTitle(hostActivity.getString(titleId));
		
		builder.setSingleChoiceItems(entries, selected, listener);
		
		AlertDialog dialog = builder.create();
		
		return dialog;
	}
	
	public AlertDialog createDialog(int titleId, Cursor entries, String column, int selected, OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity);
		builder.setTitle(hostActivity.getString(titleId));
		
		builder.setSingleChoiceItems(entries, selected, column, listener);
		
		AlertDialog dialog = builder.create();
		
		return dialog;
	}
}
