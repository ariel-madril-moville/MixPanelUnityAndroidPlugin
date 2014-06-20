package com.mixpanel.android.mpmetrics;

import org.json.JSONObject;

import android.content.Context;

public class MixPanelAndroidPlugin
{
	Context context;
	String token;
	
	static MixpanelAPI mApiInstance = null;

	public static void Initialize(Context context, String token)
	{
		mApiInstance = MixpanelAPI.getInstance(context, token);
	}
	
	public static void Track(String eventName, String values)
	{
		mApiInstance.track(eventName, GetJsonFromString(values));
	}
	
	public static void RegisterSuperProperties(String values)
	{
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
							boolean valeuAsBool = Boolean.parseBoolean(keyValue[1]);
							json.put(keyValue[0], valeuAsBool);
						}
						catch (Exception notBool)
						{
							json.put(keyValue[0], keyValue[1]);
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
