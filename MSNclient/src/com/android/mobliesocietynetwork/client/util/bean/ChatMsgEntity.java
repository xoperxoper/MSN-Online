package com.android.mobliesocietynetwork.client.util.bean;

/*
 * 
 * �Ի���Ϣʵ�壬���ڶԻ�����
 * 
 * */
public class ChatMsgEntity
{
	private String name; // ��Ϣ����
	private String date;// ��Ϣ����
	private String message;// ��Ϣ����
	private int img;
	private boolean isComMeg = true;// �Ƿ�Ϊ�յ�����Ϣ

	public ChatMsgEntity()
	{}

	public ChatMsgEntity(String name, String date, String text, int img,
			boolean isComMsg)
	{
		super();
		this.name = name;
		this.date = date;
		this.message = text;
		this.img = img;
		this.isComMeg = isComMsg;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public boolean getMsgType()
	{
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg)
	{
		isComMeg = isComMsg;
	}

	public int getImg()
	{
		return img;
	}

	public void setImg(int img)
	{
		this.img = img;
	}
}
