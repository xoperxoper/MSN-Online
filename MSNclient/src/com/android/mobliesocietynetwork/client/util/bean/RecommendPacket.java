package com.android.mobliesocietynetwork.client.util.bean;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class RecommendPacket extends IQ
{
	private static final String ITEM="item";
	private String name;
	private String namespace;
	private ArrayList<String> userList;
	
	public RecommendPacket(String name,String namespace)
	{
		super();
		setTo();
		this.name=name;
		this.namespace = namespace;
	}


	public String getNamespace()
	{
		return namespace;
	}


	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}


	@Override 
	public void setType(Type type)       
	{
		super.setType(type);
	}
	
	public void setTo()
	{
		super.setTo("wss-pc");
	}

	
	
	public ArrayList<String> getUserList()
	{
		return userList;
	}


	public void setUserList(ArrayList<String> userList)
	{
		this.userList = userList;
	}
	
	public void addUser(String user)
	{
		this.userList.add(user);
	}

	@Override
	public String getChildElementXML()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(name).append(" xmlns=\"").append(namespace).append("\">");
		// TODO Auto-generated method stub
		buf.append("</").append(name).append(">");
		return buf.toString();
	}
	
	
}
