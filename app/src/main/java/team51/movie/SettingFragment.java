package team51.movie;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingFragment extends PreferenceFragmentCompat {
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        addPreferencesFromResource(R.xml.pref_general);
    }
}