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
	 * ��ʼ��
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
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// ��״
		mPaint.setStrokeWidth(15);// ���ʿ��
		mPaint.setColor(FingerMatrix.colorValue);
		lists = new ArrayList<HandTouchView.savePath>();
		fingerMatrix = new FingerMatrix();
		timer = new Timer(true);				//��ʱ������ȡ��
	}
	
	public void minter(int w,int h){
		mBitmap =Bitmap.createBitmap(w, h, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		path = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// ��״
		mPaint.setStrokeWidth(15);// ���ʿ��
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
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// �� ״
		mPaint.setStrokeWidth(15);// ���ʿ��
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
	 * ��ָ �ƶ��¼�
	 */
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:			//������Ļʱ
			mPaint.setColor(FingerMatrix.colorValue);
			float x = event.getX();
			float y = event.getY();
			if (task!=null) {			//������ڿ�
				task.cancel();			//�رն�ʱ��////��ԭ����Ӷ������Ƴ� 
				task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);		//����handler
					}
				};
			}
			Log.i("�߳�", "����ʱ������"+x+"y����"+y);
			if (fingerMatrix==null) {
				fingerMatrix = new FingerMatrix();
				Log.i("�߳�", "<staRT>fingerMatrix���ڿ�");
				fingerMatrix.init(x, y);
			}else {
				fingerMatrix.getx(x);
				Log.i("�߳�", "<���ڿ�>fingerMatrix���ڿ�");
				fingerMatrix.getY(y);
			}
			path = new Path();
			sPath =new savePath();
			sPath.paint = mPaint;		//����·��
			sPath.path = path;
			path.moveTo(x, y);			//��ʼ����
			Xi = x;
			Yi = y;
			invalidate();			//ˢ��
			break;
		case MotionEvent.ACTION_MOVE:			//�ƶ���
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
				Log.i("�߳�", "fingerMatrix�����ڿ�");
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
		case MotionEvent.ACTION_UP:			//��ָ�����뿪��Ļʱ
			float My = event.getX();		//��ָ�뿪��Ļʱ������
			float Mx = event.getY();		
			if (fingerMatrix!=null) {
				fingerMatrix.getx(Mx);
				Log.i("�߳�", "�뿪"+Mx);
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
					timer.schedule(task, 2200, 2200);				//2200�������Ϣ��handler����Activity
				}
			}else {
				timer = new Timer(true);
				timer.schedule(task, 2200, 2200);					//2200�������Ϣ��handler����Activity
			}
			break;
		}
		return true;
	}
	
	
	/**
	 * ������Ļ��ʾ
	 */
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {			//handler����Activity��������
			case 1:						//CUT_BITMAP_SEND_TO_ACTIVITY//�и�ͼƬ���͸�Activity����
				Log.i("�߳�", "handler�յ�");
				myBitmap = mBitmap;			//��ȡд�õ�ͼƬ
				if (fingerMatrix!=null) {			//��ȡ���Ƶ���������
					myBitmap = cutBitmap(myBitmap);					//�и�BitmapͼƬ����
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
	 * ������Ϣ��handler����ACTIVITY		
	 */
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what=1;
			Log.i("�߳�", "����");
			handler.sendMessage(message);
		}
	};
	
	/**
	 * ����·����
	 * @author Administrator
	 */
	class savePath{
		Paint paint;
		Path path;
	}
	
	/**
	 * �и�ͼƬ�����͸���Activity
	 * @param bnBitmap
	 */
	public Bitmap cutBitmap(Bitmap bnBitmap){
		//��С
		float minx = fingerMatrix.getMinX();		//��С��x����
		float miny = fingerMatrix.getMinY();		//��С��y����
		//���
		float maxy = fingerMatrix.getMaxY();		//����Y����
		float maxX = fingerMatrix.getMaxX();		//����x����
		Log.i("","�����������ϢΪ��-======���X:"+maxX+"----====���Y:"+maxy+"----====��СX:"+minx+"----====��СY:"+miny); 
		int cutMinX = (int)(minx-15);		//�и����Сֵ
		int cutMinY = (int)(miny-15);		//�и����Сֵ
		int cutMaxX = (int)(maxX+15);
		int cutMaxY = (int)(maxy+15);
		if (cutMinX<=0) {
			cutMinX=0;
		}
		if (cutMinY<=0) {
			cutMinY=0;
		}
		if (cutMaxX>mBitmap.getWidth()) {			//���X�������ͼƬ�Ŀ��
			cutMaxX =  mBitmap.getWidth()-1;		//�ǾͰ�ͼƬ�Ŀ�ȸ���X���� �����ֵ
		}
		if (cutMaxY>mBitmap.getHeight()) {			//���Y�������ͼƬ�Ŀ��
			cutMaxY = mBitmap.getHeight()-1;		//�ǾͰ�ͼƬ�Ŀ�ȸ���Y���� �����ֵ
		}
		
		int width =(int)(cutMaxX - cutMinX);		//����x�����ȥ��С��x����
		int height =(int)(cutMaxY-cutMinY);			//����Y�����ȥ��С��y����
		
		Bitmap wBitmap =Bitmap.createBitmap(bnBitmap, cutMinX, cutMinY, width, height);		//���������˼����ʲô����λ�ô���һ��ͼƬ
		if (myBitmap!=null ) {
			myBitmap.recycle();
			myBitmap= null;
		}
		Log.i("�߳�", "����ͼƬ�ɹ�");
		return wBitmap;
	}
	
	/**
	 * ����ͼƬ
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
	 * ˢ�»���
	 */
	public void RefreshCanvas(){
		Log.i("�߳�", "��ʼ�����ݲ��� ˢ�»���");
		if (lists!=null&&lists.size()>0) {
			lists.remove(lists);
			if (mBitmap!=null) {
				mBitmap=null;
			}
			path=null;
//			inter(dm.widthPixels,dm.heightPixels);
			inter(dm.widthPixels,dm.heightPixels);
			
			Log.i("�߳�", "��ʼ�����ݳɹ�");
			invalidate();
		}
		if (task!=null) {
			task.cancel();
		}
	}
}
