package pppoe.school;



import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;

public class ActivityHelp extends Activity{

	private WebView m_WebView; 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		m_WebView = (WebView) findViewById(R.id.webview_help);
		WebSettings webSettings = m_WebView.getSettings();
		webSettings.setJavaScriptEnabled(true);  
		m_WebView.loadUrl("file:///android_asset/help.htm");
	}
}
