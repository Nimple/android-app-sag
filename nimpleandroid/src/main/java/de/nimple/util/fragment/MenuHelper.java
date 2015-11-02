package de.nimple.util.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;

import java.io.File;

import de.nimple.R;
import de.nimple.services.export.Export;
import de.nimple.services.export.ExportHelper;
import de.nimple.services.export.IExportExtender;
import de.nimple.ui.about.AboutNimpleActivity;
import de.nimple.ui.main.MainActivity;
import de.nimple.ui.pro.ProActivity;

/**
 * Created by NoName on 20.09.2014.
 */
public class MenuHelper {

    public static void selectMenuItem(MenuItem item, Fragment frag){
        if (item.getItemId() == R.id.menu_add) {
            startScanner(frag.getActivity());
        } else if (item.getItemId() == R.id.menu_about) {
            startAboutNimpleActivity(frag.getActivity().getApplicationContext());
        } else if (item.getItemId() == R.id.menu_share) {
            shareApp(frag.getActivity(), null);
        } else if (item.getItemId() == R.id.menu_feedback) {
            sendFeedback(frag.getActivity());
        } else if (item.getItemId() == R.id.menu_save) {
            save(((IExportExtender)frag).getExport(),frag.getActivity().getApplicationContext());
        } else if(item.getItemId() == R.id.menu_export){
            export(frag);
        } else if(item.getItemId() == R.id.menu_proVersion || item.getItemId() == R.id.menu_proVersion2){
            startProActivity(frag.getActivity().getApplicationContext());
        }
    }

    public static void export(Fragment frag){
        if(!(frag instanceof IExportExtender)){
            return;
        }
        export(((IExportExtender)frag).getExport(),frag.getActivity());
    }

    public static void export(Export export, Activity act){
        File file = new File(export.getPath(), export.getFilename() + export.getExtension());
        ExportHelper.save(export, file);
        shareApp(act, Uri.fromFile(file));
    }

    public static void save(Export export, Context ctx) {
        File file = new File(export.getPath(), export.getFilename() + export.getExtension());
        if(ExportHelper.save(export, file)){
            Toast.makeText(ctx,ctx.getString(R.string.ab_export_save), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ctx, ctx.getString(R.string.ab_export_err), Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendFeedback(Activity activity ){
        Context ctx = activity.getApplicationContext();
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity);
        intentBuilder.setType("message/rfc822");
        intentBuilder.addEmailTo(ctx.getString(R.string.feedback_email));
        intentBuilder.setSubject(ctx.getString(R.string.feedback_subject));
        intentBuilder.setText(ctx.getString(R.string.feedback_text));
        intentBuilder.setChooserTitle(ctx.getString(R.string.feedback_chooser));

        Intent intent = intentBuilder.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void shareApp(Activity activity,Uri attachment ) {
        Context ctx = activity.getApplicationContext();
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity);
        intentBuilder.setType("text/plain");
        intentBuilder.setSubject(ctx.getString(R.string.share_app_subject));
        intentBuilder.setText(ctx.getString(R.string.share_app_text));
        intentBuilder.setChooserTitle(ctx.getString(R.string.share_app_chooser));
        if(attachment != null) {
            intentBuilder.setStream(attachment);
        }
        Intent intent = intentBuilder.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void startAboutNimpleActivity(Context ctx) {
        Intent intent = new Intent(ctx, AboutNimpleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void startScanner( Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Intent intent = new Intent(Intents.Scan.ACTION);
        intent.setPackage("de.nimple");
        intent.putExtra(Intents.Scan.FORMATS, "QR_CODE");
        intent.putExtra(Intents.Scan.WIDTH, w);
        intent.putExtra(Intents.Scan.HEIGHT, h / 2);
        activity.startActivityForResult(intent, MainActivity.SCAN_REQUEST_CODE);
    }

    public static void startProActivity(Context ctx){
        Intent intent = new Intent(ctx, ProActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
