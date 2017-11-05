package com.hskj.jdccyxt.toolunit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.util.Base64;

public class PhotoTool {
	public static String getPhotoData(String photoPath) {
		String photoData = null;
		File file = new File(photoPath);
		if (!file.exists())
			return "";
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
		photoData = base64(bitmap);
		return photoData;
	}

	// base64格式转换
	public static String base64(Bitmap bitmap) {
		String photodatabase64 = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		photodatabase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
		return photodatabase64;
	}

	// 将字符串转换成Bitmap类型
	public static Bitmap stringtoBitmap(String string) {
		Bitmap bitmap = null;
		try {
			// byte[] srtbyte = string.getBytes("UTF-8");
			byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 对图片进行长度压缩
	 * 
	 * @param photoPath
	 * @return
	 */
	public static byte[] getCompressPhotoByPixel(Bitmap photoBitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 将压缩后的图片保存到baos中
		int quality = 100;
		while (baos.toByteArray().length > 100 * 1024) {
			baos.reset();
			photoBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 3;
		}
		byte[] reducedPhoto = baos.toByteArray();
		return reducedPhoto;
	}

	public static Bitmap getPhotoByPixel(Bitmap photoBitmap, double newWidth) {
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (photoBitmap.getWidth() <= newWidth)
			return photoBitmap;
		float width = photoBitmap.getWidth();
		float height = photoBitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = 0;
		float scaleHeight = 0;
		double newHeight = newWidth * height / width;
		if (width > height) {
			scaleWidth = (float) (newWidth / width);
			scaleHeight = (float) (newHeight / height);
		} else {
			scaleWidth = (float) (newWidth / height);
			scaleHeight = (float) (newHeight / width);
		}
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(photoBitmap, 0, 0, (int) width, (int) height, matrix, true);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// byte[] reducedPhoto = baos.toByteArray();
		return bitmap;
	}

}
