package de.nimple.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Dennis on 12.07.2015.
 */
public class Device {

    public static String getOwnerEmail(Context ctx){
        AccountManager manager = AccountManager.get(ctx);
        Account[] accounts = manager.getAccountsByType("com.google");
        if(accounts.length > 0){
            return accounts[0].name;
        }
        return "";
    }

    public static String getOwnerNumber(Context ctx){
        TelephonyManager tm = (TelephonyManager)ctx.getSystemService(ctx.TELEPHONY_SERVICE);
        if(tm.getLine1Number() != null) {
            return tm.getLine1Number();
        }
        return "";
    }
}
