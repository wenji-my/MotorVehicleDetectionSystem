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
	private SurfaceView mySurfaceView;// surfaceView����
	private SurfaceHolder holder;// surfaceHolder����
	private Camera myCamera;// �������
	private String[] photoTypes, photoID;// ��Ƭ����,id��
	private String currentPhotoType, filePath, jylsh, cjh, wjybh;
	boolean isLongClicked = false;// �Ƿ�����ʶ
	private ImageView flashImageView, takePhotoImageView, savePhotoImageView, focusImageView, cancelPhotoImageView,
			photoImageView;
	private LinearLayout lin_show;
	private int startIndex = 0;
	private TextView currentPhotoTextView;
	private ProgressDialog progressDialog = null;
	private String suffix = "_suffix";
	private boolean isOpenFlash = false;
	private String ip, jgbh, jkxlh;
	private boolean photoValue;// ��Ƭ�ϴ�ģʽ
	private int number = 1;
	private byte[] dataone = null;
	private int CAMERA_DIALOG_SHOW = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_SHOW;
	private int CAMERA_DIALOG_DISMISS = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_DISMISS;
	private int CAMERA_DIALOG_FAILURE = StaticConst.CAMERAINTERFACE_CAMERA_DIALOG_FAILURE;
	private int CAMERA_SURFACE_SHOW = StaticConst.CAMERAINTERFACE_CAMERA_SURFACE_SHOW;
	private int cameraPosition = 1;// 0����ǰ������ͷ��1�����������ͷ
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == CAMERA_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("�����ϴ��У����Եȡ�����", CameraInterfaceTwo.this);
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
				// myCamera.startPreview();// ����Ԥ��
				if (number == 1) {
					takePhotoImageView.setEnabled(true);
					flashImageView.setVisibility(View.VISIBLE);
					takePhotoImageView.setVisibility(View.VISIBLE);
					cancelPhotoImageView.setVisibility(View.INVISIBLE);
					savePhotoImageView.setVisibility(View.INVISIBLE);
					currentPhotoTextView.setEnabled(true);
				}
				if (number == 2) {
					// ��ǰ����Ƭ
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// �ޱ���
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_interface);
		// ��ȡ�����е�����
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		final SharedPreferences share = getSharedPreferences(StaticValues.BASIC_SETTING, 1);
		photoValue = share.getBoolean("PHOTOZ", true);
		filePath = getIntent().getStringExtra("PHOTOPATH");// ��Ƭ��ŵ�ַ
		photoTypes = getIntent().getStringArrayExtra("PHOTOTYPES");// ��Ƭ������
		photoID = getIntent().getStringArrayExtra("PHOTOIDS");// ��Ƭid��
		jylsh = getIntent().getStringExtra("JYLSH");// ������ˮ��
		currentPhotoType = getIntent().getStringExtra("CURRENTNAME");// ��ǰ��Ƭ��
		cjh = getIntent().getStringExtra("CJH");// ���ܺ�
		wjybh = getIntent().getStringExtra("WJYBH");// ���Ա���
		for (int i = 0; i < photoTypes.length; i++)
			if (photoTypes[i].equals(currentPhotoType))
				startIndex = i;
		lin_show = (LinearLayout) this.findViewById(R.id.lin_show);
		if (currentPhotoType.equals("����ʶ���������")) {
			lin_show.setVisibility(View.VISIBLE);
		}
		takePhotoImageView = (ImageView) this.findViewById(R.id.takePhotoImageView); //
		takePhotoImageView.setOnClickListener(this);
		currentPhotoTextView = (TextView) this.findViewById(R.id.currentPhotoType);// ��ǰ��Ƭ
		flashImageView = (ImageView) this.findViewById(R.id.flashImageView);
		focusImageView = (ImageView) this.findViewById(R.id.focusImageView);
		photoImageView = (ImageView) this.findViewById(R.id.photoImageView);
		focusImageView.setVisibility(View.INVISIBLE);
		// ��ǰ��Ƭ
		flashImageView.setOnClickListener(this);
		currentPhotoTextView.setText(currentPhotoType);
		currentPhotoTextView.setOnClickListener(this);
		currentPhotoTextView.setTextColor(Color.WHITE);
		currentPhotoTextView.setTextSize(20.0f);

		cancelPhotoImageView = (ImageView) this.findViewById(R.id.cancelPhotoImageView);// ȡ��
		savePhotoImageView = (ImageView) this.findViewById(R.id.savePhotoImageView); // ����
		cancelPhotoImageView.setOnClickListener(this);
		savePhotoImageView.setOnClickListener(this);
		// ��ÿؼ�
		mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mySurfaceView.setOnClickListener(this);
		mySurfaceView.setOnLongClickListener(this);
		// ��þ��
		holder = mySurfaceView.getHolder();
		// ��������
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ��ӻص�
		holder.addCallback(this);
	}

	// ����jpegͼƬ�ص����ݶ���
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
				// ����ָ����·�������ļ���
				if (!file.exists())
					file.mkdirs();
				String fileFullPath = filePath + currentPhotoType + suffix + ".jpg";
				File file2 = new File(fileFullPath);
				if (file2.exists() && !file2.canWrite()) {
					Toast.makeText(CameraInterfaceTwo.this, "�ļ�����ʹ���У����Ժ����ԣ�", Toast.LENGTH_LONG).show();
				}
				try {
					fos = new FileOutputStream(fileFullPath);
					Bitmap dataBitmap = BytesToBimap(data);
					// ͼƬ�ϳ�
					dataBitmap = mergeBitmap(BytesToBimap(dataone), dataBitmap);
					// ͼƬ����ѹ��
					dataBitmap = PhotoTool.getPhotoByPixel(dataBitmap, 960);
					// ��ˮӡ
					dataBitmap = createBitmap(dataBitmap, currentPhotoType, cjh);
					// ͼƬ����ѹ��
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

	// ��ȡͼƬ
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
	 * ��ť�����¼�
	 */
	@Override
	public boolean onLongClick(View v) {
		try {
			isLongClicked = true;
			myCamera.autoFocus(this);// �Զ��Խ�

		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * ��ť����¼�
	 */
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			// ����
			case R.id.takePhotoImageView:
				DeleteOldFile(filePath + "��������Ա��������_����.jpg");
				takePhotoImageView.setEnabled(false);
				myCamera.takePicture(null, null, jpeg);
				break;
			// ȡ��
			case R.id.cancelPhotoImageView:
				photoImageView.setVisibility(View.GONE);
				String fileFullPath = filePath + currentPhotoType + suffix + ".jpg";
				File file = new File(fileFullPath);
				if (file.exists() && file.canWrite()) {
					file.delete();
				}
				myCamera.startPreview();// ����Ԥ��
				takePhotoImageView.setEnabled(true);
				flashImageView.setVisibility(View.VISIBLE);
				takePhotoImageView.setVisibility(View.VISIBLE);
				cancelPhotoImageView.setVisibility(View.INVISIBLE);
				savePhotoImageView.setVisibility(View.INVISIBLE);
				currentPhotoTextView.setEnabled(true);
				// �ĺ�����Ƭ
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
			// ����
			case R.id.savePhotoImageView:
				if (savephoto()) {
					final String dq_zpzl = MutualConversionNameID.getZpzlID(photoTypes, photoID, currentPhotoType);
					ArrayList<HashMap<String, String>> zpsj = SQLFuntion.query(CameraInterfaceTwo.this, jylsh, dq_zpzl);
					if (zpsj.size() == 0) {
						Object[] zpdata = { jylsh, TimeTool.getTiemData(), wjybh, dq_zpzl, currentPhotoType, "01" };// 01:��ʼ��״̬��02�������ϴ�״̬��03�����ǳɹ�״̬
						SQLFuntion.insert(CameraInterfaceTwo.this, zpdata);
					} else {
						Object[] data = { TimeTool.getTiemData(), jylsh, dq_zpzl };
						SQLFuntion.updatetwo(CameraInterfaceTwo.this, data);
					}
					if (!photoValue) {
						// ��̨�ϴ�
						Object[] data = { "02", jylsh, dq_zpzl };
						SQLFuntion.update(CameraInterfaceTwo.this, data);
						CameraInterfaceTwo.this.finish();
					} else {
						// ʵʱ��
						String photoPath = filePath + currentPhotoType + ".jpg";
						File filetwo = new File(photoPath);
						if (filetwo.exists()) {
							uploadPhotos();
						} else {
							MessageTool.setmessage(CAMERA_DIALOG_FAILURE, "��Ƭ��" + currentPhotoType + "������", handler);
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
						myCamera.autoFocus(this);// �Զ��Խ�
					}
				} catch (Exception e) {

				}
				break;
			// �����
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
			// ����ͷ�л�
			case R.id.cameraSwitchImageView:
				// �л�ǰ������ͷ
				int cameraCount = 0;
				CameraInfo cameraInfo = new CameraInfo();
				cameraCount = Camera.getNumberOfCameras();// �õ�����ͷ�ĸ���

				for (int i = 0; i < cameraCount; i++) {
					Camera.getCameraInfo(i, cameraInfo);// �õ�ÿһ������ͷ����Ϣ
					if (cameraPosition == 1) {
						// �����Ǻ��ã����Ϊǰ��
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							try {
								myCamera.stopPreview();// ͣ��ԭ������ͷ��Ԥ��
								myCamera.release();// �ͷ���Դ
								myCamera = null;// ȡ��ԭ������ͷ
								myCamera = Camera.open(i);// �򿪵�ǰѡ�е�����ͷ
								initCamera();
								myCamera.setPreviewDisplay(holder);// ͨ��surfaceview��ʾȡ������
								myCamera.startPreview();// ��ʼԤ��
								cameraPosition = 0;
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								Toast.makeText(CameraInterfaceTwo.this, e.toString(), Toast.LENGTH_LONG).show();
							}
							break;
						}
					} else {
						// ������ǰ�ã� ���Ϊ����
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
	 * �ϴ���Ƭ
	 */
	public void uploadPhotos() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					handler.sendEmptyMessage(CAMERA_DIALOG_SHOW);
					// ��01J63�ӿ�
					/*String data01J63 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "01J63",
							get01J63Data(jgbh, jylsh, MutualConversionNameID.getZpzlID(photoTypes, photoID, currentPhotoType)),
							StaticValues.writeResult, StaticValues.timeoutFifteen, StaticValues.numberThree);*/
					//����
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
					MessageTool.setmessage(CAMERA_DIALOG_FAILURE, "��������ʱ����鿴���磡", handler);
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ȡ�����ύ01J63����
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
	 * ���Ϊ����ͼƬ
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
				Toast.makeText(CameraInterfaceTwo.this, temp + "����ʹ���У�", Toast.LENGTH_LONG).show();
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
	 * ׷���ļ���ʹ��FileOutputStream���ڹ���FileOutputStreamʱ���ѵڶ���������Ϊtrue
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
		// �������
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
		// �ر�Ԥ�����ͷ���Դ
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

	// ��ByteתΪBimap
	public Bitmap BytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	// ��BimapתΪByte
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
		Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // ����һ���յ�BItMap
		Canvas canvas = new Canvas(icon);// ��ʼ���������Ƶ�ͼ��icon��

		Paint photoPaint = new Paint(); // ��������
		photoPaint.setDither(true); // ��ȡ��������ͼ�����
		photoPaint.setFilterBitmap(true);// ����һЩ

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// ����һ��ָ�����¾��ε�����
		Rect dst = new Rect(0, 0, width, hight);// ����һ��ָ�����¾��ε�����
		canvas.drawBitmap(photo, src, dst, photoPaint);// ��photo ���Ż�������
														// dstʹ�õ������photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// ���û���
		textPaint.setTextSize(25.0f);// �����С
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// ����Ĭ�ϵĿ��
		textPaint.setColor(Color.RED);// ���õ���ɫ
		canvas.drawText(newcjh, 30, 45, textPaint);// ������ȥ�֣���ʼδ֪x,y������ֻ�ʻ���
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}

	// ��ȡ�ַ����Ŀ��
	public float gettextwidth(String text, float Size) {
		TextPaint FontPaint = new TextPaint();
		FontPaint.setTextSize(Size);
		float textwidth = FontPaint.measureText(text);
		return textwidth;
	}

	/**
	 * ͼƬ�ϳ�
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
		Rect firstSrc = new Rect(0, 0, firstBitmap.getWidth(), firstBitmap.getHeight());// Ҫ���Ƶ�bitmap
																						// ����
		Rect firstDst = new Rect(0, 0, firstBitmap.getWidth(), firstBitmap.getHeight());// Ҫ��bitmap
																						// ��������Ļ��ʲô�ط�
		canvas.drawBitmap(firstBitmap, firstSrc, firstDst, photoPaint);
		Rect secondSrc = new Rect(0, 0, secondBitmap.getWidth(), secondBitmap.getHeight());
		Rect secondDrc = new Rect(firstBitmap.getWidth() / 5 * 4, 50, firstBitmap.getWidth(), (firstBitmap.getHeight() / 5) + 50);
		canvas.drawBitmap(secondBitmap, secondSrc, secondDrc, photoPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}

	// �����ؼ�ʱ����ʾ������
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

	// Activity�������ߴӱ����ǡ���̨���»ص�ǰ̨ʱ������
	@Override
	protected void onResume() {
		super.onResume();
		// ����Ϊ����
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	// Activity�����ǵ������������ʱ������
	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
		// �п�����ִ����onPause��onStop��,ϵͳ��Դ���Ž�Activityɱ��,�����б�Ҫ�ڴ˱���־�����

	}

	// 1��������Activity��android:configChangesʱ�����������µ��ø����������ڣ��к���ʱ��ִ��һ�Σ�������ʱ��ִ������
	// 2������Activity��android:configChanges="orientation"ʱ���������ǻ����µ��ø����������ڣ��кᡢ����ʱֻ��ִ��һ��
	// 3������Activity��android:configChanges="orientation|keyboardHidden"ʱ�������������µ��ø����������ڣ�ֻ��ִ��onConfigurationChanged����

}