package com.bn.util.constant;

import com.bn.util.screenscale.ScreenScaleResult;

public class Constant 
{
	//=============��Ϸ����start==================
	public static float PauseLable_Left=50;
	public static float PauseLable_Right=210;
	public static float PauseLable_Top=1780;
	public static float PauseLable_Buttom=1900;
	
	//=============��Ϸ����end==================
	//=============��ͣ����start=============
	public static float ChooseLevel_Left=100;
	public static float ChooseLevel_Right=450;
	public static float ChooseLevel_Top=850;
	public static float ChooseLevel_Buttom=1150;
	
	public static float ReStart_Left=450;
	public static float ReStart_Right=700;
	public static float ReStart_Top=850;
	public static float ReStart_Buttom=1150;
	
	public static float Continue_Left=700;
	public static float Continue_Right=950;
	public static float Continue_Top=850;
	public static float Continue_Buttom=1150;
	
	//=============��ͣ����end=============
	
	//=============ʤ������start=============
	public static float WinChooseLevel_Left=100;
	public static float WinChooseLevel_Right=450;
	public static float WinChooseLevel_Top=1250;
	public static float WinChooseLevel_Buttom=1550;
	
	public static float WinReStart_Left=450;
	public static float WinReStart_Right=700;
	public static float WinReStart_Top=1250;
	public static float WinReStart_Buttom=1550;
	
	public static float WinNext_Left=700;
	public static float WinNext_Right=950;
	public static float WinNext_Top=1250;
	public static float WinNext_Buttom=1550;
	//=============ʤ������end=============
	
	public static float ChooseButton_Left=115;
	public static float ChooseButton_Right=345;
	public static float ChooseButton_Up=575;
	public static float ChooseButton_Down=805;
	
	public static float StartButton_Left=390;
	public static float StartButton_Right=770;
	public static float StartButton_Up=805;
	public static float StartButton_Down=1180;
	
	public static float ExitButton_Left=615;
	public static float ExitButton_Right=820;
	public static float ExitButton_Up=1315;
	public static float ExitButton_Down=1520;
	
	public static float HelpButton_Left=50;
	public static float HelpButton_Right=245;
	public static float HelpButton_Up=1470;
	public static float HelpButton_Down=1665;
	
	
	//===================
	public static float Choose_Back_Left=785;
	public static float Choose_Back_Right=970;
	public static float Choose_Back_Up=115;
	public static float Choose_Back_Down=250;
	
	public static float Choose_Music_Left=245;
	public static float Choose_Music_Right=845;
	public static float Choose_Music_Up=920;
	public static float Choose_Music_Down=1060;
	
	public static float Choose_Sound_Left=245;
	public static float Choose_Sound_Right=845;
	public static float Choose_Sound_Up=1200;
	public static float Choose_Sound_Down=1340;
	//===================
	
	public static float Help_Back_Left=805;
	public static float Help_Back_Right=985;
	public static float Help_Back_Up=70;
	public static float Help_Back_Down=205;

	//ѡ�ؽ���  �������곣����========start=============
	public static int LevelView_Back_Left_X=800;
	public static int LevelView_Back_Right_X=1000;
	public static int LevelView_Back_Top_Y=75;
	public static int LevelView_Back_Bottom_Y=225;
	
	public static int LevelView_Series1_Left_X=75;
	public static int LevelView_Series1_Right_X=725;
	public static int LevelView_Series1_Top_Y=450;
	public static int LevelView_Series1_Bottom_Y=750;
	
	public static int LevelView_Series2_Left_X=325;
	public static int LevelView_Series2_Right_X=975;
	public static int LevelView_Series2_Top_Y=850;
	public static int LevelView_Series2_Bottom_Y=1150;
	
	public static int LevelView_Series3_Left_X=250;
	public static int LevelView_Series3_Right_X=550;
	public static int LevelView_Series3_Top_Y=1075;
	public static int LevelView_Series3_Bottom_Y=1725;
	
	public static int LevelView_PickUp1_1_Left_X=190;
	public static int LevelView_PickUp1_1_Right_X=625;
	public static int LevelView_PickUp1_1_Top_Y=500;
	public static int LevelView_PickUp1_1_Bottom_Y=860;
	
	public static int LevelView_PickUp1_2_Left_X=410;
	public static int LevelView_PickUp1_2_Right_X=855;
	public static int LevelView_PickUp1_2_Top_Y=1072;
	public static int LevelView_PickUp1_2_Bottom_Y=1400;
	
	public static int LevelView_PickUp2_1_Left_X=485;
	public static int LevelView_PickUp2_1_Right_X=920;
	public static int LevelView_PickUp2_1_Top_Y=625;
	public static int LevelView_PickUp2_1_Bottom_Y=960;
	
	public static int LevelView_PickUp2_2_Left_X=185;
	public static int LevelView_PickUp2_2_Right_X=615;
	public static int LevelView_PickUp2_2_Top_Y=1170;
	public static int LevelView_PickUp2_2_Bottom_Y=1535;
	
	public static int LevelView_PickUp3_1_Left_X=190;
	public static int LevelView_PickUp3_1_Right_X=605;
	public static int LevelView_PickUp3_1_Top_Y=620;
	public static int LevelView_PickUp3_1_Bottom_Y=955;
	
	public static int LevelView_PickUp3_2_Left_X=500;
	public static int LevelView_PickUp3_2_Right_X=920;
	public static int LevelView_PickUp3_2_Top_Y=1230;
	public static int LevelView_PickUp3_2_Bottom_Y=1580;
	//ѡ�ؽ���  �������곣����========end=============
	
	//��׼��Ļ�Ŀ��
	public static float StandardScreenWidth=1080;
	//��׼��Ļ�ĸ߶�
	public static float StandardScreenHeight=1920;
	
	//��׼��Ļ��߱�
	public static float ratio=StandardScreenWidth/StandardScreenHeight;
	//���ż�����
	public static ScreenScaleResult ssr;
	
	//----------------��������   start----------------
	public static final float RATE = 10;//��Ļ����ʵ����ı��� 10px��1m;   
	public static final boolean DRAW_THREAD_FLAG=true;//�����̹߳�����־λ
	
	public static final float TIME_STEP = 1.0f/60.0f;//ģ��ĵ�Ƶ��   
	public static final int ITERA = 10;//����Խ��ģ��Լ��ȷ��������Խ��   
	
	//����Խ��ģ��Լ��ȷ��������Խ��  
	public static final int Vec_ITERA=6;//�ٶȵ���
	public static final int POSITON_ITERA=2;//λ�õ���
	//----------------��������   end----------------
	
	
	public static float fromPixSizeToNearSize(float size)
	{
		return size*2/StandardScreenHeight;
	}
	//��Ļx���굽�ӿ�x����
	public static float fromScreenXToNearX(float x)
	{
		return (x-StandardScreenWidth/2)/(StandardScreenHeight/2);
	}
	//��Ļy���굽�ӿ�y����
	public static float fromScreenYToNearY(float y)
	{
		return -(y-StandardScreenHeight/2)/(StandardScreenHeight/2);
	}
	//ʵ����Ļx���굽��׼��Ļx����
	public static float fromRealScreenXToStandardScreenX(float rx)
	{
		return (rx-ssr.lucX)/ssr.ratio;
	}
	//ʵ����Ļy���굽��׼��Ļy����
	public static float fromRealScreenYToStandardScreenY(float ry)
	{
		return (ry-ssr.lucY)/ssr.ratio;
	}
	//��׼��Ļx���굽ʵ����Ļx����
	public static float fromStandardScreenXToRealScreenX(float rx)
	{
		return rx*ssr.ratio+ssr.lucX;
	}
	//��׼��Ļy���굽ʵ����Ļy����
	public static float fromStandardScreenYToRealScreenY(float ry)
	{
		return ry*ssr.ratio+ssr.lucY;
	}
	public static float fromStandardScreenSizeToRealScreenSize(float size)
	{
		return size*ssr.ratio;
	}
	
}
