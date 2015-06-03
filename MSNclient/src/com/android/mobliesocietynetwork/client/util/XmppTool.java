package com.android.mobliesocietynetwork.client.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XmppTool
{
	private static XMPPConnection con = null;

	public static XMPPConnection getConnection()
	{
		if (con == null)
		{
			openConnection();
		}
		return con;
	}

	private static void openConnection()
	{
		try//***************************--------------------***************
		{
			// url���˿ڣ�Ҳ�����������ӵķ��������֣���ַ���˿ڣ��û���
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					"192.168.1.222", 5222);
			con = new XMPPConnection(connConfig);
			con.connect();
		}
		catch (XMPPException xe)
		{
			xe.printStackTrace();
		}
	}

	public static void closeConnection()
	{
		if (con != null)
			con.disconnect();
		con = null;
	}
}
