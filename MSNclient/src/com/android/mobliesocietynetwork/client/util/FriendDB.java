package com.android.mobliesocietynetwork.client.util;

import java.util.ArrayList;
import java.util.List;

import com.android.mobliesocietynetwork.client.util.bean.User;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * 
 * �������ݿ⣬ÿ���û���id��Ϊ�������洢�û��ĺ����б�
 * 
 * */
public class FriendDB
{
	private SQLiteDatabase db;

	public FriendDB(Context context)
	{
		db = context.openOrCreateDatabase(Constants.FRIENDDBNAME,
				Context.MODE_PRIVATE, null);
	}

	public void insertFriend(String name, User user)
	{
		db.execSQL("CREATE table IF NOT EXISTS _"
				+ name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uname TEXT ,"
				+ " img INTEGER , phone TEXT , sex TEXT ,email TEXT , isonline INTEGER , groups TEXT)");
		db.execSQL(
				"insert or ignore into _"
						+ name
						+ " (uname ,img ,phone,sex,email,isonline,groups) values(?,?,?,?,?,?,?)",
				new Object[] { user.getName(), user.getImg(), user.getPhone(),
						user.getSex(), user.getEmail(), user.getIsOnline(),
						user.getGroup() });
	}

	public List<User> getFriendlist(String name)
	{
		List<User> list = new ArrayList<User>();
		Cursor c = db.rawQuery("SELECT * from _" + name, null);
		while (c.moveToNext())
		{
			User u = new User();
			// u.setId(c.getInt(c.getColumnIndex("uid")));
			u.setName(c.getString(c.getColumnIndex("uname")));
			u.setImg(c.getInt(c.getColumnIndex("img")));
			u.setPhone(c.getString(c.getColumnIndex("phone")));
			u.setSex(c.getString(c.getColumnIndex("sex")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setIsOnline(c.getInt(c.getColumnIndex("isonline")));
			u.setGroup(c.getString(c.getColumnIndex("groups")));
			list.add(u);
		}
		c.close();
		return list;
	}

	public List<User> getOnlineFriendlist(String name)
	{
		List<User> list = new ArrayList<User>();
		Cursor c = db.rawQuery("SELECT * from _" + name + " where isonline=1 ",
				null);
		while (c.moveToNext())
		{
			User u = new User();
			// u.setId(c.getInt(c.getColumnIndex("uid")));
			u.setName(c.getString(c.getColumnIndex("uname")));
			u.setImg(c.getInt(c.getColumnIndex("img")));
			u.setPhone(c.getString(c.getColumnIndex("phone")));
			u.setSex(c.getString(c.getColumnIndex("sex")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setIsOnline(c.getInt(c.getColumnIndex("isonline")));
			u.setGroup(c.getString(c.getColumnIndex("groups")));
			list.add(u);
		}
		c.close();
		return list;
	}

	 public User searchfriendbyname(String name,String uname){
		 User u=new User();
		 Cursor c = db.rawQuery("SELECT * from _" + name + " where uname="+uname, null);
		 while (c.moveToNext()) 
		 {
			 u.setName(c.getString(c.getColumnIndex("uname")));
			 u.setImg(c.getInt(c.getColumnIndex("img")));
			 u.setPhone(c.getString(c.getColumnIndex("phone")));
			 u.setSex(c.getString(c.getColumnIndex("sex")));
			 u.setEmail(c.getString(c.getColumnIndex("email")));
			 u.setIsOnline(c.getInt(c.getColumnIndex("isonline")));
			 u.setGroup(c.getString(c.getColumnIndex("groups")));	
		 }
	 	c.close();
	 	return u;
	 }
	 
	public void updataFriendlist(String name, User user)
	{
		db.execSQL("UPDATE _" + name + " set uname =" + user.getName()
				+ ",set img=" + user.getImg() + ",set phone=" + user.getPhone()
				+ ",set sex=" + user.getSex() + ",set email=" + user.getEmail()
				+ ",set isonline=" + user.getIsOnline() + ",set groups="
				+ user.getGroup() + "where uname =" + user.getName());
	}

	public void updataonlineFriendlist(String name, String uname, int isonline)
	{
		db.execSQL("UPDATE _" + name + "SET isonline=" + isonline
				+ "WHERE uname =" + uname);
	}

	public void deleteFriendlist(String name, String uname)
	{
		db.execSQL("DELETE from _" + name + " WHERE uname =" + uname);
	}

	public void initialtable(String name)
	{
		db.execSQL("CREATE table IF NOT EXISTS _"
				+ name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uname TEXT ,"
				+ " img INTEGER , phone TEXT , sex TEXT ,email TEXT , isonline INTEGER , groups TEXT)");
		db.execSQL("DROP TABLE _" + name);
	}

	public void close()
	{
		if (db != null)
			db.close();
	}
}
