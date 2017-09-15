package canvas.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HandTouchView extends View {
	public Handler mbitmaphHandler;
	public Handler getMbitmaphHandler() {
		return mbitmaphHandler;
	}
	public void setMbitmaphHandler(Handler mbitmaphHandler) {
		this.mbitmaphHandler = mbitmaphHandler;
	}
	
	private DisplayMetrics dm;
	private Bitmap bitmap=null;
	private Bitmap mBitmap=null;
	private Bitmap myBitmap ;
	private Paint mPaint=null;
	private Canvas mCanvas=null;
	private Paint mBitmapPaint=null;
	private Timer timer=null;
	private savePath sPath;
	private List<savePath> lists =null;
	private FingerMatrix fingerMatrix=null;
	private float Xi,Yi;
	private Path path=null;
	public HandTouchView(Context context) {
		super(context);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		inter(dm.widthPixels, dm.heightPixels);
		
	}

	public HandTouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		inter(dm.widthPixels, dm.heightPixels);
		
		
	}

	public HandTouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		inter(dm.widthPixels, dm.heightPixels);
		
		
	}
	
	/**
	 * 初始化
	 * @param w
	 * @param h
	 */
	public void inter(int w,int h){
		mBitmap =Bitmap.createBitmap(w, h, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		path = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形状
		mPaint.setStrokeWidth(15);// 画笔宽度
		mPaint.setColor(FingerMatrix.colorValue);
		lists = new ArrayList<HandTouchView.savePath>();
		fingerMatrix = new FingerMatrix();
		timer = new Timer(true);				//定时器来截取字
	}
	
	public void minter(int w,int h){
		mBitmap =Bitmap.createBitmap(w, h, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		path = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形状
		mPaint.setStrokeWidth(15);// 画笔宽度
		mPaint.setColor(FingerMatrix.colorValue);
		lists = new ArrayList<HandTouchView.savePath>();
		fingerMatrix = new FingerMatrix();
		timer = new Timer(true);				
	}
	
	public void RefreshPaint(){
		path = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形 状
		mPaint.setStrokeWidth(15);// 画笔宽度
		mPaint.setColor(Color.RED);
	}
	
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		if (path!=null) {
			canvas.drawPath(path, mPaint);
		}
		super.onDraw(canvas);
	}
	
	
	/**
	 * 手指 移动事件
	 */
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:			//按下屏幕时
			mPaint.setColor(FingerMatrix.colorValue);
			float x = event.getX();
			float y = event.getY();
			if (task!=null) {			//如果等于空
				task.cancel();			//关闭定时器////将原任务从队列中移除 
				task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);		//发给handler
					}
				};
			}
			Log.i("线程", "触屏时的坐标"+x+"y坐标"+y);
			if (fingerMatrix==null) {
				fingerMatrix = new FingerMatrix();
				Log.i("线程", "<staRT>fingerMatrix等于空");
				fingerMatrix.init(x, y);
			}else {
				fingerMatrix.getx(x);
				Log.i("线程", "<等于空>fingerMatrix等于空");
				fingerMatrix.getY(y);
			}
			path = new Path();
			sPath =new savePath();
			sPath.paint = mPaint;		//保存路径
			sPath.path = path;
			path.moveTo(x, y);			//开始划线
			Xi = x;
			Yi = y;
			invalidate();			//刷新
			break;
		case MotionEvent.ACTION_MOVE:			//移动中
			float X1 = event.getX();
			float Y1 = event.getY();
			if (task!=null) {
				task.cancel();
				task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what=1;
						handler.sendMessage(message);
					}
				};
				
			}
			if (fingerMatrix!=null) {
				fingerMatrix.getx(X1);
				fingerMatrix.getY(Y1);
				Log.i("线程", "fingerMatrix不等于空");
			}
			float j = Math.abs(X1-Xi);
			float i = Math.abs(Yi-Y1);
			if (j>=5||i>=5) {
				path.quadTo(Xi, Yi, X1, Y1);
				Xi = X1;
				Yi = Y1;
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:			//手指松下离开屏幕时
			float My = event.getX();		//手指离开屏幕时的坐标
			float Mx = event.getY();		
			if (fingerMatrix!=null) {
				fingerMatrix.getx(Mx);
				Log.i("线程", "离开"+Mx);
				fingerMatrix.getY(My);
			}
			mCanvas.drawPath(path, mPaint);
			lists.add(sPath);
			invalidate();
			if (timer!=null) {
				if (task!=null) {
					task.cancel();
					task = new TimerTask() {
						public void run() {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					};
					timer.schedule(task, 2200, 2200);				//2200秒后发送消息给handler更新Activity
				}
			}else {
				timer = new Timer(true);
				timer.schedule(task, 2200, 2200);					//2200秒后发送消息给handler更新Activity
			}
			break;
		}
		return true;
	}
	
	
	/**
	 * 处理屏幕显示
	 */
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {			//handler告诉Activity处理数据
			case 1:						//CUT_BITMAP_SEND_TO_ACTIVITY//切割图片发送给Activity处理
				Log.i("线程", "handler收到");
				myBitmap = mBitmap;			//获取写好的图片
				if (fingerMatrix!=null) {			//获取绘制的区域坐标
					myBitmap = cutBitmap(myBitmap);					//切割Bitmap图片方法
				}
				fingerMatrix=null;		
				saveBiamtImate(myBitmap);
				Message message = new Message();
				message.what=1;
				Bundle bundle = new Bundle();;
				bundle.putParcelable("bitmap",myBitmap);
				message.setData(bundle);
				mbitmaphHandler.sendMessage(message);
				RefreshCanvas();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	/**
	 * 发送消息给handler更新ACTIVITY		
	 */
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what=1;
			Log.i("线程", "来了");
			handler.sendMessage(message);
		}
	};
	
	/**
	 * 保存路径类
	 * @author Administrator
	 */
	class savePath{
		Paint paint;
		Path path;
	}
	
	/**
	 * 切割图片方法和更新Activity
	 * @param bnBitmap
	 */
	public Bitmap cutBitmap(Bitmap bnBitmap){
		//最小
		float minx = fingerMatrix.getMinX();		//最小的x坐标
		float miny = fingerMatrix.getMinY();		//最小的y坐标
		//最大
		float maxy = fingerMatrix.getMaxY();		//最大的Y坐标
		float maxX = fingerMatrix.getMaxX();		//最大的x坐标
		Log.i("","矩阵的坐标信息为：-======最大X:"+maxX+"----====最大Y:"+maxy+"----====最小X:"+minx+"----====最小Y:"+miny); 
		int cutMinX = (int)(minx-15);		//切割的最小值
		int cutMinY = (int)(miny-15);		//切割的最小值
		int cutMaxX = (int)(maxX+15);
		int cutMaxY = (int)(maxy+15);
		if (cutMinX<=0) {
			cutMinX=0;
		}
		if (cutMinY<=0) {
			cutMinY=0;
		}
		if (cutMaxX>mBitmap.getWidth()) {			//如果X坐标大于图片的宽度
			cutMaxX =  mBitmap.getWidth()-1;		//那就把图片的宽度付给X坐标 的最大值
		}
		if (cutMaxY>mBitmap.getHeight()) {			//如果Y坐标大于图片的宽度
			cutMaxY = mBitmap.getHeight()-1;		//那就把图片的宽度付给Y坐标 的最大值
		}
		
		int width =(int)(cutMaxX - cutMinX);		//最大的x坐标减去最小的x坐标
		int height =(int)(cutMaxY-cutMinY);			//最大的Y坐标减去最小的y坐标
		
		Bitmap wBitmap =Bitmap.createBitmap(bnBitmap, cutMinX, cutMinY, width, height);		//这个步骤意思是在什么坐标位置创建一张图片
		if (myBitmap!=null ) {
			myBitmap.recycle();
			myBitmap= null;
		}
		Log.i("线程", "剪切图片成功");
		return wBitmap;
	}
	
	/**
	 * 保存图片
	 * @param bitmap1
	 */
	public void saveBiamtImate(Bitmap bitmap1){
		String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/save.png";
			try {
				FileOutputStream fos = new FileOutputStream(new File(fileUrl));
				bitmap1.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 刷新画布
	 */
	public void RefreshCanvas(){
		Log.i("线程", "初始化数据并且 刷新画布");
		if (lists!=null&&lists.size()>0) {
			lists.remove(lists);
			if (mBitmap!=null) {
				mBitmap=null;
			}
			path=null;
//			inter(dm.widthPixels,dm.heightPixels);
			inter(dm.widthPixels,dm.heightPixels);
			
			Log.i("线程", "初始化数据成功");
			invalidate();
		}
		if (task!=null) {
			task.cancel();
		}
	}
}
