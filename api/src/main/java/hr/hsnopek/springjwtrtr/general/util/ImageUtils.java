package hr.hsnopek.springjwtrtr.general.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ImageUtils {

	public static BufferedImage generateQRCode(String content, int width, int height) throws WriterException {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	public static String convertBufferedImageToBase64(BufferedImage bufferedImage) throws WriterException {
		byte[] bytes = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(bufferedImage, "png", baos);
			bytes = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(Base64.getEncoder().encode(bytes));
	}
}
