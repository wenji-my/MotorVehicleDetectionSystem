package com.hskj.jdccyxt.toolunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import android.os.Environment;

/**
 * @������DocumentTool
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ��ļ�������ɾ���飬��
 * @�������ڣ�2016.04.14
 */
public class DocumentTool {

	/**
	 * �����ļ���
	 */
	public static void addFolder(String folder_name) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFolder = new File(sdCard.getCanonicalPath() + folder_name);
				if (!nameFolder.exists()) {
					nameFolder.mkdirs();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ļ�
	 */
	public static void addFiles(String file_name) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFile = new File(sdCard.getCanonicalPath() + file_name);
				if (!nameFile.exists()) {
					nameFile.createNewFile();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ���ļ�
	 */
	public static void deleteFiles(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete();
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int j = 0; j < files.length; j++) { // ����Ŀ¼�����е��ļ�
					deleteFiles(files[j]); // ��ÿ���ļ� ������������е���
				}
				file.delete();
			}
		}
	}

	/**
	 * ��ȡ�ļ�����
	 */
	public static String readFileContent(String path) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFile = new File(sdCard.getCanonicalPath() + path);
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(nameFile);
				int len = 0;
				StringBuffer sb = new StringBuffer("");
				while ((len = fis.read(buffer)) > 0) {
					sb.append(new String(buffer, 0, len));
				}
				fis.close();
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��д����
	 */
	public static void writeData(String path, String file_data) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFile = new File(sdCard.getCanonicalPath() + path);
				FileOutputStream out = new FileOutputStream(nameFile, false);
				out.write(file_data.getBytes("UTF-8"));
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ļ�д����
	 */
	public static void writtenFileData(String path, String file_data) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFile = new File(sdCard.getCanonicalPath() + path);
				RandomAccessFile raf = new RandomAccessFile(nameFile, "rw");
				raf.seek(nameFile.length());
				raf.write(file_data.getBytes("UTF-8"));
				raf.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
