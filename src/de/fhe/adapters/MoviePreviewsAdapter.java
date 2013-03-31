package de.fhe.adapters;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import de.fhe.data.MoviePreview;
import de.fhe.data.MoviePreviewsList;
import de.fhe.activities.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Corlan McDonald
 * 
 * This class defines the MoviePreviewsAdapter for the listView under preview.xml
 */
public class MoviePreviewsAdapter extends BaseAdapter
{
	private static final int[] RATINGS = { 	R.drawable.rating_very_bad,
											R.drawable.rating_bad,
											R.drawable.rating_medium,
											R.drawable.rating_good,
											R.drawable.rating_very_good  };
	
	private MoviePreviewsList newMoviePreviewsItems = null;
	private Bitmap images[] = null;
	private Context context;
	
	public MoviePreviewsAdapter(Context context, MoviePreviewsList newMoviePreviewsItems)
	{
		this.newMoviePreviewsItems = newMoviePreviewsItems;
		this.images = new Bitmap[this.newMoviePreviewsItems.size()];
		this.context = context;
	}
	
	public Bitmap[] getImages()
	{
		return images;
	}
	
	public void setImages(Bitmap[] images)
	{
		this.images = images;
	}
	
	public int getCount() 
	{
		return newMoviePreviewsItems.size();
	}

	public Object getItem(int index) 
	{
		return newMoviePreviewsItems.get(index);
	}

	public long getItemId(int index) 
	{
		return index;
	}
	
	public View getView(int index, View convertView, ViewGroup parent)
	{
		View view = null;
		
		// use of convertViews causes errors in connection with background image loading, so not used deliberately
		// TODO: implement use of convertViews
		LayoutInflater inflator = LayoutInflater.from(context);
		view = inflator.inflate(R.layout.new_movie_previews_item, parent, false);
		
		MoviePreview moviePreview = newMoviePreviewsItems.get(index);
		
		TextView titleTextView = (TextView) view.findViewById(R.id.movie_title_textView);
		titleTextView.setText(moviePreview.getTitle());
		
		ImageView movieRatingImageView = (ImageView) view.findViewById(R.id.movie_rating_imageView);
				
		if (moviePreview.getRedaktionRating() != -1)
		{
			int previewIndex = moviePreview.getRedaktionRating() / 25;
			movieRatingImageView.setImageResource(RATINGS[previewIndex]);
		}
		else
		{
			movieRatingImageView.setImageResource(0);
		}
		
		ImageView movieImageView = (ImageView) view.findViewById(R.id.movie_image);
		ProgressBar progress = (ProgressBar) view.findViewById(R.id.movie_image_progress);
		
		if (images[index] == null)
		{
			movieImageView.setImageResource(0);
			progress.setVisibility(View.VISIBLE);
			
			ImageLoaderTask imageTask = new ImageLoaderTask(movieImageView, progress, index);
			imageTask.execute("http://pmc.ai.fh-erfurt.de:8080" + moviePreview.getImageURL());
		}
		else
		{
			movieImageView.setImageBitmap(images[index]);
			progress.setVisibility(View.GONE);
		}
		
		return view;
	}	
	
	/**
	 * Nachladen des Titelbildes des Films mit der zuvor ermittelten URL.
	 * 
	 * @author Samuel und Senta
	 *
	 */
	private class ImageLoaderTask extends AsyncTask<String, Integer, Bitmap>
	{
		private ImageView movieImageView = null;
		private ProgressBar progress = null;
		private int index = 0;
		
		
		
		public ImageLoaderTask(ImageView movieImageView, ProgressBar progress, int index)
		{
			super();
			this.movieImageView = movieImageView;
			this.progress = progress;
			this.index = index;
		}

		@Override
		protected Bitmap doInBackground(String... urls)
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			Bitmap image = null;
			
			if (urls[0] != null)
			{
		        try
		        {
					response = httpClient.execute(new HttpGet(urls[0]));
				}
		        catch (ClientProtocolException e)
		        {
		        	Log.e(getClass().getSimpleName(), "HTTP protocol error.", e);
				}
		        catch (IOException e)
				{
		        	Log.e(getClass().getSimpleName(), "Connection was aborted.", e);
				}
		        
		        if (response != null)
		        {	        	
		        	try
		        	{
						image = BitmapFactory.decodeStream(response.getEntity().getContent(), null, null);
					}
		        	catch (IllegalStateException e)
		        	{
		        		Log.e(getClass().getSimpleName(), "Response entity is not repeatable.", e);
					}
		        	catch (IOException e)
					{
		        		Log.e(getClass().getSimpleName(), "Response entity input stream could not be created.", e);
					}
		        }
			}
	        
			return image;
		}
		
		@Override
		protected void onPostExecute(Bitmap result)
		{
			if (result != null & movieImageView != null)
			{
				images[index] = result;
				movieImageView.setImageBitmap(images[index]);
				progress.setVisibility(View.GONE);
			}
		}
	}
}
