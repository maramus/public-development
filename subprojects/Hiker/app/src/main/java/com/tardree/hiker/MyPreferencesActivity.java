package com.tardree.hiker;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;


/*
displays the main settings in PrefsFragment - uses preferences.xml
- contained intent starts HeadersActivity
*/


public class MyPreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyPreferencesActivity", "Entered onCreate");

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content,
                        new MainSettingsFragment()).commit();
    }

    public static class MainSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("MainSettingsFragment", "Entered onCreate");

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
