package pppoe.school;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import com.encrypt.Encrypt;




public class MD5{
	/**
	 * @param str
	 * @return
	 */
	
	public  String getMD5(String str){ 
		MessageDigest md5 = null; 
	
		try{ 
			md5 = MessageDigest.getInstance("MD5"); 
		}
		catch(Exception e){ 
			e.printStackTrace(); 
			return ""; 
		} 
	
		char[] charArray = str.toCharArray(); 
		byte[] byteArray = new byte[charArray.length]; 
	
		for(int i = 0; i < charArray.length; i++){ 
			byteArray[i] = (byte)charArray[i]; 
		} 
	
		byte[] md5Bytes = md5.digest(byteArray); 

		StringBuffer hexValue = new StringBuffer(); 

		for( int i = 0; i < md5Bytes.length; i++){ 
			int val = ((int)md5Bytes[i])&0xff; 
	
			if(val < 16){ 
				hexValue.append("0"); 
			} 
	
			hexValue.append(Integer.toHexString(val)); 
		} 
		return hexValue.toString(); 
		} 
	public long getTime(){
		Date localDate = new Date();
	    long nYear = localDate.getYear();
	    if (nYear < 200L) nYear += 1900L;
	    long nMonth = localDate.getMonth();
	    long nDate = localDate.getDate();
	    long nHour = localDate.getHours();
	    long nMinutes = localDate.getMinutes();
	    long nSeconds = localDate.getSeconds();
	    long nTime = (nYear - 1970L) * 365L * 24L * 3600L + nMonth * 30L * 24L * 3600L + nDate * 24L * 3600L +
	    			  nHour * 3600L + nMinutes * 60L + nSeconds;
		return nTime;
		}
	public long getRand(){
		Random randrom = new Random(System.currentTimeMillis());
		return randrom.nextInt();
		}
	public String getMD5_Username(String username,String password,int version){
		long  nTime = getTime();
		String strtime = Long.toHexString(nTime);
		Encrypt encrypt = new Encrypt();
		String strR = "",strmd5 = "";
		//ÕËºÅ´óÐ´
		username = username.toUpperCase();
		
		switch(version){
		case KeyValue.DIAL_V1XX:
			 strmd5 = getMD5(strtime + "EXTR" + username );
			 strR = "~ghca" + strtime + "1000" + strmd5.substring(0,20) + username;
			 break;
		case KeyValue.DIAL_V201:
			 strmd5 = getMD5(strtime + "Chegel" + username + password);
			 strR = "~ghca" + strtime + "2001" + strmd5.substring(0,20) + username;
			 break;
		case KeyValue.DIAL_V202:
			strmd5 = getMD5(strtime + "Tyroth" + username + password);
			strR = "~ghca" + strtime + "2002" + strmd5.substring(0,20) + username;
			break;
		case KeyValue.DIAL_V205:
			strmd5 = getMD5(strtime + "TaijDa" + username + password);
			strR = "~ghca" + strtime + "2005" + strmd5.substring(0,20) + username;
			break;
		case KeyValue.DIAL_V207:
			strmd5 = getMD5("jepyid" +  username + strtime + password);
			strR = "~ghca" + strtime + "2007" + strmd5.substring(0,20) + username;
			break;
		case KeyValue.DIAL_V208:	
			strR = encrypt.stringFromEncrypt(KeyValue.LUA208_PATH,username,password);
			strR   = "~ghca" + strR;
			break;
		default:
			strR = "Error!";
			break;
		}
		
	    
		return strR;
	}
}
