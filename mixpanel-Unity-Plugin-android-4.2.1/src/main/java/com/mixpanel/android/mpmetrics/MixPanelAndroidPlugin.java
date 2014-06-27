package com.mixpanel.android.mpmetrics;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class MixPanelAndroidPlugin
{
	Context context;
	String token;
	
	static MixpanelAPI mApiInstance = null;

	public static void Initialize(Context context, String token)
	{
		Log.d("tag","entered initialize inside plugin with toke " + token );
		if(context == null)
			Log.d("tag","context is null");
		
		mApiInstance = MixpanelAPI.getInstance(context, token);
		
		if(mApiInstance != null)
			Log.d("tag", "mapi instance ok.");
	}
	
	public static void Track(String eventName, String values)
	{
		mApiInstance.track(eventName, GetJsonFromString(values));
	}
	
	public static void RegisterSuperProperties(String values)
	{
		if(mApiInstance == null)
		{
			Log.d("tag","api is null...");
		}
		mApiInstance.registerSuperProperties(GetJsonFromString(values));
	}
	
	public static void RegisterSuperPropertiesOnce(String values)
	{
		mApiInstance.registerSuperPropertiesOnce(GetJsonFromString(values));
	}
	
	public static void Flush()
	{
		mApiInstance.flush();
	}
	
	static JSONObject GetJsonFromString(String str)
	{
		String[] properties = str.split(";");
		JSONObject json = new JSONObject();
		for(int i = 0; i < properties.length; i++)
		{
			String[] keyValue = properties[i].split("/");
			try {
				
				try
				{
					int valueAsInt = Integer.parseInt(keyValue[1]);
					json.put(keyValue[0], valueAsInt);
				}
				catch(Exception notInt)
				{
					try
					{
						float valueAsFloat = Float.parseFloat(keyValue[1]);
						json.put(keyValue[0], valueAsFloat);
					}
					catch (Exception notFloat)
					{
						try
						{
							if (keyValue[1].equalsIgnoreCase("true") || keyValue[1].equalsIgnoreCase("false")) 
							{
								boolean valeuAsBool = Boolean.parseBoolean(keyValue[1]);
								json.put(keyValue[0], valeuAsBool);
							}
							else
							{
								throw new Exception();
							}
						}
						catch (Exception notBool)
						{
							try
							{
								JSONArray jsonArray = new JSONArray();
								String[] itemAsArray = keyValue[1].split(",");
								
								if(itemAsArray.length > 1)
								{
									for(int j = 0; j < itemAsArray.length; j++)
									{
										jsonArray.put(itemAsArray[j]);
									}
									json.put(keyValue[0], jsonArray);
								}
								else
								{
									throw new Exception();
								}
							}
							catch(Exception notAnArray)
							{
								json.put(keyValue[0], keyValue[1]);
							}
						}
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
}
