package com.android.mobliesocietynetwork.client;

import java.util.ArrayList;
import java.util.List;

import com.android.mobliesocietynetwork.client.LoginActivity.LoginListener;
import com.android.mobliesocietynetwork.client.R.id;
import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.User;

import android.animation.AnimatorSet.Builder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends MyActivity
{
	private SharePreferenceUtil util;
	// private User user;
	// private MyApplication application;
	// private FriendDB friendDB;
	private RadioGroup radioGroup;
	private Button menuButton;
	private TextView userName; // 用户昵称
	private ImageView userImage; // 用户头像
	private final int LISTDIALOG = 1;
	// 页卡内容
	private ViewPager mPager;
	// Tab页面列表
	private List<View> listViews;
	// 当前页卡编号
	private LocalActivityManager manager = null;
	private MyPagerAdapter mpAdapter = null;
	private int index;
	

	// 更新intent传过来的值
	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{}

	@Override
	public void onBackPressed()
	{
		Log.i("", "onBackPressed()");
		super.onBackPressed();
	}

	@Override
	protected void onPause()
	{
		Log.i("", "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		Log.i("", "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		Log.i("", "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (getIntent() != null)
		{
			index = getIntent().getIntExtra("index", 0);
			mPager.setCurrentItem(index);
			setIntent(null);
		}
		else
		{
			if (index < 3)
			{
				index = index + 1;
				mPager.setCurrentItem(index);
				index = index - 1;
				mPager.setCurrentItem(index);
			}
			else if (index == 3)
			{
				index = index - 1;
				mPager.setCurrentItem(index);
				index = index + 1;
				mPager.setCurrentItem(index);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		// application = (MyApplication) this.getApplicationContext();
		// friendDB = new FriendDB(MainActivity.this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(MainActivity.this);
		
		mPager = (ViewPager) findViewById(R.id.main_viewpager);
		userName = (TextView) findViewById(R.id.main_uname);
		userName.setText(util.getName());
		userImage = (ImageView) findViewById(R.id.main_uimage);
		userImage.setImageResource(Constants.IMGS[util.getImg()]);
		menuButton = (Button) findViewById(R.id.main_menu);
		menuButton.setOnClickListener(new MenuListener());
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();
		radioGroup = (RadioGroup) this.findViewById(R.id.main_radiogroup);
		radioGroup.check(R.id.main_radio_friend);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId)
					{
						switch (checkedId)
						{
							case R.id.main_radio_friend:
								index = 0;
								listViews.set(
										0,
										getView("A", new Intent(
												MainActivity.this,
												FriendActivity.class)));
								mpAdapter.notifyDataSetChanged();
								mPager.setCurrentItem(0);
								break;
							case R.id.main_radio_community:
								index = 1;
								listViews.set(
										1,
										getView("B", new Intent(
												MainActivity.this,
												CommunityActivity.class)));
								mpAdapter.notifyDataSetChanged();
								mPager.setCurrentItem(1);
								break;
							case R.id.main_radio_map:
								index = 2;
								listViews.set(
										2,
										getView("C", new Intent(
												MainActivity.this,
												MapActivity.class)));
								mpAdapter.notifyDataSetChanged();
								mPager.setCurrentItem(2);
								break;
							default:
								break;
						}
					}
				});
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager()
	{
		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(MainActivity.this, FriendActivity.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MainActivity.this, CommunityActivity.class);
		listViews.add(getView("B", intent));
		intent = new Intent(MainActivity.this, MapActivity.class);
		listViews.add(getView("C", intent));
		mPager.setOffscreenPageLimit(0);
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter
	{
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews)
		{
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0)
		{}

		@Override
		public int getCount()
		{
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1)
		{
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1)
		{}

		@Override
		public Parcelable saveState()
		{
			return null;
		}

		@Override
		public void startUpdate(View arg0)
		{}
	}

	/**
	 * 页卡切换监听，ViewPager改变同样改变TabHost内容
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener
	{
		@SuppressWarnings("deprecation")
		public void onPageSelected(int arg0)
		{
			manager.dispatchResume();
			switch (arg0)
			{
				case 0:
					index = 0;
					radioGroup.check(R.id.main_radio_friend);
					listViews.set(
							0,
							getView("A", new Intent(MainActivity.this,
									FriendActivity.class)));
					mpAdapter.notifyDataSetChanged();
					break;
				case 1:
					index = 1;
					radioGroup.check(R.id.main_radio_community);
					listViews.set(
							1,
							getView("B", new Intent(MainActivity.this,
									CommunityActivity.class)));
					mpAdapter.notifyDataSetChanged();
					break;
				case 2:
					index = 2;
					radioGroup.check(R.id.main_radio_map);
					listViews.set(
							2,
							getView("C", new Intent(MainActivity.this,
									MapActivity.class)));
					mpAdapter.notifyDataSetChanged();
					break;
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{}

		public void onPageScrollStateChanged(int arg0)
		{}
	}

	class MenuListener implements OnClickListener
	{
		@SuppressWarnings("deprecation")
		public void onClick(View v)
		{
			showDialog(LISTDIALOG);
		}
	}

	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		switch (id)
		{
			case LISTDIALOG:
				android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
						this);
				builder.setTitle("主菜单");
				builder.setItems(new String[] { "广播在线好友", "搜索推荐好友", "等待" },
						new DialogInterface.OnClickListener() {
							// builder.setIcon(R.drawable.dialog);
							public void onClick(
		
									DialogInterface dialogInterface, int which)
							{
								Intent intent;
								switch (which)
								{
									case 0:
										User u = new User();
										u.setName("广播在线好友");
										intent = new Intent(
												MainActivity.this,
												DialogActivity.class);
										intent.putExtra("user", u);
										startActivity(intent);
										break;
									case 1:
										intent = new Intent(MainActivity.this,QueryRecommendActivity.class);
										startActivity(intent);
										break;
									case 2:
										break;
								}
							}
						});
				dialog = builder.create();
				break;
		}
		return dialog;
	}

	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent)
	{
		return manager.startActivity(id, intent).getDecorView();
	}


}
