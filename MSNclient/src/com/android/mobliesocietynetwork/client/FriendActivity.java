package com.android.mobliesocietynetwork.client;

/**
 * 好友功能的组界面（还未做美工和好友推荐）
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.NotFilter;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.*;

import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.FriendDB;
import com.android.mobliesocietynetwork.client.util.GetData;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.User;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import org.jivesoftware.smack.RosterGroup;
public class FriendActivity extends MyActivity
{
	private SharePreferenceUtil util;
	List<String> ListUid = new ArrayList<String>();
	String mName;
	User u;
	FriendDB friendDB;
	String searchId = new String();
	String searchName = new String();
	String searchComId = new String();
	String[][] arms = new String[4][4];
	int[] DataUcid = new int[20];

	ArrayList<String> groupList = new ArrayList<String>();
	ArrayList<ArrayList<String>> childList = new ArrayList<ArrayList<String>>();
	GetData getData;
	MyExpandableListAdapter myAdapter;
	ExpandableListView expandListView;
	TextView textview;
	String reqFriId;
	String reqFriName;
	Roster roster;
	EditText newGroupTypeinEditText;
	EditText newFriendTypein;
	EditText groupNewFriendTo;
	String searchedFri;
	String groupFriTo;
	String Pfrom;
	XMPPConnection con;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(FriendActivity.this);
		
		final Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 0x1233)
				{
					final LinearLayout dealRequest = (LinearLayout) getLayoutInflater().inflate(
							R.layout.deal_req, null);
					new AlertDialog.Builder(FriendActivity.this)
							.setTitle("认证审核")
							.setMessage("您收到来自" + Pfrom + "好友添加请求，是否同意并将其加为好友？")
							.setView(dealRequest)
							.setPositiveButton("同意",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											Presence presence_back = new Presence(
													Presence.Type.unsubscribed);
											presence_back
													.setStatus("Ok, let's be friends.");
											presence_back.setTo(Pfrom);
											con.sendPacket(presence_back);
											String groupName = ((EditText) dealRequest
													.findViewById(R.id.acceptGroup))
													.getText().toString();
											try
											{
												roster.createEntry(Pfrom, Pfrom.split("@")[0], new String[] { groupName });	
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
											ArrayList<String> g = new ArrayList<String>();
											if (!groupList.contains(groupName))
											{
												groupList.add(groupName);
												g.add(Pfrom.split("@")[0]);
												childList.add(g);
											}
											else
											{
												int i = groupList
														.indexOf(groupName);
												g = (ArrayList<String>) childList
														.get(i);
												g.add(Pfrom.split("@")[0]);
												childList.set(i, g);
											}
											myAdapter.notifyDataSetChanged();
											expandListView
													.setSelection(groupList
															.size() - 1);
											toastShow("添加好友" + Pfrom + "成功");
										}
									})
							.setNegativeButton("拒绝",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											Presence presence_back = new Presence(
													Presence.Type.unsubscribed);
											presence_back
													.setStatus("Sorry, I won't be your friend.");
											presence_back.setTo(Pfrom);
											con.sendPacket(presence_back);
										}
									}).create().show();
				}
				else if (msg.what == 0x1234)
					toastShow("添加" + Pfrom + "为好友失败");
				else if (msg.what == 0x1235)
				{
					final LinearLayout addNewFriend = (LinearLayout) getLayoutInflater().inflate(
							R.layout.add_new_friend, null);
					new AlertDialog.Builder(FriendActivity.this)
							.setTitle("添加好友成功")
							.setMessage("您添加" + Pfrom + "为好友成功，想把他放在那个分组？")
							.setView(addNewFriend)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											String groupName = ((EditText) addNewFriend
													.findViewById(R.id.typeInNewGroupOfNewFri))
													.getText().toString();
											try
											{
												roster.createEntry(Pfrom, Pfrom.split("@")[0], new String[] { groupName });	
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
											ArrayList<String> g = new ArrayList<String>();
											if (!groupList.contains(groupName))
											{
												groupList.add(groupName);
												g.add(Pfrom.split("@")[0]);
												childList.add(g);
											}
											else
											{
												int i = groupList
														.indexOf(groupName);
												g = (ArrayList<String>) childList
														.get(i);
												g.add(Pfrom.split("@")[0]);
												childList.set(i, g);
											}
											myAdapter.notifyDataSetChanged();
											expandListView
													.setSelection(groupList
															.size() - 1);
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											ArrayList<String> g = new ArrayList<String>();
											groupList.add("");
											g.add(Pfrom.split("@")[0]);
											childList.add(g);
											myAdapter.notifyDataSetChanged();
											expandListView
													.setSelection(groupList
															.size() - 1);
										}
									}).create().show();
				}
			}
		};
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		mName = util.getName();
		friendDB = new FriendDB(FriendActivity.this);
		getData = new GetData(friendDB, mName);

		con = XmppTool.getConnection();
		roster = con.getRoster();
		groupList = getGroups(roster);
		childList = getEntries(roster, groupList);
		myAdapter = new MyExpandableListAdapter(groupList, childList);
		expandListView = (ExpandableListView) findViewById(R.id.list);
		expandListView.setAdapter(myAdapter);
		dealReq(myHandler);
	}
	// 添加好友功能按钮，由此进入搜索好友界面
	public void addFri(View source)
	{
		final LinearLayout wannaAddNewFriend = (LinearLayout) getLayoutInflater().inflate(
				R.layout.wanna_add_friend, null);
		new AlertDialog.Builder(FriendActivity.this)
		.setTitle("添加好友")
		.setView(wannaAddNewFriend)
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which)
					{
						newFriendTypein = (EditText) wannaAddNewFriend
								.findViewById(R.id.typeInWannaedNewFri);
						searchedFri = newFriendTypein.getText()
								.toString();
						Presence presence = new Presence(
								Presence.Type.available);
						presence.setStatus("Can we make friends?");
						presence.setTo(searchedFri);
						con.sendPacket(presence);
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which)
					{
						// TODO Auto-generated method stub
					}
				}).create().show();
	}

	// 包处理
	public void dealReq(Handler myHandler)
	{
		AndFilter presence_wannaAdd_filter = new AndFilter(
				new PacketTypeFilter(Presence.class), new PacketFilter() {
					@Override
					public boolean accept(Packet packet)
					{
						Presence p = (Presence) packet;
						if (p.getType().equals(Presence.Type.available)
								&& p.getStatus().equals("Can we make friends?"))
							return true;
						return false;
					}
				});
		addPacketListenerWithType(presence_wannaAdd_filter, 0x1233, myHandler);
		AndFilter presence_refuseAdd_filter = new AndFilter(
				new PacketTypeFilter(Presence.class), new PacketFilter() {
					@Override
					public boolean accept(Packet packet)
					{
						Presence p = (Presence) packet;
						if (p.getType().equals(Presence.Type.unsubscribed)
								&& p.getStatus().equals(
										"Sorry, I won't be your friend."))
							return true;
						return false;
					}
				});
		addPacketListenerWithType(presence_refuseAdd_filter, 0x1234, myHandler);
		AndFilter presence_passAdd_filter = new AndFilter(new PacketTypeFilter(
				Presence.class), new PacketFilter() {
			@Override
			public boolean accept(Packet packet)
			{
				Presence p = (Presence) packet;
				if (p.getType().equals(Presence.Type.unsubscribed) 
						&& p.getStatus().equals(
								"Ok, let's be friends."))
					return true;
				return false;
			}
		});
		addPacketListenerWithType(presence_passAdd_filter, 0x1235, myHandler);
	}

	public void addPacketListenerWithType(AndFilter filter, final int what,
			final Handler myHandler)
	{
		con.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet)
			{
				Pfrom = packet.getFrom();
				myHandler.sendEmptyMessage(what);
			}
		}, filter);
	}

	// 新建分组功能按钮
	public void addGroup(View source)
	{
		final LinearLayout newGroupTypein = (LinearLayout) getLayoutInflater().inflate(
				R.layout.type_in_newgroup, null);
		new AlertDialog.Builder(this)
				.setTitle("新建分组")
				.setMessage("请输入新的分组名")
				.setView(newGroupTypein)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						newGroupTypeinEditText = (EditText) newGroupTypein
								.findViewById(R.id.newGroupTypein);
						String gName = newGroupTypeinEditText.getText()
								.toString();
						if (addGro(roster, gName))
							toastShow("新建分组成功");
						toastShow("新建分组失败");
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
					}
				}).create().show();
	}

	public ArrayList<String> getGroups(Roster roster)
	{
		ArrayList<String> groupsList = new ArrayList<String>();
		Collection<RosterGroup> rosterGroups = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroups.iterator();
		while (i.hasNext())
			groupsList.add(i.next().getName());
		return groupsList;
	}

	public ArrayList<ArrayList<String>> getEntries(Roster roster,
			ArrayList<String> groupsList)
	{
		ArrayList<ArrayList<String>> childList = new ArrayList<ArrayList<String>>();
		Iterator<String> i = groupsList.iterator();
		while (i.hasNext())
		{
			RosterGroup rosterGroup = roster.getGroup(i.next());
			ArrayList<String> group = new ArrayList<String>();
			Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
			Iterator<RosterEntry> j = rosterEntry.iterator();
			while (j.hasNext())
				group.add(j.next().getName());
			childList.add(group);
		}
		return childList;
	}

	// **************************填写好友列表***********************************//
	private class MyExpandableListAdapter extends BaseExpandableListAdapter
	{
		private ArrayList<String> groupList;
		private ArrayList<ArrayList<String>> childList;

		MyExpandableListAdapter(ArrayList<String> groupList,
				ArrayList<ArrayList<String>> childList)
		{
			this.groupList = groupList;
			this.childList = childList;
		}

		// 获取指定组位置、指定子列表项处的子列表项数据
		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			return childList.get(groupPosition).size();
		}

		// 组选项的内容生成函数
		private TextView getTextView()
		{
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 40);
			TextView textView = new TextView(FriendActivity.this);
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(50, 0, 0, 0);
			textView.setTextSize(20);
			textView.setTextColor(getResources().getColor(R.color.red));
			return textView;
		}

		// 子选项的内容生成函数
		private Button getButton(final int groupPosition,
				final int childPosition)
		{
			final String selectFri = new String(childList.get(groupPosition)
					.get(childPosition));
			Button button = new Button(FriendActivity.this);
			button.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
			button.setBackgroundColor(getResources().getColor(R.color.white));
			button.setPadding(40, 0, 0, 0);
			button.setTextSize(20);
			button.setTextColor(getResources().getColor(R.color.green));
			// 单击好友，进入对话框
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					// toastShow("你单击的人为："+selectFriName+selectFriId);
					User user = new User();
					user.setName(selectFri);
					// user.setId(selectFriId);
					Intent i = new Intent(FriendActivity.this,
							DialogActivity.class);
					i.putExtra("user", user);
					startActivity(i);
				}
			});
			// 长触好友时，实行删除好友操作
			button.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v)
				{
					new AlertDialog.Builder(FriendActivity.this)
							.setTitle("删除好友")
							.setMessage("确定要删除该用户么？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											try
											{
												RosterEntry entry = roster
														.getEntry(selectFri
																+ "@wss-pc");
												roster.removeEntry(entry);
												toastShow("删除成功");
												ArrayList<String> g = (ArrayList<String>) childList
														.get(groupPosition);
												g.remove(childPosition);
												childList.set(groupPosition, g);
												notifyDataSetChanged();
											}
											catch (Exception e)
											{
												e.printStackTrace();
												toastShow("删除失败");
											}
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											// TODO Auto-generated method stub
										}
									}).create().show();
					return true;
				}
			});
			return button;
		}

		// 该方法决定每个子选项的外观
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent)
		{
			Button button = getButton(groupPosition, childPosition);
			button.setText(getChild(groupPosition, childPosition).toString());
			return button;
		}

		// 获取指定组位置处的组数据
		@Override
		public Object getGroup(int groupPosition)
		{
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount()
		{
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		// 该方法决定每个组选项的外观
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent)
		{
			LinearLayout ll = new LinearLayout(FriendActivity.this);
			ll.setOrientation(0);
			TextView textview = getTextView();
			textview.setText(getGroup(groupPosition).toString());
			ll.addView(textview);
			return ll;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}

		@Override
		public boolean hasStableIds()
		{
			return true;
		}
	}

	public boolean addGro(Roster roster, String groupName)
	{
		try
		{
			roster.createGroup(groupName);
			groupList.add(groupName);
			ArrayList<String> i = new ArrayList<String>();
			i.clear();
			childList.add(i);
			myAdapter.notifyDataSetChanged();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	

	// 提示框函数
	public void toastShow(String text)
	{
		Toast.makeText(FriendActivity.this, text, 3000).show();
	}


}
