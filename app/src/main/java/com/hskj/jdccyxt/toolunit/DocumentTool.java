package com.hskj.jdccyxt.toolunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import android.os.Environment;

/**
 * @类名：DocumentTool
 * @author:广东泓胜科技有限公司
 * @实现功能：文件的增，删，查，改
 * @创建日期：2016.04.14
 */
public class DocumentTool {

	/**
	 * 新增文件夹
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
	 * 添加文件
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
	 * 删除文件
	 */
	public static void deleteFiles(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete();
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int j = 0; j < files.length; j++) { // 遍历目录下所有的文件
					deleteFiles(files[j]); // 把每个文件 用这个方法进行迭代
				}
				file.delete();
			}
		}
	}

	/**
	 * 读取文件内容
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
	 * 重写数据
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
	 * 续文件写数据
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
