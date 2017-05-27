package com.bn.util.manager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.bn.fastcut.MySurfaceView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureManager 
{
	static String[] texturesName=
	{
		"bg_01.png","option_a.png","help_a.png","play_a.png","exit_a.png",//5
		"option_b.png","help_b.png","play_b.png","exit_b.png",
		"choose.png","musicOn.png","soundOn.png",//0-3
		"help.png","tip1.png","point_white.png",//4-6
		"level_bg.jpg","set1_a.png","set2_a.png","set3_a.png","back.png","snow.png",//7-12
		"set1-2.png","s_01_a.png","s_02_a.png",//13-15
		"set2-2.png","s_03_a.png","s_04_a.png",//16-18
		"set3-2.png","s_05_a.png","s_06_a.png",//19-21
		"soundOff.png","musicOff.png",//22-23
		"tip2.png","tip3.png",//24-25
		"s_01.png","s_02.png","s_03.png","s_04.png","s_05.png","s_06.png",//26-31
		"dartsmall.png","lable1.png",//32-33
		"gg.png","lable2.png","number.png","suspend_bg.png","zhanting.png","guanQia_a.png",//34-39
		"guanQia_b.png","replay_a.png","replay_b.png","resume_a.png","resume_b.png",//40-44
		"win.png","next_a.png","next_b.png","light.png","line.png","spark_0.png","spark_1.png","spark_2.png",//45-52
		"spark_3.png","spark_4.png","spark_5.png","spark_6.png","spark_7.png","spark_8.png","spark_9.png",//53-59
		"spark_10.png","spark_11.png","spark_12.png",//60-62
		"set1_b.png","set2_b.png","set3_b.png","tiebuff.png"//63-66
	};//����ͼ������
	
	static boolean[] isRepeat={//����S T��Ľ�ȡ��ʽ
		false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false,false,false,true,
		false,false,false,false,false,false,false,false,false,false,
		false,false,false,false,false,false,false
		};
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();//������ͼ���б�
	public static int initTexture(MySurfaceView mv,String texName,boolean isRepeat)//��������id
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,//����������id������
				textures,//����id������
				0//ƫ����
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//������id
		//����MAGʱΪ���Բ���
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR
		);
		//����MINʱΪ��������
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, 
				GLES20.GL_NEAREST
		);
		if(isRepeat)
		{
			//����S������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_REPEAT
			);
			//����T������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_REPEAT
			);
		}else
		{
			//����S������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_CLAMP_TO_EDGE
			);
			//����T������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_CLAMP_TO_EDGE
			);
		}
		
		String path="pic/"+texName;//����ͼƬ·��
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);//�����м���ͼƬ����
		GLUtils.texImage2D
		(
				GLES20.GL_TEXTURE_2D,//�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
				0,//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmap,//����ͼ��
				0//����߿�ߴ�
		);
		bitmap.recycle();//������سɹ����ͷ��ڴ��е�����ͼ
		return textures[0];
	}
	public static void loadingTexture(MySurfaceView mv,int start,int picNum)//������������ͼ
	{
		for(int i=start;i<start+picNum;i++)
		{
			int texture=initTexture(mv,texturesName[i],isRepeat[i]);
			texList.put(texturesName[i],texture);//�����ݼ��뵽�б���
		}
	}
	public static int getTextures(String texName)//�������ͼ
	{
		int result=0;
		if(texList.get(texName)!=null)//����б����д�����ͼ
		{
			result=texList.get(texName);//��ȡ����ͼ
		}else
		{
			result=-1;
		}
		return result;
	}
}
