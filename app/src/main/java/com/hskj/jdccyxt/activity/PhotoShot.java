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
 * @类名：CosmeticInspect
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆外观照片拍摄
 * @创建日期：2016.04.19
 */
public class PhotoShot extends Activity {
	private LinearLayout shot_add_photo, photo_imageview;
	private TextView querition_title;
	private Spinner add_photo;
	private Button shot_submit_audit;
	private ProgressDialog progressDialog = null;
	private C09Domain operators;// 传入的登录员信息
	private C02Domain vehiclelist;// 调01C02接口获得数据组
	private String ip, jgbh, jkxlh;
	private String[] zpzlID = null;
	private String[] zpName = null;
	private String[] uploadData = null;
	private List<C06DomainZPSM> zpsmslists;
	private String rootDirectory = StaticValues.rootDirectory;
	private String photoSubcatalog = StaticValues.photoSubcatalog;
	private int singleLineCount = 2;// 列的个数
	private int lineCount = 0;// 行的个数
	private int width, height;
	private String upload;// 图片子目录
	private String state;
	private SharedPreferences deletepreferences;
	private int SHOT_DIALOG_SHOW = StaticConst.PHOTOSHOT_SHOT_DIALOG_SHOW;
	private int SHOT_DIALOG_DISMISS = StaticConst.PHOTOSHOT_SHOT_DIALOG_DISMISS;
	private int SHOT_DIALOG_FAILURE = StaticConst.PHOTOSHOT_SHOT_DIALOG_FAILURE;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOT_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在请求中，请稍等。。。", PhotoShot.this);
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
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 上一级传入的数据
		Bundle bundle = this.getIntent().getExtras();
		operators = (C09Domain) bundle.getSerializable("operatorObj");
		vehiclelist = (C02Domain) bundle.getSerializable("vehicleQueryObj");
		state = getIntent().getStringExtra("STATE");
		String c06data = DocumentTool.readFileContent(rootDirectory + "/CllxEndZpsm/01C06.txt");
		zpsmslists = XMLParsingMethods.saxPhotoType(c06data);
		querition_title.setText(vehiclelist.getVin());
		// 照片添加控件
		setAddPhotoSpinner();
		upload = vehiclelist.getJclsh();
		if (state.equals("normal") || state.equals("normaltwo")) {
			if (state.equals("normal")) {
				shot_add_photo.setVisibility(View.VISIBLE);
			}
			if (state.equals("normaltwo")) {
				// 补拍界面操作
				shot_submit_audit.setVisibility(View.VISIBLE);
				isSQLPhotoData(vehiclelist);
			}
			zpzlID = RemoveNullValue(vehiclelist.getZpzl()).split(",");
			getZPData(zpzlID);
		} else {
			zpzlID = new String[1];
			zpzlID[0] = "000";
			zpName = new String[1];
			zpName[0] = "行驶证照片拍照";
		}
		setUploadData(zpzlID);
		if (!vehiclelist.getZpzl().equals("")) {
			addDynamicPicture(zpName, uploadData);
		} else {
			querition_title.setText("该车辆未配置查验照片！");
		}
	}

	/**
	 * 添加照片按钮辅助
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
	 * 判断是否删除照片数据
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
	 * 删除指定照片数据
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
	 * 去除照片ID里的空值
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
	 * 初始化控件
	 */
	private void setInitialization() {
		querition_title = (TextView) this.findViewById(R.id.querition_title);
		shot_submit_audit = (Button) this.findViewById(R.id.shot_submit_audit);
		shot_add_photo = (LinearLayout) this.findViewById(R.id.shot_add_photo);
		add_photo = (Spinner) this.findViewById(R.id.add_photo);
		photo_imageview = (LinearLayout) this.findViewById(R.id.photo_imageview);
	}

	/**
	 * 点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.shot_back:
			PhotoShot.this.finish();
			break;
		// 提交审核
		case R.id.shot_submit_audit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(SHOT_DIALOG_SHOW);
						// 调01J03接口
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
						MessageTool.setmessage(SHOT_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
					}
				}
			}).start();
			break;
		// 刷新
		case R.id.text_shot_refresh:
			try {
				// 重新加载TextView
				getUploadData(zpzlID, vehiclelist.getJclsh());
				SetTextViewData(uploadData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 添加照片
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
				MessageTool.setmessage(SHOT_DIALOG_FAILURE, addPhotoData.split(",")[1] + "已存在，无需添加！", handler);
			}
			break;
		}
	}

	/**
	 * 判断该照片是否已经存在
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
	 * 获取即将提交01J03数据
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
	 * 获取转换后的照片数据组
	 */
	private String[] getZPData(String[] newzpid) {
		zpName = new String[newzpid.length];
		for (int i = 0; i < newzpid.length; i++) {
			zpName[i] = MutualConversionNameID.getZpzlName(zpsmslists, newzpid[i]);
		}
		return null;
	}

	/**
	 * 初始化uploadData数组
	 */
	private void setUploadData(String[] newzpid) {
		uploadData = new String[newzpid.length];
		for (int i = 0; i < newzpid.length; i++) {
			uploadData[i] = "";
		}
	}

	/**
	 * 判断是否已上传
	 */
	private void getUploadData(String[] newzpid, String cljylsh) {
		for (int i = 0; i < newzpid.length; i++) {
			ArrayList<HashMap<String, String>> list = SQLFuntion.query(PhotoShot.this, cljylsh, newzpid[i]);
			if (list.size() == 0) {
				uploadData[i] = "";
			} else {
				if (list.get(0).get("isupload").equals("03")) {
					uploadData[i] = "\n\n已上传";
				} else if (list.get(0).get("isupload").equals("02")) {
					uploadData[i] = "\n\n上传中...";
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
	 * 动态添加照片
	 */
	private void addDynamicPicture(String[] zpData, String[] isData) {
		if (zpData != null) {
			int total = zpData.length;// 图片名字长度
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
	 * 点击事件
	 */
	public OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!v.getTag().toString().equals("")) {
				String photoSubcatalogPath = photoSubcatalog + upload + "/";
				String photoFilePath = photoSubcatalogPath + v.getTag().toString() + ".jpg";
				if (CommonData.GetUsingFileName().equals(photoFilePath)) {
					Toast.makeText(PhotoShot.this, "文件正常使用中，请稍后再试！", Toast.LENGTH_LONG).show();
				} else {
					Intent intent2 = new Intent();
					if (v.getTag().toString().equals("车辆查验员叠加拍照")) {
						intent2.setClass(PhotoShot.this, CameraInterfaceTwo.class);
					} else {
						intent2.setClass(PhotoShot.this, CameraInterface.class);
					}
					intent2.putExtra("PHOTOPATH", photoSubcatalogPath);// 图片子目录
					intent2.putExtra("PHOTOTYPES", zpName);// 图片名称集
					intent2.putExtra("PHOTOIDS", zpzlID);// 图片id集
					intent2.putExtra("JYLSH", vehiclelist.getJclsh());// 车辆检验流水号
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
	 * 长按事件
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
	 * 创建LinearLayout
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
	 * 创建RelativeLayout
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
	 * 创建ImageView
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
	 * 创建TextView
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
	 * 设置ImageView数据
	 */
	private void SetImageViewImg() {
		for (int i = 0; i < photo_imageview.getChildCount(); i++) {
			View view = photo_imageview.getChildAt(i);// 获取LinearLayout控件

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// 获取RelativeLayout控件

				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("ImageView");
					if (flag) {
						// 定位到了ImageView控件
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
						// 然后再显示新的图片
						currentView.setImageBitmap(GetImage(currentImagePath));
					}
				}
			}
		}
	}

	/**
	 * 设置TextView数据
	 */
	private void SetTextViewData(String[] newUploadData) {
		int textCount = 0;
		for (int i = 0; i < photo_imageview.getChildCount(); i++) {
			View view = photo_imageview.getChildAt(i);// 获取LinearLayout控件

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// 获取RelativeLayout控件

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
						// 定位到了ImageView控件
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
			// 重新加载TextView
			getUploadData(zpzlID, vehiclelist.getJclsh());
			SetTextViewData(uploadData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogTool.setSelectDialog("是否退出拍摄界面？", PhotoShot.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
