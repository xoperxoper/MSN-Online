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
 * 自定义一个抽象的MyActivity类，每个Activity都继承他，实现消息的接收（优化性能，减少代码重复）
 * 
 * @author way
 * 
 */
public abstract class MyActivity extends Activity 
{
//	/**
//	 * 广播接收者，接收GetMsgService发送过来的消息
//	 */


//	private BroadcastReceiver MsgReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Packet msg = (Packet) intent
//					.getSerializableExtra(Constants.MSGKEY);
//			if (msg != null) {//如果不是空，说明是消息广播
//				// System.out.println("MyActivity:" + msg);
//				getMessage(msg);// 把收到的消息传递给子类
//			} else {//如果是空消息，说明是关闭应用的广播
//				close();
//			}
//		}
//	};
//
//	/**
//	 * 抽象方法，用于子类处理消息。每个子类（Activity）需要调用此方法来获得Packet。
//	 * 
//	 * @param msg
//	 *  传递给子类的消息对象
//	 */
//	public abstract void getMessage(Packet msg);

	/**
	 * 子类直接调用这个方法关闭应用
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
	{// 在start方法中注册广播接收者
		super.onStart();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constants.ACTION);
//		registerReceiver(MsgReceiver, intentFilter);// 注册接受消息广播

	}

	@Override
	protected void onStop() 
	{// 在stop方法中注销广播接收者
		super.onStop();
//		unregisterReceiver(MsgReceiver);// 注销接受消息广播
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "退出").setIcon(android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "帮助").setIcon( android.R.drawable.ic_menu_help);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
	        case Menu.FIRST + 1:
	        {
	        	Toast.makeText(getApplicationContext(), "删除菜单被点击了", Toast.LENGTH_LONG).show();
	        	XmppTool.closeConnection();
	    		ActivityManager.getInstance().exit(); 
	        }
	        break;
	        case Menu.FIRST + 2:
	        {
	            Toast.makeText(this, "帮助菜单被点击了", Toast.LENGTH_LONG).show();
	        }
	        break;
		}
		return false;
	}
	
	
}
