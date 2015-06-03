package com.android.mobliesocietynetwork.client.util;


import android.content.Context;
import android.content.SharedPreferences;

/*
 * 
 * ���汾���û����ݣ������ڱ����¼�ߵ���Ϣ��
 * 
 * */
public class SharePreferenceUtil
{
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file)
	{
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// �û�������
	public void setPasswd(String passwd)
	{
		editor.putString("passwd", passwd);
		editor.commit();
	}

	public String getPasswd()
	{
		return sp.getString("passwd", "");
	}

	// �û�������
	public void setName(String name)
	{
		editor.putString("name", name);
		editor.commit();
	}
	
	public String getName()
	{
		return sp.getString("name", "");
	}

	// �û����Ա�
	public String getSex()
	{
		return sp.getString("sex", "");
	}

	public void setSex(String sex)
	{
		editor.putString("sex", sex);
		editor.commit();
	}

	// �û�������
	public String getEmail()
	{
		return sp.getString("email", "");
	}

	public void setEmail(String email)
	{
		editor.putString("email", email);
		editor.commit();
	}

	// �û��ĵ绰
	public String getPhone()
	{
		return sp.getString("phone", "");
	}

	public void setPhone(String phone)
	{
		editor.putString("phone", phone);
		editor.commit();
	}

	// �û��Լ���ͷ��
	public int getImg()
	{
		return sp.getInt("img", 0);
	}

	public void setImg(int i)
	{
		editor.putInt("img", i);
		editor.commit();
	}

	// ip
	public void setIp(String ip)
	{
		editor.putString("ip", ip);
		editor.commit();
	}

	public String getIp()
	{
		return sp.getString("ip", Constants.SERVER_IP);
	}

	// �˿�
	public void setPort(int port)
	{
		editor.putInt("port", port);
		editor.commit();
	}

	public int getPort()
	{
		return sp.getInt("port", Constants.SERVER_PORT);
	}

	// �Ƿ��ں�̨���б��
	public void setIsStart(boolean isStart)
	{
		editor.putBoolean("isStart", isStart);
		editor.commit();
	}

	public boolean getIsStart()
	{
		return sp.getBoolean("isStart", false);
	}

	// �Ƿ��һ�����б�Ӧ��
	public void setIsFirst(boolean isFirst)
	{
		editor.putBoolean("isFirst", isFirst);
		editor.commit();
	}

	public boolean getisFirst()
	{
		return sp.getBoolean("isFirst", true);
	}
}
