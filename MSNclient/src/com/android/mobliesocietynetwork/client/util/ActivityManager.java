package com.android.mobliesocietynetwork.client.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.provider.ContactsContract.Contacts.Data;
/**
 * 
 * Activity�������ڳ����˳�ʱ��������������
 * 
 * */
public class ActivityManager extends Application
{
	// ����������
	private List<Activity> activityList = new LinkedList<Activity>();

	// �õ���ģʽ����֤�����ActivityManager ������Ӧ����ֻ��һ��
	private static ActivityManager instance;

	private ActivityManager()
	{

	}

	public static ActivityManager getInstance()
	{
		if (instance == null)
		{
			instance = new ActivityManager();
		}
		return instance;
	}

	// �������У����Activity
	public void addActivity(Activity activity)
	{
		activityList.add(activity);
	}

	// ��������Ӧ�ó���
	public void exit()
	{

		// ���� ��������ɱ������Activity
		for (Activity activity : activityList)
		{
			if (!activity.isFinishing())
			{
				activity.finish();
			}
		}
		// ɱ�������Ӧ�ó���Ľ��̣��ͷ� �ڴ�
		int id = android.os.Process.myPid();
		if (id != 0)
		{
			android.os.Process.killProcess(id);
		}
	}

}
