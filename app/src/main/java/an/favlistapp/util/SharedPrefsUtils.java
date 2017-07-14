package an.favlistapp.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


final public class SharedPrefsUtils {
    public SharedPrefsUtils(Context _mContext) {
    }


    public static String getStringPreference(Context context, String key, String defaultValue) {
        String value = defaultValue;
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getString(key, value);
        }
        return value;
    }


    public static String getStringPreferenceWithDefaultValue(Context context, String key, String defaultValue) {
        String temp = getStringPreference(context, key, defaultValue);
        return temp != null ? temp : defaultValue;
    }


    public static boolean setStringPreference(Context context, String key, String value) {

        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a float value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static float getFloatPreference(Context context, String key, float defaultValue) {
        float value = defaultValue;
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a float value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setFloatPreference(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a long value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static long getLongPreference(Context context, String key, long defaultValue) {
        long value = defaultValue;
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a long value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setLongPreference(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve an integer value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static int getIntegerPreference(Context context, String key, int defaultValue) {
        int value = defaultValue;
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write an integer value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setIntegerPreference(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a boolean value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        boolean value = defaultValue;
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a boolean value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    public void saveDataToList(Context context, ListUtils users) {
        SharedPreferences settings = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();


        List<ListUtils> details = null;
        int leght;
        if (getUsers(context) != null) {
            leght = getUsers(context).size();
            if (leght > 0) {
                details = getUsers(context);
                details.add(users);
            }
        } else {
            details = new ArrayList<>();
            details.add(users);
        }

        Gson gson = new Gson();
        String jsonUsers = gson.toJson(details);

        editor.putString("user", jsonUsers);

        editor.commit();
    }

    public void clearList(Context context) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE).edit();
        sharedPreferences.clear();
        sharedPreferences.commit();

    }


    public ArrayList<ListUtils> getUsers(Context context) {
        SharedPreferences settings;
        ArrayList<ListUtils> users;
        settings = context.getSharedPreferences("paytmCash", Context.MODE_PRIVATE);
        if (settings.contains("user")) {
            String jsonUsers = settings.getString("user", null);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Type type = new TypeToken<ArrayList<ListUtils>>() {
            }.getType();
            ArrayList<ListUtils> userItems = gson.fromJson(jsonUsers, type);
            users = new ArrayList<ListUtils>(userItems);
        } else
            return null;
        return (ArrayList<ListUtils>) users;
    }
}
