package be.tomberndjesse.picaloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jesse on 17/09/2016.
 */
public class SettingsUtil {
    //We use this SettingsUtil class to make it easier to save data to the local storagefile.
    private Context context;
    private SharedPreferences sharedPreferences;
    public SettingsUtil(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.FILE_NAME, Context.MODE_PRIVATE);
    }
    public String getString(String key){
        return sharedPreferences.getString(key, "");
    }
    public void setString(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }
}
