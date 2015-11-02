package de.nimple;

import de.nimple.dagger.DaggerApplication;

public final class NimpleApplication extends DaggerApplication {
	// Objects which feel pretty well within the actual Application
	private AnalyticsController anc;
	private DataSyncController dsc;

	@Override
	public void onCreate() {
		super.onCreate();
		anc = new AnalyticsController(this);
		dsc = new DataSyncController(this);
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(), getString(R.string.app_id));
	}

	@Override
	public void onTerminate() {
		dsc.finish();
		anc.finish();
		super.onTerminate();
	}
}
