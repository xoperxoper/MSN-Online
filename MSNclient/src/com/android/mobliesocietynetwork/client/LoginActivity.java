package com.android.mobliesocietynetwork.client;

import java.net.Socket;
import java.util.HashMap;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.FriendDB;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.LabelPacket;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.*;

/*
 * 
 * 登陆界面
 * 
 * */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class LoginActivity extends MyActivity
{
	private EditText uid;
	private EditText pwd;
	private Button log;
	private Button reg;
	// private MyApplication application;
	private SharePreferenceUtil util;
	private FriendDB friendDB;
	
	

	// private CommunityManager manager;
	// private Dialog mDialog = null;
	// private final String SERVER_HOST_IP = "192.168.1.222"; //
	// 服务器IP:192.168.1.222
	// private final int SERVER_HOST_PORT = 5222;
	// Socket socket;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// *****2.3以上网络安全性与低版本不兼容，添加如下代码****
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		// ******************************************
		super.onCreate(savedInstanceState);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		// application = (MyApplication) this.getApplicationContext();
		setContentView(R.layout.activity_login);
		friendDB = new FriendDB(LoginActivity.this);
		// manager = new CommunityManager(this);
		PacketFilter pf=new PacketFilter()
		{
			
			@Override
			public boolean accept(Packet arg0)
			{
				// TODO Auto-generated method stub
				return true;
			}
		};
		XmppTool.getConnection().addPacketSendingListener(new SendListener(), pf);
		initview();
	}

	protected void onResume()
	{
		super.onResume();
		if (!isNetworkAvailable())
		{
			toastshow("网络连接断开");
		}
	}

	void initview()
	{
		uid = (EditText) findViewById(R.id.editusername);
		pwd = (EditText) findViewById(R.id.editpassword);
		log = (Button) findViewById(R.id.login);
		reg = (Button) findViewById(R.id.register);
		log.setOnClickListener(new LoginListener());
		reg.setOnClickListener(new RegisterListener());
	}

	class LoginListener implements OnClickListener
	{
		public void onClick(View v)
		{
			String mNametext = uid.getText().toString();
			String pwdtext = pwd.getText().toString();
			if (mNametext.length() == 0 || pwdtext.length() == 0)
			{
				toastshow("帐号或密码不能为空");
			}
			else if (pwdtext.equals("test") && mNametext.equals("666666"))
			{
				// 离线测试
				toastshow("登录成功");
				// util.setId(20132010);
				util.setName("test");
				util.setImg(1);
				util.setPhone("13243535571");
				util.setSex("men");
				util.setEmail("492767618@qq.com");
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			// Toast("正在验证账号");
			// 通过Socket验证信息
			else
			{
				toastshow("正在连接……请稍等");
				try
				{
					// 建立连接并登录
					util.setName(mNametext);
					util.setImg(1);
					util.setPhone("1001010086");
					util.setSex("men");
					util.setEmail("baidu@www.baidu.com");
					XmppTool.getConnection().login(mNametext, pwdtext);
					Presence presence = new Presence( // Presence是Packet的一个子类
							Presence.Type.available);
					XmppTool.getConnection().sendPacket(presence);
					
					/********************* LabelTest ****************************/
//					HashMap<String, String> mapTest = new HashMap<String, String>();
//					
//					mapTest.clear();
//					mapTest.put("label1", "sport");
//					mapTest.put("label2", "music");
//					mapTest.put("label3", "major");
//					mapTest.put("label4", "fruit");
//					mapTest.put("label5", "idol");
//							
//					LabelPacket labelTest = new LabelPacket(mapTest);
//					XmppTool.getConnection().sendPacket(labelTest);
					/************************************************************/
					
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					intent.putExtra("mName", mNametext);
					startActivity(intent);
					finish();
				}
				catch (XMPPException e)
				{
					XmppTool.closeConnection();
					toastshow("登录失败，请检查账户名和密码");
				}
			}
		}
	}

	class RegisterListener implements OnClickListener
	{
		public void onClick(View v)
		{
			Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			
		}
	}
	private class SendListener implements PacketListener{

		@Override
		public void processPacket(Packet packet)
		{
			// TODO Auto-generated method stub
			Packet p=packet;
			String xml=p.toXML();
			String s=p.getXmlns();
		}
		
	}

	private void toastshow(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 1, 1, "关于");
		menu.add(0, 2, 2, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		if (item.getItemId() == 2)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null)
		{
			for (int i = 0; i < info.length; i++)
			{
				if (info[i].getState() == NetworkInfo.State.CONNECTED)
				{
					return true;
				}
			}
		}
		return false;
	}
}
