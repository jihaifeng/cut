package com.bn.util.constant;

import java.util.ArrayList;

import com.bn.fastcut.MySurfaceView;
import com.bn.object.BNObject;
import uk.co.geolib.geolib.CGrid;
import uk.co.geolib.geopolygons.C2DHoledPolygon;
import uk.co.geolib.geopolygons.C2DPolygon;

public class isCutUtil{
	//�ж��ֻ������߶���ͼ���е��߶��Ƿ��ཻ
	public static boolean isIntersect(float x1,float y1,float x2,float y2,float x3,float y3,float x4,float y4)
	{
		if(x1==x2||x3==x4)//x1=x2 ����x3=x4ֱ�ӷ���false
		{
			return false;
		}
		float k1 = (float)(y1-y2)/(float)(x1-x2);//���һ��ֱ�ߵ�б��
		float b1 = (float)(x1*y2 - x2*y1)/(float)(x1-x2);
		float k2 = (float)(y3-y4)/(float)(x3-x4);//��ڶ���ֱ�ߵ�б��
		float b2 = (float)(x3*y4 - x4*y3)/(float)(x3-x4);
		if(k1==k2)//���б����ͬ
		{
			return false;//ֱ�ӷ���false
		}else
		{
			float x = (float)(b2-b1) / (float)(k1-k2);
			//��0.1����Ϊ���  �൱��ģ������
			if((((x+0.1)>=x1)&&((x-0.1)<=x2))||(((x+0.1)>=x2)&&(x-0.1)<=x1))
			{
				if((((x+0.1)>=x3)&&((x-0.1)<=x4))||(((x+0.1)>=x4)&&(x-0.1)<=x3))
				{
					return true;
				}
			}
			return false;
		}
	}
	//��������߶εĽ���
	public static float[] getIntersectPoint(float x1,float y1,float x2,float y2,float x3,float y3,float x4,float y4)
	{
		float[] result=new float[2];
		result[0] = (((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))  
				/ ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4)));
		
		result[1] = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))  
				/ ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));
		return result;
	}
	//�ж��Ƿ񻮵��˶����
	public static boolean isCutPolygon(MySurfaceView mv,C2DPolygon cp,float x1,float y1,float x2,float y2)
	{
		int indexTemp=MySurfaceView.CheckpointIndex;
		float[] polygonData=GeoLibUtil.fromC2DPolygonToVData(cp);//��ȡ���ڶ������״�ĵ�����
		boolean[] isIntersect=new boolean[polygonData.length/2];//��ȡ�߶��Ƿ��ཻ������
		int boolCount=0;//��������Լ�ֵ
		int falseCount=0;//���ཻ�߶ε�����
		int index[]=new int[2];
		int findCount=0;
		for(int j=0;j<polygonData.length/2;j++)
        {
			float[] data=MyFCData.getData(polygonData,j);//��ȡһ���ߵ�x��y����
			isIntersect[boolCount]=isIntersect(x1, y1, x2, y2,
					data[0], data[1], data[2], data[3]);//�ж������߶��Ƿ��ཻ
			//�ж��Ƿ񻮵��� �����и�ı�=========================start==========================
			if(isIntersect[boolCount])//����߶����߶��ཻ
			{
				for(int k=0;k<MyFCData.data[indexTemp].length/2;k++)//ѭ��������ݵ�����
				{
					if(((int)data[0]==MyFCData.data[indexTemp][k*2])&&((int)data[1]==MyFCData.data[indexTemp][k*2+1]))//�����õıߵĵ�һ��x����������������ܹ��ҵ�
					{
						index[0]=k;//��¼�˵�����������е�λ��
						findCount++;//��������1
					}
					if(((int)data[2]==MyFCData.data[indexTemp][k*2])&&((int)data[3]==MyFCData.data[indexTemp][k*2+1]))//�����õıߵĵڶ���x����������������ܹ��ҵ�
					{
						index[1]=k;//��¼�˵�����������е�λ��
						findCount++;//��������1
					}
				}
				if(findCount>0&&findCount%2==0)//�����ż����
				{
					if(index[0]>index[1])
					{
						if(index[1]==0)//��������ֵ-0  ���ж����ֵ�ıߵ�booleanֵ
						{
							if(!MyFCData.dataBool[indexTemp][index[0]])//�жϸ������Ƿ�����и�
							{
								mv.isCutRigid=true;
								mv.intersectPoint=getIntersectPoint(x1, y1, x2, y2,
										data[0], data[1], data[2], data[3]);
								return false;//��������и�  ��ֱ�ӷ���false
							}
						}else
						{
							if(!MyFCData.dataBool[indexTemp][index[1]])//�жϸ������Ƿ�����и�
							{
								mv.isCutRigid=true;
								mv.intersectPoint=getIntersectPoint(x1, y1, x2, y2,
										data[0], data[1], data[2], data[3]);
								return false;//��������и�  ��ֱ�ӷ���false
							}
						}
					}else
					{
						if(!MyFCData.dataBool[indexTemp][index[0]])//�жϸ������Ƿ�����и�
						{
							mv.isCutRigid=true;
							mv.intersectPoint=getIntersectPoint(x1, y1, x2, y2,
									data[0], data[1], data[2], data[3]);
							return false;//��������и�  ��ֱ�ӷ���false
						}
					}
					findCount=0;
				}else
				{
					findCount=0;
				}
			}
			//�ж��Ƿ񻮵��� �����и�ı�=========================end==========================
			if(isIntersect[boolCount]==false)//����߶β��ཻ
			{
				falseCount++;//������1
			}
			boolCount++;//��������Լ�1
        }
		if((falseCount==isIntersect.length)||(falseCount==isIntersect.length-1))
		{
			return false;
		}else
		{
			return true;
		}
	}
	//����з������Ѿ����з���Ķ�����б� 
	public static ArrayList<C2DPolygon> getCutPolysArrayList(MySurfaceView mv,ArrayList<BNObject> alBNPO,float lxs,float lys,float lxe,float lye)
	{
		ArrayList<C2DPolygon> onePolygon=new ArrayList<C2DPolygon>();//�и��ÿ������ֻ��һ������ε��б�
		ArrayList<C2DPolygon> tempPolygon=new ArrayList<C2DPolygon>();//�и��ÿ���������ж������ε��б�
		ArrayList<ArrayList<float[]>> tal=GeoLibUtil.calParts(lxs, lys, lxe,lye);//�ֳ��������������
		C2DPolygon[] cpA=GeoLibUtil.createPolys(tal);//�������������
		for(C2DPolygon cpTemp:cpA)
		{
			ArrayList<C2DHoledPolygon> polys = new ArrayList<C2DHoledPolygon>();
			//��ñ��зֳɵ���������������ͼ�ε��ص����֣�������polys�б���
			cpTemp.GetOverlaps(alBNPO.get(0).cp, polys, new CGrid());
			if(polys.size()==1)//�����������ֻ��һ�������
			{
				onePolygon.add(polys.get(0).getRim());//ֱ�Ӽӽ��б���
			}else//������������ж�������
			{
				for(C2DHoledPolygon chp:polys)//���������μӽ��б���
				{
					tempPolygon.add(chp.getRim());
				}
			}
		}
		ArrayList<C2DPolygon> result=getLastPolysArrayList(mv,onePolygon,tempPolygon,lxs, lys, lxe,lye);
		return result;
	}
	//�ж��зֵľ������ĸ������ �����ϲ��Ȳ��� ���ض�����б�
	public static ArrayList<C2DPolygon> getLastPolysArrayList(MySurfaceView mv,ArrayList<C2DPolygon> onePolygon,ArrayList<C2DPolygon> tempPolygon,float lxs,float lys,float lxe,float lye)
	{
		ArrayList<C2DPolygon> lastPolygons=new ArrayList<C2DPolygon>();//�����������ж��зֶ���ε��б�
		ArrayList<C2DPolygon> canCombineP=new ArrayList<C2DPolygon>();//��û�л��� ���Խ��кϲ�����ε��б�
		//�ж��зֵľ������ĸ������==================start======================
		if(tempPolygon.size()>0)//���һ���������ж�������
		{
			for(C2DPolygon cp:tempPolygon)//����C2DPolygon����
			{
				if(isCutPolygon(mv,cp, lxs, lys, lxe, lye))//�ж����Ƿ񻮵��˸ö���ζ���
				{
					lastPolygons.add(cp);
				}else
				{
					canCombineP.add(cp);//û�����Ķ���ηŽ�canCombine�б���
				}
			}
			if(canCombineP.size()>0)//��������ܹ��ϲ��Ķ����
			{
				C2DPolygon cc=new C2DPolygon();
				for(int i=0;i<canCombineP.size();i++)//�����ܹ��ϲ��Ķ�����б�
				{
					if(i==0)//����ǵ�һ��ѭ��
					{
						cc=onePolygon.get(0);//����ֵ
					}
					cc=MyPatchUtil.getCombinePolygon//�ϲ������
					(
							MyPatchUtil.getPolygonData(canCombineP.get(i), canCombineP.get(i).IsClockwise()), 
							MyPatchUtil.getPolygonData(cc, cc.IsClockwise()), 
							MyPatchUtil.getPolygonData(canCombineP.get(i), canCombineP.get(i).IsClockwise()).length,
							MyPatchUtil.getPolygonData(cc, cc.IsClockwise()).length
					);
				}
				lastPolygons.add(cc);//���ϲ���Ķ������ӽ��б�����
			}else//����������ܹ��ϲ��Ķ���� 
			{
				lastPolygons.add(onePolygon.get(0));//��ֱ����ӽ������б���
			}
		}else//���һ��������ֻ��һ�������
		{
			for(int i=0;i<onePolygon.size();i++)
			{
				lastPolygons.add(onePolygon.get(i));//ֱ����ӽ��б���
			}
		}
		//�ж��зֵľ������ĸ������==================end======================
		return lastPolygons;
	}
}
