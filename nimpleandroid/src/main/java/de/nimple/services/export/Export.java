package de.nimple.services.export;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * Created by dennis on 16.09.2014.
 */
public class Export<T> {

    private T content;
    private Type type;
    private String extension;
    private String filename;
    private String path;

    public Export(T content) {
        this.content = content;
        if (content instanceof String) {
            type = Type.VCard;
            extension = Export.getExtensionByType(Type.VCard);
            filename = Export.getDefaultFilenameByType(Type.VCard);
        } else if (content instanceof Bitmap) {
            type = Type.Barcode;
            extension = Export.getExtensionByType(Type.Barcode);
            filename = Export.getDefaultFilenameByType(Type.Barcode);
        }
        path = Export.getDefaultPath();
    }

    public T getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    public String getExtension(){
        return extension;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public String getFilename(){
        return  filename;
    }

    public String getPath() {
        return path;
    }

    public static enum Type {
        VCard,
        Barcode
    }

    public static String getExtensionByType(Type type) {
        switch (type) {
            case VCard:
                return ".vcf";
            case Barcode:
                return ".png";
            default:
                return ".txt";
        }
    }

    public static String getDefaultPath(){
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS;
    }

    public static String getDefaultFilenameByType(Type type){
        switch (type) {
            case VCard:
                return "NimpleCard";
            case Barcode:
                return "NimpleCode";
            default:
                return "Nimple";
        }
    }
}


