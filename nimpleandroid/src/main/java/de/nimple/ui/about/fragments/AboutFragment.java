package de.nimple.ui.about.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import de.nimple.R;
import de.nimple.util.Lg;

/**
 * Created by bjohn on 03/11/15.
 */
public class AboutFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);
        setVersion();
    }

    private void setVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            Preference aboutVersion = findPreference("prefs_about_version");
            aboutVersion.setSummary("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            Lg.e("should never occur: " + e.toString());
        }
    }
}
