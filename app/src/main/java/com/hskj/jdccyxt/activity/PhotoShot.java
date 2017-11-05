package com.hskj.jdccyxt.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C06DomainZPSM;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.CommonData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @������CosmeticInspect
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ����������Ƭ����
 * @�������ڣ�2016.04.19
 */
public class PhotoShot extends Activity {
	private LinearLayout shot_add_photo, photo_imageview;
	private TextView querition_title;
	private Spinner add_photo;
	private Button shot_submit_audit;
	private ProgressDialog progressDialog = null;
	private C09Domain operators;// ����ĵ�¼Ա��Ϣ
	private C02Domain vehiclelist;// ��01C02�ӿڻ��������
	private String ip, jgbh, jkxlh;
	private String[] zpzlID = null;
	private String[] zpName = null;
	private String[] uploadData = null;
	private List<C06DomainZPSM> zpsmslists;
	private String rootDirectory = StaticValues.rootDirectory;
	private String photoSubcatalog = StaticValues.photoSubcatalog;
	private int singleLineCount = 2;// �еĸ���
	private int lineCount = 0;// �еĸ���
	private int width, height;
	private String upload;// ͼƬ��Ŀ¼
	private String state;
	private SharedPreferences deletepreferences;
	private int SHOT_DIALOG_SHOW = StaticConst.PHOTOSHOT_SHOT_DIALOG_SHOW;
	private int SHOT_DIALOG_DISMISS = StaticConst.PHOTOSHOT_SHOT_DIALOG_DISMISS;
	private int SHOT_DIALOG_FAILURE = StaticConst.PHOTOSHOT_SHOT_DIALOG_FAILURE;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOT_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("���������У����Եȡ�����", PhotoShot.this);
				progressDialog.show();
			}
			if (msg.what == SHOT_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == SHOT_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), PhotoShot.this);
			}
		};
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_shot);
		setInitialization();
		WindowManager manager = getWindowManager();
		width = manager.getDefaultDisplay().getWidth();
		height = manager.getDefaultDisplay().getHeight();
		// ��ȡ�����е�����
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// ��һ�����������
		Bundle bundle = this.getIntent().getExtras();
		operators = (C09Domain) bundle.getSerializable("operatorObj");
		vehiclelist = (C02Domain) bundle.getSerializable("vehicleQueryObj");
		state = getIntent().getStringExtra("STATE");
		String c06data = DocumentTool.readFileContent(rootDirectory + "/CllxEndZpsm/01C06.txt");
		zpsmslists = XMLParsingMethods.saxPhotoType(c06data);
		querition_title.setText(vehiclelist.getVin());
		// ��Ƭ��ӿؼ�
		setAddPhotoSpinner();
		upload = vehiclelist.getJclsh();
		if (state.equals("normal") || state.equals("normaltwo")) {
			if (state.equals("normal")) {
				shot_add_photo.setVisibility(View.VISIBLE);
			}
			if (state.equals("normaltwo")) {
				// ���Ľ������
				shot_submit_audit.setVisibility(View.VISIBLE);
				isSQLPhotoData(vehiclelist);
			}
			zpzlID = RemoveNullValue(vehiclelist.getZpzl()).split(",");
			getZPData(zpzlID);
		} else {
			zpzlID = new String[1];
			zpzlID[0] = "000";
			zpName = new String[1];
			zpName[0] = "��ʻ֤��Ƭ����";
		}
		setUploadData(zpzlID);
		if (!vehiclelist.getZpzl().equals("")) {
			addDynamicPicture(zpName, uploadData);
		} else {
			querition_title.setText("�ó���δ���ò�����Ƭ��");
		}
	}

	/**
	 * �����Ƭ��ť����
	 */
	private void setAddPhotoSpinner() {
		String[] addPhotoData = new String[zpsmslists.size()];
		for (int i = 0; i < zpsmslists.size(); i++) {
			addPhotoData[i] = zpsmslists.get(i).getZpzl() + "," + zpsmslists.get(i).getZpmc();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addPhotoData);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		add_photo.setAdapter(adapter);
	}

	/**
	 * �ж��Ƿ�ɾ����Ƭ����
	 */
	private void isSQLPhotoData(C02Domain vehicleData) {
		deletepreferences = getSharedPreferences("is_delete", 2);
		String jylshStr = deletepreferences.getString("DELETEJYLSH", "");
		boolean isDelete = deletepreferences.getBoolean("ISDELETE", true);
		if (jylshStr.equals(vehicleData.getJclsh())) {
			if (isDelete) {
				DeleteSQLPhotoData(vehicleData);
			}
		} else {
			DeleteSQLPhotoData(vehicleData);
		}
	}

	/**
	 * ɾ��ָ����Ƭ����
	 */
	private void DeleteSQLPhotoData(C02Domain newvehicleData) {
		String[] zpzllistData = newvehicleData.getZpzl().split(",");
		for (int i = 0; i < zpzllistData.length; i++) {
			Object[] data = { newvehicleData.getJclsh(), zpzllistData[i] };
			SQLFuntion.deletePhotoData(PhotoShot.this, data);
		}
		Editor editor = deletepreferences.edit();
		editor.putString("DELETEJYLSH", newvehicleData.getJclsh());
		editor.putBoolean("ISDELETE", false);
		editor.commit();
	}

	/**
	 * ȥ����ƬID��Ŀ�ֵ
	 * 
	 * @param zpzlData
	 * @return
	 */
	private String RemoveNullValue(String zpzlData) {
		String[] zpzlIDData = zpzlData.split(",");
		String zpData = "";
		for (int i = 0; i < zpzlIDData.length; i++) {
			if (zpzlIDData[i] != null) {
				if (zpData.equals("")) {
					zpData = zpzlIDData[i];
				} else {
					zpData = zpData + "," + zpzlIDData[i];
				}
			}
		}
		return zpData;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		querition_title = (TextView) this.findViewById(R.id.querition_title);
		shot_submit_audit = (Button) this.findViewById(R.id.shot_submit_audit);
		shot_add_photo = (LinearLayout) this.findViewById(R.id.shot_add_photo);
		add_photo = (Spinner) this.findViewById(R.id.add_photo);
		photo_imageview = (LinearLayout) this.findViewById(R.id.photo_imageview);
	}

	/**
	 * ����¼�
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.shot_back:
			PhotoShot.this.finish();
			break;
		// �ύ���
		case R.id.shot_submit_audit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(SHOT_DIALOG_SHOW);
						// ��01J03�ӿ�
						String data01J03 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "01J03",
								get01J03Data(jgbh, vehiclelist.getJclsh()), StaticValues.writeResult, StaticValues.timeoutFive,
								StaticValues.numberFour);
						Log.i("AAA", "data01J03:" + data01J03);
						List<CodeDomain> codelists01J03 = XMLParsingMethods.saxcode(data01J03);
						handler.sendEmptyMessage(SHOT_DIALOG_DISMISS);
						MessageTool.setmessage(SHOT_DIALOG_FAILURE, codelists01J03.get(0).getMessage(), handler);
						if (codelists01J03.get(0).getCode().equals("1")) {
							Editor editor = deletepreferences.edit();
							editor.putBoolean("ISDELETE", true);
							editor.commit();
						}
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(SHOT_DIALOG_DISMISS);
						MessageTool.setmessage(SHOT_DIALOG_FAILURE, "��������ʱ����鿴���磡", handler);
					}
				}
			}).start();
			break;
		// ˢ��
		case R.id.text_shot_refresh:
			try {
				// ���¼���TextView
				getUploadData(zpzlID, vehiclelist.getJclsh());
				SetTextViewData(uploadData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// �����Ƭ
		case R.id.btu_add_photo:
			String addPhotoData = add_photo.getSelectedItem().toString().trim();
			if (isHavePhoto(addPhotoData)) {
				String newzpIdData[] = zpzlID;
				zpzlID = new String[newzpIdData.length + 1];
				for (int i = 0; i < (newzpIdData.length + 1); i++) {
					if (i != newzpIdData.length) {
						zpzlID[i] = newzpIdData[i];
					} else {
						zpzlID[i] = addPhotoData.split(",")[0];
					}
				}
				getZPData(zpzlID);
				setUploadData(zpzlID);
				getUploadData(zpzlID, vehiclelist.getJclsh());
				RecyleImg();
				photo_imageview.removeAllViews();
				addDynamicPicture(zpName, uploadData);
			} else {
				MessageTool.setmessage(SHOT_DIALOG_FAILURE, addPhotoData.split(",")[1] + "�Ѵ��ڣ�������ӣ�", handler);
			}
			break;
		}
	}

	/**
	 * �жϸ���Ƭ�Ƿ��Ѿ�����
	 * 
	 * @param addPhotoData
	 */
	private boolean isHavePhoto(String addPhotoData) {
		for (int i = 0; i < zpzlID.length; i++) {
			if (zpzlID[i].equals(addPhotoData.split(",")[0])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ȡ�����ύ01J03����
	 */
	private String get01J03Data(String jyjgbhData, String lshData) {
		List<String> list = new ArrayList<String>();
		list.add(jyjgbhData);
		list.add(lshData);
		String xmlData = CombinationXMLData.getWriteData(StaticValues.FieldName01J03, list);
		Log.i("AAA", "get01J03Data:" + xmlData);
		return xmlData;
	}

	/**
	 * ��ȡת�������Ƭ������
	 */
	private String[] getZPData(String[] newzpid) {
		zpName = new String[newzpid.length];
		for (int i = 0; i < newzpid.length; i++) {
			zpName[i] = MutualConversionNameID.getZpzlName(zpsmslists, newzpid[i]);
		}
		return null;
	}

	/**
	 * ��ʼ��uploadData����
	 */
	private void setUploadData(String[] newzpid) {
		uploadData = new String[newzpid.length];
		for (int i = 0; i < newzpid.length; i++) {
			uploadData[i] = "";
		}
	}

	/**
	 * �ж��Ƿ����ϴ�
	 */
	private void getUploadData(String[] newzpid, String cljylsh) {
		for (int i = 0; i < newzpid.length; i++) {
			ArrayList<HashMap<String, String>> list = SQLFuntion.query(PhotoShot.this, cljylsh, newzpid[i]);
			if (list.size() == 0) {
				uploadData[i] = "";
			} else {
				if (list.get(0).get("isupload").equals("03")) {
					uploadData[i] = "\n\n���ϴ�";
				} else if (list.get(0).get("isupload").equals("02")) {
					uploadData[i] = "\n\n�ϴ���...";
				} else {
					uploadData[i] = "";
				}
				Log.i("AAA",
						newzpid[i] + MutualConversionNameID.getZpzlName(zpsmslists, newzpid[i]) + ":"
								+ list.get(0).get("isupload"));
			}
		}
	}

	/**
	 * ��̬�����Ƭ
	 */
	private void addDynamicPicture(String[] zpData, String[] isData) {
		if (zpData != null) {
			int total = zpData.length;// ͼƬ���ֳ���
			for (int j = 1; j <= (total + 1 / 2); j++) {
				lineCount = (j + 1) / 2;
			}
			int currentIndex = 0;
			for (int i = 0; i < lineCount; i++) {
				LinearLayout linearLayout = CreateLinearLayout();
				for (int j = 0; j < singleLineCount; j++) {
					RelativeLayout relativeLayout = CreateRelativeLayout();
					ImageView imageView;
					TextView textView1;
					TextView textView2;
					if (currentIndex < total) {
						imageView = CreateImageView(zpData[currentIndex]);
						textView1 = CreateTextView(zpData[currentIndex]);
						textView2 = CreateTextViewTwo(isData[currentIndex]);
					} else {
						imageView = CreateImageView("");
						textView1 = CreateTextView("");
						textView2 = CreateTextViewTwo("");
					}
					if (imageView.getTag().toString() != "") {
						imageView.setOnClickListener(clickListener);
						imageView.setOnLongClickListener(longClickListener);

					}
					relativeLayout.addView(imageView);
					relativeLayout.addView(textView1);
					relativeLayout.addView(textView2);
					linearLayout.addView(relativeLayout);
					currentIndex++;
				}
				photo_imageview.addView(linearLayout);
			}
			SetImageViewImg();
		}
	}

	/**
	 * ����¼�
	 */
	public OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!v.getTag().toString().equals("")) {
				String photoSubcatalogPath = photoSubcatalog + upload + "/";
				String photoFilePath = photoSubcatalogPath + v.getTag().toString() + ".jpg";
				if (CommonData.GetUsingFileName().equals(photoFilePath)) {
					Toast.makeText(PhotoShot.this, "�ļ�����ʹ���У����Ժ����ԣ�", Toast.LENGTH_LONG).show();
				} else {
					Intent intent2 = new Intent();
					if (v.getTag().toString().equals("��������Ա��������")) {
						intent2.setClass(PhotoShot.this, CameraInterfaceTwo.class);
					} else {
						intent2.setClass(PhotoShot.this, CameraInterface.class);
					}
					intent2.putExtra("PHOTOPATH", photoSubcatalogPath);// ͼƬ��Ŀ¼
					intent2.putExtra("PHOTOTYPES", zpName);// ͼƬ���Ƽ�
					intent2.putExtra("PHOTOIDS", zpzlID);// ͼƬid��
					intent2.putExtra("JYLSH", vehiclelist.getJclsh());// ����������ˮ��
					intent2.putExtra("CURRENTNAME", v.getTag().toString());
					intent2.putExtra("CJH", vehiclelist.getVin());
					intent2.putExtra("WJYBH", operators.getUsername());
					RecyleImg();
					startActivity(intent2);
				}
			}
		}
	};

	/**
	 * �����¼�
	 */
	public OnLongClickListener longClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			Intent intent1 = new Intent(PhotoShot.this, ImageShow.class);
			String photoSubcatalogPath = photoSubcatalog + upload + "/";
			String photoFilePath = photoSubcatalogPath + v.getTag().toString() + ".jpg";
			File file = new File(photoFilePath);
			if (file.exists()) {
				intent1.putExtra("plateID", upload);
				intent1.putExtra("photoType", v.getTag().toString());
				intent1.putExtra("imageFullPath", photoFilePath);
				intent1.putExtra("code", v.getTag().toString() + "_" + CommonData.GetImei());
				startActivity(intent1);
			}
			return true;
		}
	};

	/**
	 * ����LinearLayout
	 */
	@SuppressWarnings("deprecation")
	private LinearLayout CreateLinearLayout() {
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 2));
		return linearLayout;
	}

	/**
	 * ����RelativeLayout
	 */
	@SuppressWarnings("deprecation")
	private RelativeLayout CreateRelativeLayout() {
		RelativeLayout relativeLayout = new RelativeLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 2);
		lp.setMargins(2, 2, 2, 2);
		relativeLayout.setBackgroundColor(Color.BLACK);
		relativeLayout.setLayoutParams(lp);
		relativeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		return relativeLayout;
	}

	/**
	 * ����ImageView
	 */
	@SuppressWarnings("deprecation")
	private ImageView CreateImageView(String picName) {
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 2));
		imageView.setTag(picName);
		if (picName != "")
			imageView.setBackgroundColor(Color.BLACK);
		imageView.getLayoutParams().height = width / 3;
		return imageView;
	}

	/**
	 * ����TextView
	 */
	@SuppressWarnings("deprecation")
	private TextView CreateTextView(String text) {
		TextView textView = new TextView(this);
		textView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextSize(20);
		textView.setTextColor(Color.WHITE);
		textView.setText(text);
		textView.setTag("1");
		return textView;
	}

	@SuppressWarnings("deprecation")
	private TextView CreateTextViewTwo(String text) {
		TextView textView = new TextView(this);
		textView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextSize(20);
		textView.setTextColor(Color.RED);
		textView.setText(text);
		textView.setTag("2");
		return textView;
	}

	/**
	 * ����ImageView����
	 */
	private void SetImageViewImg() {
		for (int i = 0; i < photo_imageview.getChildCount(); i++) {
			View view = photo_imageview.getChildAt(i);// ��ȡLinearLayout�ؼ�

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// ��ȡRelativeLayout�ؼ�

				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("ImageView");
					if (flag) {
						// ��λ����ImageView�ؼ�
						if (view3.getTag().toString().equals(""))
							return;
						String currentImagePath = photoSubcatalog + upload + "/" + view3.getTag().toString() + ".jpg";
						ImageView currentView = (ImageView) view3;
						currentView.setDrawingCacheEnabled(true);
						Bitmap bitmap = currentView.getDrawingCache();
						if (bitmap != null) {
							currentView.setImageBitmap(null);
							bitmap.recycle();
							currentView.setDrawingCacheEnabled(false);
						}
						// Ȼ������ʾ�µ�ͼƬ
						currentView.setImageBitmap(GetImage(currentImagePath));
					}
				}
			}
		}
	}

	/**
	 * ����TextView����
	 */
	private void SetTextViewData(String[] newUploadData) {
		int textCount = 0;
		for (int i = 0; i < photo_imageview.getChildCount(); i++) {
			View view = photo_imageview.getChildAt(i);// ��ȡLinearLayout�ؼ�

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// ��ȡRelativeLayout�ؼ�

				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("TextView");
					if (flag) {
						if (view3.getTag().toString().equals("2")) {
							TextView currentView = (TextView) view3;
							currentView.setText(newUploadData[textCount]);
							textCount++;
						}
					}
				}
			}
		}
	}

	public Bitmap GetImage(String currentImagePath) {
		try {
			InputStream input = new FileInputStream(currentImagePath);
			byte[] imgData = new byte[input.available()];
			input.read(imgData);
			input.close();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(currentImagePath, opts);
			BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 256 * 256);
			opts.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
			return bmp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double h = options.outWidth;
		double w = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	private void RecyleImg() {
		for (int i = 0; i < photo_imageview.getChildCount(); i++) {
			View view = photo_imageview.getChildAt(i);// LinearLayout

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// RelativeLayout
				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("ImageView");
					if (flag) {
						// ��λ����ImageView�ؼ�
						if (view3.getTag().toString().equals(""))
							return;
						ImageView currentView = (ImageView) view3;
						currentView.setDrawingCacheEnabled(true);
						Bitmap bitmap = currentView.getDrawingCache();
						if (bitmap != null) {
							currentView.setImageBitmap(null);
							bitmap.recycle();
							currentView.setDrawingCacheEnabled(false);
						}
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			CommonData.SetUsingFileName("");
			SetImageViewImg();
			// ���¼���TextView
			getUploadData(zpzlID, vehiclelist.getJclsh());
			SetTextViewData(uploadData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��д���ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogTool.setSelectDialog("�Ƿ��˳�������棿", PhotoShot.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
