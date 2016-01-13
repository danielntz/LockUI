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
     //创建全局对象
	 judgeflag   signal ;
	//创建画笔
	Paint  paint  = new Paint(Paint.ANTI_ALIAS_FLAG);
	OnpatternchangeListener  listener ; 
	//9个点
        private    Point   points [][]= new Point[3][3];
        private   boolean    isinit  , isselected , isover;                                              //判断是否初始化
        private   float    Width, Height, MoveX , MoveY;                                  //获取屏幕的宽，高
        private float   offsetsX  ;                                                 //偏移量 X
       private   float   offsetsY;                                                    //偏移量 Y
       private  float   BitmapRadious;                                   //图片的半径
       private  List<Point>  listpoint  = new ArrayList<Point>();          //用来存放按下的点
      private   Bitmap    normalpoint, errorpoint, pressedpoint , linepoint ,lineerropoint;
      private   boolean  MovingNopoint;
    
      private   Matrix  matrix = new Matrix();     //矩阵实现图片的缩放，旋转
	
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
      //类名前加static，调用不用实例化
	public    static   class   Point {
		       //三种状态
		        public   static  int    STATE_NORMAL = 0;
		        public   static  int    STATE_SELECED = 1;
		        public  static  int    STATE_ERROR = 2;
		        
		       //点的横坐标，纵坐标
		      public     float    x;
		      public    float    y;
		      public      int   index = 0, state = 0;
		      public     Point(){
		    	  
		      }
		      
		      public   Point(float  X,  float Y){
		    	  this.x = X;
		    	  this.y = Y;
		      }
		      
		    //判断重合的方法，两个点之间的距离在一定的范围内则认为这两个点是一起的，大致认为
		  	public  static   Boolean  with(float x , float y , float R ,float MoveX, float MoveY){
		  		                return  Math.sqrt(    (x - MoveX) * (x - MoveX)  +  ( y - MoveY)  * (y - MoveY)   ) < R;
		  	}
		  	
		  //两点之间的距离,用于求缩放比例
			public   static   double   distance(Point a ,Point b){
				       return  Math.sqrt(    Math.abs( a.x - b.x) * Math.abs(a.x - b.x)  + Math.abs(a.y - b.y)  * Math.abs(a.y - b.y) ) ;
			}
			
}
	/**
	 * 图案监听器，在OnTouchEvent事件上调用
	 * @author lenovo
	 *
	 */
	 
	
	//在OnDraw中画出点与线
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		         //需要初始化点，然后进行绘制
		   if(!isinit){
			     initPoints();                         //初始化点的函数
		   }
		   point2Canvas(canvas);     //把点画出来
		   
		   if(listpoint .size() > 0){       //把线画出来
			      Point a = listpoint.get(0);     //把a点取出
			    
			   for(int i = 0  ; i < listpoint.size() ; i++){
			    	     Point  b = listpoint.get(i);
			    	  //绘制九宫格中的点
			    	     linetoCanvas(canvas, a, b);
			    	     a = b;
			      }
			   
			   //绘制九宫格之外的坐标点                                                                                                                                                                                                                                                                                                                                                                                                          
			   if(MovingNopoint){
				   linetoCanvas(canvas, a, new Point(MoveX,MoveY));
			   }
		   }
		   else{
			            
		   }
	}
	
	/**
	 * 将点绘制在画布上
	 * @param canvas  画布
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
    * @param canvas  画布
    * @param a   第一个点
    * @param b  第二个点
    */
   public  void  linetoCanvas(Canvas  canvas  , Point a , Point b){
	         
	     double   linelength = Point.distance(a, b);  
	     float   degress = getDegress(a,b); 
	     canvas.rotate(degress, a.x,a.y);                        //让画布旋转，rotate这个方法旋转的中心是绕着当前点旋转
	   if(a.state == Point.STATE_SELECED){
		                 //matrix中的两种方法
	             	      matrix.setScale((float)linelength  / linepoint.getWidth(),  1) ;               //x轴，Y轴的缩放  X轴的缩放比例，Y轴不需要缩放则是1，在canvas上能够显示
	                      matrix.postTranslate(a.x - linepoint.getWidth() /2,a.y - linepoint.getHeight() /2);    //平移,从当前位置开始画,也就是点击的位置为画的起点
	            	       canvas.drawBitmap(linepoint, matrix, paint);                                         //canvas相当于一个透明图层，每次draw时是画在图层上，相当于PS总的图层，然后最终合成在一起
	   }
	               else{
	            	   matrix.setScale((float)linelength  / lineerropoint.getWidth(),  1) ;               //x轴，Y轴的缩放  X轴的缩放比例，Y轴不需要缩放则是1
                  //   matrix.postTranslate(a.x - lineerropoint.getWidth() /5,a.y - lineerropoint.getHeight() /5);
	               matrix.postTranslate(a.x - linepoint.getWidth() /2,a.y - linepoint.getHeight() /2);
	           //	    matrix.postTranslate(a.x -7, a.y - 7);
            	       canvas.drawBitmap(lineerropoint, matrix, paint);      
	               }
	   
	        canvas.rotate( - degress,a.x,a.y);       //把画布在旋转回来
   }

/**
    * 初始化点的函数
    */
	private void initPoints() {
		// TODO Auto-generated method stub
		                Width = getWidth();                          //获取屏幕宽，高
		               Height = getHeight();
		           
	                 //判断横屏还是竖屏
		               
		               if(Width > Height){
		            	      //横屏
		            	   offsetsX = (Width  - Height )/2;                    //偏移到画圆圈的地方
		            	   Width  = Height;                                                 //图形的宽高一样
		               }
		               else{
		            	     //竖屏
		            	     offsetsY = (Height - Width )/2;                 
		            	     Height  = Width;
		               }
		                
		               
		             //图片资源
		              normalpoint = BitmapFactory.decodeResource(getResources(), R.drawable.green);
		             pressedpoint =  BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
		             errorpoint =  BitmapFactory.decodeResource(getResources(), R.drawable.red);
		              linepoint = BitmapFactory.decodeResource(getResources(), R.drawable.line);
		             lineerropoint = BitmapFactory.decodeResource(getResources(), R.drawable.lineerror);
		      //计算点的坐标
		       points [0][0] = new Point(offsetsX  + Width / 4 ,offsetsY+ Width /4);
		       points [0][1] = new Point(offsetsX + Width /2,offsetsY + Width/4); 
		       points [0][2] = new Point(offsetsX + Width - Width /4 ,offsetsY + Width/4);
		       
		       points [1][0] = new Point(offsetsX  + Width / 4,offsetsY+ Width /2);
		       points [1][1] = new Point(offsetsX + Width /2,offsetsY+ Width /2); 
		       points [1][2] = new Point(offsetsX + Width - Width /4,offsetsY+ Width /2);
		       
		       points [2][0] = new Point(offsetsX  + Width / 4,offsetsY+ Width - Width /4);
		       points [2][1] = new Point(offsetsX + Width /2,offsetsY+ Width - Width /4); 
		       points [2][2] = new Point(offsetsX + Width - Width /4,offsetsY+ Width - Width /4);
	      //图片资源半径,获得当前图片的长度并且除以2
		       BitmapRadious = normalpoint.getHeight()/2;
		       //设置密码
		      int  index = 1;
		       for(Point[] points : this.points){
		    	   for(Point point : points){
		    		   point.index = index;
		    		   index ++ ;
		    	   }
		       }
		       //初始化完成
		       isinit = true;
		   
	}
     
	   //覆盖View中的Touch事件
@Override
public boolean onTouchEvent(MotionEvent event) {
	// TODO Auto-generated method stub
	     isover = false;
	  MovingNopoint   =  false;
	   MoveX  = event.getX();      //获得当前按下的点的X坐标
	   MoveY   = event.getY();     //获得当前按下的点的Y坐标
	   Log.i(TAG, MoveX+"");
	   Log.i(TAG, MoveY+"");
	  
	     Point   point    = null ;
	   
	   switch (event.getAction()) {
	  //按下
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
		//移动
	  case  MotionEvent.ACTION_MOVE:
		        if(isselected){
		        	point  = checkpoint();
		        	if(point  == null){
		        		MovingNopoint = true;
		        	}
		        }
		  
		         break;
		  
		 //抬起
	  case MotionEvent.ACTION_UP:
		         isover = true;
		         isselected   = false;
		         break;
		        
	}
	   //选中重复检查
	   if(  !isover  && isselected && point !=null){
		    //交叉点        
		   if(crossPoint(point )){
		            	      MovingNopoint  = true;
		             }
		   //新点 
		   else{
		             point.state = Point.STATE_SELECED; 
			           listpoint.add(point);
		            	 
		             }
	   }
	   //绘制结束
	   if(isover){
		    //绘制不成立
		   if(listpoint.size() == 1){
		    	    reset();
		     }
		   //绘制错误
		     else if(listpoint.size() < 5 &&  listpoint.size()  > 0){
		    	          errorPoint();
		    	          if(listener != null){
		    	        	 listener.onpatterchanged(null);
		    	        	 // listener.onpatterchanged("nimei");
		    	        	 
		    	          }
		    	          
		     }
		   //绘制成功,触发监听事件
		     else{
		    	  if(listener != null){
		    		  String  passstr = "";
		    		  for(int i = 0 ; i < listpoint.size(); i++){
		    			       passstr = passstr + listpoint.get(i).index;
		    		  }
		    		  if(!TextUtils.isEmpty(passstr))
		    		 listener.onpatterchanged(passstr);       //绘制成功把绘制成功的密码返回出去
		    		//  listener.onpatterchanged("nimei");
		    	  }
		     }
	   }
	  postInvalidate();       //每次ontouch 事件都必须让View重新绘制一下，刷新View,会重新调用OnDraw的方法
	             return  true;
}
  
//交叉点的检查 
  private boolean crossPoint( Point   point){
	        if(listpoint.contains(point)){
	        	return true;
	        }
	        else{
	        	
	        	  return false;
	        }
	  
  }
  //获取角度
    public  float  getDegress (Point a  ,Point b){
    	    float  ax = a.x;
    	    float  ay = a.y;
    	    float   bx = b.x;
    	    float  by = b.y;
    	    float  degress = 0;
    	    if(bx == ax){           //y轴相等 90度或270度
    	         if(by > ay){       //在Y轴下边90
    	        	  degress = 90;
    	         }else if(by < ay){     //在Y轴上边270
    	        	 degress = 270;
    	         }
    	    }else if(by  == ay){     //y轴相等 0 或 180 
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

//绘制不成立的方法
public  void   reset(){
	  for(int i = 0 ; i < listpoint.size() ; i ++){
		       Point point = listpoint.get(i);
		   point.state = Point.STATE_NORMAL;
	  }
	
	listpoint.clear();
}

//绘制错误的方法
public   void    errorPoint(){
	      for(Point  point :   listpoint){
	    	       point.state = Point.STATE_ERROR;
	      }
}
	/**
	 * 判断当前点击的地方的坐标和点的坐标是否相重合（可以有一定范围）
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
	//判断重合的方法
	public  static   Boolean  with(float x , float y , float R ,float MoveX, float MoveY){
		                return  Math.sqrt(    (x - MoveX) * (x - MoveX)  +  ( y - MoveY)  * (y - MoveY)   ) < R;
	}
	
	  public   static  interface OnpatternchangeListener{
	      //图案改变
		  void  onpatterchanged(String passwordstr);       //返回密码字符串
	      //图案是否重新绘制
		  void  onpatterstart(boolean   isstart);          //当不画的时候就显示请绘制的字符串
	        
   }
   
   public   void   setOnpatternLListener(OnpatternchangeListener hhh){
	   if(hhh != null)    
	   this.listener = hhh;
   }

}

