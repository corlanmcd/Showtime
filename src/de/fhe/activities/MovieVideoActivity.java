package de.fhe.activities;

import de.fhe.util.DialogFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MovieVideoActivity extends Activity implements OnClickListener, OnCancelListener, OnPreparedListener, OnCompletionListener
{
	private DialogFactory dialogFactory = null;
	private ProgressDialog progressDialog = null;

	private String videoUrl = null;
	private VideoView videoView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.movie_video);
		videoView = (VideoView) findViewById(R.id.movie_video_viewView);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		dialogFactory = new DialogFactory(this);
		
		videoUrl = getIntent().getExtras().getString("videoUrl");
		
		if (videoUrl != null)
		{
			progressDialog = ProgressDialog.show(this, getString(R.string.movie_video_progress), getString(R.string.please_wait), false, true, this);
			loadVideoInBackground();
		}
		// keine URL übergeben
		else
		{
			Toast.makeText(this, R.string.movie_video_urlErrorText, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Beendet den eventuell offenen Progress-Dialog.
	 */
	@Override
	public void onDestroy()
	{
		if (progressDialog != null)
			progressDialog.dismiss();
		
		if (videoView.isPlaying())
			videoView.stopPlayback();
		
		super.onDestroy();
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
		finish();
	}
	
	private void loadVideoInBackground()
	{
		try
	    {
			MediaController mediaController = new MediaController(this);
			mediaController.setAnchorView(videoView);
			
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(Uri.parse(videoUrl));
			videoView.requestFocus();
			videoView.setOnPreparedListener(this);
			videoView.setOnCompletionListener(this);
	    }
		catch(Exception e)
		{
			progressDialog.dismiss();
			dialogFactory.createDialog(R.string.no_connection, R.string.movie_details_noConnectionText, this).show();
		}   
	}
	
	public void onPrepared(MediaPlayer mp)
	{
		progressDialog.dismiss();     
		videoView.start();
	}

	public void onCompletion(MediaPlayer mp)
	{
		finish();
	}
}
