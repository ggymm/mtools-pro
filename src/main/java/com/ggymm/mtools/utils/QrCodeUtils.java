package com.ggymm.mtools.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongym
 * @version 创建时间: 2022-04-01 18:16
 */
public class QrCodeUtils {

    private static final Map<EncodeHintType, Object> hints = new HashMap<>();
    private static final Map<DecodeHintType, Object> dHints = new HashMap<>();

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 2);

        dHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }

    private static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        final BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        final Graphics graphics = newImage.getGraphics();
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();
        return newImage;
    }

    private static BitMatrix updateBitMatrix(BitMatrix matrix, int margin) {
        final int tempM = margin * 2;
        //获取二维码图案的属性
        final int[] rec = matrix.getEnclosingRectangle();
        final int resWidth = rec[2] + tempM;
        final int resHeight = rec[3] + tempM;
        // 按照自定义边框生成新的BitMatrix
        final BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        // 循环，将二维码图案绘制到新的bitMatrix中
        for (int i = margin; i < resWidth - margin; i++) {
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    public static Image toImage(String content, int size) {
        try {
            int margin = 4;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            bitMatrix = updateBitMatrix(bitMatrix, margin);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            bufferedImage = zoomInImage(bufferedImage, size, size);

            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toDecode(Image image) {
        return toDecode(SwingFXUtils.fromFXImage(image, null));
    }

    public static String toDecode(BufferedImage bufferedImage) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Result result = new MultiFormatReader().decode(binaryBitmap, dHints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
