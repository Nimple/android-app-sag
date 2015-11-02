package de.nimple.dagger;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.nimple.events.NoOpEvent;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    EventBus eventBus;
    public Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerApplication) getApplication()).inject(this);
        eventBus.register(this);
        ctx = getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    public void onEvent(NoOpEvent ev) {
    }
}
