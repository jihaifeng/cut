package com.bn.util.constant;

import android.opengl.GLES20;

public class ParticleConstant {
	//����ϵͳ-ѩ===================start==================
		//ѩ�ĳ�ʼ��λ��  
	    public static float distancesFireXZ=0f;
	    public static float[][] positionFireXZ={{0,distancesFireXZ},{0,distancesFireXZ}};
		//��ǰ����  
	    public static int CURR_INDEX=0;
		//��ʼ��ɫ
	    public static final float[][] START_COLOR=
		{
	    	{0.9f,0.9f,0.9f,1.0f},
	    	{0.9f,0.9f,0.9f,1.0f}
		};
	    //��ֹ��ɫ
	    public static final float[][] END_COLOR=
		{
	    	{1.0f,1.0f,1.0f,0.0f},
	    	{1.0f,1.0f,1.0f,0.0f}
		};
	    
	    public static final float[] FLY_START_COLOR=
	    	{
	    		0.9f,0.9f,0.9f,1.0f
	    	};
	    public static final float[] FLY_END_COLOR=
	    	{
	    		1.0f,1.0f,1.0f,0.0f
	    	};
	    
	    //Դ�������
	    public static final int[] SRC_BLEND=
		{
	    	GLES20.GL_SRC_ALPHA,
	    	GLES20.GL_SRC_ALPHA
		};
	    //Ŀ��������
	    public static final int[] DST_BLEND=
		{
	    	GLES20.GL_ONE_MINUS_SRC_ALPHA,
	    	GLES20.GL_ONE_MINUS_SRC_ALPHA
		};
	    //��Ϸ�ʽ
	    public static final int[] BLEND_FUNC=
		{
	    	GLES20.GL_FUNC_ADD,
	    	GLES20.GL_FUNC_ADD
		};
	    //�������Ӱ뾶
	    public static final float[] RADIS=
	   	{
	    	0.018f,
	    	0.013f
	  	};
	    
	    //�������������
	    public static final float[] MAX_LIFE_SPAN=
	    {
	    	4f,
	    	4f
	    };
	    
	    //�����������ڲ���
	    public static final float[] LIFE_SPAN_STEP=
	    {
	    	0.02f,
	    	0.02f
	    };
	    
	    //���ӷ����X���ҷ�Χ
	    public static final float[] X_RANGE=
		{
	    	0.7f,
	    	0.6f
		};
	    
	    //ÿ���緢���������
	    public static final int[] GROUP_COUNT=
		{
	    	1,
	    	2,
	    	3
		};
	    
	    //����Y�������ڵ��ٶ�
	    public static final float[] VY=
		{
	    	-0.005f,
	    	-0.004f
		};
	    //���Ӹ��������߳���Ϣʱ��
	    public static final int[] THREAD_SLEEP=
	    {
	    	15,
	    	15
	    };
		//����ϵͳ-ѩ===================end==================

}
