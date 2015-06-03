package com.android.mobliesocietynetwork.client.util.bean;



import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

/*
 * ��չ��iq�������ڴ����û��ĵ���λ����Ϣ 
 * 
 * */
public class LonlatPacket extends IQ
{
	private LonlatExtensionPacket lonlatExtensionPacket;
	
	public LonlatPacket(String labelName,String labelNamespace){
		super();
		setTo();
		lonlatExtensionPacket = new LonlatExtensionPacket(labelName, labelNamespace);
		

	}
	
	public void addInfo(String user,String longitude,String latitude)
	{
		
		lonlatExtensionPacket.addUser(user);
		lonlatExtensionPacket.addLongitude(longitude);
		lonlatExtensionPacket.addLatitude(latitude);
		
	}
	
	
	public ArrayList<String> getUserList()
	{
		return lonlatExtensionPacket.getUserList();
	}
	
	public ArrayList<String> getLonArrayList()
	{
		return lonlatExtensionPacket.getLongitudeList();
	}
	
	public ArrayList<String> getLatArrayList()
	{
		return lonlatExtensionPacket.getLatitudeList();
	}


	public void setTypeSet(){
		super.setType(Type.SET);
	}
	
	public void setTypeGet(){
		super.setType(Type.GET);
	}
	
	public void setTo()
	{
		super.setTo("wss-pc");
	}
	
	@Override
	public String getChildElementXML()
	{
		return lonlatExtensionPacket.toXML();
	}	
}
