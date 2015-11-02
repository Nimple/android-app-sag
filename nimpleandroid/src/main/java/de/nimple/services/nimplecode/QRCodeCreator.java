package de.nimple.services.nimplecode;

import java.util.EnumMap;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import de.nimple.util.Lg;

public abstract class QRCodeCreator {
	public static Bitmap generateQrCode(String content) {
		BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

		int width0 = 800;
		int height0 = 800;

		int colorBack = 0xFF000000;
		int colorFront = 0xFFFFFFFF;

		QRCodeWriter writer = new QRCodeWriter();
		try {
			EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hint.put(EncodeHintType.MARGIN, 3); /* default = 4 */
			BitMatrix bitMatrix = writer.encode(content, barcodeFormat, width0, height0, hint);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {

					pixels[offset + x] = bitMatrix.get(x, y) ? colorBack : colorFront;
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			Lg.e(e.getMessage());
			return null;
		}
	}
}