package canvas.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
/**
 * url www.johdan.com
 * @author 重写EditText的功能是为了输入框里有一条输入线
 *
 */
public class ImageEditTextView extends EditText {
	
	private Paint mPaint;
	private Rect mRect;
	private Context mContext;
	
	public ImageEditTextView(Context context) {
		super(context);

        mContext = context;
		
	}
	

	public ImageEditTextView(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mPaint = new Paint();     
        mPaint.setStyle(Paint.Style.STROKE);     
        mPaint.setColor(Color.RED);
        
        mContext = context;
	
	}
	public ImageEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mPaint = new Paint();     
        mPaint.setStyle(Paint.Style.STROKE);     
        mPaint.setColor(Color.BLUE);
        
        
        mContext = context;
	}
	
	
	
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		System.out.println("left:"+left+".top:"+top+".right:"+right+".bottom:"+bottom);
		super.onLayout(changed, left, top, right, bottom);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.i("线程", "-------");
		WindowManager wm = (WindowManager) mContext.getSystemService("window");  
        int windowWidth = wm.getDefaultDisplay().getWidth();  
        int windowHeight = wm.getDefaultDisplay().getHeight();  
          
        Paint paint = new Paint();  
        paint.setStyle(Paint.Style.FILL);  
        paint.setColor(Color.BLACK);  
          
        int paddingTop    = getPaddingTop();  
        int paddingBottom = getPaddingBottom();  
          
        int scrollY       = getScrollY();  		//
        Log.i("线程", "----===---"+scrollY);
        int scrollX       = getScrollX()+windowWidth;  
        int innerHeight   = scrollY + getHeight() - paddingTop - paddingBottom;  
        int lineHeight    = getLineHeight();  
        int baseLine      = scrollY + (lineHeight - (scrollY % lineHeight));  
        Log.i("线程", "----=---"+baseLine);
        int x = 8;  
        while (baseLine < innerHeight) {  
            //canvas.drawBitmap(line, x, baseLine + paddingTop, paint);   
            canvas.drawLine(x, baseLine + paddingTop,scrollX, baseLine + paddingTop, paint);  
            baseLine += lineHeight;  
        } 
	}
	
	
}
