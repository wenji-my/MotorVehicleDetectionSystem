package com.hskj.jdccyxt.activity;

import java.io.File;
import com.hskj.jdccyxt.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageShow extends Activity implements OnClickListener {

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	Bitmap bmp;
	String localTempImgDir, plateID, photoType, code;

	@Override
	protected void onDestroy() {
		if (bmp != null)
			bmp.recycle();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_show);
		try {
			Intent intent = getIntent();
			localTempImgDir = intent.getStringExtra("imageFullPath");
			plateID = intent.getStringExtra("plateID");
			photoType = intent.getStringExtra("photoType");
			code = intent.getStringExtra("code");
			bmp = getLoacalBitmap(localTempImgDir);

			ImageView view = (ImageView) findViewById(R.id.imageViewShow);
			view.setImageBitmap(bmp);

			int screenWith, screenHeight;
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			screenWith = display.getWidth();
			screenHeight = display.getHeight();
			float scale = (float) screenWith / (float) bmp.getWidth();
			// Toast.makeText(ImageShowActivity.this,
			// screenWith + "  " +screenHeight+" "+bmp.getWidth()+ " "+
			// bmp.getHeight()+" "+(float) bmp.getHeight()*scale+" "+Math
			// .abs(((float) screenHeight - ((float) bmp.getHeight()*scale)) /
			// 2), Toast.LENGTH_LONG)
			// .show();

			matrix.postScale(scale, scale, 0, Math.abs(((float) screenHeight - (float) bmp.getHeight() * scale) / 2));
			view.setImageMatrix(matrix);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ImageView view = (ImageView) v;
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						savedMatrix.set(matrix);
						start.set(event.getX(), event.getY());
						mode = DRAG;
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDist = spacing(event);
						if (oldDist > 10f) {
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (mode == DRAG) {
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
							Log.i("AAA", "MIN");
						} else if (mode == ZOOM) {
							float newDist = spacing(event);
							if (newDist > 10f) {
								matrix.set(savedMatrix);
								float scale = newDist / oldDist;
								matrix.postScale(scale, scale, mid.x, mid.y);
								Log.i("AAA", "ADD");
							}
						}
						break;
					}
					view.setImageMatrix(matrix);
					return true;
				}

				private float spacing(MotionEvent event) {
					float x = event.getX(0) - event.getX(1);
					float y = event.getY(0) - event.getY(1);
					return FloatMath.sqrt(x * x + y * y);
				}

				private void midPoint(PointF point, MotionEvent event) {
					float x = event.getX(0) + event.getX(1);
					float y = event.getY(0) + event.getY(1);
					point.set(x / 2, y / 2);
				}
			});

			ImageView deleteImageView = (ImageView) findViewById(R.id.deleteImageView);
			deleteImageView.setOnClickListener(clickListener);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ImageShow.this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(ImageShow.this).setTitle("删除警告").setMessage("您确认要删除这张图片吗？")
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							File file = new File(localTempImgDir);
							if (file.exists() && file.canWrite()) {
								file.delete();
							}
							// DBVehPhotoHelper helper = new
							// DBVehPhotoHelper(ImageShowActivity.this);
							// ContentValues values = new
							// ContentValues();
							// values.put("isUploadOk", -1);
							// boolean ret =
							// helper.update(values,plateID, photoType);
							ImageShow.this.finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();

		}
	};

	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(this).setTitle("删除警告").setMessage("您确认要删除这张图片吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						File file = new File(localTempImgDir);
						if (file.exists() && file.canWrite()) {
							file.delete();
						}
						ImageShow.this.finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();

	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			Bitmap bmp = BitmapFactory.decodeFile(url);
			return bmp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
