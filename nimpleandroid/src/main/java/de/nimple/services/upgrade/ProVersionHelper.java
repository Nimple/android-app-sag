package de.nimple.services.upgrade;

import android.content.Context;

import de.nimple.util.SharedPrefHelper;

/**
 * Created by dennis on 19.10.2014.
 */
public class ProVersionHelper {
    private Context m_ctx;
    private boolean m_isPro = false;
    private static ProVersionHelper m_proHelper = null;
    private ProObservable proObserver = new ProObservable();

    public static ProVersionHelper getInstance(Context ctx) {
        if (m_proHelper == null) {
            m_proHelper = new ProVersionHelper(ctx);
        }
        return m_proHelper;
    }

    private ProVersionHelper(Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException();
        }
        m_ctx = ctx;
        load();
    }

    public void save() {
        SharedPrefHelper.putBoolean(IS_PRO_VERSION, m_isPro, m_ctx);
    }

    public void load() {
        m_isPro = SharedPrefHelper.getBoolean(IS_PRO_VERSION, m_ctx);
    }

    public boolean IsPro() {
        return m_isPro;
    }

    public void setPro(boolean isPro) {
        m_isPro = isPro;
        proObserver.notifyObservers(m_isPro);
        save();
    }

    public void addObserver(Object item, ProObservable.State state){
        proObserver.addObserver(item, state);
    }

    public  void notifyObserver(){
        proObserver.notifyObservers(m_isPro);
    }

    /* --------------        CONSTANT   ------------------------------------       */
    private final String IS_PRO_VERSION = "isProVersion";

}
