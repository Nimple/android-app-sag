package de.nimple.services.upgrade;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by dennis on 23.10.2014. Not a real Observer Patter beacause the
 * Observer doesnt implement the interface
 */
public class ProObservable {
    private ArrayList<Object> lsPro = new ArrayList<Object>();
    private ArrayList<Object> lsBasic = new ArrayList<Object>();

    public void addObserver(Object item, State state) {
        if (!(item instanceof MenuItem || item instanceof LinearLayout || item instanceof Button)) {
            throw new IllegalArgumentException();
        }
        switch (state) {
            case BASIC:
                lsBasic.add(item);
                break;
            case PRO:
                lsPro.add(item);
        }
    }

    public void notifyObservers(boolean isPro) {
        for (Object item : lsBasic) {
            if (item instanceof MenuItem) {
                ((MenuItem) item).setVisible(!isPro);
            } else if (item instanceof LinearLayout) {
                if (isPro) {
                    ((LinearLayout) item).setVisibility(View.GONE);
                } else {
                    ((LinearLayout) item).setVisibility(View.VISIBLE);
                }
            } else if (item instanceof Button) {
                if (isPro) {
                    ((Button) item).setVisibility(View.GONE);
                } else {
                    ((Button) item).setVisibility(View.VISIBLE);
                }
            }
        }
        for (Object item : lsPro) {
            if (item instanceof MenuItem) {
                ((MenuItem) item).setVisible(isPro);
            } else if (item instanceof LinearLayout) {
                if (isPro) {
                    ((LinearLayout) item).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) item).setVisibility(View.GONE);
                }
            } else if (item instanceof Button) {
                if (isPro) {
                    ((Button) item).setVisibility(View.VISIBLE);
                } else {
                    ((Button) item).setVisibility(View.GONE);
                }
            }
        }
    }

    public enum State {
        BASIC,
        PRO
    }
}
