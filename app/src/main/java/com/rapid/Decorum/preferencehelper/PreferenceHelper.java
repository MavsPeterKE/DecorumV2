package com.rapid.Decorum.preferencehelper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class PreferenceHelper {

    Context context;

    SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        this.context = context;

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void putData(String key,String data)
    {
sharedPreferences.edit().putString(key,data).commit();
    }

    public String getData(String key)
    {
       return sharedPreferences.getString(key,"");
    }

    public void putBooleanData(String key,boolean data)
    {
        sharedPreferences.edit().putBoolean(key,data).commit();
    }

    public boolean getBoolData(String key)
    {
        return sharedPreferences.getBoolean(key,false);

    }

    public void clearData()
    {
        sharedPreferences.edit().clear().commit();
    }
}
