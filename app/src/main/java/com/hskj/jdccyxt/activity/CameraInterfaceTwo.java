package com.hskj.jdccyxt.activity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.CommonData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.PhotoTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.TimeTool;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class CameraInterfaceTwo extends Activity implements Callback, OnClickListener, OnLongClickListener, AutoFocusCallback {
	private SurfaceView mySurfaceView;// surfaceView声明
	private SurfaceHolder holder;// surfaceHolder声明
	private Camera myCamera;// 相机声明
	private String[] photoTypes, photoID;// 照片名称,id组
	private String currentPhotoType, filePath, jylsh, cjh, wjybh;
	boolean isLongClicked = false;// 是否点击标识
	private ImageView flashImageView, takePhotoImageView, savePhotoImageView, focusImageView, cancelPhotoImageView,
			photoImageView;
	private LinearLayout lin_show;
	private int startIndex = 0;
	private TextView currentPhotoTextView;
	private ProgressDialog progressDialog = null;
	private String suffix = "_suffix";
	private boolean isOpenFlash = false;
	private String ip, jgbh, jkxlh;
	private boolean photoValue;// 照片上传模式
	private int number = 1;
	private byte[] dataone = null;
	private int CAMERA_DIALOG_SHOW = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_SHOW;
	private int CAMERA_DIALOG_DISMISS = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_DISMISS;
	private int CAMERA_DIALOG_FAILURE = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_FAILURE;
	private int CAMERA_SURFACE_SHOW = StaticConst.CAMERAINTERFACE_CAMERA_SURFACE_SHOW;
	private int cameraPosition = 1;// 0代表前置摄像头，1代表后置摄像头
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == CAMERA_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在上传中，请稍等。。。", CameraInterfaceTwo.this);
				progressDialog.show();
			}
			if (msg.what == CAMERA_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == CAMERA_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), CameraInterfaceTwo.this);
			}
			if (msg.what == CAMERA_SURFACE_SHOW) {
				// ChangePhotoType();
				// myCamera.startPreview();// 开启预览
				if (number == 1) {
					takePhotoImageView.setEnabled(true);
					flashImageView.setVisibility(View.VISIBLE);
					takePhotoImageView.setVisibility(View.VISIBLE);
					cancelPhotoImageView.setVisibility(View.INVISIBLE);
					savePhotoImageView.setVisibility(View.INVISIBLE);
					currentPhotoTextView.setEnabled(true);
				}
				if (number == 2) {
					// 拍前置照片
					CameraInfo cameraInfotwo = new CameraInfo();
					int cameraCounttwo = Camera.getNumberOfCameras();
					for (int i = 0; i < cameraCounttwo; i++) {
						Camera.getCameraInfo(i, cameraInfotwo);
						if (cameraInfotwo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							try {
								myCamera.stopPreview();
								myCamera.release();
								myCamera = null;
								myCamera = Camera.open(i);
								initCamera();
								myCamera.setPreviewDisplay(holder);
								myCamera.startPreview();
							} catch (Exception e) {
								Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
							}
						}
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					myCamera.takePicture(null, null, jpeg);
				}
			}
		};
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_interface);
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		final SharedPreferences share = getSharedPreferences(StaticValues.BASIC_SETTING, 1);
		photoValue = share.getBoolean("PHOTOZ", true);
		filePath = getIntent().getStringExtra("PHOTOPATH");// 照片存放地址
		photoTypes = getIntent().getStringArrayExtra("PHOTOTYPES");// 照片名称组
		photoID = getIntent().getStringArrayExtra("PHOTOIDS");// 照片id组
		jylsh = getIntent().getStringExtra("JYLSH");// 检验流水号
		currentPhotoType = getIntent().getStringExtra("CURRENTNAME");// 当前照片名
		cjh = getIntent().getStringExtra("CJH");// 车架号
		wjybh = getIntent().getStringExtra("WJYBH");// 外检员编号
		for (int i = 0; i < photoTypes.length; i++)
			if (photoTypes[i].equals(currentPhotoType))
				startIndex = i;
		lin_show = (LinearLayout) this.findViewById(R.id.lin_show);
		if (currentPhotoType.equals("车辆识别代号拍照")) {
			lin_show.setVisibility(View.VISIBLE);
		}
		takePhotoImageView = (ImageView) this.findViewById(R.id.takePhotoImageView); //
		takePhotoImageView.setOnClickListener(this);
		currentPhotoTextView = (TextView) this.findViewById(R.id.currentPhotoType);// 当前照片
		flashImageView = (ImageView) this.findViewById(R.id.flashImageView);
		focusImageView = (ImageView) this.findViewById(R.id.focusImageView);
		photoImageView = (ImageView) this.findViewById(R.id.photoImageView);
		focusImageView.setVisibility(View.INVISIBLE);
		// 当前照片
		flashImageView.setOnClickListener(this);
		currentPhotoTextView.setText(currentPhotoType);
		currentPhotoTextView.setOnClickListener(this);
		currentPhotoTextView.setTextColor(Color.WHITE);
		currentPhotoTextView.setTextSize(20.0f);

		cancelPhotoImageView = (ImageView) this.findViewById(R.id.cancelPhotoImageView);// 取消
		savePhotoImageView = (ImageView) this.findViewById(R.id.savePhotoImageView); // 保存
		cancelPhotoImageView.setOnClickListener(this);
		savePhotoImageView.setOnClickListener(this);
		// 获得控件
		mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mySurfaceView.setOnClickListener(this);
		mySurfaceView.setOnLongClickListener(this);
		// 获得句柄
		holder = mySurfaceView.getHolder();
		// 设置类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 添加回调
		holder.addCallback(this);
	}

	// 创建jpeg图片回调数据对象
	PictureCallback jpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (number == 1) {
				dataone = data;
				number = 2;
				data = null;
				System.gc();
				/*
				 * flashImageView.setVisibility(View.INVISIBLE);
				 * takePhotoImageView.setVisibility(View.INVISIBLE);
				 * cancelPhotoImageView.setVisibility(View.VISIBLE);
				 * savePhotoImageView.setVisibility(View.VISIBLE);
				 * currentPhotoTextView.setEnabled(false);
				 */
				handler.sendEmptyMessage(CAMERA_SURFACE_SHOW);
			} else {
				FileOutputStream fos = null;
				File file = new File(filePath);
				// 按照指定的路径创建文件夹
				if (!file.exists())
					file.mkdirs();
				String fileFullPath = filePath + currentPhotoType + suffix + ".jpg";
				File file2 = new File(fileFullPath);
				if (file2.exists() && !file2.canWrite()) {
					Toast.makeText(CameraInterfaceTwo.this, "文件正常使用中，请稍后再试！", Toast.LENGTH_LONG).show();
				}
				try {
					fos = new FileOutputStream(fileFullPath);
					Bitmap dataBitmap = BytesToBimap(data);
					// 图片合成
					dataBitmap = mergeBitmap(BytesToBimap(dataone), dataBitmap);
					// 图片长度压缩
					dataBitmap = PhotoTool.getPhotoByPixel(dataBitmap, 960);
					// 加水印
					dataBitmap = createBitmap(dataBitmap, currentPhotoType, cjh);
					// 图片质量压缩
					data = PhotoTool.getCompressPhotoByPixel(dataBitmap);
					fos.write(data, 0, data.length);
					fos.close();
				} catch (Exception e) {
					if (fos != null)
						e.printStackTrace();
				}
				data = null;
				dataone = null;
				System.gc();
				flashImageView.setVisibility(View.INVISIBLE);
				takePhotoImageView.setVisibility(View.INVISIBLE);
				cancelPhotoImageView.setVisibility(View.VISIBLE);
				savePhotoImageView.setVisibility(View.VISIBLE);
				currentPhotoTextView.setEnabled(false);
				photoImageView.setImageBitmap(getLoacalBitmap(fileFullPath));
				photoImageView.setVisibility(View.VISIBLE);
				number = 1;
			}
		}
	};

	// 获取图片
	public static Bitmap getLoacalBitmap(String url) {
		try {
			Bitmap bmp = BitmapFactory.decodeFile(url);
			return bmp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		try {
			if (success) {
				if (isLongClicked)
					myCamera.takePicture(null, null, jpeg);
			} else
				takePhotoImageView.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(CameraInterfaceTwo.this, "autoFocus:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		isLongClicked = false;
	}

	/**
	 * 按钮长按事件
	 */
	@Override
	public boolean onLongClick(View v) {
		try {
			isLongClicked = true;
			myCamera.autoFocus(this);// 自动对焦

		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			// 拍照
			case R.id.takePhotoImageView:
				DeleteOldFile(filePath + "车辆查验员叠加拍照_副本.jpg");
				takePhotoImageView.setEnabled(false);
				myCamera.takePicture(null, null, jpeg);
				break;
			// 取消
			case R.id.cancelPhotoImageView:
				photoImageView.setVisibility(View.GONE);
				String fileFullPath = filePath + currentPhotoType + suffix + ".jpg";
				File file = new File(fileFullPath);
				if (file.exists() && file.canWrite()) {
					file.delete();
				}
				myCamera.startPreview();// 开启预览
				takePhotoImageView.setEnabled(true);
				flashImageView.setVisibility(View.VISIBLE);
				takePhotoImageView.setVisibility(View.VISIBLE);
				cancelPhotoImageView.setVisibility(View.INVISIBLE);
				savePhotoImageView.setVisibility(View.INVISIBLE);
				currentPhotoTextView.setEnabled(true);
				// 拍后置照片
				CameraInfo cameraInfotwo = new CameraInfo();
				int cameraCounttwo = Camera.getNumberOfCameras();
				for (int i = 0; i < cameraCounttwo; i++) {
					Camera.getCameraInfo(i, cameraInfotwo);
					if (cameraInfotwo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
						try {
							myCamera.stopPreview();
							myCamera.release();
							myCamera = null;
							myCamera = Camera.open(i);
							initCamera();
							myCamera.setPreviewDisplay(holder);
							myCamera.startPreview();
						} catch (Exception e) {
							Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
						}
					}
				}
				break;
			// 保存
			case R.id.savePhotoImageView:
				if (savephoto()) {
					final String dq_zpzl = MutualConversionNameID.getZpzlID(photoTypes, photoID, currentPhotoType);
					ArrayList<HashMap<String, String>> zpsj = SQLFuntion.query(CameraInterfaceTwo.this, jylsh, dq_zpzl);
					if (zpsj.size() == 0) {
						Object[] zpdata = { jylsh, TimeTool.getTiemData(), wjybh, dq_zpzl, currentPhotoType, "01" };// 01:初始化状态，02：正在上传状态，03：长城成功状态
						SQLFuntion.insert(CameraInterfaceTwo.this, zpdata);
					} else {
						Object[] data = { TimeTool.getTiemData(), jylsh, dq_zpzl };
						SQLFuntion.updatetwo(CameraInterfaceTwo.this, data);
					}
					if (!photoValue) {
						// 后台上传
						Object[] data = { "02", jylsh, dq_zpzl };
						SQLFuntion.update(CameraInterfaceTwo.this, data);
						CameraInterfaceTwo.this.finish();
					} else {
						// 实时传
						String photoPath = filePath + currentPhotoType + ".jpg";
						File filetwo = new File(photoPath);
						if (filetwo.exists()) {
							uploadPhotos();
						} else {
							MessageTool.setmessage(CAMERA_DIALOG_FAILURE, "照片：" + currentPhotoType + "不存在", handler);
						}
					}
				}
				break;
			case R.id.currentPhotoType:
				ChangePhotoType();
				break;
			case R.id.surfaceView1:
				try {
					if (cancelPhotoImageView.getVisibility() == View.INVISIBLE) {
						isLongClicked = false;
						myCamera.autoFocus(this);// 自动对焦
					}
				} catch (Exception e) {

				}
				break;
			// 闪光灯
			case R.id.flashImageView:
				try {
					Camera.Parameters params = myCamera.getParameters();
					if (isOpenFlash) {
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						flashImageView.setImageResource(R.drawable.flash_off);
						isOpenFlash = false;
					} else {
						params.setFlashMode(Parameters.FLASH_MODE_TORCH);// FLASH_MODE_TORCH
						flashImageView.setImageResource(R.drawable.flash_on);
						isOpenFlash = true;
					}
					myCamera.setParameters(params);
				} catch (Exception e) {

				}
				break;
			// 摄像头切换
			case R.id.cameraSwitchImageView:
				// 切换前后摄像头
				int cameraCount = 0;
				CameraInfo cameraInfo = new CameraInfo();
				cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

				for (int i = 0; i < cameraCount; i++) {
					Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
					if (cameraPosition == 1) {
						// 现在是后置，变更为前置
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							try {
								myCamera.stopPreview();// 停掉原来摄像头的预览
								myCamera.release();// 释放资源
								myCamera = null;// 取消原来摄像头
								myCamera = Camera.open(i);// 打开当前选中的摄像头
								initCamera();
								myCamera.setPreviewDisplay(holder);// 通过surfaceview显示取景画面
								myCamera.startPreview();// 开始预览
								cameraPosition = 0;
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
							}
							break;
						}
					} else {
						// 现在是前置， 变更为后置
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
							try {
								myCamera.stopPreview();
								myCamera.release();
								myCamera = null;
								myCamera = Camera.open(i);
								initCamera();
								myCamera.setPreviewDisplay(holder);
								myCamera.startPreview();
								cameraPosition = 1;
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
							}

							break;
						}
					}

				}
				break;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 上传照片
	 */
	public void uploadPhotos() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					handler.sendEmptyMessage(CAMERA_DIALOG_SHOW);
					// 调01J63接口
					/*String data01J63 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "01J63",
							get01J63Data(jgbh, jylsh, MutualConversionNameID.getZpzlID(photoTypes, photoID, currentPhotoType)),
							StaticValues.writeResult, StaticValues.timeoutFifteen, StaticValues.numberThree);*/
					//离线
					Thread.sleep(500);
					String data01J63 = StaticData.Data01J63;
					Log.i("AAA", "data01J63:" + data01J63);
					List<CodeDomain> codelists01J63 = XMLParsingMethods.saxcode(data01J63);
					handler.sendEmptyMessage(CAMERA_DIALOG_DISMISS);
					if (codelists01J63.get(0).getCode().equals("1")) {
						Object[] data = { "03", jylsh, MutualConversionNameID.getZpzlID(photoTypes, photoID, currentPhotoType) };
						SQLFuntion.update(CameraInterfaceTwo.this, data);
						CameraInterfaceTwo.this.finish();
					} else {
						MessageTool.setmessage(CAMERA_DIALOG_FAILURE, codelists01J63.get(0).getMessage(), handler);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(CAMERA_DIALOG_DISMISS);
					MessageTool.setmessage(CAMERA_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 获取即将提交01J63数据
	 */
	private String get01J63Data(String jyjgbh, String cljylsh, String clzpzl) {
		ArrayList<HashMap<String, String>> list = SQLFuntion.query(CameraInterfaceTwo.this, cljylsh, clzpzl);
		List<String> datalist = new ArrayList<String>();
		datalist.add(jyjgbh);
		datalist.add(list.get(0).get("jylsh"));
		datalist.add(list.get(0).get("pssj"));
		datalist.add(list.get(0).get("wjybh"));
		datalist.add(list.get(0).get("zpzl"));
		datalist.add("1");
		String newPhotoPath = filePath + currentPhotoType + ".jpg";
		String xmlData = CombinationXMLData.getPhotoWriteData(StaticValues.FieldName01J63, datalist,
				PhotoTool.getPhotoData(newPhotoPath));
		Log.i("AAA", "01J63xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 变更为下张图片
	 */
	void ChangePhotoType() {
		int currentIndex = 0;
		for (int i = 0; i < photoTypes.length; i++) {
			String photoTypeStr = photoTypes[i].replace(" ", "");
			String currentPhotoTypeStr = currentPhotoType.replace(" ", "");
			if (photoTypeStr.equals(currentPhotoTypeStr)) {
				currentIndex = i;
				break;
			}
		}
		String temp = "";
		String fileFullPath = "";
		while (true) {
			temp = photoTypes[currentIndex + 2 > photoTypes.length ? 0 : currentIndex + 1];
			for (int i = 0; i < photoTypes.length; i++)
				if (photoTypes[i].equals(temp) && i == startIndex)
					this.finish();
			fileFullPath = filePath + temp + ".jpg";
			if (CommonData.GetUsingFileName().equals(fileFullPath)) {
				currentIndex = (currentIndex + 2) > photoTypes.length ? 0 : currentIndex + 1;
				Toast.makeText(CameraInterfaceTwo.this, temp + "正在使用中！", Toast.LENGTH_LONG).show();
			} else
				break;
		}

		currentPhotoType = temp;
		currentPhotoTextView.setText(currentPhotoType);
		fileFullPath = filePath + currentPhotoType + ".jpg";
		CommonData.SetUsingFileName(fileFullPath);
		focusImageView.setVisibility(View.INVISIBLE);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try {
			initCamera();
			myCamera.startPreview();
		} catch (Exception e) {
			Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@SuppressWarnings("deprecation")
	public void initCamera() {
		Camera.Parameters params = myCamera.getParameters();
		// myCamera.setDisplayOrientation(90);
		params.setPictureFormat(PixelFormat.JPEG);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int widthR = display.getWidth();
		int heightR = display.getHeight();
		List<Size> sizes = params.getSupportedPreviewSizes();
		Size optimalPreviewSize = getOptimalPreviewSize(sizes, widthR, heightR);
		params.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		List<Size> psizes = params.getSupportedPictureSizes();
		Size optimalPictureSize = GetOptimalPictureSize(psizes);
		Log.i("AAA", "optimalPictureSize:" + optimalPictureSize.height + "    " + optimalPictureSize.width + "");
		params.setPictureSize(optimalPictureSize.width, optimalPictureSize.height);
		// params.setPictureSize(960, 720);
		params.setJpegQuality(50);
		myCamera.setParameters(params);
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public void writeSDFile(String str, String fileName) {
		try {
			FileWriter fw = new FileWriter(filePath + "//" + fileName, true);
			File f = new File(filePath + "//" + fileName);
			fw.write(str);
			FileOutputStream os = new FileOutputStream(f);
			DataOutputStream out = new DataOutputStream(os);
			out.writeShort(2);
			out.writeUTF("");
			System.out.println(out);
			fw.flush();
			fw.close();
			System.out.println(fw);
		} catch (Exception e) {
		}
	}

	private Size GetOptimalPictureSize(List<Size> sizes) {
		List<Size> tempSizes = new ArrayList<Size>();
		for (int i = 0; i < sizes.size(); i++) {
			if (0 < sizes.get(i).width && sizes.get(i).width < 3200)
				tempSizes.add(sizes.get(i));
		}
		Size curSize = tempSizes.get(0);
		for (int i = 0; i < tempSizes.size(); i++) {
			if ((float) ((float) curSize.width / (float) curSize.height) <= (float) ((float) tempSizes.get(i).width / (float) tempSizes
					.get(i).height)) {
				curSize = tempSizes.get(i);
			}
		}
		return curSize;
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 开启相机
		if (myCamera == null) {
			myCamera = Camera.open();
			try {
				myCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 关闭预览并释放资源
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;

	}

	public boolean savephoto() {
		try {
			String newFillFullPath = filePath + currentPhotoType + ".jpg";
			DeleteOldFile(newFillFullPath);
			String tempFileFullPath = filePath + currentPhotoType + suffix + ".jpg";
			File file = new File(tempFileFullPath);
			if (file.exists() && file.canWrite()) {
				file.renameTo(new File(newFillFullPath));
			}
			// ChangePhotoType();
			return true;
		} catch (Exception e) {
			Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
	}

	void DeleteOldFile(String fileFulePath) {
		for (int i = 0; i < 20; i++) {
			File file = new File(fileFulePath + i);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	// 由Byte转为Bimap
	public Bitmap BytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	// 由Bimap转为Byte
	public byte[] BitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	private Bitmap createBitmap(Bitmap photo, String strname, String newcjh) {
		// String str = TimeTool.getTiemData();
		// String sjimei = "imei:" + ((TelephonyManager)
		// CameraInterface.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		int width = photo.getWidth(), hight = photo.getHeight();
		Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上

		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
														// dst使用的填充区photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(25.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.RED);// 采用的颜色
		canvas.drawText(newcjh, 30, 45, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}

	// 获取字符串的宽度
	public float gettextwidth(String text, float Size) {
		TextPaint FontPaint = new TextPaint();
		FontPaint.setTextSize(Size);
		float textwidth = FontPaint.measureText(text);
		return textwidth;
	}

	/**
	 * 图片合成
	 * 
	 * @param firstBitmap
	 * @param secondBitmap
	 * @return
	 */
	private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
		Bitmap icon = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(icon);
		Paint photoPaint = new Paint();
		photoPaint.setDither(true);
		photoPaint.setFilterBitmap(true);
		Rect firstSrc = new Rect(0, 0, firstBitmap.getWidth(), firstBitmap.getHeight());// 要绘制的bitmap
																						// 区域
		Rect firstDst = new Rect(0, 0, firstBitmap.getWidth(), firstBitmap.getHeight());// 要将bitmap
																						// 绘制在屏幕的什么地方
		canvas.drawBitmap(firstBitmap, firstSrc, firstDst, photoPaint);
		Rect secondSrc = new Rect(0, 0, secondBitmap.getWidth(), secondBitmap.getHeight());
		Rect secondDrc = new Rect(firstBitmap.getWidth() / 5 * 4, 50, firstBitmap.getWidth(), (firstBitmap.getHeight() / 5) + 50);
		canvas.drawBitmap(secondBitmap, secondSrc, secondDrc, photoPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}

	// 按返回键时，显示弹出框
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			String fileFullPath = filePath + currentPhotoType + suffix + ".jpg";
			File file = new File(fileFullPath);
			if (cancelPhotoImageView.getVisibility() == View.VISIBLE && file.exists() && file.canWrite()) {
				file.delete();
			}
			finish();
			return true;
		}
		return false;
	}

	// Activity创建或者从被覆盖、后台重新回到前台时被调用
	@Override
	protected void onResume() {
		super.onResume();
		// 设置为横屏
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	// Activity被覆盖到下面或者锁屏时被调用
	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
		// 有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据

	}

	// 1、不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
	// 2、设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
	// 3、设置Activity的android:configChanges="orientation|keyboardHidden"时，切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法

}