package de.fhe.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Speichert ein Objekt in einer Datei oder lädt ein Onjekt aus einer Datei.
 * 
 * @author Senta und Samuel
 *
 */
public class ObjectUtil
{
	public static void saveObjectToFile(Activity owner, String filename, Object objectToSave)
	{
		try
		{			
			FileOutputStream fos = owner.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream objOut = new ObjectOutputStream(fos);
			objOut.writeObject(objectToSave);
			objOut.close();
			fos.close();
		}
		catch (IOException e)
		{
			Log.i(owner.getClass().getSimpleName(), "Error writing output file " + filename + ".");
		}
	}
	
	public static Object loadObjectFromFile(Activity owner, String filename)
	{
		Object result = null;
		
		try
		{
			FileInputStream fis = owner.openFileInput(filename);
			ObjectInputStream objIn = new ObjectInputStream(fis);
			result = objIn.readObject();
			objIn.close();
			fis.close();
		}
		catch (ClassNotFoundException e)
		{
			Log.i(owner.getClass().getSimpleName(), "Input file could not be found.");
		}
		catch (IOException e)
		{
			Log.i(owner.getClass().getSimpleName(), "Error reading input file " + filename + ".");
		}
		
		return result;
	}
}
