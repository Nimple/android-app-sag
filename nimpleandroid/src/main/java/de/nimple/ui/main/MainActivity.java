package de.nimple.ui.main;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.dagger.BaseActivity;
import de.nimple.events.ApplicationStartedEvent;
import de.nimple.events.ContactAddedEvent;
import de.nimple.events.DuplicatedContactEvent;
import de.nimple.events.NimpleCodeScanFailedEvent;
import de.nimple.events.NimpleCodeScannedEvent;
import de.nimple.ui.main.fragments.ContactListFragment;
import de.nimple.ui.main.fragments.NimpleCardFragment;
import de.nimple.ui.main.fragments.NimpleCodeFragment;
import de.nimple.util.Lg;
import de.nimple.util.fragment.MenuHelper;

public class MainActivity extends BaseActivity {
    private NimplePagerAdapter adapter;

    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.fab_home)
    FloatingActionButton fab;

    private static final int REQUEST_CAMERA = 0;

    public static final int SCAN_REQUEST_CODE = 0x0000c0de;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adapter = new NimplePagerAdapter(getFragmentManager());

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);

        tabs.setupWithViewPager(pager);
        pager.setCurrentItem(1);

        EventBus.getDefault().post(new ApplicationStartedEvent());

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    public void onEvent(ContactAddedEvent ev) {
        // jump to contacts list
        pager.setCurrentItem(2);
        Toast.makeText(ctx, R.string.contact_added_toast, Toast.LENGTH_LONG).show();
    }

    public void onEvent(NimpleCodeScanFailedEvent ev) {
        Toast.makeText(ctx, R.string.contact_scan_failed, Toast.LENGTH_LONG).show();
    }

    public void onEvent(DuplicatedContactEvent ev) {
        Toast.makeText(ctx, String.format(getString(R.string.contact_scan_duplicated), ev.getContact().getName()), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fab_home)
    public void floatingActionButtonClicked() {
        MenuHelper.startScanner(this);
    }

    private class NimplePagerAdapter extends FragmentPagerAdapter {
        public NimplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            switch (pos) {
                case 0:
                    return getString(R.string.ncard_title);
                case 1:
                    return getString(R.string.ncode_title);
                case 2:
                    return getString(R.string.contacts_title);
                default:
                    return null;
            }
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return NimpleCardFragment.newInstance();
                case 1:
                    return NimpleCodeFragment.newInstance();
                case 2:
                    return ContactListFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Lg.d("requestCode:" + requestCode + ", resultCode:" + resultCode);

        if (requestCode == SCAN_REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult.getContents() != null) {
                String data = scanResult.getContents();
                Lg.d("scan result: " + data);
                EventBus.getDefault().post(new NimpleCodeScannedEvent(data));
            }
        }

    }
}