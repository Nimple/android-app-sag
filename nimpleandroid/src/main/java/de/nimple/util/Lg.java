package de.nimple.util;

import android.util.Log;

import de.nimple.BuildConfig;

/**
 * Android Logging Helper
 *
 * @author Andreas Wenger
 */
public class Lg {
    private static final String TAG = "nimple";
    private static final float TAB_WIDTH = 8.0f;
    private static final int DEFAULT_MSG_WIDTH = 40;

    // d,e,i,v,w
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            String[] nMsg = createMsg(msg);
            Log.d(nMsg[0], nMsg[1]);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            String[] nMsg = createMsg(msg);
            Log.e(nMsg[0], nMsg[1]);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            String[] nMsg = createMsg(msg);
            Log.i(nMsg[0], nMsg[1]);
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            String[] nMsg = createMsg(msg);
            Log.v(nMsg[0], nMsg[1]);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            String[] nMsg = createMsg(msg);
            Log.w(nMsg[0], nMsg[1]);
        }
    }

    private static String[] createMsg(String rawMsg) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        String msg = rawMsg;
        String tag = TAG;

        try {
            Class clazz = Class.forName(ste.getClassName());
            tag = (String) clazz.getField("LOG_TAG").get(null);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
        }
        msg += createTabing(rawMsg.length());
        msg += "\t@" + ste.getFileName() + ":" + ste.getMethodName() + " (L" + ste.getLineNumber() + "): ";
        return new String[]{tag, msg};
    }

    private static String createTabing(int length) {
        int tabs = (int) Math.ceil((DEFAULT_MSG_WIDTH - length) / TAB_WIDTH);
        String result = "";
        for (int i = 0; i < tabs; i++)
            result += "\t";
        return result;
    }
}