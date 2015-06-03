package com.android.mobliesocietynetwork.client;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.MultiUserChatDB;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.User;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class CommunityActivity extends MyActivity
{

	private static XMPPConnection connection;
	private MultiUserChatDB mutiUserChatDB;
	LinearLayout comPageActionLayout;
	LinearLayout comPageItems;
	Button comPageCreation;
	Button comPageSearch;
	EditText comPageInput;
	TextView textview;
	TextView communityItemTextView;
	View communityItemView;
	LayoutInflater inflater;
	private SharePreferenceUtil util;
	private TextView comNameTextView;
	private TextView passwordTextView;
	private RadioGroup mRadioGroup;
	private boolean isPersistentroom=false;
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);
		ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(CommunityActivity.this);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		connection=XmppTool.getConnection();
		mutiUserChatDB=MultiUserChatDB.getInstance();
		if (savedInstanceState == null)
		{
			getFragmentManager().beginTransaction()
					.add(R.id.community, new PlaceholderFragment()).commit();
		}
		initUI();
		initData();
	}

	
	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
		initData();
	}

	public void initData()
	{
		inflater = LayoutInflater.from(this);
		ArrayList<String> existingCom = mutiUserChatDB.getAllName();
		comPageItems.removeAllViews();
		for (int i = 0; i < existingCom.size(); i++)
		{
			communityItemView = inflater.inflate(R.layout.community_item, null);
			communityItemTextView = (TextView) communityItemView.findViewById(R.id.item_text);
			communityItemTextView.setText(existingCom.get(i));
			communityItemTextView.setOnLongClickListener(new textlonglister()); // �����¼�
			comPageItems.addView(communityItemView);
		}
	}


	public void initUI()
	{
		comPageCreation = (Button) findViewById(R.id.community_action_creation);
		comPageSearch = (Button) findViewById(R.id.community_action_search);
		comPageItems = (LinearLayout) findViewById(R.id.community_items);
		// ����community�ڵ�listener
		MyOnclickListener mListener = new MyOnclickListener();
		comPageCreation.setOnClickListener(mListener);
		comPageSearch.setOnClickListener(mListener);
		comNameTextView= (TextView) findViewById(R.id.tvComName);
		passwordTextView= (TextView) findViewById(R.id.tvPassWord);
		mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
	}

	class MyOnclickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch (v.getId())
			{
				case R.id.community_action_creation:
					ShowDialogCreatCommunity();
					break;
				case R.id.community_action_search:
					Intent intent = new Intent(CommunityActivity.this, QueryCommunityActivity.class);
					startActivity(intent);
					break;
			}
		}
	}

	
	
	public void ShowDialogCreatCommunity(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);   
		LayoutInflater factory = LayoutInflater.from(this);  
		final View textEntryView = factory.inflate(R.layout.creat_community_dialog, null);  
		builder.setTitle("����������");  
		builder.setView(textEntryView);  
		mRadioGroup = (RadioGroup)textEntryView.findViewById(R.id.radioGroup);
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				//��ȡ������ѡ�����ID
				if (arg1 == R.id.radioButton1) 
				{
//					Toast.makeText(getApplicationContext(), "����", 1).show();
					isPersistentroom=true;
				} 
				else if (arg1 == R.id.radioButton2) 
				{
//					Toast.makeText(getApplicationContext(), "��ʱ", 1).show();
					isPersistentroom=false;
				}
			}
		});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int whichButton) 
			{  

				EditText comName = (EditText)textEntryView.findViewById(R.id.etComName);  
				EditText password = (EditText)textEntryView.findViewById(R.id.etPassWord);
//				RadioGroup mRadioGroup = (RadioGroup) textEntryView.findViewById(R.id.radioGroup);
//				RadioButton r1=(RadioButton)textEntryView.findViewById(R.id.radioButton1);
//				RadioButton r2=(RadioButton)textEntryView.findViewById(R.id.radioButton2);
				String comNametext =comName.getText().toString();
				String passwordtext =password.getText().toString();
				MultiUserChat muc=createRoom(util.getName(), comNametext, passwordtext,isPersistentroom);
				if(muc!=null)
				{
					mutiUserChatDB.addMuc(muc);
					ArrayList<String> existingCom = mutiUserChatDB.getAllName();
					comPageItems.removeAllViews();
					for (int i = 0; i < existingCom.size(); i++)
					{
						communityItemView = inflater.inflate(R.layout.community_item, null);
						communityItemTextView = (TextView) communityItemView.findViewById(R.id.item_text);
						communityItemTextView.setText(existingCom.get(i));
						comPageItems.addView(communityItemView);
					}
					Toast.makeText(getApplicationContext(), "�����Ҵ����ɹ���", 3000).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "�����Ҵ���ʧ�ܣ�", 3000).show();
				}
			}  
		});  
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
		{  
			public void onClick(DialogInterface dialog, int whichButton) {  
		
		    }  
	    });  
		builder.create().show(); 
	}
	
	// xml���textview�����˵���ͳ�����ʽ
	public void CommunityItemOnClick(View v)
	{ 
		TextView clickedTextView = (TextView) v;
		String selectcName = clickedTextView.getText().toString();
		User u=new User();
		u.setName(selectcName);
		u.setIsCom(true);
		// Toast.makeText(getApplicationContext(),clickedTextView.getText().toString(),3000).show();
		Intent intent = new Intent(CommunityActivity.this, DialogActivity.class);
		intent.putExtra("user", u);
		startActivity(intent);
	}

	// �����˳�����
	class textlonglister implements OnLongClickListener
	{
		public boolean onLongClick(View v)
		{
			String quitcname = communityItemTextView.getText().toString();
			new AlertDialog.Builder(CommunityActivity.this)
					.setTitle("ȷ�ϲ���")
					.setMessage("��ȷ��Ҫ�˳���" + quitcname + "������")
					.setPositiveButton("ȷ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									//û��multiuserchat�ҵ���ô�˳���								
									comPageItems.removeAllViews();
									ArrayList<String> existingCom = mutiUserChatDB.getAllName();
									for (int i = 0; i < existingCom.size(); i++)
									{
										communityItemView = inflater.inflate(
												R.layout.community_item, null);
										communityItemTextView = (TextView) communityItemView
												.findViewById(R.id.item_text);
										communityItemTextView
												.setText(existingCom.get(i));
										comPageItems.addView(communityItemView);
									}
								}
							})
					.setNegativeButton(
							"ȡ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									// ȡ��ɾ��������ʲô������
								}
							}).create().show();
			return false;
		}
	}

	
	/** 
     * �������� 
     *  
     * @param roomName 
     *            �������� 
     */ 
    public MultiUserChat createRoom(String user, String roomName,  
            String password,boolean isPersistentroom) {  
        if (connection == null)  
            return null;  
   
        MultiUserChat muc = null;  
        try {  
            // ����һ��MultiUserChat  
            muc = new MultiUserChat(connection, roomName + "@conference." 
                    + connection.getServiceName());  
            // ����������  
            muc.create(roomName);  
            // ��������ҵ����ñ�  
            Form form = muc.getConfigurationForm();  
            // ����ԭʼ������һ��Ҫ�ύ���±���  
            Form submitForm = form.createAnswerForm();  
            // ��Ҫ�ύ�ı����Ĭ�ϴ�  
            for (Iterator<FormField> fields = form.getFields(); fields  
                    .hasNext();) {  
                FormField field = (FormField) fields.next();  
                if (!FormField.TYPE_HIDDEN.equals(field.getType())  
                        && field.getVariable() != null) {  
                    // ����Ĭ��ֵ��Ϊ��  
                    submitForm.setDefaultAnswer(field.getVariable());  
                }  
            }  
            // ���������ҵ���ӵ����  
//            List<String> owners = new ArrayList<String>();  
//            owners.add(connection.getUser());// �û�JID  
//            String ss=connection.getUser();
//            submitForm.setAnswer("muc#roomconfig_roomowners", owners);  
            // �����������ǳ־������ң�����Ҫ����������  
            submitForm.setAnswer("muc#roomconfig_persistentroom", isPersistentroom);  
            // ������Գ�Ա����  
            submitForm.setAnswer("muc#roomconfig_membersonly", false);  
            // ����ռ��������������  
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);  
            if (!password.equals("")) {  
                // �����Ƿ���Ҫ����  
                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",  
                        true);  
                // ���ý�������  
                submitForm.setAnswer("muc#roomconfig_roomsecret", password);  
            }  
            // �ܹ�����ռ������ʵ JID �Ľ�ɫ  
//             submitForm.setAnswer("muc#roomconfig_whois", "anyone");  
            // ��¼����Ի�  
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);  
            // ������ע����ǳƵ�¼  
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);  
            // ����ʹ�����޸��ǳ�  
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);  
            // �����û�ע�᷿��  
            submitForm.setAnswer("x-muc#roomconfig_registration", false);  
            // ��������ɵı�����Ĭ��ֵ����������������������  
            muc.sendConfigurationForm(submitForm);  
        } catch (XMPPException e) {  
            e.printStackTrace();  
            return null;  
        }  
        return muc;  
    }  
   


	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("NewApi")
	public static class PlaceholderFragment extends Fragment
	{
		public PlaceholderFragment()
		{
			
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
