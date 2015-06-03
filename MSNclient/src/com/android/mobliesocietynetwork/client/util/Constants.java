package com.android.mobliesocietynetwork.client.util;

import com.android.mobliesocietynetwork.client.R;

public class Constants
{
	public static final String SERVER_IP = "192.168.1.222";// 服务器ip
	public static final int SERVER_PORT = 5222;// 服务器端口
	public static final String ACTION = "com.way.message";// 消息广播action
	public static final String MSGKEY = "message";// 消息的key
	public static final String IP_PORT = "ipPort";// 保存ip、port的xml文件名
	public static final String SAVE_USER = "saveUser";// 保存用户信息的xml文件名
	public static final String BACKKEY_ACTION = "com.way.backKey";// 返回键发送广播的action
	public static final String FRIENDDBNAME = "qqfri.db";// 数据库名称
	public static final String MESSAGEDBNAME = "qqmes.db";// 数据库名称
	
	
	//标签
	public static final String[] ListTitle={"体育","影视","娱乐","兴趣","关注"};
	public static final String[][] LabelListCN={
		{"篮球","足球","游泳","羽毛球","乒乓球"},
		{"韩剧","美剧","英剧","日剧","国产剧"},
		{"电影","动漫","旅游","购物","KTV"},
		{"看书","写作","美术","音乐","跳舞"},
		{"科技","经济","地产","艺术","时政"}};
	public static final String[][] LabelListEN={
		{"basketball","football","swimming","badminton","tabletennis"},
		{"SKTV","ATV","BTV","JTV","CTV"},
		{"movie","cartoon","travel","shopping","KTV"},
		{"reading","writing","drawing","music","dancing"},
		{"S&T","economy","realestate","art","politics"}};
	public static final int[][] LabelListNUM={
		{1,2,3,4,5},
		{6,7,8,9,10},
		{11,12,13,14,15},
		{16,17,18,19,20},
		{21,22,23,24,25}
		};
	
	public static final int[] IMGS = { R.drawable.icon, R.drawable.f1, R.drawable.f2,
		R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6,
		R.drawable.f7, R.drawable.f8, R.drawable.f9 };// 头像资源
}
