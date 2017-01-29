package com.jcedar.sdahyoruba.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.jcedar.sdahyoruba.R;

public class PreferenceFrag extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        settings.registerOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        settings.unregisterOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference devPref = (EditTextPreference)findPreference("developer");
        //devPref.setSummary("Afolayan Oluwaseyi");
        devPref.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.preference.Preference preference) {
                String url = "http://www.facebook.com/afolayanjeph";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });

        EditTextPreference desPref = (EditTextPreference)findPreference("designer");
        //desPref.setSummary("Bamisaye Oluwafemi");
        desPref.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.preference.Preference preference) {
                String url = "http://www.facebook.com/obamisaye";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });

        changeFontSize();


    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());


            onNotificationChanged(key);
        }
    };

    private void onNotificationChanged(String key) {
        if(key.equals("notification")){
            SwitchPreference switchPreference = (SwitchPreference) findPreference(key);
            if(null == switchPreference)  return;


        }
    }

    private void changeFontSize(){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Resources resources = getActivity().getResources();
        String fontSize = settings.getString("fonts", "20");

        ListPreference fontPref = (ListPreference) findPreference("fonts");
        fontPref.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                settings
                return false;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
    }

    private void updatePrefSummary(PreferenceFrag preferenceFrag) {

    }
}