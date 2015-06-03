package com.android.mobliesocietynetwork.client.util;

import java.util.ArrayList;
import java.util.List;

import com.android.mobliesocietynetwork.client.util.bean.ChatMsgEntity;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * 
 * 消息数据库，每个用户的id作为表名，存储用户的对话消息
 * 
 * */
public class MessageDB
{
	private SQLiteDatabase db;

	public MessageDB(Context context)
	{
		db = context.openOrCreateDatabase(Constants.MESSAGEDBNAME,
				Context.MODE_PRIVATE, null);
	}

	public void saveMsg(String name, ChatMsgEntity entity)
	{
		db.execSQL("CREATE table IF NOT EXISTS _"
				+ name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, img TEXT,date TEXT,isCome TEXT,message TEXT)");
		int isCome = 0;
		if (entity.getMsgType())
		{
			isCome = 1;
		}
		db.execSQL(
				"insert into _" + name
						+ " (name,img,date,isCome,message) values(?,?,?,?,?)",
				new Object[] { entity.getName(), entity.getImg(),
						entity.getDate(), isCome, entity.getMessage() });
	}

	public List<ChatMsgEntity> getMsg(String name)
	{
		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		db.execSQL("CREATE table IF NOT EXISTS _"
				+ name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, img TEXT,date TEXT,isCome TEXT,message TEXT)");
		Cursor c = db.rawQuery("SELECT * from _" + name
				+ " ORDER BY _id DESC LIMIT 5", null);
		while (c.moveToNext())
		{
			String nameFrom = c.getString(c.getColumnIndex("name"));
			int img = c.getInt(c.getColumnIndex("img"));
			String date = c.getString(c.getColumnIndex("date"));
			int isCome = c.getInt(c.getColumnIndex("isCome"));
			String message = c.getString(c.getColumnIndex("message"));
			boolean isComMsg = false;
			if (isCome == 1)
			{
				isComMsg = true;
			}
			ChatMsgEntity entity = new ChatMsgEntity(nameFrom, date, message, img,
					isComMsg);
			list.add(entity);
		}
		c.close();
		return list;
	}

	public void close()
	{
		if (db != null)
			db.close();
	}
}
