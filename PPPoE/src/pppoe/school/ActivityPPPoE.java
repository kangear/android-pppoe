package pppoe.school;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.Toast;




public class ActivityPPPoE extends Activity{
	
	public static final int ITEM0 = Menu.FIRST;
	public static final int ITEM1 = Menu.FIRST + 1;
	public static final int ITEM2 = Menu.FIRST + 2;
	
	
	
	private ProgressDialog m_processDialogDial = null;
	private ProgressDialog m_processDialogHang = null;
	private ProgressDialog m_processDialogTest = null;
	private String m_strChoose = null;
	private String m_strInterface = null;
	private String m_strusername  = null;
	private String m_strpassword  = null;
	private WebView m_WebView; 

	
	private String 	 m_strdns1 = null;
	private String 	 m_strdns2 = null;
	private boolean  m_bisdns  = true;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);        
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
		setContentView(R.layout.pppoe);
		
		m_WebView = (WebView) findViewById(R.id.webview);
		
		m_strChoose = getIntent().getStringExtra(KeyValue.FUNCATION);
		
		
		
		m_strusername  = getIntent().getStringExtra(KeyValue.KEY_USERNAME);
		m_strpassword  = getIntent().getStringExtra(KeyValue.KEY_PASSWORD);
		m_strInterface = getIntent().getStringExtra(KeyValue.INTERFACE);
				
		if(m_strChoose.equals(KeyValue.DIAL)) {
			Dial();
		}
		else if(m_strChoose.equals(KeyValue.HANG)){
			Hang();
		}
		else if(m_strChoose.equals(KeyValue.TEST)){
			Test();
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
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM0, 0, "断开网络").setIcon(R.drawable.ic_return);
		menu.add(0, ITEM1, 0, "测试网络").setIcon(R.drawable.ic_state);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0: 
			actionClickMenuItem0();
		break;
		case ITEM1: 
			actionClickMenuItem1(); 
		break;
		}
		return super.onOptionsItemSelected(item);
	}
    private boolean Dial(){
    	
		
    	m_processDialogTest = new ProgressDialog(ActivityPPPoE.this); 
		m_processDialogTest.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 				
		m_processDialogTest.setProgress(100); 				
		m_processDialogTest.setTitle("尝试读取网页......"); 			
		m_processDialogTest.setCancelable(false); 			
		m_processDialogTest.show();
		
		m_processDialogDial = new ProgressDialog(ActivityPPPoE.this); 
		m_processDialogDial.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		m_processDialogDial.setProgress(100); 
		m_processDialogDial.setTitle("PPPoE拨号中......"); 
		m_processDialogDial.setCancelable(false);
		m_processDialogDial.show(); 
	
		
		Log.d("Debug", m_strInterface);
		Log.d("Debug", m_strusername);
		Log.d("Debug", m_strpassword);
		
		
		new Thread() {  
			public void run()  {  
				try { 		
						RootCmd rootcommand = new RootCmd();
						rootcommand.RootCommand(KeyValue.RWX_POWER + KeyValue.PPPD_PATH);
						rootcommand.RootCommand(KeyValue.RWX_POWER + KeyValue.PPPOE_PATH);
				
						String strCmd  =    
								 KeyValue.PPPD_PATH  + " pty '" +
								 KeyValue.PPPOE_PATH + " -p " +
								 KeyValue.PPPOE_PID  + " -I " +
								 m_strInterface + " -T 30 -U -m 1412'" + "noipdefault noauth default-asyncmap defaultroute hide-password nodetach usepeerdns mtu 1492 mru 1492 noaccomp nodeflate nopcomp novj novjccomp user " + 
								 m_strusername  + " password " + 
	                     		 m_strpassword  + " lcp-echo-interval 20 lcp-echo-failure 3 &";
						
						rootcommand.RootCommand(strCmd);
						
						Log.d("Debug",strCmd);
						
						for (int i = 0;i < 50;i++) {  
							m_processDialogDial.setProgress(i);  
							Thread.sleep(100); 
                    	}	
						
						String strPPP = rootcommand.Get_Eth("ppp");
						
						for (int i = 50;i < 80;i++) {  
							m_processDialogDial.setProgress(i);
							if(strPPP.contentEquals(""))
							 Thread.sleep(100);
							else
							 Thread.sleep(50);	
                    	}
			
						strPPP = rootcommand.Get_Eth("ppp");
										
						rootcommand.RootCommand("route del default");
						rootcommand.RootCommand("ip route del default");
						
						rootcommand.RootCommand("route add default " + strPPP);
						rootcommand.RootCommand("ip route add default " + strPPP);
						
						rootcommand.RootCommand("route add -net 0.0.0.0 netmask 0.0.0.0 dev " + strPPP);
						rootcommand.RootCommand("ip route add -net 0.0.0.0 netmask 0.0.0.0 dev " + strPPP);
						
						if(!m_bisdns) 
						{
							if(!rootcommand.GetDNS1(strPPP).contentEquals(""))
								m_strdns1 = rootcommand.GetDNS1(strPPP);
							if(!rootcommand.GetDNS2(strPPP).contentEquals(""))
								m_strdns2 = rootcommand.GetDNS2(strPPP);
						}
						rootcommand.RootCommand("setprop net.dns1 " + m_strdns1); 
						rootcommand.RootCommand("setprop net.dns2 " + m_strdns2); 
						for (int i = 80;i < 100;i++) {  
							m_processDialogDial.setProgress(i);
							Thread.sleep(100);
                    	}
						
						Log.d("Debug", "DNS1 " + m_strdns1 +" DNS2 "+ m_strdns2);
						
						m_processDialogDial.dismiss();
						m_processDialogDial = null;
						
						WebSettings webSettings = m_WebView.getSettings();
						webSettings.setJavaScriptEnabled(true);
						for (int i = 0;i <= 100;i++) {  
							m_processDialogTest.setProgress(i);  
							Thread.sleep(100); 
							if(i%20 == 0)
								m_WebView.loadUrl("http://www.google.com.hk");
                		} 
					
						m_processDialogTest.dismiss();
						m_processDialogTest = null;
						
						
					}  
					catch (InterruptedException e) {						
						if(m_processDialogDial != null){
							m_processDialogDial.dismiss();
							m_processDialogDial = null;
						  }
						if(m_processDialogTest != null){
							m_processDialogTest.dismiss();
							m_processDialogTest = null;
						  }
						e.printStackTrace(); 
					} 
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}  
				}  
			}.start();	
		Toast.makeText(ActivityPPPoE.this, "如果网页能够打开，则已连接网络！\r\n若不成功，请确认多尝试几次！",Toast.LENGTH_LONG).show();
    	return false;
    }
    private boolean Hang(){
    	
    	m_processDialogHang = new ProgressDialog(ActivityPPPoE.this); 
    	m_processDialogHang.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 				
    	m_processDialogHang.setProgress(100); 				
    	m_processDialogHang.setTitle("网络断开中......"); 			
    	m_processDialogHang.setCancelable(false); 			
    	m_processDialogHang.show();	

		new Thread() {  
			public void run()  { 
				try{
						File localFile = new File(KeyValue.PPPOE_PID);
						RootCmd rootcommand = new RootCmd();
				    	if (localFile.exists()){
				    		FileReader localFileReader = new FileReader(localFile);
				    		BufferedReader localBufferedReader = new BufferedReader(localFileReader);
				    		int j = Integer.parseInt(localBufferedReader.readLine());
				    		String strPID = "kill -9 " + j;
				    		rootcommand.RootCommand(strPID);
				    	}
				    	for (int i = 0;i < 100;i++) {  
				    		m_processDialogHang.setProgress(i);
				    		Thread.sleep(10);
				    	}			
						m_processDialogHang.dismiss();
						m_processDialogHang = null;
						ActivityPPPoE.this.finish();
					}
				catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ActivityPPPoE.this.finish();
					} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ActivityPPPoE.this.finish();
				}  
			}  	
		}.start();
		 
    	return false;
    }
    private boolean Test(){
 
    	m_processDialogTest = new ProgressDialog(ActivityPPPoE.this); 
		m_processDialogTest.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 				
		m_processDialogTest.setProgress(100); 				
		m_processDialogTest.setTitle("尝试读取网页......"); 			
		m_processDialogTest.setCancelable(false); 			
		m_processDialogTest.show();	
		
		new Thread() {  
			public void run()  {   
				try{
					WebSettings webSettings = m_WebView.getSettings();
					webSettings.setJavaScriptEnabled(true);  
					
					for (int i = 0;i <= 100;i++) {  
						m_processDialogTest.setProgress(i);  
						Thread.sleep(100); 
							if(i%20 == 0){
								m_WebView.loadUrl("http://www.google.com.hk");
							}
                		} 
					m_processDialogTest.dismiss();
					m_processDialogTest = null;
					
					}
				catch (InterruptedException e) { 
					if(m_processDialogTest != null){
						m_processDialogTest.dismiss(); 
						m_processDialogTest = null;
					  }
					
					e.printStackTrace(); 
					}  
			}
			}.start();	
		Toast.makeText(ActivityPPPoE.this, "如果网页能够打开，则已连接网络！\r\n若网页没打开，请确认账号情况！",Toast.LENGTH_LONG).show();     
    	return false;
    }
	private void actionClickMenuItem0(){
		Hang();
	}
	private void actionClickMenuItem1(){	
		Test();
	}
	private void loadData(){
		
		//载入配置文件
		SharedPreferences sp = getSharedPreferences(KeyValue.PREFS_NAME, 0);
		m_strdns1 = sp.getString(KeyValue.KEY_DNS1,KeyValue.DEF_KEY_DNS1);
		m_strdns2 = sp.getString(KeyValue.KEY_DNS2,KeyValue.DEF_KEY_DNS2);
		m_bisdns  = sp.getBoolean(KeyValue.IS_SELFDNS, false);
	}
}