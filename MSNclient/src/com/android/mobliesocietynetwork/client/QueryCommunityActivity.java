package com.android.mobliesocietynetwork.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.MultiUserChatDB;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;


import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QueryCommunityActivity extends MyActivity
{

	private ListView listView;
	private static XMPPConnection connection;
	private SharePreferenceUtil util;
	private MultiUserChatDB mutiUserChatDB;
	private List<String> list;
	private String clickedCom;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_community);
		ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(QueryCommunityActivity.this);
		
		listView = (ListView) findViewById(R.id.query_listview);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		connection = XmppTool.getConnection();
		mutiUserChatDB = MultiUserChatDB.getInstance();
		
		try
		{
			list=getConferenceRoom();
		} catch (XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Map<String, Object>> listItems=new ArrayList<Map<String, Object>>();
		for(int i=0;i<list.size();i++)
		{
			Map<String, Object> listItem =new LinkedHashMap<String, Object>();
			listItem.put("name", list.get(i));
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, listItems, R.layout.list_view,
				new String[]{"name"}, new int[]{R.id.item_community});
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(new ListClickListener());
	}

	
	class ListClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			// TODO Auto-generated method stub
			TextView tv=(TextView)arg1.findViewById(R.id.item_text);
//			Toast.makeText(getApplicationContext(), "你点击的位置为："+list.get(arg2),Toast.LENGTH_SHORT).show();
			clickedCom=list.get(arg2);
			showDialog();
		}
	}
	
	public void showDialog(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);   
		LayoutInflater factory = LayoutInflater.from(this);  
		final EditText passwordEditText=new EditText(this);
		builder.setIcon(R.drawable.icon);  
		builder.setTitle("请输入密码");  
		builder.setView(passwordEditText);  
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int whichButton) 
			{  
				String passwordtext = passwordEditText.getText().toString();
				MultiUserChat muc=joinMultiUserChat(util.getName(), clickedCom, passwordtext);
				if(muc!=null)
				{
					mutiUserChatDB.addMuc(muc);
					Intent intent = new Intent(QueryCommunityActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}  
		});  
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{  
			public void onClick(DialogInterface dialog, int whichButton) {  
		
		    }  
	    });  
		builder.create().show(); 
	}
	
	
	
	/**	 * 获取服务器上所有会议室	 * @return	 * @throws XMPPException	 */	
	public static List<String> getConferenceRoom() throws XMPPException 
	{		
		List<String> list = new ArrayList<String>();		
		new ServiceDiscoveryManager(connection);		
		if (!MultiUserChat.getHostedRooms(connection,connection.getServiceName()).isEmpty())
		{			
			for (HostedRoom k : MultiUserChat.getHostedRooms(connection,connection.getServiceName())) 
			{				
				for (HostedRoom j : MultiUserChat.getHostedRooms(connection,k.getJid())) 
				{					
					RoomInfo info2 = MultiUserChat.getRoomInfo(connection,j.getJid());		
					if (j.getJid().indexOf("@") > 0) {				
						list.add(j.getName());					
					}
				}
			}
		}
		return list;	
	}
	

	 /** 
     * 加入会议室 
     *  
     * @param user 
     *            昵称 
     * @param password 
     *            会议室密码 
     * @param roomsName 
     *            会议室名 
     */ 
    public MultiUserChat joinMultiUserChat(String user, String roomsName,  
            String password) {  
        if (connection == null)  
            return null;  
        try {  
            // 使用XMPPConnection创建一个MultiUserChat窗口  
            MultiUserChat muc = new MultiUserChat(connection, roomsName  
                    + "@conference." + connection.getServiceName());  
            // 聊天室服务将会决定要接受的历史记录数量  
            DiscussionHistory history = new DiscussionHistory();  
            history.setMaxChars(0);  
            // history.setSince(new Date());  
            // 用户加入聊天室  
            muc.join(user, password, history,  
                    SmackConfiguration.getPacketReplyTimeout());  
            Log.i("MultiUserChat", "会议室【"+roomsName+"】加入成功........");  
            Toast.makeText(getApplicationContext(), "会议室【"+roomsName+"】加入成功", 3000).show();
            return muc;  
        } catch (XMPPException e) {  
            e.printStackTrace();  
            Log.i("MultiUserChat", "会议室【"+roomsName+"】加入失败........");  
            Toast.makeText(getApplicationContext(), "会议室【"+roomsName+"】加入失败", 3000).show();
            return null;  
        }  
    }  
	


}
