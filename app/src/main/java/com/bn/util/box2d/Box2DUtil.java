package com.bn.util.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.bn.fastcut.MySurfaceView;
import com.bn.object.BNObject;
import com.bn.util.manager.ShaderManager;
import static com.bn.util.constant.Constant.RATE;
//����������״�Ĺ�����
public class Box2DUtil 
{
	//����ֱ��
	public static Body createEdge
	(
			float[] data,
			World world,//����
			boolean isStatic,
			float density,//�ܶ�
			float friction,//Ħ��ϵ��
			float restitution,
			int index
	)
	{
		//������������
		BodyDef bd=new BodyDef();
		//�����Ƿ�Ϊ���˶�����
		if(isStatic)
		{
			bd.type=BodyType.STATIC;
		}
		else
		{
			bd.type=BodyType.DYNAMIC;
		}
		float positionX=(data[0]+data[2])/2;
		float positionY=(data[1]+data[3])/2;
		//����λ��						
		bd.position.set(positionX/RATE,positionY/RATE);
		//�������д�������
		Body bodyTemp = null;
		
		while(bodyTemp==null)
		{
			bodyTemp = world.createBody(bd);
		}
		//�ڸ����м�¼��Ӧ�İ�װ����
		bodyTemp.setUserData(index);
		//����������״
		EdgeShape ps=new EdgeShape();
		ps.set(new Vec2((data[0]-positionX)/RATE,(data[1]-positionY)/RATE), new Vec2((data[2]-positionX)/RATE,(data[3]-positionY)/RATE));
		//����������������
		FixtureDef fd=new FixtureDef();
		//����Ħ��ϵ��
		fd.friction =friction;
		//����������ʧ�ʣ�������
		fd.restitution = restitution;
		 //�����ܶ�
		fd.density=density;
		//������״
		fd.shape=ps;
		//���������������������
		if(!isStatic)
		{
			bodyTemp.createFixture(fd);
		}
		else
		{
			bodyTemp.createFixture(ps, 0);//�����ܶ�Ϊ0��PolygonShape����
		}
		return bodyTemp;
	}
	//����Բ�Σ���ɫ��
	public static BNObject createCircle
	(
			MySurfaceView mv,
			float x,//x����
			float y,//y����
			float radius,//�뾶
			World world,//����
			int programId,//����ID
			int texId,//��������
			float density,//�ܶ�
			float friction,//Ħ��ϵ��
			float restitution,//�ָ�ϵ��
			int index
	)
	{
		//������������
		BodyDef bd=new BodyDef(); 
		//�����Ƿ�Ϊ���˶�����		
		bd.type=BodyType.DYNAMIC;
		//����λ��
		bd.position.set(x/RATE,y/RATE);
		//�������д�������
		Body bodyTemp=null;
		while(bodyTemp==null)
		{
			bodyTemp= world.createBody(bd); 
		}
		//����������״
		CircleShape cs=new CircleShape();
		cs.m_radius=radius/RATE;		
		//����������������
		FixtureDef fd=new  FixtureDef();
		//�����ܶ�
		fd.density = density;   		
		//����Ħ��ϵ��
		fd.friction = friction;   
		//����������ʧ�ʣ�������
		fd.restitution =restitution;      
		//������״
		fd.shape=cs;
		//���������������������
		bodyTemp.createFixture(fd);
		//����BNObject�����
		return new BNObject(mv,bodyTemp, x, y, radius*2, radius*2, texId,ShaderManager.getShader(programId),index);
	}
}
