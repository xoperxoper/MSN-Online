package com.android.mobliesocietynetwork.client;



import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.XmppTool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * �Զ���һ�������MyActivity�࣬ÿ��Activity���̳�����ʵ����Ϣ�Ľ��գ��Ż����ܣ����ٴ����ظ���
 * 
 * @author way
 * 
 */
public abstract class MyActivity extends Activity 
{
//	/**
//	 * �㲥�����ߣ�����GetMsgService���͹�������Ϣ
//	 */


//	private BroadcastReceiver MsgReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Packet msg = (Packet) intent
//					.getSerializableExtra(Constants.MSGKEY);
//			if (msg != null) {//������ǿգ�˵������Ϣ�㲥
//				// System.out.println("MyActivity:" + msg);
//				getMessage(msg);// ���յ�����Ϣ���ݸ�����
//			} else {//����ǿ���Ϣ��˵���ǹر�Ӧ�õĹ㲥
//				close();
//			}
//		}
//	};
//
//	/**
//	 * ���󷽷����������ദ����Ϣ��ÿ�����ࣨActivity����Ҫ���ô˷��������Packet��
//	 * 
//	 * @param msg
//	 *  ���ݸ��������Ϣ����
//	 */
//	public abstract void getMessage(Packet msg);

	/**
	 * ����ֱ�ӵ�����������ر�Ӧ��
	 */
	public void close() 
	{
//		Intent i = new Intent();
//		i.setAction(Constants.ACTION);
//		sendBroadcast(i);
		finish();
	}

	@Override
	public void onStart() 
	{// ��start������ע��㲥������
		super.onStart();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constants.ACTION);
//		registerReceiver(MsgReceiver, intentFilter);// ע�������Ϣ�㲥

	}

	@Override
	protected void onStop() 
	{// ��stop������ע���㲥������
		super.onStop();
//		unregisterReceiver(MsgReceiver);// ע��������Ϣ�㲥
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "�˳�").setIcon(android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "����").setIcon( android.R.drawable.ic_menu_help);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
	        case Menu.FIRST + 1:
	        {
	        	Toast.makeText(getApplicationContext(), "ɾ���˵��������", Toast.LENGTH_LONG).show();
	        	XmppTool.closeConnection();
	    		ActivityManager.getInstance().exit(); 
	        }
	        break;
	        case Menu.FIRST + 2:
	        {
	            Toast.makeText(this, "�����˵��������", Toast.LENGTH_LONG).show();
	        }
	        break;
		}
		return false;
	}
	
	
}
