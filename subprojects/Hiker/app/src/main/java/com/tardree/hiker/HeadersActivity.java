package com.tardree.hiker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *          displays preference_headers.xml - selection of either Header
 *          displays appropriate (determined by extra argument)
 *          preferences in HeadersFragment
 */
public class HeadersActivity extends PreferenceActivity {

    private static List<String> fragments = new ArrayList<String>();

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers,target);
        fragments.clear();
        for (Header header : target) {
            fragments.add(header.fragment);
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return fragments.contains(fragmentName);
    }


    public static class HeadersFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("HeadersFragment", "Entered onCreate");
            String settings = getArguments().getString("header");
            if ("one".equals(settings)) {
                addPreferencesFromResource(R.xml.preference_header_one);
            } else if ("two".equals(settings)) {
                addPreferencesFromResource(R.xml.preference_header_two);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.d("HeadersFragment", "Entered onResume");
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d("HeadersFragment", "Entered onPause");
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences
                                                sharedPreferences, String key) {
            Log.d("HeadersFragment", "Entered onSharedPreferenceChanged");
            Preference preference = findPreference(key);
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}
