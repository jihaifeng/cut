package com.bn.util.constant;

import java.util.ArrayList;

import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geopolygons.C2DPolygon;
import android.graphics.Point;

public class MyPatchUtil {//������
	//���ݶ���λ�ȡPoint����
	public static Point[] getPolygonData(C2DPolygon cp,boolean IsClockwise)
	{
		ArrayList<C2DPoint> pointCopyIn=new ArrayList<C2DPoint>();//����C2DPoint�����б�
		cp.GetPointsCopy(pointCopyIn);//��ֵ
		int numsCpTemp=pointCopyIn.size();//����б�ĳ���
		Point[] pArray=new Point[numsCpTemp];//����Point����
		//˳ʱ���򶥵����鸳ֵ
		if(IsClockwise)
		{
			for(int j=0;j<numsCpTemp;j++)
			{
				C2DPoint tempCP1=pointCopyIn.get(numsCpTemp-1-j);
				pArray[j]=new Point((int)tempCP1.x,(int)tempCP1.y);
			}
		}else//��ʱ���򶥵����鸳ֵ
		{
			for(int j=0;j<numsCpTemp;j++)
			{
				C2DPoint tempCP1=pointCopyIn.get(j);
				pArray[j]=new Point((int)tempCP1.x,(int)tempCP1.y);
			}
		}
		Point[] answer=new Point[100];
		for(int j=0;j<numsCpTemp;j++)
		{
			answer[j] = pArray[j];//��pArray1��������ݱ�����answer������
		}
		return pArray;
	}
	//����������κϲ���һ�������
	public static C2DPolygon getCombinePolygon(Point[] pArray1,Point[] pArray2,int num1,int num2)
	{
		Point[] tempAnswer=new Point[100];//������¼��ʱ�Ĵ�����
		int indexAnswer = -1;//��ʱ���������������
		for(int ii=0;ii<num1;ii++)//�� ��ǰ������ ���б���
		{
			boolean flag = false;//��־λ
			for(int jj=0;jj<num2;jj++)//�� ��ǰ����εĵ����� ���б���
			{
				if(pArray1[ii].equals(pArray2[jj]))//�� ��ǰ�𰸵�  �����εĵ�ǰ��  ����ͬ��
				{
					flag = true;
					indexAnswer++;
					tempAnswer[indexAnswer]=pArray1[ii];//�ѵ�ǰ����� tempAnswer��
					int indexii=0;
					//��indexii��ֵΪ  ����εĵ�ǰ�����������һ����
					if(jj==0)//����ǰ���������0����indexii��ֵΪ���鳤��-1
					{
						indexii = num2-1;
					}else
					{
						indexii = jj - 1;
					}
					if(pArray1[(ii+1)%num1].equals(pArray2[indexii]))//��ǰ��� ��һ��  ��  ����εĵ�ǰ�����һ��   ����ͬ�� ���򽫶���εĲ��ֵ���ӵ�tempAnswer������
					{
						for(int kk=(jj+1)%num2;;kk=(kk+1)%num2)//����pArray2����
						{
							if(pArray2[kk].equals(pArray2[indexii]))//�����forѭ������ֹ����  ���� ����εĵ�ǰ�����һ�� ��  �������ĵ���ͬʱ��
							{
								break;
							}
							indexAnswer++;
							tempAnswer[indexAnswer]=pArray2[kk];//������ӵ���������
						}
					}
				}
			}
			if(flag == false)
			{
				indexAnswer++;//�������1
				tempAnswer[indexAnswer]=pArray1[ii];//����ʱ�����鸳ֵ
			}
		}
		ArrayList<C2DPoint> c2d=new ArrayList<C2DPoint>();//����C2DPoint�б�
		for(int p=0;p<tempAnswer.length;p++)//����Point����
		{
			if(tempAnswer[p]!=null)
			{
				c2d.add(new C2DPoint(tempAnswer[p].x,tempAnswer[p].y));//����ӽ��б���
			}
		}
		C2DPolygon gon=new C2DPolygon();//��������ζ���
		gon.Create(c2d,true);//����
		return gon;
	}
}
