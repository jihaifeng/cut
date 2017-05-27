package com.bn.object;

import static com.bn.util.constant.Constant.RATE;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.jbox2d.dynamics.Body;
import uk.co.geolib.geopolygons.C2DPolygon;
import android.opengl.GLES20;

import com.bn.fastcut.MySurfaceView;
import com.bn.util.constant.Constant;
import com.bn.util.constant.GeoLibUtil;
import com.bn.util.constant.MatrixState;

public class BNObject
{
	public C2DPolygon cp;//��������ζ���
	public FloatBuffer mVertexBuffer;//�����������ݻ���
	public FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle;//����λ����������id  
    int maTexCoorHandle;//��������������������id
    
    int muSjFactor;//˥����������id
    
    int programId;//�Զ�����Ⱦ���߳���id
	int texId;//����ͼƬ��
	int vCount;//�������
    boolean initFlag=false;//�ж��Ƿ��ʼ����ɫ��  
    public Body body;//����Body����
    float x;//��Ҫƽ�Ƶ�x����
	float y;//��Ҫƽ�Ƶ�y����
	public float ballPositionX;
	public float ballPositionY;
	boolean isArea=false;
	int num=0;//�ж�Ϊ�ڼ�������
	MySurfaceView mv;
	boolean isLine=false;//�Ƿ��������--true����
	int index=1;//0-���� 1-����
	float angle=0;//��������ʱ��ת�ĽǶ�
	
	public BNObject(MySurfaceView mv,Body body,float x,float y,float picWidth,float picHeight,int texId,int programId,int index)
	{
		this.mv=mv;
		this.x=x;
		this.y=y;
		this.body=body;//���Body����
		mv.BallBody.add(body);//��body ������ӵ�MySurfaceView�е��б�
		this.body.setUserData(index);//����body�����ID
		this.texId=texId;//��ö�Ӧ������ID
		this.programId=programId;//��ö�Ӧ�ĳ���ID
		initVertexData(picWidth,picHeight);//��ʼ����������
	}
	public BNObject(float sx,float sy,float ex,float ey,int texId,int programId,boolean isLine,int index)
	{//�����и���
		float length=(float)Math.sqrt((ex-sx)*(ex-sx)+(ey-sy)*(ey-sy));//�����ĳ���
		float halfx=0;//�����İ��
		float halfy=0;//�����İ��
		this.isLine=isLine;//��������
		this.angle=(float)Math.toDegrees(Math.atan((ex-sx)/(ey-sy)));//�������ת�ĽǶ�
		if(sx<=ex&&sy<=ey)//����б������
		{
			halfx=sx+Math.abs(ex-sx)/2;
			halfy=sy+Math.abs(ey-sy)/2;
		}
		else if(sx>=ex&&sy>=ey)//����б������
		{
			halfx=ex+Math.abs(ex-sx)/2;
			halfy=ey+Math.abs(ey-sy)/2;
		}
		else if(sx>=ex&&sy<=ey)//����б������
		{
			halfx=ex+Math.abs(ex-sx)/2;
			halfy=sy+Math.abs(ey-sy)/2;
		}
		else if(sx<=ex&&sy>=ey)//����б������
		{
			halfx=sx+Math.abs(ex-sx)/2;
			halfy=ey+Math.abs(ey-sy)/2;
		}
		this.x=Constant.fromScreenXToNearX(halfx);//����Ļxת�����ӿ�x����
		this.y=Constant.fromScreenYToNearY(halfy);//����Ļyת�����ӿ�y����
		this.texId=texId;
		this.programId=programId;
		this.index=index;
		if(index==0)//�и���
		{
			initVertexData(8,length);//��ʼ����������
		}else
		{
			initVertexData(50,length);//��ʼ����������
		}
	}
	public BNObject(float x,float y,float picWidth,float picHeight,int texId,int programId)
	{
		this.x=Constant.fromScreenXToNearX(x);//����Ļxת�����ӿ�x����
		this.y=Constant.fromScreenYToNearY(y);//����Ļyת�����ӿ�y����
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);//��ʼ����������
	}
	public BNObject(float x,float y,float picWidth,float picHeight,int texId,int programId,int num)
	{
		this.num=num;
		isArea=true;//���и�������ݶ���
		this.x=Constant.fromScreenXToNearX(x);//����Ļxת�����ӿ�x����
		this.y=Constant.fromScreenYToNearY(y);//����Ļyת�����ӿ�y����
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);//��ʼ����������
	}
    public BNObject(int texId,int programId,float[] vData,float yswidth,float ysheight)
    {
    	this.texId=texId;//�������ͼƬ����
    	this.programId=programId;//���ʹ�����׳���
    	cp=GeoLibUtil.createPoly(vData);//���������
    }
	public void initVertexData(float width,float height)//��ʼ����������
	{
		vCount=4;//�������
		
		float degree=1;//�и���������ͼƬ��Tֵ
		width=Constant.fromPixSizeToNearSize(width);//��Ļ���ת�����ӿڿ��
		height=Constant.fromPixSizeToNearSize(height);//��Ļ�߶�ת�����ӿڸ߶�
		
		if(isLine&&index==0)//��������
		{
			degree=height;
		}
		//��ʼ��������������
		float vertices[]=new float[]
		{
				-width/2,height/2,0,
				-width/2,-height/2,0,
				width/2,height/2,0,
				width/2,-height/2,0
		};
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
		vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mVertexBuffer=vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);//�򻺳����з��붥����������
		mVertexBuffer.position(0);//���û�������ʼλ��
		float[] texCoor=new float[12];//��ʼ��������������
		if(isLine)//�и�����
		{
			texCoor=new float[]
					{
					0,0,
					0,degree,
					1,0,
					1,degree,
					1,0,
					0,degree
					};
		}else if(!isArea)
		{					//����ͼ�ε���������
			texCoor=new float[]
					{
					0,0,
					0,1,
					1,0,
					1,1,
					1,0,
					0,1
					};
		}
		else//�и��������
		{
			float rate=0.1f*num;
			texCoor=new float[]
					{
					0+rate,0,
					0+rate,1,
					1*0.1f+rate,0,
					1*0.1f+rate,1,
					1*0.1f+rate,0,
					0+rate,1
					};
		}
		ByteBuffer cbb=ByteBuffer.allocateDirect(texCoor.length*4);//�������������������ݻ���
		cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mTexCoorBuffer=cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);//���û�������ʼλ��
	}
    //��ʼ����ɫ��
    public void initShader()
    {
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES20.glGetAttribLocation(programId, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(programId, "uMVPMatrix");  
    }
    
    //����ľ����� �����ŵķ���=========================start===============================
    public void initShader(boolean isFly)
    {
        //��ȡ�����ж���λ����������id
        maPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        //��ȡ�����ж�������������������id
        maTexCoorHandle= GLES20.glGetAttribLocation(programId, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(programId, "uMVPMatrix");  
        //��ȡ������˥����������id
        muSjFactor=GLES20.glGetUniformLocation(programId, "sjFactor");
    }
    public void drawSelf(float sj){}
    //����ľ����� �����ŵķ���=========================end===============================
    
    //���ƻ�
    public void drawSelf(int TexID)
    {        
    	if(!initFlag)
    	{
    		//��ʼ����ɫ��        
    		initShader();
    		initFlag=true;
    	}
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(programId);
    	MatrixState.pushMatrix();//��������
		MatrixState.translate(x,y, 0);//ƽ��
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv
    	(
    			muMVPMatrixHandle,
    			1,
    			false,
    			MatrixState.getFinalMatrix(),
    			0
    	);
    	//Ϊ����ָ������λ������
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	//Ϊ����ָ������������������
    	GLES20.glVertexAttribPointer  
    	(
    			maTexCoorHandle, 
    			2, 
    			GLES20.GL_FLOAT, 
    			false,
    			2*4,   
    			mTexCoorBuffer
    			);   
    	//������λ����������
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
    	
    	//������
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,TexID);
    	
    	//�����������--������
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND);
    	
    	MatrixState.popMatrix();//�ָ�����
    }
    //����ͼ��
    public void drawSelf()
    {        
    	if(!initFlag)
    	{
    		//��ʼ����ɫ��        
    		initShader();
    		initFlag=true;
    	}
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(programId);
    	if(body!=null)
    	{
    		x=body.getPosition().x*RATE;//���ݸ�����x����
    		y=body.getPosition().y*RATE;//���ݸ�����y����
    		
    		ballPositionX=x;
    		ballPositionY=y;	
    		x=Constant.fromScreenXToNearX(x);//����Ļxת�����ӿ�x����
    		y=Constant.fromScreenYToNearY(y);//����Ļyת�����ӿ�y����
    	}
    	MatrixState.pushMatrix();//��������
		MatrixState.translate(x,y, 0);//ƽ��
		
		if(isLine)//�����и�����
		{
			MatrixState.rotate(angle, 0, 0, 1);//��ת
		}
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv
    	(
    			muMVPMatrixHandle, 
    			1, 
    			false, 
    			MatrixState.getFinalMatrix(), 
    			0
    	); 
    	//Ϊ����ָ������λ������
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,
    			3, 
    			GLES20.GL_FLOAT,
    			false,
    			3*4,
    			mVertexBuffer
    			);
    	//Ϊ����ָ������������������
    	GLES20.glVertexAttribPointer
    	(
    			maTexCoorHandle,
    			2,
    			GLES20.GL_FLOAT,
    			false,
    			2*4,
    			mTexCoorBuffer
    			);   
    	//������λ����������
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
    	
    	//������
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texId);
    	
    	//�����������--������
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND);
    	
    	MatrixState.popMatrix();//�ָ�����
    }
}
