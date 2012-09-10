package pppoe.school;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;



import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;


public class ActivityMain extends Activity {

	private EditText m_ed_username;
	private EditText m_ed_password;
	private CheckBox m_cb_school;
	private CheckBox m_cb_save;
	private Button 	 m_btn_Connect;
	private Button 	 m_btn_Stop;
	private Button 	 m_btn_State;
	private Button 	 m_btn_Set;
	
	public static final int ITEM0 = Menu.FIRST;
	public static final int ITEM1 = Menu.FIRST + 1;

	
	private RootCmd  m_rootcommand = null;
	private int 	 m_nghca = 0;
	private int 	 m_neth = 0;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);


        m_rootcommand = new RootCmd();
        
        m_ed_username =(EditText)findViewById(R.id.edit_username);
        m_ed_password =(EditText)findViewById(R.id.edit_password);
        m_cb_school	  =(CheckBox)findViewById(R.id.check_school);
        m_cb_save	  =(CheckBox)findViewById(R.id.check_remember);
        
        m_btn_Connect =(Button)findViewById(R.id.btn_Connect);
        m_btn_Stop    =(Button)findViewById(R.id.btn_Stop);
        
        m_btn_State   =(Button)findViewById(R.id.btn_State);
        m_btn_Set     =(Button)findViewById(R.id.btn_Set);
        
        
        m_btn_Connect.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					arg0.setBackgroundResource(R.drawable.button_pressed); 
					break;
				case MotionEvent.ACTION_UP:
					 arg0.setBackgroundResource(R.drawable.button_normal);
					break;
				default:
					break;
				}
				return false;
			}
        	
        });
        m_btn_Stop.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					arg0.setBackgroundResource(R.drawable.button_pressed); 
					break;
				case MotionEvent.ACTION_UP:
					 arg0.setBackgroundResource(R.drawable.button_normal);
					break;
				default:
					break;
				}
				return false;
			}
        	
        });
        m_btn_State.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					arg0.setBackgroundResource(R.drawable.button_pressed); 
					break;
				case MotionEvent.ACTION_UP:
					 arg0.setBackgroundResource(R.drawable.button_normal);
					break;
				default:
					break;
				}
				return false;
			}
        	
        });
        m_btn_Set.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					arg0.setBackgroundResource(R.drawable.button_pressed); 
					break;
				case MotionEvent.ACTION_UP:
					 arg0.setBackgroundResource(R.drawable.button_normal);
					break;
				default:
					break;
				}
				return false;
			}
        	
        });
        
        m_btn_Connect.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) {                  
        		    PPPoE();
        		}          
        	}); 
        m_btn_Stop.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) {                          			
        			HangUp();
        		}
        	}); 
        
        m_btn_State.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) {                  
        			NetTest();
        		}          
        	}); 
        m_btn_Set.setOnClickListener(new OnClickListener() {        
        	public void onClick(View v) {                          			
        			NetSet();
        		}          
        	}); 
        if(!m_rootcommand.Get_Eth("wlan").contentEquals("") || !m_rootcommand.Get_Eth("eth").contentEquals("")){
		}
		else{
			Toast.makeText(ActivityMain.this, "没有可用网卡！请设置！",Toast.LENGTH_LONG).show();
		}
        if(m_rootcommand.HasRoot()){
        }	
        else{
        	Toast.makeText(ActivityMain.this, "你没有root权限！",Toast.LENGTH_LONG).show();
        }
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM0, 0, "帮助").setIcon(R.drawable.ic_help);
		menu.add(0, ITEM1, 0, "退出").setIcon(R.drawable.ic_return);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0: 
			Help();
		break;
		case ITEM1: 
			Quit(); 
		break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void loadData(){
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);
	    String userName      = sp.getString(KeyValue.KEY_USERNAME, "");
	    String userPassword  = sp.getString(KeyValue.KEY_PASSWORD, "");
	    
	    m_nghca   = sp.getInt(KeyValue.DIAL_VESION,KeyValue.DIAL_V207);
		m_neth    = sp.getInt(KeyValue.ETH_TYPE,KeyValue.E_WALN);
		
	    if (!userName.contentEquals("") && !userPassword.contentEquals("")){
	    	m_ed_username.setText(userName);
	    	m_ed_password.setText(userPassword);
	    }
	    m_cb_school.setChecked(sp.getBoolean(KeyValue.IS_SCHOOL, true));
	    m_cb_save.setChecked(sp.getBoolean(KeyValue.IS_SAVE, true));
	    if(sp.getBoolean(KeyValue.IS_FRIST,true)){

			//写入配置文件
			Editor spEd = sp.edit();
			
			spEd.putString(KeyValue.KEY_DNS1, KeyValue.DEF_KEY_DNS1);
			spEd.putString(KeyValue.KEY_DNS2, KeyValue.DEF_KEY_DNS2);
			
			spEd.commit();
	    	Help();
	    }

	}
	private void saveData(){
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);

		//写入配置文件
		Editor spEd = sp.edit();
		
		spEd.putBoolean(KeyValue.IS_SCHOOL, m_cb_school.isChecked());
		spEd.putBoolean(KeyValue.IS_SAVE, m_cb_save.isChecked());
		
		spEd.putInt(KeyValue.DIAL_VESION, m_nghca);
		spEd.putInt(KeyValue.ETH_TYPE,m_neth);
		
		if (m_cb_save.isChecked()){
			spEd.putString(KeyValue.KEY_USERNAME, m_ed_username.getText().toString());
			spEd.putString(KeyValue.KEY_PASSWORD, m_ed_password.getText().toString());
		}
		else{
			spEd.putString(KeyValue.KEY_USERNAME, "");
			spEd.putString(KeyValue.KEY_PASSWORD, "");
		}
		spEd.putBoolean(KeyValue.IS_FRIST,false);
		spEd.commit();

	}
	private void PPPoE(){
		
		
		String strUserName  = null;
		String strPassWord  = null;
		String strInterface = null;
		strUserName = m_ed_username.getText().toString();
		strPassWord = m_ed_password.getText().toString();
		
		if(TextUtils.isEmpty(strUserName)&&
		   TextUtils.isEmpty(strPassWord)){
			Toast.makeText(ActivityMain.this, "账号与密码不能为空！", Toast.LENGTH_SHORT).show(); 
			return;
		}
		m_rootcommand.Dg_RootCommand("netcfg");
		//选择网卡
		switch(m_neth){
		case KeyValue.E_WALN:
			strInterface = m_rootcommand.Get_Eth("wlan");
			break;
		case KeyValue.E_ETH:
			strInterface = m_rootcommand.Get_Eth("eth");
			break;
		default:
			break;
		}
		//网卡为空
		if(strInterface.contentEquals(""))
		{
			switch(m_neth){
			case KeyValue.E_WALN:
				strInterface = m_rootcommand.Get_Eth("eth");
				break;
			case KeyValue.E_ETH:
				strInterface = m_rootcommand.Get_Eth("wlan");
				break;
			}	
		}
		//网卡
		if(strInterface.contentEquals("")){		
			Builder bd = new AlertDialog.Builder(ActivityMain.this).setTitle("没有可用网卡！").setMessage("是否进行设置？");
			bd.setPositiveButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					Intent intent = new Intent("/");
					ComponentName cm = new ComponentName("com.android.settings","com.android.settings.Settings");
					intent.setComponent(cm);
					intent.setAction("android.intent.action.VIEW");
					ActivityMain.this.startActivityForResult( intent ,0);
					
				}
			}).setNeutralButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show(); 
			return;
		}
		else
			Toast.makeText(ActivityMain.this, "使用网卡："+strInterface, Toast.LENGTH_SHORT).show();
		
		if(m_cb_school.isChecked()){
			MD5 user_md5 = new MD5();
			Log.d("Debug", "GHCA:" + Integer.toString(m_nghca));
			strUserName  = user_md5.getMD5_Username(m_ed_username.getText().toString(),m_ed_password.getText().toString(),m_nghca);
			Toast.makeText(ActivityMain.this, "使用协同通信拨号器协议！",Toast.LENGTH_LONG).show();
		}	
		else
			Toast.makeText(ActivityMain.this, "普通PPPoE拨号！",Toast.LENGTH_LONG).show();
		
		saveData();
		Intent intent = new Intent();
		intent.putExtra(KeyValue.FUNCATION,KeyValue.DIAL);
		intent.putExtra(KeyValue.KEY_USERNAME,strUserName);
		intent.putExtra(KeyValue.KEY_PASSWORD,strPassWord);
		intent.putExtra(KeyValue.INTERFACE,strInterface);			
		intent.setClass(ActivityMain.this,ActivityPPPoE.class);
		ActivityMain.this.startActivity(intent);
	}
	
	private void HangUp(){

		Toast.makeText(ActivityMain.this, "断开网络",Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.putExtra(KeyValue.FUNCATION,KeyValue.HANG);	
		intent.setClass(ActivityMain.this,ActivityPPPoE.class);
		ActivityMain.this.startActivity(intent);
		
	}	
	private void NetTest(){

		
		Intent intent = new Intent();
		intent.putExtra(KeyValue.FUNCATION,KeyValue.TEST);	
		intent.setClass(ActivityMain.this,ActivityPPPoE.class);
		ActivityMain.this.startActivity(intent);

	}	
	private void NetSet(){
		saveData();
		Intent intent = new Intent();
		intent.setClass(ActivityMain.this,ActivitySet.class);
		ActivityMain.this.startActivity(intent);
	}	

	private void Help(){
		Intent intent = new Intent();
		intent.setClass(ActivityMain.this,ActivityHelp.class);
		ActivityMain.this.startActivity(intent);
		
//		m_rootcommand.Dg_RootCommand("busybox ps");
//		m_rootcommand.Dg_RootCommand("busybox ifconfig");
		
//      169.254.148.117
//      255.255.0.0
//		16
//		192.168.1.1
//		218.6.200.139
//		61.139.2.69
		
	}	
	private void Quit(){
		
		saveData();
		System.exit(0);
	}	
	


}