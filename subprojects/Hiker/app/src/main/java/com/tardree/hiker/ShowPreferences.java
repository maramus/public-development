package com.tardree.hiker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Set;

/**
 * gets the shared preferences and shows them in a table
 */
public class ShowPreferences extends Activity {

    private static final String ACCURACY_PREFERENCE = "accuracy_preference";
    private static final String CHILD_ACCURACY_PREFERENCE = "child_accuracy_preference";
    private static final String AUDIO_PREFERENCE = "audio_preference";
    private static final String NAME_PREFERENCE = "name_preference";
    private static final String LOGGING_PREFERENCE = "logging_preference";
    private static final String FORMAT_PREFERENCE = "format_preference";
    private static final String BATTERY_SAVING_PREFERENCE = "battery_saving_preference";
    private static final String MULTI_CHOICE_PREFS = "multi_choice_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ShowPreferences", "Entered onCreate");
        setContentView(R.layout.activity_showpreferences);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean accuracy_Preference = sharedPreferences
                .getBoolean(ACCURACY_PREFERENCE, true);

        boolean childAccuracyPreference = sharedPreferences.getBoolean(CHILD_ACCURACY_PREFERENCE, true);
        boolean audioPreference = sharedPreferences.getBoolean(AUDIO_PREFERENCE, true);
        String namePref = sharedPreferences.getString(NAME_PREFERENCE, "empty");
        boolean loggingPreference = sharedPreferences.getBoolean(LOGGING_PREFERENCE, true);
        String formatPreference = sharedPreferences.getString(FORMAT_PREFERENCE, "empty");
        String batterySavingPreference = sharedPreferences.getString(BATTERY_SAVING_PREFERENCE, "empty");

        Set<String> multiChoicePrefs = sharedPreferences.getStringSet(MULTI_CHOICE_PREFS, null);
        String multiChoiceString = "";
        for(String s:multiChoicePrefs) {
            multiChoiceString = multiChoiceString + "\n" + s;
        }

        TextView textViewMultipleChoice = (TextView) findViewById(R.id.multiple_choice_preference);
        textViewMultipleChoice.setText(multiChoiceString);

        TextView textViewAccuracy = (TextView) findViewById(R.id.accuracy_preference);
        textViewAccuracy.setText(String.valueOf(accuracy_Preference));

        TextView textViewChildAccuracy = (TextView) findViewById(R.id.child_accuracy_preference);
        textViewChildAccuracy.setText(String.valueOf(childAccuracyPreference));

        TextView textViewAudio = (TextView) findViewById(R.id.audio_preference);
        textViewAudio.setText(String.valueOf(audioPreference));

        TextView textViewLogging = (TextView) findViewById(R.id.logging_preference);
        textViewLogging.setText(String.valueOf(loggingPreference));

        TextView textViewFormat = (TextView) findViewById(R.id.format_preference);
        textViewFormat.setText(String.valueOf(formatPreference));

        TextView textViewBatterySaving = (TextView) findViewById(R.id.battery_saving_preference);
        textViewBatterySaving.setText(String.valueOf(batterySavingPreference));

        TextView textViewName = (TextView) findViewById(R.id.name_preference);
        textViewName.setText(String.valueOf(namePref));
    }

}
