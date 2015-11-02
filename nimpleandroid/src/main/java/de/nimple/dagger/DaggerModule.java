package de.nimple.dagger;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import de.nimple.AnalyticsController;
import de.nimple.DataSyncController;
import de.nimple.services.contacts.ContactsSQLiteImpl;
import de.nimple.services.contacts.ContactsService;
import de.nimple.services.nimplecode.NimpleCodeHelper;
import de.nimple.services.nimplecode.NimpleCodeService;
import de.nimple.ui.contact.DisplayContactActivity;
import de.nimple.ui.main.MainActivity;
import de.nimple.ui.main.fragments.ContactListFragment;

/**
 * Modules which uses injection are declared here.
 */
@Module(injects = {
        AnalyticsController.class,
        DataSyncController.class,
        MainActivity.class,
        ContactListFragment.class,
        DisplayContactActivity.class
}, library = true, complete = false)
public class DaggerModule {
    private Context context;

    public DaggerModule(Context context) {
        this.context = context;
    }

    @Provides
    @Named("App")
    public Context provideAppContext() {
        return context;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    public ContactsService provideContactsService(@Named("App") Context context) {
        return new ContactsSQLiteImpl(context);
    }

    @Provides
    @Singleton
    public NimpleCodeService provideNimpleCodeService(@Named("App") Context context) {
        return new NimpleCodeHelper(context);
    }
}
