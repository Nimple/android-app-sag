package de.nimple.dagger;

import android.app.Application;

import java.util.Arrays;

import dagger.ObjectGraph;

/**
 * Builds the dagger object graph for injection.
 */
public abstract class DaggerApplication extends Application {
	private ObjectGraph objectGraph;

	@Override
	public void onCreate() {
		super.onCreate();
		objectGraph = ObjectGraph.create(getModules());
	}

	public Object[] getModules() {
		return Arrays.asList(new DaggerModule(this)).toArray();
	}

	/**
	 * Inject every dependency declared in the object with the @Inject annotation if the dependency
	 * has been already declared in a module and already initialized by Dagger.
	 *
	 * @param target to inject.
	 */
	public void inject(Object target) {
		objectGraph.inject(target);
	}
}
