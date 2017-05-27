package com.bn.fastcut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geopolygons.C2DPolygon;
import com.bn.object.BNObject;
import com.bn.object.BNPolyObject;
import com.bn.object.BNView;
import com.bn.util.box2d.Box2DUtil;
import com.bn.util.box2d.MyContactFilter;
import com.bn.util.constant.Constant;
import com.bn.util.constant.GeoLibUtil;
import com.bn.util.constant.GeometryConstant;
import com.bn.util.constant.MatrixState;
import com.bn.util.constant.MyFCData;
import com.bn.util.constant.ParticleConstant;
import com.bn.util.constant.SwitchIndex;
import com.bn.util.constant.isCutUtil;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.snow.ParticleForDraw;
import com.bn.util.snow.ParticleSystem;
import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import static com.bn.util.constant.Constant.*;

@SuppressLint({ "UseSparseArrays", "ViewConstructor" })
public class MySurfaceView extends GLSurfaceView
{
	private SceneRenderer mRenderer;//������Ⱦ��
	MyActivity activity;
	
	ArrayList<BNObject> alBNBall=new ArrayList<BNObject>();//�����豻���Ƶ�������б�
	ArrayList<BNObject> alFlyPolygon=new ArrayList<BNObject>();//���ߵĶ����
	ArrayList<Body> alBNBody=new ArrayList<Body>();//��ø�������б�
	
	List<ParticleSystem> fps=new ArrayList<ParticleSystem>();//��ѩ������ϵͳ���б�
	HashMap<Integer,ArrayList<BNObject>> GameData=new HashMap<Integer,ArrayList<BNObject>>();//�����Ϸ���������
	ArrayList<ArrayList<BNObject>> alBNPO=new ArrayList<ArrayList<BNObject>>();//����������������
	public ArrayList<Body> BallBody=new ArrayList<Body>();//��������
	
	int FireID[]=new int[13];//��Ż�ID
	public static int CheckpointIndex=0;//����ѡ��ڼ���
	int kniftNum=0;//�и�ĵ���
	int gameTime=0;//��Ϸʱ��
	int tipIndex=1;//����������ʾ�����־λ
	int tempIndex=0;//���ƻ𻨵�����ֵ
	int pauseDegree=0;//��ת�Ľ�����־
    int isAgain=0;//����
    int pauseOne=0;//��ͣ����ֻ����һ��
	int lineIndex=0;//���������м�ľ����ߵı�־
	int chooseIndex=1;//ѡ�ؽ���1--on-on 2--on-off 3--off-on 4--off-off
	
	int beforeArea=100;
	int beforeKnifeNum=1;
	int tieCount=0;
	
	public World world;//��������
	
	public boolean isCutRigid=false;//�Ƿ������ձ�
	boolean isJudgePolygon=true;//�Ƿ�������������ɾ����Χ�������Ӱ�Χ��
	boolean isJudgeBall=true;//�Ƿ�������������ɾ������������������
	boolean isStartGame=false;//�Ƿ�ʼ��Ϸ----����ͼƬ�ɹ��ı�־λ
	boolean isOpen=true;//�Ƿ񲥷�����
	boolean isWorldStep=true;//�Ƿ���������ģ��
	boolean isFly=false;//trueΪ���ߵ�ľ��
	boolean isPause=false;//��ͣ
	boolean isWin=false;//ʤ��
	boolean BackGroundMusic=true;//ѡ����汳�����ֵı�־λ
	boolean SoundEffect=true;//ѡ�����������Ч�ı�־λ
	
	boolean isDrawSnow=false;//����ѩ��
	boolean isLine=false;//�����и���
	boolean isLight=false;//���Ƶ���
	boolean tryAgain=false;//���濪ʼ��Ϸ����ı�־λ
	boolean twinkle=false;//�������Ƿ���˸
	boolean isFirstPause=false;//��ͣ������ת�����ı�־λ
	boolean isPush=false;//�Ƿ�push
	boolean isDrawWin=false;
	boolean isCut=false;//�����ж����
	boolean isOwnAxe=false;
	boolean isCutOne=false;
	
	public float[] intersectPoint=new float[2];//�����߶εĽ���
	float[] cpData;//����εĶ�������
	float AreaSize=0;//��������
	float AllArea=0;//�����
	float tip0_X=540;//���������һ��С��ʾ��x����
    float tip1_X=1240;//��������ڶ���С��ʾ��x����
    float tip2_X=1940;//�������������С��ʾ��x����
    float point_X=430;//��������С�׵��x����
	float x;//��ʼ���µ�x����
	float y;//��ʼ���µ�y����
	
	long gameST=0;//��Ϸ��ʼ��ʱ��
	long pauseTime=0;//��Ϸ��ͣ
	
	ParticleForDraw[] fpfd;//ѩ�����ƵĶ���
	
	BNObject line=null;//0---��     1--����
	BNObject Knifefire;//���ƻ𻨵Ķ���
	BNObject axe;//���Ƹ�ͷ
	
	Object lockA=new Object();//��������
	Object lockB=new Object();//��������
	Object lockC=new Object();//��ѡ�ؽ������
	Object lockD=new Object();//��ʤ���������
	
	BNPolyObject bnpo;
	
	SwitchIndex switchIndex=SwitchIndex.MainFrame;
	public MySurfaceView(MyActivity activity){
		super(activity);
		this.activity=activity;
		this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
		mRenderer = new SceneRenderer();	//����������Ⱦ��
		setRenderer(mRenderer);				//������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(switchIndex)
		{
		case MainFrame://������
			MainTouchTask main=new MainTouchTask();
			main.doTask(e);
			break;
		case OptionFrame://ѡ�����
			OptionTouchTask option=new OptionTouchTask();
			option.doTask(e);
			break;
		case HelpFrame://��������
			HelpTouchTask help=new HelpTouchTask();
			help.doTask(e);
			break;
		case SelectFrame://ѡ�ؽ���
			SelectTouchTask select=new SelectTouchTask();
			select.doTask(e);
			break;
		case FirstLevelFrame://��һ���
			FirstLevelTouchTask first=new FirstLevelTouchTask();
			first.doTask(e);
			break;
		case SecondLevelFrame://�ڶ����
			SecondLevelTouchTask second=new SecondLevelTouchTask();
			second.doTask(e);
			break;
		case ThirdLevelFrame://�������
			ThirdLevelTouchTask third=new ThirdLevelTouchTask();
			third.doTask(e);
			break;
		case GameViewFrame://��Ϸ����
			GameViewTouchTask gameView=new GameViewTouchTask();
			gameView.doTask(e);
			break;
		}
		return true;
	}
	public void HelpTip(int tipIndex,boolean isLeftSliding)//���������л�ͼƬ����
	{
		if(tipIndex==1)//����ǰ��������һ��С��ʾ
		{
			if(isLeftSliding)//�������
			{
				tip0_X=540;
				tip1_X=1240;
				tip2_X=1940;
				point_X=430;
			}else//������һ�
			{
				tip0_X=540;
				tip1_X=1240;
				tip2_X=-160;
				point_X=430;
			}
		}else if(tipIndex==2)//����ǰ�������ڶ���С��ʾ
		{
			tip0_X=-160;
			tip1_X=540;
			tip2_X=1240;
			point_X=515;
		}else if(tipIndex==3)//����ǰ������������С��ʾ
		{
			if(isLeftSliding)//�������
			{
				tip0_X=1240;
				tip1_X=1940;
				tip2_X=540;
				point_X=605;
			}else//������һ�
			{
				tip0_X=1240;
				tip1_X=-160;
				tip2_X=540;
				point_X=605;
			}
		}
		BNObject bn0=new BNObject(tip0_X,1050,700,1150,
				TextureManager.getTextures("tip1.png"),ShaderManager.getShader(0));//��һ����ʾͼ
		BNObject bn1=new BNObject(tip1_X,1050,700,1150,
				TextureManager.getTextures("tip2.png"),ShaderManager.getShader(0));//�ڶ�����ʾͼ
		BNObject bn2=new BNObject(tip2_X,1050,700,1150,
				TextureManager.getTextures("tip3.png"),ShaderManager.getShader(0));//��������ʾͼ
		BNObject bn=new BNObject(point_X,1675,38,38,
    			TextureManager.getTextures("point_white.png"),ShaderManager.getShader(0));//�׵�ͼ
		synchronized(lockC)
		{
			alBNPO.get(2).remove(1);
			alBNPO.get(2).add(1,bn0);
			alBNPO.get(2).remove(2);
			alBNPO.get(2).add(2,bn1);
			alBNPO.get(2).remove(3);
			alBNPO.get(2).add(3,bn2);
			alBNPO.get(2).remove(4);
			alBNPO.get(2).add(4,bn);
		}
	}
	
	public void drawWinBuffer()//����ʤ�����淽��
	{
		if(isDrawWin)
		{
			isWin=true;
			
			ArrayList<BNObject> windata=MyFCData.getCurrentData(getAreaPercent(),625,1100,50,70);//��õ�ǰʣ������
			synchronized(lockB)
			{
				for(int i=4;i<GameData.get(6).size();i++)//ɾ����ǰ������
				{
					GameData.get(6).remove(i);
				}
				for(int i=0;i<windata.size();i++)//����İٷֱ�
				{
					GameData.get(6).add(windata.get(i));
				}
				if(kniftNum<10)
				{
					windata=MyFCData.getCurrentData(kniftNum,680,950,50,80);
				}else if(kniftNum>10&&kniftNum<100)
				{
					windata=MyFCData.getCurrentData(kniftNum,625,950,50,80);
				}else if(kniftNum>100)
				{
					windata=MyFCData.getCurrentData(kniftNum,580,950,50,80);
				}
				for(int i=0;i<windata.size();i++)//�и�ĵ���
				{
					GameData.get(6).add(windata.get(i));
				}
				if(gameTime<10)
				{
					windata=MyFCData.getCurrentData(gameTime,680,800,50,80);
				}else if(gameTime>10&&gameTime<100)
				{
					windata=MyFCData.getCurrentData(gameTime,625,800,50,80);
				}else if(gameTime>100)
				{
					windata=MyFCData.getCurrentData(gameTime,580,800,50,80);
				}
				for(int i=0;i<windata.size();i++)//��Ϸ����ʱ��
				{
					GameData.get(6).add(windata.get(i));
				}
			}
		}
	}
	
	//MainFrame�ڲ���=======================start==================================
	
	class MainTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction()){
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				//ѡ�ť
				if(x>Constant.ChooseButton_Left&&x<Constant.ChooseButton_Right
						&&y>Constant.ChooseButton_Up&&y<Constant.ChooseButton_Down)
				{
					BNObject bn=new BNObject(230,690,260,240,
							TextureManager.getTextures("option_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(1);
						alBNPO.get(0).add(1,bn);
					}
				}
				//��ʼ��ť
				else if(x>Constant.StartButton_Left&&x<Constant.StartButton_Right
						&&y>Constant.StartButton_Up&&y<Constant.StartButton_Down)
				{
					BNObject bn=new BNObject(580,990,440,430,
							TextureManager.getTextures("play_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(4);
						alBNPO.get(0).add(4,bn);
					}
				}
				//�˳���ť
				else if(x>Constant.ExitButton_Left&&x<Constant.ExitButton_Right
						&&y>Constant.ExitButton_Up&&y<Constant.ExitButton_Down)
				{
					BNObject bn=new BNObject(720,1420,220,220,
							TextureManager.getTextures("exit_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(3);
						alBNPO.get(0).add(3,bn);
					}
				}
				//������ť
				else if(x>Constant.HelpButton_Left&&x<Constant.HelpButton_Right
						&&y>Constant.HelpButton_Up&&y<Constant.HelpButton_Down)
				{
					BNObject bn=new BNObject(145,1565,210,220,
							TextureManager.getTextures("help_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(2);
						alBNPO.get(0).add(2,bn);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				//ѡ�ť
				if(x>Constant.ChooseButton_Left&&x<Constant.ChooseButton_Right
						&&y>Constant.ChooseButton_Up&&y<Constant.ChooseButton_Down)
				{
					BNObject bn=new BNObject(230,690,280,260,
							TextureManager.getTextures("option_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(1);
						alBNPO.get(0).add(1,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					//on--on
					if(BackGroundMusic&&SoundEffect)
					{
						chooseIndex=1;//�е�ѡ�ؽ���1
					}else if(BackGroundMusic&&(!SoundEffect))//on--off
					{
						chooseIndex=2;//�е�ѡ�ؽ���2
					}else if((!BackGroundMusic)&&SoundEffect)//off--on
					{
						chooseIndex=3;//�е�ѡ�ؽ���3
					}else//off--off
					{
						chooseIndex=4;//�е�ѡ�ؽ���4
					}
					switchIndex=SwitchIndex.OptionFrame;
				}
				//��ʼ��ť
				else if(x>Constant.StartButton_Left&&x<Constant.StartButton_Right
						&&y>Constant.StartButton_Up&&y<Constant.StartButton_Down)
				{
					BNObject bn=new BNObject(580,990,460,450,
							TextureManager.getTextures("play_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(4);
						alBNPO.get(0).add(4,bn);
					}
					switchIndex=SwitchIndex.SelectFrame;
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					x=0;y=0;
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}
				//�˳���ť
				else if(x>Constant.ExitButton_Left&&x<Constant.ExitButton_Right
						&&y>Constant.ExitButton_Up&&y<Constant.ExitButton_Down)
				{
					BNObject bn=new BNObject(720,1420,240,240,
							TextureManager.getTextures("exit_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(3);
						alBNPO.get(0).add(3,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					System.exit(0);//�˳���Ϸ
				}
				//������ť
				else if(x>Constant.HelpButton_Left&&x<Constant.HelpButton_Right
						&&y>Constant.HelpButton_Up&&y<Constant.HelpButton_Down)
				{
					BNObject bn=new BNObject(145,1565,230,240,
							TextureManager.getTextures("help_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(0).remove(2);
						alBNPO.get(0).add(2,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					switchIndex=SwitchIndex.HelpFrame;
				}
				break;
			}
		}
	}
	
	//MainFrame�ڲ���=======================end==================================
	
	//ѡ������ڲ���==========================start==================================
	
	class OptionTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction()){
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				//����
				if(x>Constant.Choose_Back_Left&&x<Constant.Choose_Back_Right
						&&y>Constant.Choose_Back_Up&&y<Constant.Choose_Back_Down)
				{
					switchIndex=SwitchIndex.MainFrame;
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}//��������
				else if(x>Constant.Choose_Music_Left&&x<Constant.Choose_Music_Right
						&&y>Constant.Choose_Music_Up&&y<Constant.Choose_Music_Down)
				{
					BackGroundMusic=!BackGroundMusic;//��������״̬�÷�
					//on--on
					if(BackGroundMusic&&SoundEffect)
					{
						chooseIndex=1;//�е�ѡ�ؽ���1
					}else if(BackGroundMusic&&(!SoundEffect))//on--off
					{
						chooseIndex=2;//�е�ѡ�ؽ���2
					}else if((!BackGroundMusic)&&SoundEffect)//off--on
					{
						chooseIndex=3;//�е�ѡ�ؽ���3
					}else//off--off
					{
						chooseIndex=4;//�е�ѡ�ؽ���4
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}//������Ч
				else if(x>Constant.Choose_Sound_Left&&x<Constant.Choose_Sound_Right
						&&y>Constant.Choose_Sound_Up&&y<Constant.Choose_Sound_Down)
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					SoundEffect=!SoundEffect;//������Ч״̬�÷�
					//on--on
					if(BackGroundMusic&&SoundEffect)
					{
						chooseIndex=1;//�е�ѡ�ؽ���1
					}else if(BackGroundMusic&&(!SoundEffect))//on--off
					{
						chooseIndex=2;//�е�ѡ�ؽ���2
					}else if((!BackGroundMusic)&&SoundEffect)//off--on
					{
						chooseIndex=3;//�е�ѡ�ؽ���3
					}else//off--off
					{
						chooseIndex=4;//�е�ѡ�ؽ���4
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				//on--on
				if(chooseIndex==1)
				{
					BNObject bn=new BNObject(545,990,600,140,
	            			TextureManager.getTextures("musicOn.png"),ShaderManager.getShader(0));
					BNObject bn1=new BNObject(545,1270,600,140,
	            			TextureManager.getTextures("soundOn.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(1).remove(1);
						alBNPO.get(1).add(1,bn);
						alBNPO.get(1).remove(2);
						alBNPO.get(1).add(2,bn1);
					}
				}else if(chooseIndex==2)
				{
					BNObject bn=new BNObject(545,990,600,140,
	            			TextureManager.getTextures("musicOn.png"),ShaderManager.getShader(0));
					BNObject bn1=new BNObject(545,1270,600,140,
	            			TextureManager.getTextures("soundOff.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(1).remove(1);
						alBNPO.get(1).add(1,bn);
						alBNPO.get(1).remove(2);
						alBNPO.get(1).add(2,bn1);
					}
				}else if(chooseIndex==3)
				{
					BNObject bn=new BNObject(545,990,600,140,
	            			TextureManager.getTextures("musicOff.png"),ShaderManager.getShader(0));
					BNObject bn1=new BNObject(545,1270,600,140,
	            			TextureManager.getTextures("soundOn.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(1).remove(1);
						alBNPO.get(1).add(1,bn);
						alBNPO.get(1).remove(2);
						alBNPO.get(1).add(2,bn1);
					}
				}else if(chooseIndex==4)
				{
					BNObject bn=new BNObject(545,990,600,140,
	            			TextureManager.getTextures("musicOff.png"),ShaderManager.getShader(0));
					BNObject bn1=new BNObject(545,1270,600,140,
	            			TextureManager.getTextures("soundOff.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(1).remove(1);
						alBNPO.get(1).add(1,bn);
						alBNPO.get(1).remove(2);
						alBNPO.get(1).add(2,bn1);
					}
				}
				break;
			}
		}
	}
	
	//ѡ������ڲ���==========================end==================================
	
	//���������ڲ���==============================start=========================
	
	class HelpTouchTask
	{
		void doTask(MotionEvent e)
		{
			boolean isLeftSliding=true;
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				float mxe=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				BNObject bn0=new BNObject(tip0_X-(x-mxe),1050,700,1150,
						TextureManager.getTextures("tip1.png"),ShaderManager.getShader(0));//��һ����ʾͼ
				BNObject bn1=new BNObject(tip1_X-(x-mxe),1050,700,1150,
						TextureManager.getTextures("tip2.png"),ShaderManager.getShader(0));//�ڶ�����ʾͼ
				BNObject bn2=new BNObject(tip2_X-(x-mxe),1050,700,1150,
						TextureManager.getTextures("tip3.png"),ShaderManager.getShader(0));//��������ʾͼ
				synchronized(lockC)
				{
					alBNPO.get(2).remove(1);
					alBNPO.get(2).add(1,bn0);
					alBNPO.get(2).remove(2);
					alBNPO.get(2).add(2,bn1);
					alBNPO.get(2).remove(3);
					alBNPO.get(2).add(3,bn2);
				}
				break;
			case MotionEvent.ACTION_UP:
				float lxe=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				float lye=Constant.fromRealScreenYToStandardScreenY(e.getY());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				//����
				if(lxe>Constant.Help_Back_Left&&lxe<Constant.Help_Back_Right
						&&lye>Constant.Help_Back_Up&&lye<Constant.Help_Back_Down)
				{
					switchIndex=SwitchIndex.MainFrame;
				}
				//��
				if(x-lxe>350)
				{
					isLeftSliding=true;
					if(tipIndex==1)//��ǰ�ǵ�һ����ʾͼ
					{
						tipIndex=2;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==2)//��ǰ�ǵڶ�����ʾͼ
					{
						tipIndex=3;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==3)//��ǰ�ǵ�������ʾͼ
					{
						tipIndex=1;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}
				}else
				{
					isLeftSliding=true;
					if(tipIndex==1)//��ǰ�ǵ�һ����ʾͼ
					{
						tipIndex=1;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==2)//��ǰ�ǵڶ�����ʾͼ
					{
						tipIndex=2;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==3)//��ǰ�ǵ�������ʾͼ
					{
						tipIndex=3;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}
				}
				if(lxe-x>350)//�һ�
				{
					isLeftSliding=false;
					if(tipIndex==1)//��ǰ�ǵ�һ����ʾͼ
					{
						tipIndex=3;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==2)//��ǰ�ǵڶ�����ʾͼ
					{
						tipIndex=1;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==3)//��ǰ�ǵ�������ʾͼ
					{
						tipIndex=2;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}
				}else
				{
					isLeftSliding=false;
					if(tipIndex==1)//��ǰ�ǵ�һ����ʾͼ
					{
						tipIndex=1;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==2)//��ǰ�ǵڶ�����ʾͼ
					{
						tipIndex=2;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}else if(tipIndex==3)//��ǰ�ǵ�������ʾͼ
					{
						tipIndex=3;
						HelpTip(tipIndex,isLeftSliding);//�л�ͼƬ
					}
				}
				break;
			}
		}
	}
	
	//���������ڲ���==============================end=========================
	
	//ѡ�ؽ����ڲ���============================start==========================
	
	class SelectTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(x>=LevelView_Back_Left_X&&x<=LevelView_Back_Right_X&&
				y>=LevelView_Back_Top_Y&&y<=LevelView_Back_Bottom_Y)//������ذ�ť ��ص��׽���
				{
					isDrawSnow=false;
					switchIndex=SwitchIndex.MainFrame;
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}
				if(x>=LevelView_Series1_Left_X&&x<=LevelView_Series1_Right_X&&
						y>=LevelView_Series1_Top_Y&&y<=LevelView_Series1_Bottom_Y)//���ѡ���˵�һС��
				{
					BNObject bn=new BNObject(400,600,650,300,
							TextureManager.getTextures("set1_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(1);
						alBNPO.get(3).add(1,bn);
					}
				}else if(x>=LevelView_Series2_Left_X&&x<=LevelView_Series2_Right_X&&
						y>=LevelView_Series2_Top_Y&&y<=LevelView_Series2_Bottom_Y)//���ѡ���˵ڶ�С��
				{
					BNObject bn=new BNObject(650,1000,650,300,
							TextureManager.getTextures("set2_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(2);
						alBNPO.get(3).add(2,bn);
					}
				}else if(x>=LevelView_Series3_Left_X&&x<=LevelView_Series3_Right_X&&
						y>=LevelView_Series3_Top_Y&&y<=LevelView_Series3_Bottom_Y)//���ѡ���˵���С��
				{
					BNObject bn=new BNObject(400,1400,650,300,
							TextureManager.getTextures("set3_b.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(3);
						alBNPO.get(3).add(3,bn);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(x>=LevelView_Series1_Left_X&&x<=LevelView_Series1_Right_X&&
				y>=LevelView_Series1_Top_Y&&y<=LevelView_Series1_Bottom_Y)//���ѡ���˵�һС��
				{
					isDrawSnow=false;
					switchIndex=SwitchIndex.FirstLevelFrame;
					BNObject bn=new BNObject(400,600,650,300,
							TextureManager.getTextures("set1_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(1);
						alBNPO.get(3).add(1,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}else if(x>=LevelView_Series2_Left_X&&x<=LevelView_Series2_Right_X&&
						y>=LevelView_Series2_Top_Y&&y<=LevelView_Series2_Bottom_Y)//���ѡ���˵ڶ�С��
				{
					isDrawSnow=false;
					switchIndex=SwitchIndex.SecondLevelFrame;
					BNObject bn=new BNObject(650,1000,650,300,
							TextureManager.getTextures("set2_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(2);
						alBNPO.get(3).add(2,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}else if(x>=LevelView_Series3_Left_X&&x<=LevelView_Series3_Right_X&&
						y>=LevelView_Series3_Top_Y&&y<=LevelView_Series3_Bottom_Y)//���ѡ���˵���С��
				{
					isDrawSnow=false;
					switchIndex=SwitchIndex.ThirdLevelFrame;
					BNObject bn=new BNObject(400,1400,650,300,
							TextureManager.getTextures("set3_a.png"),ShaderManager.getShader(0));
					synchronized(lockC)
					{
						alBNPO.get(3).remove(3);
						alBNPO.get(3).add(3,bn);
					}
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}
				break;
			}
		}
	}
	
	//ѡ�ؽ����ڲ���============================end==========================
	
	//��һ�ؿ������ڲ���=========================start========================
	
	class FirstLevelTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(x>=LevelView_Back_Left_X&&x<=LevelView_Back_Right_X&&
				y>=LevelView_Back_Top_Y&&y<=LevelView_Back_Bottom_Y)//������ذ�ť ��ص�ѡ�ؽ���
				{
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					switchIndex=SwitchIndex.SelectFrame;
					setPressSoundEffect("switchpane.ogg");//���Ű�����
				}else if(x>=LevelView_PickUp1_1_Left_X&&x<=LevelView_PickUp1_1_Right_X&&
						y>=LevelView_PickUp1_1_Top_Y&&y<=LevelView_PickUp1_1_Bottom_Y)//ѡ���һ��صĵ�һС��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=0;//1-1
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}else if(x>=LevelView_PickUp1_2_Left_X&&x<=LevelView_PickUp1_2_Right_X&&
						y>=LevelView_PickUp1_2_Top_Y&&y<=LevelView_PickUp1_2_Bottom_Y)//ѡ���һ��صĵڶ�С��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=1;//1-2
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}
				break;
			}
		}
	}
	
	//��һ�ؿ������ڲ���=========================start========================
	
	//�ڶ��ؿ������ڲ���=========================start========================
	
	class SecondLevelTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(x>=LevelView_Back_Left_X&&x<=LevelView_Back_Right_X&&
				y>=LevelView_Back_Top_Y&&y<=LevelView_Back_Bottom_Y)//������ذ�ť ��ص�ѡ�ؽ���
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					switchIndex=SwitchIndex.SelectFrame;
				}else if(x>=LevelView_PickUp2_1_Left_X&&x<=LevelView_PickUp2_1_Right_X&&
						y>=LevelView_PickUp2_1_Top_Y&&y<=LevelView_PickUp2_1_Bottom_Y)//ѡ��ڶ���صĵ�һС��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=2;//2-1
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}else if(x>=LevelView_PickUp2_2_Left_X&&x<=LevelView_PickUp2_2_Right_X&&
						y>=LevelView_PickUp2_2_Top_Y&&y<=LevelView_PickUp2_2_Bottom_Y)//ѡ��ڶ���صĵڶ�С��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=3;//2-2
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}
				break;
			}
		}
	}
	//�ڶ��ؿ������ڲ���=========================end========================
	
	//�����ؿ������ڲ���=========================start======================
	
	class ThirdLevelTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(x>=LevelView_Back_Left_X&&x<=LevelView_Back_Right_X&&
				y>=LevelView_Back_Top_Y&&y<=LevelView_Back_Bottom_Y)//������ذ�ť ��ص�ѡ�ؽ���
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					switchIndex=SwitchIndex.SelectFrame;
				}else if(x>=LevelView_PickUp3_1_Left_X&&x<=LevelView_PickUp3_1_Right_X&&
						y>=LevelView_PickUp3_1_Top_Y&&y<=LevelView_PickUp3_1_Bottom_Y)//ѡ�������صĵ�һС��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=4;//3-1
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}else if(x>=LevelView_PickUp3_2_Left_X&&x<=LevelView_PickUp3_2_Right_X&&
						y>=LevelView_PickUp3_2_Top_Y&&y<=LevelView_PickUp3_2_Bottom_Y)//ѡ�������صĵڶ�С��
				{
					setPressSoundEffect("switchpane.ogg");//���Ű�����
					CheckpointIndex=5;//3-2
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					switchIndex=SwitchIndex.GameViewFrame;
				}
				break;
			}
		}
	}
	//�����ؿ������ڲ���=========================end========================
	
	//��Ϸ�����ڲ���=======================start========================
	class GameViewTouchTask
	{
		void doTask(MotionEvent e)
		{
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(isPause&&x>Constant.ChooseLevel_Left&&x<Constant.ChooseLevel_Right&&y>Constant.ChooseLevel_Top&&y<Constant.ChooseLevel_Buttom)
				{//��ͣ���� ѡ��ؿ�
					BNObject object=MyFCData.ChangeLable(300, 1000,200, 200, "guanQia_b.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(1);
						GameData.get(5).add(1,object);
					}
				}
				else if(isPause&&x>Constant.ReStart_Left&&x<Constant.ReStart_Right&&y>Constant.ReStart_Top&&y<Constant.ReStart_Buttom)
				{//��ͣ���� ����
					BNObject object=MyFCData.ChangeLable(550,1000,200,200, "replay_b.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(2);
						GameData.get(5).add(2,object);
					}
				}
				else if(isPause&&x>Constant.Continue_Left&&x<Constant.Continue_Right&&y>Constant.Continue_Top&&y<Constant.Continue_Buttom)
				{//��ͣ���� ����
					BNObject object=MyFCData.ChangeLable(800,1000,200,200, "resume_b.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(3);
						GameData.get(5).add(3,object);
					}
				}
				else if(isWin&&x>Constant.WinChooseLevel_Left&&x<Constant.WinChooseLevel_Right&&y>Constant.WinChooseLevel_Top&&y<Constant.WinChooseLevel_Buttom)
				{//ʤ������ ѡ��ؿ�
					BNObject object=MyFCData.ChangeLable(300, 1400,200, 200, "guanQia_b.png");
					synchronized(lockB)
					{
						GameData.get(6).remove(1);
						GameData.get(6).add(1,object);
					}
				}
				else if(isWin&&x>Constant.WinReStart_Left&&x<Constant.WinReStart_Right&&y>Constant.WinReStart_Top&&y<Constant.WinReStart_Buttom)
				{//ʤ������  ����
					BNObject object=MyFCData.ChangeLable(550,1400,200,200, "replay_b.png");
					synchronized(lockB)
					{
						GameData.get(6).remove(2);
						GameData.get(6).add(2,object);
					}
				}
				else if(isWin&&x>Constant.WinNext_Left&&x<Constant.WinNext_Right&&y>Constant.WinNext_Top&&y<Constant.WinNext_Buttom)
				{//ʤ������  ��һ��
					BNObject object=MyFCData.ChangeLable(800,1400,200,200, "next_b.png");
					
					synchronized(lockB)
					{
						GameData.get(6).remove(3);
						GameData.get(6).add(3,object);
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Knifefire=null;
				isCutRigid=false;
    			if(!isPause&&!isWin&&!isCut)
    			{
    				float mxe=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
        			float mye=Constant.fromRealScreenYToStandardScreenY(e.getY());
    				if(Math.abs(x-mxe)<=10||Math.abs(y-mye)<=10)//������
        			{
        				isLine=false;//������
        				isCutRigid=false;
        			}else
        			{
        				line=new BNObject(x,y,mxe,mye, 
            					TextureManager.getTextures("line.png"),//����ͼƬ����
            					ShaderManager.getShader(0),true,0);
            			isLine=true;//�����и���
        			}
    			}
				break;	
			case MotionEvent.ACTION_UP:
				Knifefire=null;
				isCutRigid=false;//û���е��ձ�
				line=null;//ɾ���и��ߵĶ���
	    		isLine=false;//ֹͣ������
				float lxe=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				float lye=Constant.fromRealScreenYToStandardScreenY(e.getY());
				
				if(!isCutUtil.isCutPolygon(MySurfaceView.this,GameData.get(2).get(0).cp, x, y, lxe, lye)&&!isCut)
				{
					if(isCutRigid)//�Ƿ��е��˸ձ�
					{
						synchronized(lockA)
						{
							Knifefire=new BNObject(intersectPoint[0],intersectPoint[1],150,150,
									TextureManager.getTextures("spark_1.png"),//����ͼƬ����
									ShaderManager.getShader(0));//�����𻨶���
							isLine=false;//�����и���
						}
						setPressSoundEffect("peng.ogg");						
					}
				}
				
				if(!isCut&&(pauseOne==0)&&lxe>Constant.PauseLable_Left&&lxe<Constant.PauseLable_Right&&lye>Constant.PauseLable_Top&&lye<Constant.PauseLable_Buttom)
				{//������ͣ����
					pauseOne=1;//��ͣ����ֻ����һ��
					setPressSoundEffect("click.ogg");			
					isPause=true;
					isFirstPause=true;//��ͣ������ת����
					synchronized(lockA)
					{
						alFlyPolygon.clear();//��շ��ߵĶ�����б�
					}
					pauseTime=System.currentTimeMillis();
				}
				if(isPause&&lxe>Constant.ChooseLevel_Left&&lxe<Constant.ChooseLevel_Right&&lye>Constant.ChooseLevel_Top&&lye<Constant.ChooseLevel_Buttom)
				{//��ͣ����  ѡ��ؿ�
					pauseOne=0;//���������ͣ����
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					setPressSoundEffect("click.ogg");
					switchIndex=SwitchIndex.SelectFrame;
					BNObject object=MyFCData.ChangeLable(300, 1000,200, 200, "guanQia_a.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(1);
						GameData.get(5).add(1,object);
					}
				}
				else if(isPause&&lxe>Constant.ReStart_Left&&lxe<Constant.ReStart_Right&&lye>Constant.ReStart_Top&&lye<Constant.ReStart_Buttom)
				{//��ͣ����  ����
					pauseOne=0;//���������ͣ����
					setPressSoundEffect("click.ogg");
					switchIndex=SwitchIndex.GameViewFrame;
					tryAgain=true;//����
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					BNObject object=MyFCData.ChangeLable(550,1000,200,200, "replay_a.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(2);
						GameData.get(5).add(2,object);
					}
				}
				else if(isPause&&lxe>Constant.Continue_Left&&lxe<Constant.Continue_Right&&lye>Constant.Continue_Top&&lye<Constant.Continue_Buttom)
				{//��ͣ����  ����
					pauseOne=0;//���������ͣ����
					setPressSoundEffect("click.ogg");
					if(pauseTime<System.currentTimeMillis())
					{
						pauseTime=System.currentTimeMillis()-pauseTime;
					}
					isPause=false;
					switchIndex=SwitchIndex.GameViewFrame;
					BNObject object=MyFCData.ChangeLable(800,1000,200,200, "resume_a.png");
					synchronized(lockB)
					{
						GameData.get(5).remove(3);
						GameData.get(5).add(3,object);
					}
				}
				else if(isWin&&lxe>Constant.WinChooseLevel_Left&&lxe<Constant.WinChooseLevel_Right&&lye>Constant.WinChooseLevel_Top&&lye<Constant.WinChooseLevel_Buttom)
				{//ʤ������  ѡ��ؿ�
					isDrawSnow=true;
					initSnow();//��ʼ��ѩ������
					setPressSoundEffect("click.ogg");
					switchIndex=SwitchIndex.SelectFrame;
					BNObject object=MyFCData.ChangeLable(300, 1400,200, 200, "guanQia_a.png");
					synchronized(lockB)
					{
						GameData.get(6).remove(1);
						GameData.get(6).add(1,object);
					}
				}
				else if(isWin&&lxe>Constant.WinReStart_Left&&lxe<Constant.WinReStart_Right&&lye>Constant.WinReStart_Top&&lye<Constant.WinReStart_Buttom)
				{//ʤ������  ����
					setPressSoundEffect("click.ogg");
					switchIndex=SwitchIndex.GameViewFrame;
					tryAgain=true;//����
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					BNObject object=MyFCData.ChangeLable(550,1400,200,200, "replay_a.png");
					synchronized(lockB)
					{
						GameData.get(6).remove(2);
						GameData.get(6).add(2,object);
					}
				}
				else if(isWin&&lxe>Constant.WinNext_Left&&lxe<Constant.WinNext_Right&&lye>Constant.WinNext_Top&&lye<Constant.WinNext_Buttom)
				{//ʤ������  ��һ��
					if(CheckpointIndex>=5)
					{
						CheckpointIndex=0;
					}else
					{
						CheckpointIndex++;
					}
					isLine=true;
					initGameView();//��ʼ����Ϸ����
					gameST=System.currentTimeMillis();
					BNObject object=MyFCData.ChangeLable(800,1400,200,200, "next_a.png");
					synchronized(lockB)
					{
						GameData.get(6).remove(3);
						GameData.get(6).add(3,object);
					}
				}
				if(!isPause&&!isWin&&!isCut&&x!=lxe&&y!=lye)
				{
					//�ж��Ƿ񻮵��˶����
					if(!isCutUtil.isCutPolygon(MySurfaceView.this,GameData.get(2).get(0).cp, x, y, lxe, lye))
					{
						return;
					}
					line=null;
					line=new BNObject(x,y,lxe,lye, 
							TextureManager.getTextures("light.png"),//����ͼƬ����
							ShaderManager.getShader(0),true,1);
					isLight=true;//��ʼ���Ƶ���
					kniftNum++;//�е�ľ��ĵ���
					//����зֺ�ľ����ϲ��Ȳ����Ķ�����б�
					ArrayList<C2DPolygon> lastPolygons=isCutUtil.getCutPolysArrayList(MySurfaceView.this,GameData.get(2), x, y, lxe, lye);
					//�ж��ֻ�����������Ҫȥ���Ĳ��ֲ�������Χ��
					C2DPoint[] pointLocation=MyFCData.getBallPosition(alBNBall);//��ø������λ��
					//�ж��и��ߵľ����Ƿ�С����İ뾶===================start==========================
					for(int i=0;i<pointLocation.length;i++)
					{
						//�и��ߵľ������С����İ뾶
						if(GeometryConstant.lengthPointToLine((float)pointLocation[i].x,(float)pointLocation[i].y, x, y, lxe, lye)<=50)
						{
							setPressSoundEffect("gamefail.ogg");//������Ϸʧ�ܵ�����
							tryAgain=true;
							initGameView();//��ʼ����Ϸ����
							gameST=System.currentTimeMillis();
							return;//ֱ�ӷ���
						}
					}
					//�ж��и��ߵľ����Ƿ�С����İ뾶===================end==========================
					synchronized(lockA)
					{
						alFlyPolygon.clear();//�����ߵ������б����
					}
					boolean isPlayWin=false;//���ųɹ��е�ľ��
					
					for(C2DPolygon cp:lastPolygons)//����C2DPolygon����
					{
						int kk=0;
						for(int i=0;i<pointLocation.length;i++)
						{
							//�ö�������Ƿ�����
							if(cp.Contains(pointLocation[i]))
							{
								kk++;
							}
						}
						if(kk==alBNBall.size())//����ö������������������
						{
							isPlayWin=true;
							AreaSize=(float)cp.GetArea();//��ö���ε����
							
							cpData=GeoLibUtil.fromC2DPolygonToVData(cp);
							isJudgePolygon = true;//����ɾ�����ߴ�����Χ��
							isJudgeBall=false;//������ɾ�����ߴ�����
							
							GeometryConstant.judgeDirection(cp,x, y, lxe, lye);//�ж϶����ľ����ߵķ���
							ArrayList<BNObject> bnObject=new ArrayList<BNObject>();
							BNPolyObject bnpo=new BNPolyObject//��������α���ͼ
									(
											MySurfaceView.this,
											TextureManager.getTextures(MyFCData.gamePicName[CheckpointIndex]),//����ͼƬ����
											ShaderManager.getShader(0),//����ID
											GeoLibUtil.fromC2DPolygonToVData(cp),//��������
											1080,//ԭʼͼ�εĿ�
											1920,//ԭʼͼ�εĸ�
											0,
											0
									);
							bnObject.add(bnpo);
							
							GameData.remove(2);//����豻���Ƶ�BNPolyObject�����б�
							GameData.put(2, bnObject);
							
							bnObject=new ArrayList<BNObject>();
							bnObject=MyFCData.getCurrentData(getAreaPercent(),850,80,40,50);//��õ�ǰʣ������
							GameData.remove(3);//�����ʾʣ��������ݵ��б�
							GameData.put(3, bnObject);//��ӽ��б�
							
							//��ø�ͷ����===============start====================
							if(CheckpointIndex>=4)
							{
								if(kniftNum==1)
								{
									beforeArea=100;
								}
								if(beforeArea-getAreaPercent()>25)
								{
									System.out.println("25==========================");
									beforeKnifeNum=kniftNum;
									axe=new BNObject(540,960,200,200,TextureManager.getTextures("tiebuff.png"),
											ShaderManager.getShader(0));
									for(int i=0;i<MyFCData.dataBool[CheckpointIndex].length;i++)
									{
										MyFCData.dataBool[CheckpointIndex][i]=true;
									}
									tieCount=0;
									isOwnAxe=true;
								}
								beforeArea=getAreaPercent();
								if(kniftNum-beforeKnifeNum==1)
								{
									isOwnAxe=false;
									isCutOne=true;
									if(CheckpointIndex==4)
									{
										for(int i=0;i<MyFCData.dataBool[CheckpointIndex].length;i++)
										{
											if(i==0||i==3||i==5)
											{
												MyFCData.dataBool[CheckpointIndex][i]=false;
											}else
											{
												MyFCData.dataBool[CheckpointIndex][i]=true;
											}
										}
									}else
									{
										for(int i=0;i<MyFCData.dataBool[CheckpointIndex].length;i++)
										{
											if(i==1||i==2||i==5||i==6)
											{
												MyFCData.dataBool[CheckpointIndex][i]=false;
											}else
											{
												MyFCData.dataBool[CheckpointIndex][i]=true;
											}
										}
									}
								}
							}
							//��ø�ͷ����===============end====================
							
							
							int goal=Integer.parseInt(MyFCData.goal[CheckpointIndex]);//Ŀ�����
							if(goal>=getAreaPercent())//ʤ��
							{
								isCut=true;
								gameTime=(int)((System.currentTimeMillis()-gameST-pauseTime)/1000);//�����Ϸʱ��
								if(gameTime<1)//���ʱ��С��1��  ��Ĭ��Ϊ1��
								{
									gameTime=1;
								}
								//��Ϸʤ������ ʹ����ͣ����======================start=====================
								deleteBall();
								float[][] ballBaseData={
										{(float)pointLocation[0].x,(float)pointLocation[0].y,50,2.5f,0.8f,0.35f,30,50},
										{(float)pointLocation[1].x,(float)pointLocation[1].y,50,1f,0.8f,0.35f,20,40}
								};
								synchronized(lockD)
								{
									alBNBall.clear();
									addBall(ballBaseData);
								}
								//��Ϸʤ������ ʹ����ͣ����======================end=====================
								setPressSoundEffect("gamesucc.ogg");
							}
						}else if(kk==0)//ľ�����
						{
							BNPolyObject bnfly=new BNPolyObject//��������α���ͼ
									(
											MySurfaceView.this,
											TextureManager.getTextures(MyFCData.gamePicName[CheckpointIndex]),//����ͼƬ����
											ShaderManager.getShader(2),//����ID
											GeoLibUtil.fromC2DPolygonToVData(cp),//��������
											1080,//ԭʼͼ�εĿ�
											1920,//ԭʼͼ�εĸ�
											0.005f,
											-0.005f
									);
							synchronized(lockA)
							{
								alFlyPolygon.add(bnfly);
							}
							continue;
						}else if(kk==1)
						{
							twinkle=true;//����˸��
							lineIndex=0;
							line=new BNObject(x,y,lxe,lye, 
		        					TextureManager.getTextures("line.png"),//����ͼƬ����
		        					ShaderManager.getShader(0),true,0);
		        			isLine=true;//�����и���
						}
					}
					if(isPlayWin)//����ɹ��е�ľ�� �򲥷ųɹ�������
					{
						setPressSoundEffect("cut.ogg");
					}
					//�ж��ֻ�����������Ҫȥ���Ĳ��ֲ�������Χ��==============end====================
				}
				isLight=false;//ֹͣ���Ƶ���
				break;
			}
		}
	}
	//��Ϸ�����ڲ���==============================end====================================
	
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		public void onDrawFrame(GL10 gl) 
		{
			drawWinBuffer();//��ʤ��������л���
			//�����Ȼ�������ɫ����
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    		//��ʼ������
			if(!isStartGame)
    		{
    			initArrayList();
    		}
			if(BackGroundMusic&&!(switchIndex.equals(SwitchIndex.GameViewFrame))&&(!isOpen))
			{
				activity.sm.StartBackGroundSound();//������������
				isOpen=true;
			}
			if(isOpen&&(switchIndex.equals(SwitchIndex.GameViewFrame)||(!BackGroundMusic)))
			{
				activity.sm.EndBackGroundSound();//�رձ�������
				isOpen=false;
			}
			if(switchIndex.equals(SwitchIndex.GameViewFrame))
			{//������Ϸ����
				drawGameView();
			}else{
				//���ƿ�ʼ�������ѡ�ؽ���
				drawFirstView();
			}
			if(isCutRigid&&Knifefire!=null)//���ƻ�
			{
				drawFire();
			}
			if(isOwnAxe)//���Ƹ�ͷ
			{
				if(tieCount>150)
				{
					isCut=false;//���������
					axe=null;
					axe=new BNObject(980,500,120,120,TextureManager.getTextures("tiebuff.png"),
							ShaderManager.getShader(0));
					axe.drawSelf();
				}else
				{
					isCut=true;//�����������
					tieCount++;
					MatrixState.pushMatrix();//��������
					MatrixState.translate(tieCount*0.003f, tieCount*0.003f, 0);
					MatrixState.scale((1f-tieCount*0.002f), (1f-tieCount*0.002f), 1);
					axe.drawSelf();
					MatrixState.popMatrix();//��������
				}
			}
			if(!isOwnAxe&&isCutOne)//ʹ���˸�ͷ
			{
				tieCount++;
				MatrixState.pushMatrix();//��������
				MatrixState.translate(-tieCount*0.0005f, -tieCount*0.0005f, 0);
				MatrixState.scale(1+tieCount*0.001f, 1+tieCount*0.001f, 1);
				axe.drawSelf();
				MatrixState.popMatrix();//��������
				if(tieCount>300)
				{
					isCutOne=false;
				}
			}
		}
		public void onSurfaceChanged(GL10 gl, int width, int height){
			//�����Ӵ���С��λ��
			GLES20.glViewport
			(
					(int)Constant.ssr.lucX,//x
					(int)Constant.ssr.lucY,//y
					(int)(Constant.StandardScreenWidth*Constant.ssr.ratio),//width
					(int)(Constant.StandardScreenHeight*Constant.ssr.ratio)//height
			);
			//����GLSurfaceView�Ŀ�߱�
			float ratio = (float) width / height;
			//���ô˷����������͸��ͶӰ����
			MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
			//���ô˷������������9����λ�þ���
			MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
			//��ʼ���任����
			MatrixState.setInitStack();
			//��ʼ����Դλ��   
            MatrixState.setLightLocation(0, 15, 0);
		}
		public void onSurfaceCreated(GL10 gl, EGLConfig config) 
		{
			Vec2 gravity = new Vec2(0.0f,0.0f);
    		world = new World(gravity);//����World�����
    		world.setAllowSleep(true);//����ֹ��������
    		//�����������ײ���������
    		world.setContactFilter(new MyContactFilter(MySurfaceView.this));
			//��ʼ����ɫ��
			ShaderManager.loadingShader(MySurfaceView.this);		
			//������Ļ����ɫRGBA
			GLES20.glClearColor(0,0,0, 0);
			if(!switchIndex.equals(SwitchIndex.GameViewFrame))
    		{
    			activity.sm.StartBackGroundSound();
    		}
		}
	}
	//��ʼ�����ֽ���
	int step=1;
	public void initArrayList()
	{
		if(step==8)
    	{
    		initStepEight();//��ͼƬ
    		isStartGame=true;
    		return;
    	}
    	else if(step==1)
    	{
    		initStepOne();//0�׽���
        	step++;
    	}else if(step==2)
    	{
    		initStepTwo();//1ѡ�����--on--on
        	step++;
    	}else if(step==3)
    	{
    		initStepThree();//2��������--��1��
        	step++;
    	}else if(step==4)
    	{
    		initStepFour();//��һ���ѡ�ؽ���
    		step++;
    	}else if(step==5)
    	{
    		initStepFive();//��һС��ѡ�ؽ���
    		step++;
    	}else if(step==6)
    	{
    		initStepSix();//�ڶ�С��ѡ�ؽ���
    		step++;
    	}else if(step==7)
    	{
    		initStepSeven();//����С��ѡ�ؽ���
    		step++;
    	}
	}
	//��ʼ����������ķ���
	ArrayList<BNObject> tempArrayList=new ArrayList<BNObject>();
	BNView bn;
	//�׽���
	public void initStepOne()
	{
		TextureManager.loadingTexture(MySurfaceView.this,0,9);
    	//0�׽���
    	bn=new BNView(540,960,1080,1920,
    			TextureManager.getTextures("bg_01.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(230,690,280,260,
    			TextureManager.getTextures("option_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(145,1565,230,240,
    			TextureManager.getTextures("help_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(720,1420,240,240,
    			TextureManager.getTextures("exit_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(580,990,460,450,
    			TextureManager.getTextures("play_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	alBNPO.add(0, tempArrayList);
	}
	//ѡ�����--on--on
	public void initStepTwo()
	{
		TextureManager.loadingTexture(MySurfaceView.this,9,3);
		TextureManager.loadingTexture(MySurfaceView.this,30,2);
    	//1ѡ�����--on--on
    	tempArrayList=new ArrayList<BNObject>();
    	bn=new BNView(540,960,1080,1920,
    			TextureManager.getTextures("choose.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(545,990,600,140,
    			TextureManager.getTextures("musicOn.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(545,1270,600,140,
    			TextureManager.getTextures("soundOn.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	alBNPO.add(1, tempArrayList);
	}
	//��������--��1��
	public void initStepThree()
	{
		TextureManager.loadingTexture(MySurfaceView.this,12,3);
		TextureManager.loadingTexture(MySurfaceView.this,32,2);
       	//2��������--��1��
    	tempArrayList=new ArrayList<BNObject>();
    	bn=new BNView(540,960,1080,1920,
    			TextureManager.getTextures("help.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(540,1050,700,1150,
    			TextureManager.getTextures("tip1.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	//������==========================start===============================
    	bn=new BNView(1240,1050,700,1150,
				TextureManager.getTextures("tip2.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		
		bn=new BNView(1940,1050,700,1150,
				TextureManager.getTextures("tip3.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		
		//������==========================end===============================
    	bn=new BNView(430,1675,38,38,
    			TextureManager.getTextures("point_white.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	alBNPO.add(2, tempArrayList);
	}
	//��һ���ѡ�ؽ���
	public void initStepFour()
	{
		TextureManager.loadingTexture(MySurfaceView.this,15,6);
    	tempArrayList=new ArrayList<BNObject>();
    	//3���ص�һ���ѡ�ؽ��������===================start==========================
    	bn=new BNView(540,960,1080,1920,
    			TextureManager.getTextures("level_bg.jpg"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(400,600,650,300,
    			TextureManager.getTextures("set1_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(650,1000,650,300,
    			TextureManager.getTextures("set2_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(400,1400,650,300,
    			TextureManager.getTextures("set3_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(900,150,200,150,
    			TextureManager.getTextures("back.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	alBNPO.add(3, tempArrayList);
    	//���ص�һ���ѡ�ؽ��������===================end==========================
	}
	//��һС��ѡ�ؽ���
	public void initStepFive()
	{
		TextureManager.loadingTexture(MySurfaceView.this,21,3);
    	tempArrayList=new ArrayList<BNObject>();
    	//4���ص�һС��ѡ�ؽ��������===================start==========================
    	bn=new BNView(540,960,1080,1920,
    			TextureManager.getTextures("set1-2.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(400,700,250,400,
    			TextureManager.getTextures("s_01_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(640,1230,250,400,
    			TextureManager.getTextures("s_02_a.png"),ShaderManager.getShader(0));
    	tempArrayList.add(bn.getBNObject());
    	bn=new BNView(900,150,200,150,
				TextureManager.getTextures("back.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		alBNPO.add(4, tempArrayList);
		//���ص�һС��ѡ�ؽ��������===================end==========================
	}
	//�ڶ�С��ѡ�ؽ���
	public void initStepSix()
	{
		TextureManager.loadingTexture(MySurfaceView.this,24,3);
		tempArrayList=new ArrayList<BNObject>();
		//5���صڶ�С��ѡ�ؽ��������===================start==========================
		bn=new BNView(540,960,1080,1920,
				TextureManager.getTextures("set2-2.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		bn=new BNView(670,800,300,480,
				TextureManager.getTextures("s_03_a.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
        bn=new BNView(380,1380,250,400,
				TextureManager.getTextures("s_04_a.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
        bn=new BNView(900,150,200,150,
				TextureManager.getTextures("back.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		alBNPO.add(5, tempArrayList);
		//���صڶ�С��ѡ�ؽ��������===================end==========================
	}
	//����С��ѡ�ؽ���
	public void initStepSeven()
	{
		TextureManager.loadingTexture(MySurfaceView.this,27,3);
  		tempArrayList=new ArrayList<BNObject>();
		//6���ص���С��ѡ�ؽ��������===================start==========================
		bn=new BNView(540,960,1080,1920,
				TextureManager.getTextures("set3-2.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		bn=new BNView(400,800,300,480,
				TextureManager.getTextures("s_05_a.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		bn=new BNView(680,1400,250,400,
				TextureManager.getTextures("s_06_a.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		bn=new BNView(900,150,200,150,
				TextureManager.getTextures("back.png"),ShaderManager.getShader(0));
		tempArrayList.add(bn.getBNObject());
		alBNPO.add(6, tempArrayList);
		//���ص���С��ѡ�ؽ��������===================end==========================
	}
	//��ͼƬ
	public void initStepEight()
	{
		TextureManager.loadingTexture(MySurfaceView.this,34,41);
		for(int i=0;i<=12;i++)
		{
			FireID[i]=TextureManager.getTextures("spark_"+i+".png");
		}
	}
	//���ƻ�
	public void drawFire()
	{
		synchronized(lockA)
		{
			Knifefire.drawSelf(FireID[tempIndex]);//���ƻ�
			tempIndex++;
		}
		if(tempIndex==13)//�������һ��ͼƬʱ
		{
			tempIndex=0;
			isCutRigid=false;//�Ƿ��е��ձߵı�־λ��Ϊfalse
			Knifefire=null;
		}
		}
	//����ʤ������
	public void drawWinView()
	{
		synchronized(lockB)
		{
			for(BNObject win:GameData.get(6))//����ʤ������
			{
				win.drawSelf();
			}
		}
	}
	//���ƿ�ʼ�������ѡ�ؽ���
	public void drawFirstView()
	{
		synchronized(lockC)
		{
			if(switchIndex.equals(SwitchIndex.HelpFrame))
			{
				for(int i=0;i<alBNPO.get(switchIndex.ordinal()).size();i++)
				{
					if(i==1||i==2||i==3)
					{
						GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
						GLES20.glScissor(
								(int)Constant.fromStandardScreenXToRealScreenX(190), 
								(int)Constant.fromStandardScreenXToRealScreenX(300),
								(int)Constant.fromStandardScreenSizeToRealScreenSize(700),
								(int)Constant.fromStandardScreenSizeToRealScreenSize(1150)
								);
						alBNPO.get(switchIndex.ordinal()).get(i).drawSelf();
					}else
					{
						GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
						alBNPO.get(switchIndex.ordinal()).get(i).drawSelf();
					}
				}
			}else
			{
				for(BNObject bn:alBNPO.get(switchIndex.ordinal()))
				{
					bn.drawSelf();
				}
			}
		}
		if(isDrawSnow)//����ѩ��
		{
			for(int i=0;i<fps.size();i++)
			{
				fps.get(i).drawSelf();
			}
		}
	}
	//������Ϸ����
	public void drawGameView()
	{
		if(tryAgain)//����
		{
			MatrixState.pushMatrix();//��������
			isPush=true;//push��Ϊtrue
	    	MatrixState.scale(0.02f*isAgain,0.02f*isAgain, 1);//����
	    	isAgain++;
		}
		synchronized(lockB)
		{
			for(int i=1;i<=4;i++)
			{
				if(GameData.get(i)!=null)
				{
					for(BNObject data:GameData.get(i))//������Ϸ��������ݶ���
					{
						data.drawSelf();//����
					}
				}
			}
		}
		synchronized(lockD)
		{
			for(BNObject ball:alBNBall)//�����������б�
			{
				ball.drawSelf();//������
				if(Math.abs(ball.body.getLinearVelocity().x)<4f&&Math.abs(ball.body.getLinearVelocity().y)<4f)
				{
					isDrawWin=true;
				}else
				{
					isDrawWin=false;
				}
			}
		}
		synchronized(lockA)
		{
			for(BNObject fly:alFlyPolygon)
			{
				fly.drawSelf(1.0f);//���ߵ�ľ��
			}
		}
		if((isLine||isLight)&&line!=null)//�����и���
		{
			drawCutLine();
		}
		if(isPause)//������ͣ����
		{
			drawPauseView();
		}
		if(isDrawWin&&isWin)//����ʤ������
		{
			drawWinView();
		}
		isWorldStep=worldStep();//һֱ�����ж��Ƿ����ģ��
		if(isWorldStep)//�������ģ��
		{
			if(!isPause&&!isWin)//��������ͣ�������ʤ������
			{
				world.step(TIME_STEP, Vec_ITERA,POSITON_ITERA);//��ʼģ��
			}
		}else
		{
			deletePolygon();//������������ɾ����Χ��
			addPolygon(cpData);//��������������Ӱ�Χ��
			if(isJudgeBall)
			{
				deleteBall();//������������ɾ����
				addBall(MyFCData.ballData);//�����������������
				isJudgeBall=false;//����־λ��Ϊfalse
			}
			isJudgePolygon=false;//����־λ��Ϊfalse
		}
		if(isPush)//���push�Ļ�
		{
			MatrixState.popMatrix();//����pop
			if(isAgain>50)
			{
				isAgain=0;
				tryAgain=false;
			}
			isPush=false;
		}
	
	}
	public void drawCutLine()
	{//�����и���
		if(twinkle)
		{
			if(lineIndex%17==0)
			{
				line.drawSelf();//�����ߺ͵���	
			}
			if(lineIndex==612)
			{
				isLine=false;
				twinkle=false;
			}
			lineIndex=lineIndex+18;
		}else
		{
			line.drawSelf();//�����ߺ͵���	
		}	
	}
	//������ͣ����
	public void drawPauseView()
	{
		synchronized(lockB)
		{
			if(isFirstPause)//��ת������ͣ����
			{
				MatrixState.pushMatrix();//��������
				MatrixState.rotate(10*pauseDegree, 0, 0, 1);//��ת
		    	MatrixState.scale(0.028f*pauseDegree,0.028f*pauseDegree, 1);//����
		    	
				pauseDegree++;
				for(BNObject pause:GameData.get(5))//������ת��������ͣ����
				{
					pause.drawSelf();
				}
				if(pauseDegree>=36)
				{
					pauseDegree=0;
					isFirstPause=false;
				}
				MatrixState.popMatrix();//�ָ�����
			}
			else
			{
				for(BNObject pause:GameData.get(5))//������ͣ����
				{
					pause.drawSelf();
				}
			}
		}
	}
	//��ʼ��ѩ
	public void initSnow()
	{
		fps.clear();//���ѩ���б�
		int count=ParticleConstant.START_COLOR.length;//ѩ������ĸ���
		fpfd=new ParticleForDraw[count];//4������ţ�4����ɫ
		//��������ϵͳ
		for(int i=0;i<count;i++)
		{
			ParticleConstant.CURR_INDEX=i;
			fpfd[i]=new ParticleForDraw(ParticleConstant.RADIS[ParticleConstant.CURR_INDEX],ShaderManager.getShader(1),TextureManager.getTextures("snow.png"));  
			//��������,��ѩ���ĳ�ʼλ�ô���������
			fps.add(new ParticleSystem(ParticleConstant.positionFireXZ[i][0],ParticleConstant.positionFireXZ[i][1],fpfd[i]));
		}
	}
	//��ʼ��GameView
	public void initGameView()
	{
		synchronized(lockD)
		{
			deleteBall();
			addBall(MyFCData.ballData);
		}
		isOwnAxe=false;
		isCut=false;
		isWin=false;//��Ϸʤ������ı�־λ
		isDrawWin=false;
		isLight=false;//ֹͣ���Ƶ���
		isCutRigid=false;//ֹͣ���ƻ�
		synchronized(lockA)
		{
			alFlyPolygon.clear();//���ߵ��б����
		}
		
		cpData=MyFCData.data[CheckpointIndex];//������ζ������ݸ���float����
		isJudgePolygon=true;//����ɾ�����ߴ�����Χ��
		isJudgeBall=true;//����ɾ�����ߴ��������
		
		ArrayList<ArrayList<BNObject>> bn=new ArrayList<ArrayList<BNObject>>();//��ʱ�б�
		
		ArrayList<BNObject> tempArrayList=new ArrayList<BNObject>();
		//===========start============����lable����
		tempArrayList=MyFCData.getLableObject();//�����Ϸ�����lable����
		bn.add(tempArrayList);
		//==========end=============
		
		//===========start============//�����и�����
		BNPolyObject bnpo=new BNPolyObject//�����и�����
				(
						MySurfaceView.this,
						TextureManager.getTextures(MyFCData.gamePicName[CheckpointIndex]),
						ShaderManager.getShader(0),
						MyFCData.data[CheckpointIndex],
						1080,
						1920,
						0,
						0
						);
		tempArrayList=new ArrayList<BNObject>();
		tempArrayList.add(bnpo);
		bn.add(tempArrayList);
		//==========end=============
		
		//===========start============//��õ�ǰʣ������
		AllArea=(float)tempArrayList.get(0).cp.GetArea();//����ܵ�ԭʼ���
		AreaSize=0;
		tempArrayList=new ArrayList<BNObject>();
		tempArrayList=MyFCData.getCurrentData(getAreaPercent(),850,80,40,50);//��õ�ǰʣ������
		bn.add(tempArrayList);
		//==========end=============
		
		//===========start============//��ʼ����ǰ��ľ�����и�����
		tempArrayList=new ArrayList<BNObject>();
		tempArrayList=MyFCData.getData(CheckpointIndex);//��ʼ����ǰ��ľ�����и�����
		bn.add(tempArrayList);
		//==========end=============
		
		//===========start============//��ͣ����
		tempArrayList=new ArrayList<BNObject>();
		tempArrayList=MyFCData.getPauseView();//��ͣ����
		bn.add(tempArrayList);
		//==========end=============
		
		
		//===========start============//ʤ������
		tempArrayList=new ArrayList<BNObject>();
		tempArrayList=MyFCData.WinView();//ʤ������
		bn.add(tempArrayList);
		
		synchronized(lockB)
		{
			GameData.clear();//�����Ϸ���������
			for(int i=1;i<=6;i++)
			{
				GameData.put(i, bn.get(i-1));
			}
		}
		//==========end=============
		gameTime=0;//��Ϸʱ��
		gameST=0;//��Ϸ��ʼ��ʱ��
		pauseTime=0;//��Ϸ��ͣ
		kniftNum=0;//�и��
		isPause=false;//��ʼ����Ϸ���棬����ͣ����ı�־λ��Ϊfalse
		
	}
	//���Ű�����
	public void setPressSoundEffect(String music)
	{
		if(SoundEffect)
		{
			activity.sm.playSound(music,0);
		}
	}	
	//����и�İٷֱ�
	public int getAreaPercent()
	{
		int AreaPercent = 100;
		if(AreaSize==0.0f)
		{
			AreaPercent=100;
		}
		else
		{
			AreaPercent=(int) (AreaSize/AllArea*AreaPercent);
		}
		return AreaPercent;
	}
	//������Χ��=====================start=========================
	public void addPolygon(float[] cpData)
	{
		alBNBody.clear();
		for(int j=0;j<cpData.length/2;j++)//������Χ��
		{
			float[] data=MyFCData.getData(cpData,j);
			Body bd=Box2DUtil.createEdge//���´�����Χ��
					(
							data,
							world,
							true,
							0,
							0,
							0,
							-1
							);
			alBNBody.add(bd);//�������İ�Χ����ӽ��б���
		}
	}
	//������Χ��=====================end=========================
	//ɾ����Χ��=====================start=========================
	public void deletePolygon()
	{
		for(int i=0;i<alBNBody.size();i++)//����alBNBody�б��еİ�Χ��Body
		{
			world.destroyBody(alBNBody.get(i));
		}
	}
	//ɾ����Χ��=====================end=========================
	//������=====================start=========================
	public void addBall(float[][] ballData){
		alBNBall.clear();
		alBNBall=MyFCData.getBall(MySurfaceView.this,world,ballData);//������б�
	}
	//������=====================end=========================
	//ɾ����=====================start=========================
	public void deleteBall(){
		for(int i=0;i<BallBody.size();i++)//����alBNBody�б��еİ�Χ��Body
		{
			world.destroyBody(BallBody.get(i));
		}
		BallBody.clear();
	}
	//ɾ����=====================end=========================
	//�ж��Ƿ�ģ��===================start========================
	public boolean worldStep()
	{
		if(isJudgePolygon)//��������������ģ��
		{
			return false;
		}else//������������ģ��
		{
			return true;
		}
	}
}
