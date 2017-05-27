package com.bn.util.constant;

import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geopolygons.C2DPolygon;

public class GeometryConstant{
	//��������ĵ��߶εľ���=====================================================================
	public static float lengthPointToLine(float x,float y,float xs,float ys,float xm,float ym){
		float lengthpointToline=0;
		//�������뿪ʼ��������������������봥���ƶ���������нǺ��������봥���ƶ�����������������뿪ʼ������������нǾ�Ϊ���
		if(((y-ys)*(ym-ys)+(x-xs)*(xm-xs)>0)&&((y-ym)*(ys-ym)+(x-xm)*(xs-xm)>0))
		{
			lengthpointToline=(float) (Math.abs(x*(ym-ys)-y*(xm-xs)-xs*(ym-ys)+ys*(xm-xs))/Math.sqrt((ym-ys)*(ym-ys)+(xm-xs)*(xm-xs)));
		}else{
			float length1=(x-xs)*(x-xs)+(y-ys)*(y-ys);
			float length2=(x-xm)*(x-xm)+(y-ym)*(y-ym);
			if(length1<=length2)
			{
				lengthpointToline=(float)Math.sqrt(length1);
			}else{
				lengthpointToline=(float)Math.sqrt(length2);
			}
		}
		return lengthpointToline;
	}
	
	//�ж϶����ľ����ߵķ���
	public static int cutDirection=1;
	public static void judgeDirection(C2DPolygon cp,float Xd,float Yd,float Xu,float Yu)
	{
		C2DPoint point=cp.GetCentroid();
		float middle_x=(float) ((Xd+Xu)/2-point.x);
		float middle_y=(float) ((Yd+Yu)/2-point.y);
		if(Math.abs(Yd-Yu)<100)//����
		{
			if(middle_y>0)//����������
			{
				cutDirection=2;//down
			}else
			{
				cutDirection=1;//up
			}	
		}else if(Yd<Yu)//���ϵ�����
		{
			if(Xu>=Xd)//б����
			{
				if(middle_x>0)//�������ұ�
				{
					cutDirection=4;//right
				}else
				{
					cutDirection=3;//left
				}		
			}else//б����
			{
				if(middle_x>0)//�������ұ�
				{
					cutDirection=4;//right
				}else
				{
					cutDirection=3;//left
				}
			}
		}else//���µ�����
		{
			if(Xd>=Xu)
			{
				if(middle_x>0)//�������ұ�
				{
					cutDirection=4;//right
				}else
				{
					cutDirection=3;//left
					}
				}
				else
				{
					if(middle_x>0)//�������ұ�
					{
						cutDirection=4;//right
					}else
					{
						cutDirection=3;//left
					}
				}
			}
		}
	//����λ�� 1--up 2--down 3--left 4--right
	public static float calculateDisplacement(int index,int t)
	{
		float vi=0.03f;//���ٶ�
		float a=-0.002f;//���ٶ�	
		float diaplacement=0;//λ��
		if(index==1)//up
		{
			diaplacement=vi*t+a*t*t/2;//λ��
		}else if(index==2)//down
		{
			if(t>10)
			{
				vi=0.03f;
				a=-0.002f;	
				diaplacement=vi*t+a*t*t/2-0.3f;//λ��
			}
			else
			{
				vi=0.02f;
				a=-0.002f;
				diaplacement=-(vi*t+a*t*t/2);
			}
		}else 
		{
			vi=0.003f;
			a=-0.0001f;
			if(index==3)//left
			{
				diaplacement=-(vi*t+a*t*t/2);//λ��
			}else if(index==4)//right
			{
				diaplacement=vi*t+a*t*t/2;//λ��
			}
		}
		return diaplacement;
	}
}
