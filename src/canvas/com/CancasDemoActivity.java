package canvas.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CancasDemoActivity extends Activity implements OnClickListener{
	public HandTouchView touchView;
	private Bitmap tempBitmap;
	public int mWidth,mHeight;
	public Button saveButton;
	private Button redButton;
	private Bitmap savebitmap;
	private Button whiteButton;
	private Button blueButton;
	private ImageEditTextView imageEditTextView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FindId();
    }
    
    public void FindId(){
    	Display display = this.getWindowManager().getDefaultDisplay();
		mWidth = display.getWidth();
		mHeight = display.getHeight();
    	touchView=(HandTouchView)this.findViewById(R.id.hand_touch);
    	touchView.setMbitmaphHandler(handler);
    	imageEditTextView=(ImageEditTextView)this.findViewById(R.id.main_image_edit);
    	saveButton=(Button)this.findViewById(R.id.main_save);
    	redButton=(Button)this.findViewById(R.id.main_red);
    	whiteButton=(Button)this.findViewById(R.id.main_white);
    	blueButton=(Button)this.findViewById(R.id.main_blue);
    	saveButton.setOnClickListener(this);
    	redButton.setOnClickListener(this);
    	whiteButton.setOnClickListener(this);
    	blueButton.setOnClickListener(this);
    }
    
    /**
     * 图片处理方法
     */
    Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Bundle bundle = new Bundle();
				bundle = msg.getData();
				tempBitmap = bundle.getParcelable("bitmap");			//获取图片
				if (null!=tempBitmap) {
					Log.i("线程", "前台取到VIEW的值");
					int w  = tempBitmap.getWidth();							//获取图片的宽度如果大就缩小如果小就不变
					int h = tempBitmap.getHeight();
					//取到当前的行高和行宽
					int lineHeight =  (int) (mHeight/6f);
					int lineWidth = (int) (mWidth/4f); 
					if(mHeight == 1280 && mWidth == 800){
						lineHeight =  (int) (mHeight/5.4f);
						lineWidth = (int) (mWidth/4f); 
					}
					tempBitmap =BitmapAmplification(tempBitmap);
					editInsertBitmap(tempBitmap);				//显示在EditText上
				}
				break;
			}
			
			super.handleMessage(msg);
		}
    	
    };
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_red:
			editImageDelete();
			break;
			
		case R.id.main_blue:
			FingerMatrix.colorValue=Color.BLUE;
			break;
		case R.id.main_white:
			FingerMatrix.colorValue=Color.WHITE;
			break;
		case R.id.main_save:
			EditTextSaveImage();
			break;
		}
		
	}
	
	Bitmap bp;
	public Bitmap BitmapAmplification(Bitmap path) {
			if (path!=null) {
				if(bp!=null){
					bp=null;
				}
				int w = path.getWidth();	//得到图片的宽度
				int h = path.getHeight();//得到图片的高度
				Log.v("nnn", "宽"+w+"高"+h);
				float ww = ((float) 62) / w;
				float hh = ((float) 45) / h;
				Matrix matrix = new Matrix();
				matrix.postScale(ww, hh);
				Log.v("nnn", "可以www");
				bp = Bitmap.createBitmap(path, 0, 0, w, h, matrix, true);
				if (tempBitmap!=null ) {
					tempBitmap=null;
				}
				return bp;
			}
			return null;
	}
	
	/**
	 * 将bitmap添加到自定义edittext中
	 * @param bitmap
	 */
	public void editInsertBitmap(Bitmap bitmap) {
		SpannableString ss = new SpannableString("1");
		ImageSpan span = new ImageSpan(bitmap, ImageSpan.ALIGN_BOTTOM);
		ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		imageEditTextView.append(ss);
		imageEditTextView.setPadding(0, 0, 0, 10);
	}
	
	/**
	 * 删除方法
	 */
	public void editImageDelete(){
		Editable editable = imageEditTextView.getText();
		int end = imageEditTextView.getSelectionEnd();		//删除最后的一个
		if (end==1) {
			imageEditTextView.setText("");
			return;
		}
		if (end<=0) {
			imageEditTextView.setText("");
			return;
		}
		CharSequence sequence = editable.subSequence(0, end-1);
		imageEditTextView.setText(sequence);
		imageEditTextView.setSelection(end-1);
	}
	
	public void EditTextSaveImage(){
		try {
			if (savebitmap!=null) {
				savebitmap=null;
				
			}
			boolean drawing = imageEditTextView.isDrawingCacheEnabled();
			if (!drawing) {
				imageEditTextView.setDrawingCacheEnabled(true);
				imageEditTextView.buildDrawingCache();
			}
			imageEditTextView.setFocusable(false);
			savebitmap= imageEditTextView.getDrawingCache();
			if (savebitmap!=null) {
				String sd = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScrawlImage/";
				File file = new File(sd);
				if (!file.exists()) {
					file.mkdirs();
				}
				file.createNewFile();
				FileOutputStream is = new FileOutputStream(file+"/nnn.jpg");
				savebitmap.compress(CompressFormat.JPEG, 100, is);
				is.flush();
				is.close();
				savebitmap=null;
				imageEditTextView.setFocusable(true);
				imageEditTextView.setFocusableInTouchMode(true);
				imageEditTextView.requestFocus();
			}
		} catch (FileNotFoundException e) {
			Toast.makeText(CancasDemoActivity.this, "保存失败!",1).show();
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(CancasDemoActivity.this, "保存失败!",1).show();
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
}