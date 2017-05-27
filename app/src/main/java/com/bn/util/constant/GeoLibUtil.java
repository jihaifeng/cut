package com.bn.util.constant;

import static com.bn.util.constant.Constant.StandardScreenHeight;
import static com.bn.util.constant.Constant.StandardScreenWidth;

import java.util.ArrayList;
import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geopolygons.C2DPolygon;

public class GeoLibUtil
{
	static final float xmin=0;
	static final float xmax=StandardScreenWidth;
	static final float ymin=0;
	static final float ymax=StandardScreenHeight;
	public static C2DPolygon createPoly(float[] polyData)//���ݶ������ݴ��������
	{
		ArrayList<C2DPoint> al=new ArrayList<C2DPoint>();//����ArrayList<C2DPoint>����
		for(int i=0;i<polyData.length/2;i++)//������������
		{
			C2DPoint tempP=new C2DPoint(polyData[i*2],polyData[i*2+1]);//����C2DPoint����x��y��
			al.add(tempP);//��C2DPoint�������ArrayList��
		}
		C2DPolygon p = new C2DPolygon();//����C2DPolygon����
		p.Create(al, true);//������ѡ����������Ķ���ε�
		return p;
	}
	//�����зֺ����������ζ���
	public static C2DPolygon[] createPolys(ArrayList<ArrayList<float[]>> alIn)
	{
		C2DPolygon[] cps=new C2DPolygon[2];
		int index=0;
		for(ArrayList<float[]> p:alIn)
		{
			cps[index]=new C2DPolygon();
			ArrayList<C2DPoint> al=new ArrayList<C2DPoint>();
			for(float[] fa:p)
			{
				C2DPoint tempP=new C2DPoint(fa[0],fa[1]);
				al.add(tempP);
			}
			cps[index].Create(al, true);
			index++;
		}
		return cps;
	}
	public static ArrayList<Float> fromConvexToTris(ArrayList<float[]> points)//��͹���������ת��������������
	{
		ArrayList<Float> result=new ArrayList<Float>();
		int count=points.size();
		for(int i=0;i<count-2;i++)
		{
			float[] d1=points.get(0);//��������εĵ�һ����������
			float[] d2=points.get(i+1);//��������εĵڶ�����������
			float[] d3=points.get(i+2);//��������εĵ�������������
			
			result.add(d1[0]);result.add(d1[1]);//�������εĵ�һ���������ݷŽ��б���
			result.add(d2[0]);result.add(d2[1]);//�������εĵڶ����������ݷŽ��б���
			result.add(d3[0]);result.add(d3[1]);//�������εĵ������������ݷŽ��б���
		}
		return result;		
	}
	public static float[] fromAnyPolyToTris(float[] vdata)//�����������ת�������ζ�������
	{
		C2DPolygon cp=createPoly(vdata);//������εĶ����������C2DPolygon����
		ArrayList<C2DPolygon> subAreas = new ArrayList<C2DPolygon>();
		cp.ClearConvexSubAreas();//���͹������
		cp.CreateConvexSubAreas();//����͹������
		cp.GetConvexSubAreas(subAreas);//���͹������
		ArrayList<Float> resultData=new ArrayList<Float>();
		for(C2DPolygon cpTemp:subAreas)//����C2DPolygon����
		{
			ArrayList<float[]> points=new ArrayList<float[]>();
			ArrayList<C2DPoint> alp=new ArrayList<C2DPoint>();
			cpTemp.GetPointsCopy(alp);//������εĶ������ݿ�����ArrayList<C2DPoint>��
			for(C2DPoint p:alp)
			{
				float[] fa=new float[]{(float)p.x,(float)p.y};
				points.add(fa);//��C2DPointת��Ϊfloat����
			}
			ArrayList<Float> tempConvex=fromConvexToTris(points);//���ݶ���εĶ������ݽ�͹�����ת����������
			for(Float f:tempConvex)
			{
				resultData.add(f);//�������ζ������ݷŽ�ArrayList<Float>��
			}
		}
		float[] result=new float[resultData.size()];//��ArrayList<Float>ת��һά����
		
		for(int i=0;i<resultData.size();i++)
		{
			result[i]=resultData.get(i);
		}
		return result;
	}
	public static float[] fromC2DPolygonToVData(C2DPolygon cp)//��C2DPolygon����ת���ɶ�������
	{
		ArrayList<float[]> points=new ArrayList<float[]>();
		ArrayList<C2DPoint> alp=new ArrayList<C2DPoint>();
		cp.GetPointsCopy(alp);//������εĶ������ݿ�����ArrayList<C2DPoint>��
		for(C2DPoint p:alp)//����ArrayList<C2DPoint>����
		{
			float[] fa=new float[]{(float)p.x,(float)p.y};
			points.add(fa);//��C2DPointת��Ϊfloat����
		}
		float[] result=new float[points.size()*2];
		for(int i=0;i<points.size();i++)//��ArrayList<float[]>����ת����float[]
		{
			float[] p=points.get(i);
			result[i*2]=p[0];
			result[i*2+1]=p[1];
		}
		return result;
	}
	//�зֶ����
	public static ArrayList<ArrayList<float[]>> calParts(float sx,float sy,float ex,float ey)
	{
		// 0[xmin,ymin]----------3[xmax,ymin]
		// |                              | 
		// |                              |
		// 1[xmin,ymax]----------2[xmax,ymax]
		int currIndex=0;
		ArrayList<float[]> al=new ArrayList<float[]>();
		al.add(new float[]{xmin,ymin});
		currIndex++;
		int jd1Index=-1;
		int jd2Index=-1;
		
		//��0-1�߶��봫���и��ߵĽ��� X=xmin
		float t=(xmin-sx)/(ex-sx);
		float y=(ey-sy)*t+sy;
		if(y>ymin&&y<ymax)
		{
			jd1Index=currIndex;
			al.add(new float[]{xmin,y});
			currIndex++;
		}
		
		al.add(new float[]{xmin,ymax});
		currIndex++;
		
		//��1-2�߶δ����и��ߵĽ��� y=ymax
		t=(ymax-sy)/(ey-sy);
		float x=(ex-sx)*t+sx;
		if(x>xmin&&x<xmax)
		{
			if(jd1Index==-1)
			{
				jd1Index=currIndex;
			}
			else
			{
				jd2Index=currIndex;
			}
			al.add(new float[]{x,ymax});
			currIndex++;
		}
		
		al.add(new float[]{xmax,ymax});
		currIndex++;
		
		//��2-3�߶δ����и��ߵĽ��� x=xmax
		t=(xmax-sx)/(ex-sx);
		y=(ey-sy)*t+sy;
		if(y>ymin&&y<ymax)
		{
			if(jd1Index==-1)
			{
				jd1Index=currIndex;
			}
			else
			{
				jd2Index=currIndex;
			}
			al.add(new float[]{xmax,y});
			currIndex++;
		}
		al.add(new float[]{xmax,ymin});
		currIndex++;
		
		//��3--0�߶δ����и��ߵĽ��� y=ymin
		t=(ymin-sy)/(ey-sy);
		x=(ex-sx)*t+sx;
		if(x>xmin&&x<xmax)
		{
			if(jd1Index==-1)
			{
				jd1Index=currIndex;
			}
			else
			{
				jd2Index=currIndex;
			}
			al.add(new float[]{x,ymin});
			currIndex++;
		}
		//���Ƶ�һ�������
		ArrayList<float[]> p1=new ArrayList<float[]>();
		int startIndex=jd1Index;
		while(true)
		{
			p1.add(al.get(startIndex));			
			if(startIndex==jd2Index)
			{
				break;
			}			
			startIndex=(startIndex+1)%al.size();
		}
		//���Ƶڶ��������
		ArrayList<float[]> p2=new ArrayList<float[]>();
		startIndex=jd2Index;
		while(true)
		{
			p2.add(al.get(startIndex));			
			if(startIndex==jd1Index)
			{
				break;
			}			
			startIndex=(startIndex+1)%al.size();
		}
		ArrayList<ArrayList<float[]>> result=new ArrayList<ArrayList<float[]>>();
		result.add(p1);
		result.add(p2);
		return result;
	}
}