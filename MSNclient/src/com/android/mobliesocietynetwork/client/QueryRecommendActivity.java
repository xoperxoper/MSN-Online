package com.android.mobliesocietynetwork.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.RecommendPacket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class QueryRecommendActivity extends Activity
{

	private Button getRecommendButton;
	private ListView mListView;
	private ArrayList<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_recommend);
		getRecommendButton=(Button)findViewById(R.id.recommend_button_query_recommend);
		getRecommendButton.setOnClickListener(new RecommendListener());
		mListView=(ListView)findViewById(R.id.listView_query_recommend);
		
		ProviderManager.getInstance().addIQProvider("query", "com.qiao.test.plugin.recommemd",new RecommendIQProvider());
		PacketFilter packetFilterIQ = new PacketTypeFilter(IQ.class);  

		XmppTool.getConnection().addPacketListener(new RecommendPacketListener(), packetFilterIQ);

		listItems=new ArrayList<Map<String, Object>>();
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, listItems, R.layout.list_view,
				new String[]{"name"}, new int[]{R.id.item_community});
		mListView.setAdapter(simpleAdapter);
		
	}
	
	private class RecommendListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			RecommendPacket rePacket=new RecommendPacket("query", "com.qiao.test.plugin.recommend");
			rePacket.setType(Type.GET);
			XmppTool.getConnection().sendPacket(rePacket);
		}
		
	}
	
	private class RecommendIQProvider implements IQProvider{

		@Override
		public IQ parseIQ(XmlPullParser parser) throws Exception
		{
			// TODO Auto-generated method stub
			RecommendPacket recommendPacket=new RecommendPacket("query", "com.qiao.test.plugin.recommend");
			recommendPacket.setType(Type.SET);
  		    int eventType = parser.getEventType(); 
  		    while (eventType!= XmlPullParser.END_DOCUMENT) 
  		    {
  		    	if (eventType == XmlPullParser.START_TAG) 
  		    	{
  		    		if ("item".equals(parser.getName())) {
  		    			String user=parser.getAttributeValue("","username");
  		    			recommendPacket.addUser(user);	
  		    		}
  		    	}
  		    	else if(eventType == XmlPullParser.END_TAG)
  		    	{
  		    		if ("query".equals(parser.getName()))
  		    		{
  		    			break;
  		    		}
  		    	}
  		    	eventType = parser.next();  
  		    }
  			return recommendPacket; 
		}
		
	}
	
	 private class RecommendPacketListener implements PacketListener{

		@Override
		public void processPacket(Packet packet)
		{
			// TODO Auto-generated method stub
			RecommendPacket recommendPacket=(RecommendPacket)packet;
			if(recommendPacket.getType().toString().equals("result"))
			{
				listItems.clear();
				ArrayList<String> list=recommendPacket.getUserList();
				for(int i=0;i<list.size();i++)
				{
					Map<String, Object> listItem =new LinkedHashMap<String, Object>();
					listItem.put("name", list.get(i));
					listItems.add(listItem);
				}
				simpleAdapter.notifyDataSetChanged();
			}
			
			
		}
    	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query_recommend, menu);
		return true;
	}

}
