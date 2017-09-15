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
	 * 初始化数�?
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
//		if(maxX == minX){ //第一次赋�?
//			if(x>maxX){
//				maxX=x;
//			}else if(x<minX){
//				minX=x;
//			}
//		}else{  //第二次赋�?
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
//		if(maxY == minY){ //第一次赋�?
//			if(y > maxY){
//				maxY= y;
//			}else if(y < minY){
//				minY= y ;
//			}
//		}else{  //第二次赋�?
//			if(y > maxY && y > minY){
//				maxY=y;
//			}else if(y < maxY && y < minY){
//				minY=y;
//			}
//		}
//	}
//	
	/**
	 * ����������X��������ֵ����Сֵ
	 * @param x
	 */
	public void getx(float x){
		if (x<0) {
			return;
		}else {
			if (maxX==minX) {			//��һ�ν���ʱִ��
				if (x>maxX) {
					maxX=x;
				}else if(x<minX){
					minX=x;
				}
			}else {						// �ڶ��ι���ʱִ��
				if (x>maxX&&x>minX) {
					maxX=x;
					//Log.i("�߳�", "FingerMatrix"+x);
				}else if (x<minX&&x<maxX) {
					minX=x;
					//Log.i("�߳�", "FingerMatrix"+x);
				}
			}
		}
	}
	
	/**
	 * �������ڸ���y��������ֵ����Сֵ
	 * @param y
	 */
	public void getY(float y){
		if (y<0) {
			return;
		}else {
			if (minY==maxY) {			//��һ�ι���ʱִ��
				if (y<minY) {
					minY = y;
				}else if (y>maxY) {
					maxY =y;
				}	
			}else {						//�ڶ��ν�����ִ��
				if (y>minY&&y>maxY) {
					//Log.i("�߳�", "FingerMatrix"+y);
					maxY =y;
				}else if (y<maxY&&y<minY) {
					//Log.i("�߳�", "FingerMatrix"+y);
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
