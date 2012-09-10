package pppoe.school;




import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;



public class ActivitySet extends Activity{
	private CheckBox m_cb_static_dns = null;
	private CheckBox m_cb_school = null;
	private ListView m_listview = null;
	private Builder  m_bd = null;	
	private EditText m_ed = null;
	private Spinner  m_sp_ghca = null;
	private Spinner  m_sp_eth  = null;
	
	
	private FrameLayout m_fl = null;
	private String 	 m_strdns1 = null;
	private String 	 m_strdns2 = null;
	private int 	 m_nset = 0;
	private int 	 m_nghca = 0;
	private int 	 m_neth = 0;
	ArrayAdapter<CharSequence> m_ghcaAdapter = null;
	ArrayAdapter<CharSequence> m_ethAdapter = null;
	
	HashMap<String,String> m_mapDns1 = null;
	HashMap<String,String> m_mapDns2 = null;
	SimpleAdapter m_listAdapter = null;
	ArrayList<HashMap<String,String>> m_dnslist = null;
	

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		loadData();

		
		m_ethAdapter = ArrayAdapter.createFromResource(this,R.array.all_eth,android.R.layout.simple_spinner_item);
		m_ethAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		m_sp_eth     =(Spinner)findViewById(R.id.spinner_eth);
		m_sp_eth.setAdapter(m_ethAdapter);
		m_sp_eth.setPrompt("设置你要使用的网卡");
		
		m_sp_eth.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:	
					m_neth = KeyValue.E_WALN;
					break;
				case 1:
					m_neth = KeyValue.E_ETH;
					break;
				default:
					break;
				}
				saveData();
				Log.d("Debug","Eth" + Integer.toString(arg2));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		m_ghcaAdapter = ArrayAdapter.createFromResource(this,R.array.ghca,android.R.layout.simple_spinner_item);
		m_ghcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		m_sp_ghca     = (Spinner)findViewById(R.id.spinner_ghca);
		m_sp_ghca.setAdapter(m_ghcaAdapter);
		m_sp_ghca.setPrompt("设置拨号使用的算法版本");
		
		
		m_sp_ghca.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				m_nghca = arg2;
				saveData();
				Log.d("Debug","ghca" + Integer.toString(arg2));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		m_cb_school    =(CheckBox)findViewById(R.id.check_school);		
		m_cb_school.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) {  
        			
        			if(m_cb_school.isChecked()){
        				Toast.makeText(ActivitySet.this, "使用电信协同通信拨号协议！",Toast.LENGTH_LONG).show();
        				m_sp_ghca.setEnabled(true);
        			}	
        			else{
        				Toast.makeText(ActivitySet.this, "普通PPPoE拨号！",Toast.LENGTH_LONG).show();
        				m_sp_ghca.setEnabled(false);
        			}
        			saveData();	
        		}          
        	}); 
		
		m_cb_static_dns =(CheckBox)findViewById(R.id.check_static_dns);	
		m_cb_static_dns.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) { 
        			
        			if(m_cb_static_dns.isChecked()){
        				m_listview.setEnabled(true);
        				Toast.makeText(ActivitySet.this, "自定义DNS地址！",Toast.LENGTH_LONG).show();
        			}
        			else{
        				m_listview.setEnabled(false);
        				Toast.makeText(ActivitySet.this, "自动获取DNS地址！",Toast.LENGTH_LONG).show();
        			}
        			saveData();
        		}          
        	}); 
		
		
		m_listview  =(ListView)findViewById(R.id.list_dns);
		
		
		m_dnslist = new ArrayList<HashMap<String,String>>();
				
		m_mapDns1 = new HashMap<String,String>();		
		m_mapDns1.put("name", "首选域名服务器");
		m_mapDns1.put("data", m_strdns1);
		m_dnslist.add(m_mapDns1);
		
		m_mapDns2 = new HashMap<String,String>();
		m_mapDns2.put("name", "备用域名服务器");
		m_mapDns2.put("data", m_strdns2);
		m_dnslist.add(m_mapDns2);

		m_listAdapter = new SimpleAdapter(this,m_dnslist,
													  R.layout.item,
													  new String[]{"name","data"},
													  new int[]{R.id.name,R.id.data});
		
		m_listview.setAdapter(m_listAdapter);
		m_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				m_nset = arg2;
				switch(arg2){
					case 0:
						Log.d("Debug","DNS1");
						SetData("首选域名服务器地址");
						m_mapDns1.put("data", m_strdns1);
						break;
					case 1:
						Log.d("Debug","DNS2");
						SetData("备用域名服务器地址");
						m_mapDns2.put("data", m_strdns2);
						break;
					default:
						break;
				}
				saveData();
				Log.d("Debug","dns" + Integer.toString(arg2));
			}
			
		});		
	}
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	loadData();
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	saveData();
    }
    public void SetData(String strTitle){

    		m_ed = new EditText(ActivitySet.this);
    		m_ed.setGravity(Gravity.CENTER);
    		switch(m_nset){
			case 0:
				m_ed.setText(m_strdns1);
				break;
			case 1:
				m_ed.setText(m_strdns2);
				break;
			default:
				break;
			}

    		m_fl = new FrameLayout(ActivitySet.this);
    		m_fl.addView(m_ed,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

    		m_bd = new AlertDialog.Builder(ActivitySet.this);
    		m_bd.setView(m_fl);
    		m_bd.setTitle(strTitle);
    		m_bd.setPositiveButton("是", new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
    				
    				switch(m_nset){
					case 0:
						m_strdns1 = m_ed.getText().toString();
						m_mapDns1.put("data", m_strdns1);
						break;
					case 1:
						m_strdns2 = m_ed.getText().toString();
						m_mapDns2.put("data", m_strdns2);
						break;
					default:
						break;
    				}
    				m_listview.setAdapter(m_listAdapter);
    				saveData();
				}
    			}).setNeutralButton("否", new DialogInterface.OnClickListener() {
			
    			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
    				dialog.cancel();
				}
    		}).show();
    	
    }
	public void saveData(){
		//配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);

		//写入配置文件
		Editor spEd = sp.edit();
		

		spEd.putString(KeyValue.KEY_DNS1, m_strdns1);
		spEd.putString(KeyValue.KEY_DNS2, m_strdns2);
		
		spEd.putBoolean(KeyValue.IS_SELFDNS, m_cb_static_dns.isChecked());
		spEd.putBoolean(KeyValue.IS_SCHOOL, m_cb_school.isChecked());
		
		spEd.putInt(KeyValue.DIAL_VESION, m_nghca);
		spEd.putInt(KeyValue.ETH_TYPE, m_neth);
		spEd.commit();
		if(m_cb_static_dns.isChecked())
			Dns_Static_Set();
		else
			Dns_Dhcp_Set();
	}
	public void loadData(){
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);
		
		
		m_strdns1 = sp.getString(KeyValue.KEY_DNS1, KeyValue.DEF_KEY_DNS1);
		m_strdns2 = sp.getString(KeyValue.KEY_DNS2, KeyValue.DEF_KEY_DNS2);
		m_nghca   = sp.getInt(KeyValue.DIAL_VESION,KeyValue.DIAL_V207);
		m_neth    = sp.getInt(KeyValue.ETH_TYPE,KeyValue.E_WALN);
		if(m_cb_school != null)
			m_cb_school.setChecked(sp.getBoolean(KeyValue.IS_SCHOOL, true));
		if(m_cb_static_dns != null)
			m_cb_static_dns.setChecked(sp.getBoolean(KeyValue.IS_SELFDNS, false));
		if(m_listview != null)
			m_listview.setEnabled(sp.getBoolean(KeyValue.IS_SELFDNS, true));
		if(m_sp_eth != null)
			m_sp_eth.setSelection(m_neth);
		if(m_sp_ghca != null)
			m_sp_ghca.setSelection(m_nghca);
	}

	public void Dns_Dhcp_Set(){
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);
		//写入配置文件
		Editor spEd  = sp.edit();
		
		
		spEd.putBoolean(KeyValue.IS_SELFDNS, false);
		spEd.commit();
		
	}
	public void Dns_Static_Set(){
		
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);
		//写入配置文件
		Editor spEd  = sp.edit();
		
		spEd.putBoolean(KeyValue.IS_SELFDNS, true);
		spEd.putString(KeyValue.KEY_DNS1, m_strdns1);
		spEd.putString(KeyValue.KEY_DNS2, m_strdns2);
		spEd.commit();
		
		Log.d("Debug", "SSetDns");	
	}
}
