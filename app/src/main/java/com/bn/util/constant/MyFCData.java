package com.bn.util.constant;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import uk.co.geolib.geolib.C2DPoint;

import com.bn.fastcut.MySurfaceView;
import com.bn.object.BNObject;
import com.bn.util.box2d.Box2DUtil;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class MyFCData {
	//================start========================
	public static String[] gamePicName={"s_01.png","s_02.png",
		"s_03.png","s_04.png","s_05.png","s_06.png"};
	public static String goal[]={"40","30","20","20","20","20"};//Ŀ�����
	//����ԭʼ����
	public static float[][] data={
		{255,826, 74,1216,  520,1626, 1022,548, 689,371,  468,1143},
		{696,288, 114,1072, 422,1156, 316,1716, 978,930, 580,856},
		{536,384, 380,712,  38,772,   289,1034, 230,1392, 540,1224, 844,1392, 788,1028, 1038,772, 694,714},
		{544,320, 112,506,  114,870,  334,970,  110,1066, 108,1434, 542,1618, 968,1434, 966,1066, 754,970, 970,870, 972,506},
		{542,374, 378,828,  52,726, 290,1152,  100,1404,  986,1404, 792,1152, 1032,726, 706,830},
		{290,362, 288,1096, 88,1332, 294,1578, 800,1578, 802,848, 1006,606, 798,362}
	};
	public static boolean[][] dataBool={
		{true,true,true,true,true,true},
		{true,true,true,true,true,true},
		{true,true,false,true,false,true,false,true,true,true},
		{false,false,true,true,true,true,true,true,false,true,true,true},
		{false,true,true,false,true,false,true,true,true},
		{true,false,false,true,true,false,false,true}
	};
	public static float[][] ballData={
		{540,960,50,2.5f,0,1.0f,30,50},
		{550,980,50,1.0f,0,1.0f,20,40}
		};//���������-x,yλ�á��뾶���ܶȡ�Ħ�������ָ�ϵ�����ٶȣ�x��y��
	
	//================end========================��һ��
	
	//��ô�����Χ��Ķ�������
	public static float[] getData(float[] boxData,int i)
	{
		float[] result=new float[4];//��������Ϊ4��һά����
		for(int j=0;j<result.length;j++)
		{
			result[j]=boxData[(2*i+j)%boxData.length];
		}
		return result;//���ؽ��
	}
	
	//������λ��
	public static C2DPoint[] getBallPosition(ArrayList<BNObject> alBNBall)
	{
		C2DPoint[] pointLocation=new C2DPoint[alBNBall.size()];//������ʱλ�õĵ����
		int count=0;
		for(BNObject ball:alBNBall)//���������
		{
			pointLocation[count]=new C2DPoint();
			pointLocation[count].x=ball.ballPositionX;//��x����
			pointLocation[count].y=ball.ballPositionY;//��y����
			count++;
		}
		return pointLocation;
	}
		
	public static ArrayList<Body> getBody(World world,float[] bodydata)//�������������ı߸���
	{
		ArrayList<Body> alBNBody=new  ArrayList<Body>();//��ñ߿��������б�
		for(int i=0;i<bodydata.length/2;i++)//������Χ��
        {
        	float[] data=getData(bodydata,i);
        	Body bd=Box2DUtil.createEdge//����ֱ��
        	(
        		data,
        		world,
        		true,
        		0f,
        		0f,
        		0f,
        		-1
        	);
        	alBNBody.add(bd);
        }
		return alBNBody;
	}
	
	public static ArrayList<BNObject> getPauseView()//���ݾ������ݻ��Object����
	{
		ArrayList<BNObject> pauseView=new ArrayList<BNObject>();
		pauseView.add(
				new BNObject(//������ͣ����
						540, 
						900,
						1000,
						800, 
						TextureManager.getTextures("suspend_bg.png"),
						ShaderManager.getShader(0)
						)
				);
		pauseView.add(
				new BNObject(//����ѡ��ؿ���ť
						300, 
						1000,
						200, 
						200, 
						TextureManager.getTextures("guanQia_a.png"),
						ShaderManager.getShader(0)
						)
				);
		pauseView.add(
				new BNObject(//�������水ť
						550, 
						1000,
						200, 
						200, 
						TextureManager.getTextures("replay_a.png"),
						ShaderManager.getShader(0)
						)
				);
		pauseView.add(
				new BNObject(//���Ƽ�����ť
						800, 
						1000,
						200, 
						200, 
						TextureManager.getTextures("resume_a.png"),
						ShaderManager.getShader(0)
						)
				);
		return pauseView;
	}
	
	public static ArrayList<BNObject> getBall(MySurfaceView mv,World world,float[][] ballBaseData)//�����豻���Ƶ���
	{
		ArrayList<BNObject> alBNBall=new ArrayList<BNObject>();//�����豻���Ƶ�������б�
		for(int i=0;i<ballBaseData.length;i++)
		{
			BNObject bn=Box2DUtil.createCircle//������
					(
							mv,
							ballBaseData[i][0],
							ballBaseData[i][1],
							ballBaseData[i][2],
							world,
							0,
							TextureManager.getTextures("dartsmall.png"),
							ballBaseData[i][3],
							ballBaseData[i][4],
							ballBaseData[i][5],
							-2
					);
			bn.body.setLinearVelocity(new Vec2(ballBaseData[i][6],ballBaseData[i][7]));//��������ٶ�ֵ
			alBNBall.add(bn);//������ӽ������б���
		}
		return alBNBall;
	}
	public static BNObject ChangeLable(float x,float y, float width,float height,String texName)//�л���ť״̬
	{
		BNObject object=new BNObject(
				x, 
				y,
				width, 
				height, 
				TextureManager.getTextures(texName),
				ShaderManager.getShader(0)
				
				);
		return object;
	}
	public static ArrayList<BNObject> WinView()//ʤ������
	{
		ArrayList<BNObject> winView=new ArrayList<BNObject>();
		winView.add(
				new BNObject(//����ʤ������
						540, 
						900,
						1000, 
						1500, 
						TextureManager.getTextures("win.png"),
						ShaderManager.getShader(0)
						)
				);
		winView.add(
				new BNObject(//����ѡ��ؿ���ť
						300, 
						1400,
						200, 
						200, 
						TextureManager.getTextures("guanQia_a.png"),
						ShaderManager.getShader(0)
						)
				);
		winView.add(
				new BNObject(//�������水ť
						550, 
						1400,
						200, 
						200, 
						TextureManager.getTextures("replay_a.png"),
						ShaderManager.getShader(0)
						)
				);
		winView.add(
				new BNObject(//���Ƽ�����ť
						800, 
						1400,
						200, 
						200, 
						TextureManager.getTextures("next_a.png"),
						ShaderManager.getShader(0)
						)
				);
		return winView;
	}
	public static ArrayList<BNObject> getCurrentData(int cur,float x,float y,float width,float height)//��õ�ǰʣ��������������
	{
		String currentArea=cur+"";//��õ�ǰʣ�����
	    ArrayList<BNObject> AreaData=new ArrayList<BNObject>();//ʣ����������ݶ���
	    for(int i=0;i<=currentArea.length()-1;i++)
	    {
	    	String str=currentArea.charAt(i)+"";//�������
	    	int data=Integer.parseInt(str);//ת������Ӧ������
	    	AreaData.add(
	    			new BNObject(//�и����
	    					x+width*i, 
	    					y, 
	    					width, 
	    					height,
	    					TextureManager.getTextures("number.png"),
	    					ShaderManager.getShader(0), 
	    					data
	    				)
	    			);
	    }
		return AreaData;
	}
	
	public static ArrayList<BNObject> getData(int level)//���ԭʼ�������
	{
		String goal=MyFCData.goal[level];//��һ��

	    ArrayList<BNObject> AreaData=new ArrayList<BNObject>();//�����и����
	    for(int i=0;i<=goal.length()-1;i++)
	    {
	    	String str=goal.charAt(i)+"";//�������
	    	int data=Integer.parseInt(str);//ת������Ӧ������
	    	AreaData.add(
	    			new BNObject(//�и����
	    					370+40*i, 
	    					80, 
	    					40, 
	    					50,
	    					TextureManager.getTextures("number.png"),
	    					ShaderManager.getShader(0), 
	    					data
	    					)
	    			);
	    }
		return AreaData;
	}
	public static ArrayList<BNObject> getLableObject()//�����Ϸ�����һЩ����lable
	{
		ArrayList<BNObject> lable=new ArrayList<BNObject>();//����lable������б�
		lable.add(
				new BNObject(//��Ϸ����ͼ
						1080/2,
						1920/2, 
						1080, 
						1920, 
						TextureManager.getTextures("gg.png"),
						ShaderManager.getShader(0)
						)
				);
		lable.add(
				new BNObject(//�и����lable1
						180,
						80, 
						280, 
						100, 
						TextureManager.getTextures("lable1.png"),
						ShaderManager.getShader(0)
						)
				);
		lable.add(
				new BNObject(//Ŀ������İٷֺ�
						480,
						80, 
						80, 
						80, 
						TextureManager.getTextures("lable2.png"),
						ShaderManager.getShader(0)
						)
				);
		lable.add(
				new BNObject(//ʣ��������ݵİٷֺ�
						990,
						80, 
						80, 
						80, 
						TextureManager.getTextures("lable2.png"),
						ShaderManager.getShader(0)
						)
				);
		lable.add(
				new BNObject(//��ͣ��ť
						110,
						1850, 
						135, 
						140, 
						TextureManager.getTextures("zhanting.png"),
						ShaderManager.getShader(0)
						)
				);
		return lable;
	}
}
