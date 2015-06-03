package com.android.mobliesocietynetwork.client;




import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;

import com.android.mobliesocietynetwork.client.util.ActivityManager;
import com.android.mobliesocietynetwork.client.util.Constants;
import com.android.mobliesocietynetwork.client.util.FriendDB;
import com.android.mobliesocietynetwork.client.util.MyDate;
import com.android.mobliesocietynetwork.client.util.SharePreferenceUtil;
import com.android.mobliesocietynetwork.client.util.XmppTool;
import com.android.mobliesocietynetwork.client.util.bean.LonlatPacket;
import com.android.mobliesocietynetwork.client.util.bean.User;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationData;  
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.map.l;
//import com.baidu.baidulocationdemo.LocationApplication.MyLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//�����õ�λ�����ѹ��ܣ���Ҫimport����
//���ʹ�õ���Χ�����ܣ���Ҫimport������
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.LocationClientOption.LocationMode;



import android.os.Bundle;
import android.os.Vibrator;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MapActivity extends Activity {  
    private MapView mMapView = null;  
    private BaiduMap mBaiduMap;
    private Button mLocationButton;
    private double mlongitude;
    private double mlatitude;
    private MapStatus mMapStatus;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private MyLocationListener mMyLocationListener;
    private Button mUploadButton;
    private Button mGetFriendLocationButton;
    private Button mTestButton;
    private SharePreferenceUtil util;
    private ListView mListView;
    private MyListAdapter myAdapter;
    private ArrayList<User> userList;
    private FriendDB friendDB;
    private ArrayList<String> lonList;
	private ArrayList<String> latList;
	private ArrayList<String> nameList;
	private int isfirst;
	private String lastPid;
	private String getFriendTime;

	
    private TextView mLocationResult,logMsg;

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_map);  
        
        ActivityManager exitM = ActivityManager.getInstance();
		exitM.addActivity(MapActivity.this);
        //��ȡ��ͼ�ؼ�����  
        util = new SharePreferenceUtil(this, Constants.SAVE_USER);
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();  
        userList=new ArrayList<User>();
        lonList=new ArrayList<String>();
        latList=new ArrayList<String>();
        nameList=new ArrayList<String>();
        isfirst=1;
    	lastPid="";
        mListView=(ListView) findViewById(R.id.listView_map_actvity);
        myAdapter = new MyListAdapter(this,R.layout.listview_items_map);
        mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(new ListItemsClickListener());
		friendDB= new FriendDB(MapActivity.this);
        mUploadButton= (Button)findViewById(R.id.upload_bt);
        mUploadButton.setOnClickListener(new UploadListener());
        mGetFriendLocationButton=(Button) findViewById(R.id.getFriendLocation_bt);
        mGetFriendLocationButton.setOnClickListener(new GetFriLocaListener());
        mLocationButton = (Button)findViewById(R.id.location_bt);
        mLocationButton.setOnClickListener(new OnClickListener() {
        	@Override  
        	public void onClick(View v)
    		{
    			// TODO Auto-generated method stub
        		
    			mLocationClient.start();
    			//һֱ�����߳��޷��ٴζ�λ��
    			mLocationClient.stop();
    		}
        });
        mTestButton=(Button)findViewById(R.id.test_bt_map);
        mTestButton.setOnClickListener(new TestListener());
        
        mLocationResult=(TextView)findViewById(R.id.longla_text);
        mLocationResult.isInEditMode();
        //��ͨ��ͼ  
	    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
	    //���ǵ�ͼ  
	    //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
	    LatLng cenpt = new LatLng(30.67,104.06); 
	    mMapStatus = new MapStatus.Builder().target(cenpt).zoom(12).build();
	    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //�ı��ͼ״̬
	    mBaiduMap.setMapStatus(mMapStatusUpdate); 
	    mBaiduMap.setMyLocationEnabled(true);  
	    mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
	    mLocationClient.registerLocationListener(myListener );    //ע���������
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		mLocationClient.setLocOption(option);
		
		ProviderManager.getInstance().addIQProvider("query", "com.qiao.test.plugin.location",new LocationIQProvider());
      

        PacketFilter packetFilterIQ = new PacketTypeFilter(IQ.class);  
        PacketFilter packetFilterEX=new PacketExtensionFilter("query", "com.qiao.test.plugin.location");

		XmppTool.getConnection().addPacketListener(new LocationPacketListener(), packetFilterIQ);
//		XmppTool.getConnection().addPacketListener(new LonlatPacketListener(), packetFilterEX);

		
    }  
    
    
    
    private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time:"+location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
			sb.append("\nlongitude:"+location.getLongitude());
			sb.append("\nlatitude:"+location.getLatitude());
			mlatitude=location.getLatitude();
		    mlongitude=location.getLongitude();
//			sb.append("\nradius: ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : "+location.getSpeed());
//				sb.append("\nsatellite : "+location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : "+sb.append(location.getAddrStr()));
//			} 
			logMsg(sb.toString()); 
		    //����Maker�����  
		    LatLng point = new LatLng(mlatitude, mlongitude);   //��������γ�ȣ��پ��ȡ�-��
		    //����Markerͼ��  
		    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);  
		    //����MarkerOption�������ڵ�ͼ�����Marker  
		    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);  
		    //�ڵ�ͼ�����Marker������ʾ  
		    mBaiduMap.addOverlay(option);
		    
		    mMapStatus = new MapStatus.Builder().target(point).zoom(15).build();
		    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		    mBaiduMap.animateMapStatus(mMapStatusUpdate,500); 
		}
	}
    
    //�������λ��
    private class UploadListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			//ԭ����
//			HashMap<String,String> locationMap=new HashMap<String, String>();
//			locationMap.clear();
//			locationMap.put("latitude", mlatitude+"");
//			locationMap.put("longitude", mlongitude+"");
//			LabelPacket labelPacket=new LabelPacket("query","com.qiao.test.plugin.location");
//			labelPacket.addMap(locationMap);
//			XmppTool.getConnection().sendPacket(labelPacket);
//			String ss=labelPacket.toXML();
			//�ڶ��ַ���
//			LonlaPacket lonlaPacket=new LonlaPacket("com.qiao.test.plugin.location");
//			lonlaPacket.setLongitude(mlongitude+"");
//			lonlaPacket.setLatitude(mlatitude+"");
//			lonlaPacket.setUser(util.getName());
//			XmppTool.getConnection().sendPacket(lonlaPacket);
//			String ss2=lonlaPacket.toXML();

		
			//�����ַ���
			LonlatPacket lonlatPacket=new LonlatPacket("query", "com.qiao.test.plugin.location");
			lonlatPacket.setTypeSet();
			
			lonlatPacket.addInfo(util.getName(), mlongitude+"", mlatitude+"");
			XmppTool.getConnection().sendPacket(lonlatPacket);
			String ss1=lonlatPacket.toXML();
			Toast.makeText(getApplicationContext(), "�����ɹ�", Toast.LENGTH_SHORT).show();
		}
	}
    
    //��ú��ѵ���λ��
    private class GetFriLocaListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			LonlatPacket lonlatPacket=new LonlatPacket("query", "com.qiao.test.plugin.location");
			lonlatPacket.setTypeGet();
			XmppTool.getConnection().sendPacket(lonlatPacket);
			String ss1=lonlatPacket.toXML();
			Toast.makeText(getApplicationContext(), "�����ɹ�", Toast.LENGTH_SHORT).show();
		
		}
    	
    }

    private class LocationIQProvider implements IQProvider{

  		@Override
  		public IQ parseIQ(XmlPullParser parser) throws Exception
  		{
  		    LonlatPacket lonlatPacket=new LonlatPacket("query", "com.qiao.test.plugin.location");
  		    lonlatPacket.setTypeGet();
  		    int eventType = parser.getEventType(); 
  		    while (eventType!= XmlPullParser.END_DOCUMENT) 
  		    {
  		    	if (eventType == XmlPullParser.START_TAG) 
  		    	{
  		    		if ("item".equals(parser.getName())) {
  		    			String username=parser.getAttributeValue("", "username");
  		    			String lon=parser.getAttributeValue("", "lon");
  		    			String lat=parser.getAttributeValue("", "lat");
  		    			lonlatPacket.addInfo(username, lon, lat);	
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
  			return lonlatPacket; 
  		}	
    }
    
    
    private class LocationPacketListener implements PacketListener{

		@Override
		public void processPacket(Packet packet)
		{
			// TODO Auto-generated method stub
			LonlatPacket lonlatPacket=(LonlatPacket)packet;
			if((isfirst==1&&lonlatPacket.getType().toString().equals("result"))
					||(isfirst==0&&lastPid!=lonlatPacket.getPacketID()&&lonlatPacket.getType().toString().equals("result"))){
				nameList.clear();
				nameList=lonlatPacket.getUserList();
				lonList=lonlatPacket.getLonArrayList();
				latList=lonlatPacket.getLatArrayList();
				for(int i=0;i<lonList.size();i++)
				{
			  
					LatLng point = new LatLng(Double.parseDouble(latList.get(i)), 
							Double.parseDouble(lonList.get(i)));  
					//����Markerͼ��  
					BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka_friend);  
					//����MarkerOption�������ڵ�ͼ�����Marker  
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);  
					mBaiduMap.addOverlay(option);
				}
				lastPid=lonlatPacket.getPacketID();
				isfirst=0;
				getFriendTime=MyDate.getDateEN();
				myAdapter.notifyDataSetChanged();
			}	
		}
    	
    }
    
 
    

	private class ListItemsClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		        long arg3)
		{
			// TODO Auto-generated method stub
			LatLng point = new LatLng(Double.parseDouble(latList.get(position)), 
					Double.parseDouble(lonList.get(position)));  
			mMapStatus = new MapStatus.Builder().target(point).zoom(15).build();
		    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		    mBaiduMap.animateMapStatus(mMapStatusUpdate,500); 
		    StringBuffer sb = new StringBuffer(256);
			sb.append("time:"+getFriendTime);
			sb.append("\nlongitude:"+lonList.get(position));
			sb.append("\nlatitude:"+latList.get(position));
			logMsg(sb.toString()); 
		}
	}
	
	private class TestListener implements OnClickListener{

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			 LatLng cenpt = new LatLng(30.67,104.06); 
			 mMapStatus = new MapStatus.Builder().target(cenpt).zoom(14).build();
			 MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);//�ı��ͼ״̬
			 mBaiduMap.setMapStatus(mMapStatusUpdate);
			 
		}
		
	}
    
	private class MyListAdapter extends ArrayAdapter<Object>
	{
		int mTextViewResourceID = 0;
		private Context mContext;

		public MyListAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);
			mTextViewResourceID = textViewResourceId;
			mContext = context;
		}

		public int getCount()
		{
			return nameList.size();
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
			ImageView iamge = null;
			TextView name = null;

//			if (convertView == null)
//			{
				convertView = LayoutInflater.from(mContext).inflate( mTextViewResourceID, null);
				iamge = (ImageView) convertView.findViewById(R.id.image_listitems_map);
				name = (TextView) convertView.findViewById(R.id.name_listitems_map);
				
				iamge.setImageResource(Constants.IMGS[1]);
				name.setText(nameList.get(position));
//			}
			return convertView;
		}
	}
    
    
    
    /**
	 * ��ʾ�����ַ���
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onPause();  
        }  



}
