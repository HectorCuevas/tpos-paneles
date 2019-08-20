package com.rasoftec;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * -------------------------------------------------------------------------------------------------
 * Created by Norman on 24/02/19.
 * -------------------------------------------------------------------------------------------------
 */
public class PreferenceManager {

    /**
     * ------------------------------ Nombres de variables a utilizar ------------------------------
     * (estas variables son similares a las variables de sesion en web)
     */

    public static final String PREF_USER_ID="user_id";
    public static final String PREF_USERNAME ="username";
    public static final String PREF_SW1="status1";
    public static final String PREF_SW2="status2";
    public static final String PREF_SW3="status3";
    public static final String PREF_EDT1 = "editText1";
    public static final String PREF_ZONA = "0";
    public static final String PREF_STR_DEPTO = "Guatemala";
    public static final String PREF_STR_MUN = "Mixco";
    public static final String PREF_STR_ZONA = "1";


    /**
     * Nombre de la instancia de SharedPreference (es similar al nombre de la sesion en web)
     */
    public static final String NAME_PREF ="Preference_mi_app";


    //unico metodo para todas las variables
    //******************************************************************************

    public static void setPref(Context context, String prefName, String prefValue){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }
    public static void delPref(Context context, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(prefName);
        editor.apply();
    }
    public static String getPref(Context context, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME_PREF,
                Context.MODE_PRIVATE);
        return sharedPref.getString(prefName,"");
    }
    public static boolean checkPref(Context context, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME_PREF,
                Context.MODE_PRIVATE);
        return sharedPref.contains(prefName);
    }
}

