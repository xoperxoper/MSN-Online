package com.android.mobliesocietynetwork.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.android.mobliesocietynetwork.client.R.id;
import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.MessageDB;
import com.android.mobliesocietynetwork.client.util.MultiUserChatDB;
import com.android.mobliesocietynetwork.client.util.MyDate;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.ChatMsgEntity;
import com.android.mobliesocietynetwork.client.util.bean.User;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class DialogActivity extends MyActivity
{
	private EditText content;
	private Button send, back;
	private ListView mListView;
	private TextView mFriendName;
	private User user;
	private SharePreferenceUtil util;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	private MessageDB messageDB;
	// private ChatManager cm;
	private Chat chat;
	private XMPPConnection con;
	private Roster roster;
	private MultiUserChatDB multiUserChatDB;
	private MultiUserChat muc;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog);
		ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(DialogActivity.this);
		
		user = (User) getIntent().getSerializableExtra("user");
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		messageDB = new MessageDB(this);
		con = XmppTool.getConnection();
		roster = con.getRoster();
		multiUserChatDB = MultiUserChatDB.getInstance();
		initView();
		initData();
	}

	public void initView()
	{
		mListView = (ListView) findViewById(R.id.listview);
		content = (EditText) findViewById(R.id.chat_editmessage);
		send = (Button) findViewById(R.id.chat_send);
		back = (Button) findViewById(id.chat_back);
		mListView = (ListView) findViewById(id.listview);
		mFriendName = (TextView) findViewById(id.chat_name);
		mFriendName.setText(user.getName());
		if (user.isCom() == true)
		{
			muc = multiUserChatDB.findMuc(user.getName());
			muc.addMessageListener(new multiListener());
		}
		else
		{
			ChatManager cm = initChatManager();
			chat = cm.createChat(user.getName() + "@wss-pc", null);
		}
		send.setOnClickListener(new SendListener());
		back.setOnClickListener(new BackListener());
	}

	public void initData()
	{
		List<ChatMsgEntity> list = messageDB.getMsg(user.getName());
		if (list.size() > 0)
		{
			for (ChatMsgEntity entity : list)
			{
				if (entity.getName() == null)
				{
					entity.setName(user.getName());
				}
				if (entity.getImg() < 0)
				{
					entity.setImg(user.getImg());
				}
				mDataArrays.add(entity);
			}
			Collections.reverse(mDataArrays);
		}
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		mListView.setSelection(mAdapter.getCount() - 1);
	}

	class BackListener implements OnClickListener
	{
		public void onClick(View v)
		{
			// if(chat != null)if (user.getCid() > 0) // 组播
			{
				// if (out != null)
				// {
				// Packet l = new Packet();
				// l.setData(contenttext, null, null, null);
				// l.setData_length();
				// l.setType(1, 1, 0);
				// l.setUid(util.getId());
				// l.setUcid(user.getCid());
				// l.setLength();
				// out.setMsg(l);
				// }
			}
			// chat.close();
			finish();
		}
	}

	class SendListener implements OnClickListener
	{
		public void onClick(View v)
		{
			String contenttext = content.getText().toString();
			if (contenttext.length() > 0)
			{
				// 对话框显示对话
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setName(util.getName());
				entity.setDate(MyDate.getDateEN());
				entity.setMessage(contenttext);
				entity.setImg(util.getImg());
				entity.setMsgType(false);
				messageDB.saveMsg(user.getName(), entity);
				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
				content.setText("");// 清空编辑框数据
				mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
				// 发送给服务器
				if (user.getName().equals("广播在线好友")) // 广播
				{
					Collection<RosterEntry> rosterEntry = roster.getEntries();
					Iterator<RosterEntry> i = rosterEntry.iterator();
					ChatManager c = con.getChatManager();
					while (i.hasNext())
					{
						String name = i.next().getName();
						chat = c.createChat(name + "@wss-pc", null);
						try
						{
							chat.sendMessage(contenttext);
						}
						catch (XMPPException e)
						{
							e.printStackTrace();
						}
					}
				}
				else if (user.isCom() == true) // 组播
				{
					try
					{
						muc.sendMessage(contenttext);
					}
					catch (XMPPException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{ // 单播
					try
					{
						chat.sendMessage(contenttext);
					}
					catch (XMPPException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected ChatManager initChatManager()
	{
		ChatManager c = con.getChatManager();
		c.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able)
			{
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message)
					{
						String[] names = message.getFrom().split("@");
						ChatMsgEntity entity = new ChatMsgEntity(
								user.getName(), MyDate.getDateEN(), message
										.getBody(), util.getImg(), true);
						if (message.getBody()!=null&&message.getFrom().contains(user.getName()))
						{
							messageDB.saveMsg(user.getName(), entity);
							mDataArrays.add(entity);
							mAdapter.notifyDataSetChanged();
							// mListView.setSelection(mListView.getCount() - 1);
						}
						else if(message.getBody()!=null)
						{
							messageDB.saveMsg(user.getName(), entity);// 保存到数据库
							Toast.makeText(
									DialogActivity.this,
									"您有新的消息来自：" + message.getFrom() + ":"
											+ message.getBody(), 0).show();// 其他好友的消息，就先提示，并保存到数据库
						}
					}
				});
			}
		});
		return c;
	}

	/**
	 * 会议室消息监听类
	 * 
	 * @author Administrator
	 * 
	 */
	public class multiListener implements PacketListener
	{
		@Override
		public void processPacket(org.jivesoftware.smack.packet.Packet packet)
		{
			Message message = (Message) packet; // 接收来自聊天室的聊天信息
			String body = message.getBody();
			String from = StringUtils.parseResource(message.getFrom());
			String fromRoomName = StringUtils.parseName(message.getFrom());
			if (!from.equals(util.getName())) // 忽略自己来的消息
			{
				ChatMsgEntity entity = new ChatMsgEntity(from,
						MyDate.getDateEN(), message.getBody(), util.getImg(),
						true);
				if (fromRoomName.contains(user.getName()))
				{
					messageDB.saveMsg(fromRoomName, entity);
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
				}
				else
				{
					messageDB.saveMsg(fromRoomName, entity);// 保存到数据库
					Toast.makeText(
							DialogActivity.this,
							"您有新的消息来自：" + message.getFrom() + ":"
									+ message.getBody(), 0).show();// 其他好友的消息，就先提示，并保存到数据库
				}
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		messageDB.close();
	}

}
