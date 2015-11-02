package de.nimple.ui.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import de.nimple.R;
import de.nimple.util.Lg;

public class AboutNimpleActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		addPreferencesFromResource(R.xml.about);

		setVersion();
	}

	private void setVersion() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;

			Preference aboutVersion = findPreference("prefs_about_version");
			aboutVersion.setSummary("Version " + version);
		} catch (NameNotFoundException e) {
			Lg.e("should never occur: " + e.toString());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return false;
		}
	}
}