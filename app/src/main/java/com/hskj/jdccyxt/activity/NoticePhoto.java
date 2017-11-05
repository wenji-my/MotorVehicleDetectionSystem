package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C04Domain;
import com.hskj.jdccyxt.domain.C05DomainBH;
import com.hskj.jdccyxt.domain.C06DomainZPSM;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.PhotoTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

/**
 * @类名：NoticePhoto
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆公告照片
 * @创建日期：2016.05.27
 */
public class NoticePhoto extends Activity {
	private Button query_announcement_photo;
	private TextView text_notice;
	private LinearLayout photo_imageview;
	private ProgressDialog progressDialog = null;
	private String data01C05;
	private List<C04Domain> ggzplist;
	private String ip, jgbh, jkxlh;
	private String bh;
	private int width, height;
	private List<C06DomainZPSM> zpsmslists;
	private String rootDirectory = StaticValues.rootDirectory;
	private int NOTICEPHOTO_DIALOG_SHOW = StaticConst.NOTICEPHOTO_DIALOG_SHOW;
	private int NOTICEPHOTO_DIALOG_DISMISS = StaticConst.NOTICEPHOTO_DIALOG_DISMISS;
	private int NOTICEPHOTO_DIALOG_FAILURE = StaticConst.NOTICEPHOTO_DIALOG_FAILURE;
	private int NOTICEPHOTO_PHOTO_SHOW = StaticConst.NOTICEPHOTO_PHOTO_SHOW;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == NOTICEPHOTO_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在请求中，请稍等。。。", getParent());
				progressDialog.show();
			}
			if (msg.what == NOTICEPHOTO_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == NOTICEPHOTO_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), getParent());
			}
			if (msg.what == NOTICEPHOTO_PHOTO_SHOW) {
				addDynamicPicture();
			}
		};
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_photo);
		setInitialization();
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 上一级传入的数据
		data01C05 = getIntent().getStringExtra("C05DATA");
		WindowManager manager = getWindowManager();
		width = manager.getDefaultDisplay().getWidth();
		height = manager.getDefaultDisplay().getHeight();
		String c06data = DocumentTool.readFileContent(rootDirectory + "/CllxEndZpsm/01C06.txt");
		zpsmslists = XMLParsingMethods.saxPhotoType(c06data);
		List<CodeDomain> codelists01C05 = XMLParsingMethods.saxcode(data01C05);
		if (codelists01C05.get(0).getCode().equals("1")) {
			List<C05DomainBH> bhslists = XMLParsingMethods.saxBH(data01C05);
			bh = bhslists.get(0).getBh();
			Log.i("AAA", bh);
			if (!bh.equals("0") && !bh.equals("")) {
				query_announcement_photo.setVisibility(View.VISIBLE);
			} else {
				text_notice.setText("无公告照片");
				text_notice.setVisibility(View.VISIBLE);
			}
		} else {
			text_notice.setText(codelists01C05.get(0).getMessage());
			text_notice.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		query_announcement_photo = (Button) this.findViewById(R.id.query_announcement_photo);
		text_notice = (TextView) this.findViewById(R.id.text_notice);
		photo_imageview = (LinearLayout) this.findViewById(R.id.photo_imageview);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.query_announcement_photo:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(NOTICEPHOTO_DIALOG_SHOW);
						// 调01C04接口
						/*String data01C04 = ConnectMethods.connectWebServicetwo(ip, StaticValues.queryObject, jkxlh, "01C04",
								get01C04Data(bh), StaticValues.queryResult, StaticValues.timeoutNine, StaticValues.numberThree);
						Log.i("AAA", "data01C04:" + data01C04);*/
						//离线数据
						Thread.sleep(500);
						String data01C04 = DocumentTool.readFileContent(rootDirectory + "/CllxEndZpsm/01C04.txt");
						List<CodeDomain> codelists01C04 = XMLParsingMethods.saxcode(data01C04);
						if (codelists01C04.get(0).getCode().equals("1")) {
							List<C04Domain> ggzplists = XMLParsingMethods.saxNoticePhoto(data01C04);
							ggzplist = ggzplists;
							handler.sendEmptyMessage(NOTICEPHOTO_PHOTO_SHOW);
						} else {
							MessageTool.setmessage(NOTICEPHOTO_DIALOG_FAILURE, codelists01C04.get(0).getMessage(), handler);
						}
						handler.sendEmptyMessage(NOTICEPHOTO_DIALOG_DISMISS);
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(NOTICEPHOTO_DIALOG_DISMISS);
						MessageTool.setmessage(NOTICEPHOTO_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
					}
				}
			}).start();
			break;
		}
	}

	/**
	 * 获取即将提交01C04数据
	 */
	private String get01C04Data(String bhData) {
		List<String> list = new ArrayList<String>();
		list.add(bhData);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C04, list);
		Log.i("AAA", "01C04xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 动态添加照片
	 */
	private void addDynamicPicture() {
		if (ggzplist.size() != 0) {
			int total = ggzplist.size();// 图片名字长度
			int singleLineCount = 1;// 列的个数
			int lineCount = ggzplist.size();// 行的个数
			int currentIndex = 0;
			for (int i = 0; i < lineCount; i++) {
				LinearLayout linearLayout = CreateLinearLayout();
				for (int j = 0; j < singleLineCount; j++) {
					RelativeLayout relativeLayout = CreateRelativeLayout();
					ImageView imageView;
					// TextView textView1;
					if (currentIndex < total) {
						String zpName = MutualConversionNameID.getZpzlName(zpsmslists, ggzplist.get(currentIndex).getZpzl());
						imageView = CreateImageView(currentIndex + "");
						// textView1 = CreateTextView(zpName);
					} else {
						imageView = CreateImageView("");
						// textView1 = CreateTextView("");
					}
					relativeLayout.addView(imageView);
					// relativeLayout.addView(textView1);
					linearLayout.addView(relativeLayout);
					currentIndex++;
				}
				photo_imageview.addView(linearLayout);
			}
			SetImageViewImg();
		}
	}

	/**
	 * 配置照片
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
						ImageView currentView = (ImageView) view3;
						currentView.setDrawingCacheEnabled(true);
						Bitmap bitmap = currentView.getDrawingCache();
						if (bitmap != null) {
							currentView.setImageBitmap(null);
							bitmap.recycle();
							currentView.setDrawingCacheEnabled(false);
						}
						// 然后再显示新的图片
						currentView.setImageBitmap(GetImageBitmap(view3.getTag().toString()));
					}
				}
			}
		}
	}

	private Bitmap GetImageBitmap(String zpAddress) {
		String zpdata = ggzplist.get(Integer.parseInt(zpAddress)).getZp();
		Bitmap zpBitmap = PhotoTool.stringtoBitmap(zpdata);
		return zpBitmap;
	}

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
		imageView.getLayoutParams().height = width / 2;
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
}
