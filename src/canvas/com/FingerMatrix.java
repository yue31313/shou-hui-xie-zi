package canvas.com;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.util.Log;

/**
 * url www.johdan.com
 * @author johdan
 *
 */
public class FingerMatrix {
	public static int colorValue=Color.RED;
	private float maxX = 0;
	private float maxY = 0;
	private float minX = 0;
	private float minY = 0;
	
	
	
	
	/**
	 * 鍒濆鍖栨暟鎹?
	 * @param x
	 * @param y
	 */
	public void init(float x,float y){
		maxX=x;
		minX=x;
		maxY=y;
		minY=y;
	}
	
//	public void setX(float x){
//		if(x<0){
//			return;
//		}
//		if(maxX == minX){ //绗竴娆¤祴鍊?
//			if(x>maxX){
//				maxX=x;
//			}else if(x<minX){
//				minX=x;
//			}
//		}else{  //绗簩娆¤祴鍊?
//			if(x > maxX && x > minX){
//				maxX=x;
//			}else if(x < maxX && x < minX){
//				minX=x;
//			}
//		}
//	}
//	
//	public void setY(float y){
//		if(y<0){
//			return;
//		}
//		if(maxY == minY){ //绗竴娆¤祴鍊?
//			if(y > maxY){
//				maxY= y;
//			}else if(y < minY){
//				minY= y ;
//			}
//		}else{  //绗簩娆¤祴鍊?
//			if(y > maxY && y > minY){
//				maxY=y;
//			}else if(y < maxY && y < minY){
//				minY=y;
//			}
//		}
//	}
//	
	/**
	 * 这里是在做X坐标的最大值和最小值
	 * @param x
	 */
	public void getx(float x){
		if (x<0) {
			return;
		}else {
			if (maxX==minX) {			//第一次进来时执行
				if (x>maxX) {
					maxX=x;
				}else if(x<minX){
					minX=x;
				}
			}else {						// 第二次过来时执行
				if (x>maxX&&x>minX) {
					maxX=x;
					//Log.i("线程", "FingerMatrix"+x);
				}else if (x<minX&&x<maxX) {
					minX=x;
					//Log.i("线程", "FingerMatrix"+x);
				}
			}
		}
	}
	
	/**
	 * 这里是在付给y坐标的最大值和最小值
	 * @param y
	 */
	public void getY(float y){
		if (y<0) {
			return;
		}else {
			if (minY==maxY) {			//第一次过来时执行
				if (y<minY) {
					minY = y;
				}else if (y>maxY) {
					maxY =y;
				}	
			}else {						//第二次进来进执行
				if (y>minY&&y>maxY) {
					//Log.i("线程", "FingerMatrix"+y);
					maxY =y;
				}else if (y<maxY&&y<minY) {
					//Log.i("线程", "FingerMatrix"+y);
					minY = y;
				}
			}
		}
	}
	
	
	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

}
