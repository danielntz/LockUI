package com.example.lock;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LockPatternView  extends  View{
      
	 private static final String TAG = null;
     //����ȫ�ֶ���
	 judgeflag   signal ;
	//��������
	Paint  paint  = new Paint(Paint.ANTI_ALIAS_FLAG);
	OnpatternchangeListener  listener ; 
	//9����
        private    Point   points [][]= new Point[3][3];
        private   boolean    isinit  , isselected , isover;                                              //�ж��Ƿ��ʼ��
        private   float    Width, Height, MoveX , MoveY;                                  //��ȡ��Ļ�Ŀ���
        private float   offsetsX  ;                                                 //ƫ���� X
       private   float   offsetsY;                                                    //ƫ���� Y
       private  float   BitmapRadious;                                   //ͼƬ�İ뾶
       private  List<Point>  listpoint  = new ArrayList<Point>();          //������Ű��µĵ�
      private   Bitmap    normalpoint, errorpoint, pressedpoint , linepoint ,lineerropoint;
      private   boolean  MovingNopoint;
    
      private   Matrix  matrix = new Matrix();     //����ʵ��ͼƬ�����ţ���ת
	
      public LockPatternView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
      //����ǰ��static�����ò���ʵ����
	public    static   class   Point {
		       //����״̬
		        public   static  int    STATE_NORMAL = 0;
		        public   static  int    STATE_SELECED = 1;
		        public  static  int    STATE_ERROR = 2;
		        
		       //��ĺ����꣬������
		      public     float    x;
		      public    float    y;
		      public      int   index = 0, state = 0;
		      public     Point(){
		    	  
		      }
		      
		      public   Point(float  X,  float Y){
		    	  this.x = X;
		    	  this.y = Y;
		      }
		      
		    //�ж��غϵķ�����������֮��ľ�����һ���ķ�Χ������Ϊ����������һ��ģ�������Ϊ
		  	public  static   Boolean  with(float x , float y , float R ,float MoveX, float MoveY){
		  		                return  Math.sqrt(    (x - MoveX) * (x - MoveX)  +  ( y - MoveY)  * (y - MoveY)   ) < R;
		  	}
		  	
		  //����֮��ľ���,���������ű���
			public   static   double   distance(Point a ,Point b){
				       return  Math.sqrt(    Math.abs( a.x - b.x) * Math.abs(a.x - b.x)  + Math.abs(a.y - b.y)  * Math.abs(a.y - b.y) ) ;
			}
			
}
	/**
	 * ͼ������������OnTouchEvent�¼��ϵ���
	 * @author lenovo
	 *
	 */
	 
	
	//��OnDraw�л���������
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		         //��Ҫ��ʼ���㣬Ȼ����л���
		   if(!isinit){
			     initPoints();                         //��ʼ����ĺ���
		   }
		   point2Canvas(canvas);     //�ѵ㻭����
		   
		   if(listpoint .size() > 0){       //���߻�����
			      Point a = listpoint.get(0);     //��a��ȡ��
			    
			   for(int i = 0  ; i < listpoint.size() ; i++){
			    	     Point  b = listpoint.get(i);
			    	  //���ƾŹ����еĵ�
			    	     linetoCanvas(canvas, a, b);
			    	     a = b;
			      }
			   
			   //���ƾŹ���֮��������                                                                                                                                                                                                                                                                                                                                                                                                          
			   if(MovingNopoint){
				   linetoCanvas(canvas, a, new Point(MoveX,MoveY));
			   }
		   }
		   else{
			            
		   }
	}
	
	/**
	 * ��������ڻ�����
	 * @param canvas  ����
	 */
	
   private void point2Canvas(Canvas canvas) {
		// TODO Auto-generated method stub
		    for(int i = 0  ; i < points.length ; i++){
		    	     for(int j = 0 ; j < points[i].length ; j++){
		    	      	 
		    	    	     Point  point = points[i][j];
		    	    	     if(point.state == Point.STATE_NORMAL){
		    	    	    	     canvas.drawBitmap(normalpoint, point.x - BitmapRadious, point.y - BitmapRadious, paint );
		    	    	     }
		    	    	     else if(point.state == Point.STATE_ERROR){
		    	    	   	 canvas.drawBitmap(errorpoint, point.x  - BitmapRadious, point.y - BitmapRadious, paint);
		    	    	    
		    	    	     }
		    	    	     else{
		    	    	    	 canvas.drawBitmap(pressedpoint, point.x  - BitmapRadious, point.y  - BitmapRadious, paint);
		    	    	     }
		    	    	 
		   
		    	     
		    	     
		    	     }
		    }
	}
   /**
    * 
    * @param canvas  ����
    * @param a   ��һ����
    * @param b  �ڶ�����
    */
   public  void  linetoCanvas(Canvas  canvas  , Point a , Point b){
	         
	     double   linelength = Point.distance(a, b);  
	     float   degress = getDegress(a,b); 
	     canvas.rotate(degress, a.x,a.y);                        //�û�����ת��rotate���������ת�����������ŵ�ǰ����ת
	   if(a.state == Point.STATE_SELECED){
		                 //matrix�е����ַ���
	             	      matrix.setScale((float)linelength  / linepoint.getWidth(),  1) ;               //x�ᣬY�������  X������ű�����Y�᲻��Ҫ��������1����canvas���ܹ���ʾ
	                      matrix.postTranslate(a.x - linepoint.getWidth() /2,a.y - linepoint.getHeight() /2);    //ƽ��,�ӵ�ǰλ�ÿ�ʼ��,Ҳ���ǵ����λ��Ϊ�������
	            	       canvas.drawBitmap(linepoint, matrix, paint);                                         //canvas�൱��һ��͸��ͼ�㣬ÿ��drawʱ�ǻ���ͼ���ϣ��൱��PS�ܵ�ͼ�㣬Ȼ�����պϳ���һ��
	   }
	               else{
	            	   matrix.setScale((float)linelength  / lineerropoint.getWidth(),  1) ;               //x�ᣬY�������  X������ű�����Y�᲻��Ҫ��������1
                  //   matrix.postTranslate(a.x - lineerropoint.getWidth() /5,a.y - lineerropoint.getHeight() /5);
	               matrix.postTranslate(a.x - linepoint.getWidth() /2,a.y - linepoint.getHeight() /2);
	           //	    matrix.postTranslate(a.x -7, a.y - 7);
            	       canvas.drawBitmap(lineerropoint, matrix, paint);      
	               }
	   
	        canvas.rotate( - degress,a.x,a.y);       //�ѻ�������ת����
   }

/**
    * ��ʼ����ĺ���
    */
	private void initPoints() {
		// TODO Auto-generated method stub
		                Width = getWidth();                          //��ȡ��Ļ����
		               Height = getHeight();
		           
	                 //�жϺ�����������
		               
		               if(Width > Height){
		            	      //����
		            	   offsetsX = (Width  - Height )/2;                    //ƫ�Ƶ���ԲȦ�ĵط�
		            	   Width  = Height;                                                 //ͼ�εĿ��һ��
		               }
		               else{
		            	     //����
		            	     offsetsY = (Height - Width )/2;                 
		            	     Height  = Width;
		               }
		                
		               
		             //ͼƬ��Դ
		              normalpoint = BitmapFactory.decodeResource(getResources(), R.drawable.green);
		             pressedpoint =  BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
		             errorpoint =  BitmapFactory.decodeResource(getResources(), R.drawable.red);
		              linepoint = BitmapFactory.decodeResource(getResources(), R.drawable.line);
		             lineerropoint = BitmapFactory.decodeResource(getResources(), R.drawable.lineerror);
		      //����������
		       points [0][0] = new Point(offsetsX  + Width / 4 ,offsetsY+ Width /4);
		       points [0][1] = new Point(offsetsX + Width /2,offsetsY + Width/4); 
		       points [0][2] = new Point(offsetsX + Width - Width /4 ,offsetsY + Width/4);
		       
		       points [1][0] = new Point(offsetsX  + Width / 4,offsetsY+ Width /2);
		       points [1][1] = new Point(offsetsX + Width /2,offsetsY+ Width /2); 
		       points [1][2] = new Point(offsetsX + Width - Width /4,offsetsY+ Width /2);
		       
		       points [2][0] = new Point(offsetsX  + Width / 4,offsetsY+ Width - Width /4);
		       points [2][1] = new Point(offsetsX + Width /2,offsetsY+ Width - Width /4); 
		       points [2][2] = new Point(offsetsX + Width - Width /4,offsetsY+ Width - Width /4);
	      //ͼƬ��Դ�뾶,��õ�ǰͼƬ�ĳ��Ȳ��ҳ���2
		       BitmapRadious = normalpoint.getHeight()/2;
		       //��������
		      int  index = 1;
		       for(Point[] points : this.points){
		    	   for(Point point : points){
		    		   point.index = index;
		    		   index ++ ;
		    	   }
		       }
		       //��ʼ�����
		       isinit = true;
		   
	}
     
	   //����View�е�Touch�¼�
@Override
public boolean onTouchEvent(MotionEvent event) {
	// TODO Auto-generated method stub
	     isover = false;
	  MovingNopoint   =  false;
	   MoveX  = event.getX();      //��õ�ǰ���µĵ��X����
	   MoveY   = event.getY();     //��õ�ǰ���µĵ��Y����
	   Log.i(TAG, MoveX+"");
	   Log.i(TAG, MoveY+"");
	  
	     Point   point    = null ;
	   
	   switch (event.getAction()) {
	  //����
	  case MotionEvent.ACTION_DOWN   :
		        reset();     
		        if(listener != null){
		        	listener.onpatterstart(true);
		        }
	          point = checkpoint();
		         if(point != null){
		        	     isselected = true;
		            }
	 	break;
		//�ƶ�
	  case  MotionEvent.ACTION_MOVE:
		        if(isselected){
		        	point  = checkpoint();
		        	if(point  == null){
		        		MovingNopoint = true;
		        	}
		        }
		  
		         break;
		  
		 //̧��
	  case MotionEvent.ACTION_UP:
		         isover = true;
		         isselected   = false;
		         break;
		        
	}
	   //ѡ���ظ����
	   if(  !isover  && isselected && point !=null){
		    //�����        
		   if(crossPoint(point )){
		            	      MovingNopoint  = true;
		             }
		   //�µ� 
		   else{
		             point.state = Point.STATE_SELECED; 
			           listpoint.add(point);
		            	 
		             }
	   }
	   //���ƽ���
	   if(isover){
		    //���Ʋ�����
		   if(listpoint.size() == 1){
		    	    reset();
		     }
		   //���ƴ���
		     else if(listpoint.size() < 5 &&  listpoint.size()  > 0){
		    	          errorPoint();
		    	          if(listener != null){
		    	        	 listener.onpatterchanged(null);
		    	        	 // listener.onpatterchanged("nimei");
		    	        	 
		    	          }
		    	          
		     }
		   //���Ƴɹ�,���������¼�
		     else{
		    	  if(listener != null){
		    		  String  passstr = "";
		    		  for(int i = 0 ; i < listpoint.size(); i++){
		    			       passstr = passstr + listpoint.get(i).index;
		    		  }
		    		  if(!TextUtils.isEmpty(passstr))
		    		 listener.onpatterchanged(passstr);       //���Ƴɹ��ѻ��Ƴɹ������뷵�س�ȥ
		    		//  listener.onpatterchanged("nimei");
		    	  }
		     }
	   }
	  postInvalidate();       //ÿ��ontouch �¼���������View���»���һ�£�ˢ��View,�����µ���OnDraw�ķ���
	             return  true;
}
  
//�����ļ�� 
  private boolean crossPoint( Point   point){
	        if(listpoint.contains(point)){
	        	return true;
	        }
	        else{
	        	
	        	  return false;
	        }
	  
  }
  //��ȡ�Ƕ�
    public  float  getDegress (Point a  ,Point b){
    	    float  ax = a.x;
    	    float  ay = a.y;
    	    float   bx = b.x;
    	    float  by = b.y;
    	    float  degress = 0;
    	    if(bx == ax){           //y����� 90�Ȼ�270��
    	         if(by > ay){       //��Y���±�90
    	        	  degress = 90;
    	         }else if(by < ay){     //��Y���ϱ�270
    	        	 degress = 270;
    	         }
    	    }else if(by  == ay){     //y����� 0 �� 180 
    	    	if(bx > ax){      
    	    		degress = 0;
    	    	}else if(bx < ax){
    	    		degress = 180;
    	    	}
    	    }
    	    else{
    	    	  degress =   (float) Math.toDegrees((float) Math.atan2(b.y - a.y, b.x - a.x));
    	    }
    	    
    	    return   degress;
    	   
    	    
    	    
    	    
    }

//���Ʋ������ķ���
public  void   reset(){
	  for(int i = 0 ; i < listpoint.size() ; i ++){
		       Point point = listpoint.get(i);
		   point.state = Point.STATE_NORMAL;
	  }
	
	listpoint.clear();
}

//���ƴ���ķ���
public   void    errorPoint(){
	      for(Point  point :   listpoint){
	    	       point.state = Point.STATE_ERROR;
	      }
}
	/**
	 * �жϵ�ǰ����ĵط�������͵�������Ƿ����غϣ�������һ����Χ��
	 * @return
	 */
	public   Point checkpoint (){
	        
		   for(int i= 0 ; i < points.length ; i++){
			   for( int j = 0 ; j < points[i].length ; j++){
				     Point  point = points[i][j];
				     if(Point.with(point.x, point.y, BitmapRadious, MoveX, MoveY)){
				    	   return   point;
				     }
			   }
		   }
		         
		
		        return  null;
	}
	//�ж��غϵķ���
	public  static   Boolean  with(float x , float y , float R ,float MoveX, float MoveY){
		                return  Math.sqrt(    (x - MoveX) * (x - MoveX)  +  ( y - MoveY)  * (y - MoveY)   ) < R;
	}
	
	  public   static  interface OnpatternchangeListener{
	      //ͼ���ı�
		  void  onpatterchanged(String passwordstr);       //���������ַ���
	      //ͼ���Ƿ����»���
		  void  onpatterstart(boolean   isstart);          //��������ʱ�����ʾ����Ƶ��ַ���
	        
   }
   
   public   void   setOnpatternLListener(OnpatternchangeListener hhh){
	   if(hhh != null)    
	   this.listener = hhh;
   }

}

