package com.android.mobliesocietynetwork.client.util.bean;


import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

//扩展类IQ包，直接继承自IQ，用于发送和接收标签
public class LabelPacket extends IQ
{
	private static final String ITEM="item";
	private String name;
	private String namespace;
	private String user;
	private ArrayList<Integer> labelList;
	
	
	public LabelPacket(String name,String namespace,String user)
	{
		super();
		setType(Type.SET);
		setTo();
		this.name=name;
		this.namespace = namespace;
		this.user=user;
		labelList=new ArrayList<Integer>();
	}


	public String getNamespace()
	{
		return namespace;
	}


	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}


	public void addlabelList(ArrayList<Integer> list){
		labelList=list;
	}


	@Override 
	public void setType(Type type)       
	{
		super.setType(Type.SET);
	}
	
	public void setTo()
	{
		super.setTo("wss-pc");
	}
	//xmpp底层有查错功能：xml结构错误的话，xmpp不会发送报文
	@Override
	public String getChildElementXML()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(name).append(" xmlns=\"").append(namespace).append("\">");
		buf.append("<").append(ITEM).append(" user=\"").append(user).append("\"");
		for(int i=1;i<=labelList.size();i++)
		{
			buf.append(" label"+i+"=\"").append(labelList.get(i-1)).append("\"");
		}
		buf.append("/>");
		buf.append("</").append(name).append(">");
		// TODO Auto-generated method stub
		return buf.toString();
	}

}
