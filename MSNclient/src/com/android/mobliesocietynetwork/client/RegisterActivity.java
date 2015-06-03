package com.android.mobliesocietynetwork.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.LabelPacket;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	private EditText unameEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	private ListView mlistView;
	private MyAdapter myAdapter;
	private Button okButton;

	private boolean[][] LabelSelect=new boolean[5][5];
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		unameEditText=(EditText)findViewById(R.id.username_edittext_register);
		passwordEditText=(EditText)findViewById(R.id.password_edittext_register);
		repasswordEditText=(EditText)findViewById(R.id.repassword_edittext_register);
		mlistView=(ListView)findViewById(R.id.listView_label_register);
		okButton=(Button)findViewById(R.id.reg_button_register);
		okButton.setOnClickListener(new RegListener());
		myAdapter = new MyAdapter(this,R.layout.listview_items_register);
		mlistView.setAdapter(myAdapter);
		
	}

	
	private class RegListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			String uname=unameEditText.getText().toString();
			String password=passwordEditText.getText().toString();
			String repassword=repasswordEditText.getText().toString();
			if(uname.length()!=0&&password.length()!=0&&repassword.length()!=0)
			{
				if(password.equals(repassword))
				{
					if (createAccount(uname, password))
					{
						
						ArrayList<Integer> list = new ArrayList<Integer>();
						for(int i=0;i<LabelSelect.length;i++)
						{
							for(int j=0;j<LabelSelect[i].length;j++)
							{
								if(LabelSelect[i][j]==true)
									list.add(Constants.LabelListNUM[i][j]);
							}
						}
						LabelPacket labelPacket = new LabelPacket("setlabel","com.qiao.test.plugin.label",uname);
						labelPacket.addlabelList(list);
						XmppTool.getConnection().sendPacket(labelPacket);
						Toast.makeText(getApplicationContext(), "恭喜你注册成功", Toast.LENGTH_SHORT).show();
//						finish();
					}
					else
						Toast.makeText(getApplicationContext(), "抱歉，注册失败了，请重新尝试", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "两次密码输入不统一！", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
			}
		}
		
		
	}
	
	private boolean createAccount(String regUserName, String regUserPwd)
	{
		try
		{
			XmppTool.getConnection().getAccountManager().createAccount(regUserName, regUserPwd);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	
	}
	private class MyAdapter extends ArrayAdapter<Object>{
		int mTextViewResourceID = 0;
		private Context mContext;

		public MyAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);
			mTextViewResourceID = textViewResourceId;
			mContext = context;
		}

		public int getCount()
		{
			return Constants.ListTitle.length;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(final int position, View convertView,
		        ViewGroup parent)
		{
			TextView title = null;
			CheckBox checkBox1 = null;
			CheckBox checkBox2 = null;
			CheckBox checkBox3 = null;
			CheckBox checkBox4 = null;
			CheckBox checkBox5 = null;
			
//			if (convertView == null)
//			{
				convertView = LayoutInflater.from(mContext).inflate( mTextViewResourceID, null);
				title = (TextView) convertView.findViewById(R.id.title_textview_listitems_register);
				checkBox1=(CheckBox) convertView.findViewById(R.id.checkBox1_listitems_register);
				checkBox2=(CheckBox) convertView.findViewById(R.id.checkBox2_listitems_register);
				checkBox3=(CheckBox) convertView.findViewById(R.id.checkBox3_listitems_register);
				checkBox4=(CheckBox) convertView.findViewById(R.id.checkBox4_listitems_register);
				checkBox5=(CheckBox) convertView.findViewById(R.id.checkBox5_listitems_register);
				title.setText(Constants.ListTitle[position]);
				checkBox1.setText(Constants.LabelListCN[position][0]);
				checkBox2.setText(Constants.LabelListCN[position][1]);
				checkBox3.setText(Constants.LabelListCN[position][2]);
				checkBox4.setText(Constants.LabelListCN[position][3]);
				checkBox5.setText(Constants.LabelListCN[position][4]);
				checkBox1.setChecked(LabelSelect[position][0]);
				checkBox2.setChecked(LabelSelect[position][1]);
				checkBox3.setChecked(LabelSelect[position][2]);
				checkBox4.setChecked(LabelSelect[position][3]);
				checkBox5.setChecked(LabelSelect[position][4]);
				checkBox1.setOnCheckedChangeListener(new CheckBoxListener(position));
				checkBox2.setOnCheckedChangeListener(new CheckBoxListener(position));
				checkBox3.setOnCheckedChangeListener(new CheckBoxListener(position));
				checkBox4.setOnCheckedChangeListener(new CheckBoxListener(position));
				checkBox5.setOnCheckedChangeListener(new CheckBoxListener(position));
				
//			}
			return convertView;
		}
	}
		
	
	private class CheckBoxListener implements OnCheckedChangeListener{

		private int position;
		public CheckBoxListener(int position)
		{
			// TODO Auto-generated constructor stub
			this.position=position;
		}
		@Override
		public void onCheckedChanged(CompoundButton ButtonView, boolean arg1)
		{
			// TODO Auto-generated method stub
			switch (ButtonView.getId())
			{
				case R.id.checkBox1_listitems_register:
					if(arg1==true)
					{
						LabelSelect[position][0]=true;
					}
					else 
					{
						LabelSelect[position][0]=false;
					}
					break;
				case R.id.checkBox2_listitems_register:
					if(arg1==true)
					{
						LabelSelect[position][1]=true;
					}
					else 
					{
						LabelSelect[position][1]=false;
					}
					break;
				case R.id.checkBox3_listitems_register:
					if(arg1==true)
					{
						LabelSelect[position][2]=true;
					}
					else 
					{
						LabelSelect[position][2]=false;
					}
					break;
				case R.id.checkBox4_listitems_register:
					if(arg1==true)
					{
						LabelSelect[position][3]=true;
					}
					else 
					{
						LabelSelect[position][3]=false;
					}
					break;
				case R.id.checkBox5_listitems_register:
					if(arg1==true)
					{
						LabelSelect[position][4]=true;
					}
					else 
					{
						LabelSelect[position][4]=false;
					}
					break;
				
				default:
					break;
			}
		}
		
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
