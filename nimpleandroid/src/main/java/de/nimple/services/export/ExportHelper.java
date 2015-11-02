package de.nimple.services.export;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dennis on 17.09.2014.
 */
public class ExportHelper {

    public static boolean save(Export export, File file) {
        if (export == null || file == null) {
            return false;
        }
        try {
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(file);
            if (export.getType() == Export.Type.Barcode) {
                ((Bitmap) export.getContent()).compress(Bitmap.CompressFormat.PNG, 100, fOut);
            } else if (export.getType() == Export.Type.VCard) {
                fOut.write(((String)export.getContent()).getBytes());
            }
            fOut.flush();
            fOut.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
