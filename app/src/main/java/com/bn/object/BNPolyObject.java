package com.bn.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.bn.fastcut.MySurfaceView;
import com.bn.util.constant.Constant;
import com.bn.util.constant.GeoLibUtil;
import com.bn.util.constant.GeometryConstant;
import com.bn.util.constant.MatrixState;
import android.opengl.GLES20;

//����������
public class BNPolyObject extends BNObject
{
	MySurfaceView mv;
	float vx;
	float vy;
	int count=1;
    public BNPolyObject(MySurfaceView mv,int texId,int program,float[] vData,float yswidth,float ysheight,float vx,float vy)
    {
    	super(texId,program,vData,yswidth,ysheight);
    	this.mv=mv;
    	this.vx=vx;
    	this.vy=vy;
    	initVertexData(vData,yswidth,ysheight);
    }
    //��ʼ��������������ɫ���ݵķ���
    public void initVertexData(float[] vData,float yswidth,float ysheight)
    {
    	//--------�����н��������λ��Ʒ�ʽ----------
    	float[] dd= GeoLibUtil.fromAnyPolyToTris(vData);//��������зֳ���������
    	vCount=dd.length/2;
    	//�����������ݵĳ�ʼ��================begin============================
    	float vertices[]=new float[vCount*3];
    	for(int i=0;i<vCount;i++)
    	{
    		vertices[i*3]=Constant.fromScreenXToNearX(dd[i*2]);//x����
    		vertices[i*3+1]=Constant.fromScreenYToNearY(dd[i*2+1]);//y����
    		vertices[i*3+2]=0;//z����
    	}		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //���������������ݵĳ�ʼ��================begin============================
        float texCoor[]=new float[vCount*2];
        for(int i=0;i<vCount;i++)
        {
        	texCoor[i*2]=dd[i*2]/yswidth;//����s����
        	texCoor[i*2+1]=dd[i*2+1]/ysheight;//����t����
        }
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }
    
    //����ľ����� �����ŵķ���=========================start===============================
    public void drawSelf(float sj)
    {
    	if(!initFlag)
    	{
    		//��ʼ����ɫ��
    		initShader(true);
    		initFlag=true;
    	}
    	if(count>15)
    	{
    		sj=sj-0.04f*(count-16);
    	}
    	if(sj<0.7)
    	{
    		GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
        	//���û������
        	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	}
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(programId);
    	
    	MatrixState.pushMatrix();//��������
    	
    	//���﹩������=========================================================
    	 count++; 
    	 //����
    	 if(GeometryConstant.cutDirection==1)
    	 {
    		 MatrixState.translate(0, GeometryConstant.calculateDisplacement(1,count), 0);
    	 }//����
    	 else if(GeometryConstant.cutDirection==2)
    	 {	
    		 MatrixState.translate(0,  GeometryConstant.calculateDisplacement(2,count), 0);   		 
    	 }//����
    	 else if(GeometryConstant.cutDirection==3)
    	 {
    		 MatrixState.translate( GeometryConstant.calculateDisplacement(3,count), GeometryConstant.calculateDisplacement(1,count), 0);   	 
    	 }//����
    	 else if(GeometryConstant.cutDirection==4)
    	 {
    		 MatrixState.translate( GeometryConstant.calculateDisplacement(4,count), GeometryConstant.calculateDisplacement(1,count), 0);
    	 }
    	 MatrixState.rotate(-count*0.5f, 0, 0, 1);//��ת
    	//���﹩������=========================================================
    	
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
    	//��˥�����Ӵ���shader����
    	GLES20.glUniform1f(muSjFactor, sj);
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
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //�����������
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
         if(sj<0.7)
         {
        	 //�رջ��
        	 GLES20.glDisable(GLES20.GL_BLEND);
         }
         MatrixState.popMatrix();//�ָ�����
    }
    //����ľ����� �����ŵķ���=========================end===============================
    
    //����ͼ��
    public void drawSelf()
    {        
    	if(!initFlag)
    	{
    		//��ʼ����ɫ��        
    		initShader();  
    		initFlag=true;
    	}
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(programId); 
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	MatrixState.pushMatrix();//��������
    	
    	//���﹩������=========================================================
    	 count++;
         MatrixState.translate(vx*count, vy*count, 0);
    	//���﹩������=========================================================
    	
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
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
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	
    	//�����������--�����η�
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND); 
    	
    	MatrixState.popMatrix();//�ָ�����
    }
}
