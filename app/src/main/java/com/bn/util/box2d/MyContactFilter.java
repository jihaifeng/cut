package com.bn.util.box2d;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import com.bn.fastcut.MySurfaceView;

public class MyContactFilter extends ContactFilter//��ײ���������
{
	public MyContactFilter(MySurfaceView mv){	}//������
	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB)///�����Ƿ���ײ�ķ���
	{
		Body bodyA= fixtureA.getBody();
		Body bodyB= fixtureB.getBody();
		if((Integer)(bodyA.getUserData())==-1||(Integer)(bodyB.getUserData())==-1)//�жϲ�����ײ�ĸ����Ƿ�����ǽ��
		{
			return true;
		}else
		{
			return false;
		}
	}
}
