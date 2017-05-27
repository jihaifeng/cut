package com.bn.util.snow;

import com.bn.util.constant.MatrixState;

public class ParticleSingle 
{    
    public float x;
    public float y;
    public float vx;
    public float vy;
    public float lifeSpan;
    
    ParticleForDraw fpfd;
    
    public ParticleSingle(float x,float y,float vx,float vy,ParticleForDraw fpfd)
    {
    	this.x=x;
    	this.y=y;
    	this.vx=vx;
    	this.vy=vy;
    	this.fpfd=fpfd;
    }
    
    public void go(float lifeSpanStep)
    {
    	//���ӽ����ƶ��ķ�����ͬʱ��������ķ���
    	y=y+vy;
    	lifeSpan+=lifeSpanStep;
    }
    
    public void drawSelf(float[] startColor,float[] endColor,float maxLifeSpan){
    	MatrixState.pushMatrix();//�����ֳ�
    	MatrixState.translate(x, y, 0);
    	float sj=(maxLifeSpan-lifeSpan)/maxLifeSpan;//˥���������𽥵ı�С������Ϊ0
    	fpfd.drawSelf(sj,startColor,endColor);//���Ƶ������� 
    	MatrixState.popMatrix();//�ָ��ֳ�
    }
}
