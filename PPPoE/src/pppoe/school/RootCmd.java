package pppoe.school;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


import android.util.Log;







public class RootCmd {

	
	public void RootCommand(String strCmd) {
		try {
			Process localProcess = Runtime.getRuntime().exec("su");
			OutputStream localOutputStream = localProcess.getOutputStream();
			DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);

			localDataOutputStream.writeBytes(strCmd + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
				
			localProcess.waitFor();   
			}
		catch (Exception e){	
		    e.printStackTrace();
		    }
		}
	public void Dg_RootCommand(String strCmd) {
		try {
			Process localProcess = Runtime.getRuntime().exec("su");
			OutputStream localOutputStream = localProcess.getOutputStream();
			DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
			InputStream localInputStream = localProcess.getInputStream();
			DataInputStream localDataInputStream = new DataInputStream(localInputStream);
	      
			localDataOutputStream.writeBytes(strCmd + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			
			String strTmp = localDataInputStream.readLine();
			while(strTmp != null){
				Log.d("Debug", strTmp);
				strTmp = localDataInputStream.readLine();
			}
			
			localProcess.waitFor(); 
			localInputStream.close();
		 }
	    catch (Exception e){	
	    	e.printStackTrace();
	    }
	}
	public boolean FileExists(String strPath){
		File file = new File(strPath);
		return file.exists();
	}
	public boolean HasRoot(){
		
		if ( !FileExists(KeyValue.ROOT1) && !FileExists(KeyValue.ROOT2)){
			return false;
		}
		return true;
	}
	public String Re_RootCommand(String strCmd) {
		try {
			Process localProcess = Runtime.getRuntime().exec("su");
			OutputStream localOutputStream = localProcess.getOutputStream();
			DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
			InputStream localInputStream = localProcess.getInputStream();
			DataInputStream localDataInputStream = new DataInputStream(localInputStream);
	      
			localDataOutputStream.writeBytes(strCmd + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			
			
			String strResult = "",strTmp = localDataInputStream.readLine();
			while(strTmp != null){
				strResult += strTmp;
				strTmp = localDataInputStream.readLine();
			}
			localProcess.waitFor(); 
			localInputStream.close();
			return strResult;
		 }
	    catch (Exception e){	
	    	e.printStackTrace();
	    	return "";
	    }
	}
	public String Get_Eth(String strType){           
			try {
				
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();	
				String strEth = "";
				while (interfaces.hasMoreElements()) {   
					NetworkInterface ni = (NetworkInterface)interfaces.nextElement();      
					byte[] bna = ni.getDisplayName().getBytes();                 
					byte[] dst = new byte[bna.length];                 
					for (int i=0; i < dst.length; i++) {                     
						dst[i] = '\n';                 
					}                               
					for (int i=0; i < bna.length; i++) {  
						dst[i] = bna[i];               
					}                 
					strEth = new String(dst).trim();
					Log.d("Debug", strEth);        
					if(strEth.contains(strType))
						return strEth;
				 }   
			} 
			catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			return "";  
		}
	public String GetDNS1(String strface){
		return Re_RootCommand("getprop net." + strface + ".dns1" ).trim();
	}
	public String GetDNS2(String strface){
		return Re_RootCommand("getprop net." + strface + ".dns2" ).trim();
	}
	
}
