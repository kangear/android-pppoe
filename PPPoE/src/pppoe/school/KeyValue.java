package pppoe.school;



public class KeyValue {
	public static final String PREFS_NAME = "INFO";
	public static final String DIAL 	  = "DIAL";
	public static final String HANG       = "HANG";
	public static final String TEST       = "TEST";
	public static final String ETH_TYPE     = "ETH";
	public static final String DIAL_VESION  = "GHCA";
	public static final String KEY_USERNAME = "USR";
	public static final String KEY_PASSWORD = "PWD";

	public static final String KEY_DNS1    = "DNS1";
	public static final String KEY_DNS2    = "DNS2";
	
	public static final String IS_SELFDNS  = "ISSELFDNS";
	public static final String IS_SCHOOL   = "ISSCHOOL";
	public static final String IS_FRIST    = "ISFRIST";
	public static final String IS_SAVE     = "ISAVE";

	
	public static final String FUNCATION   = "FUNCATION";
	public static final String INTERFACE   = "INTERFACE";
	
	public static final String DEF_KEY_DNS1    = "218.6.200.139";
	public static final String DEF_KEY_DNS2    = "61.139.2.69";
	
	public static final String LUA208_PATH        = "/data/data/pppoe.school/lib/libencrypt.so";

	
	public static final String SYS_PPPD_PATH     = "/system/bin/pppd";
	public static final String PPPD_PATH         = "/data/data/pppoe.school/lib/libpppd.so";
	public static final String PPPOE_PATH        = "/data/data/pppoe.school/lib/libpppoe.so";

	
	public static final String RWX_POWER         = "chmod 755 ";
	
	public static final String PPPOE_PID       = "/data/data/pppoe.school/pppoe.pid";
	public static final String ROOT1           = "/system/bin/su";
	public static final String ROOT2           = "/system/xbin/su";


	
	public static final int DIAL_V1XX    	 = 0;
	public static final int DIAL_V201   	 = 1;
	public static final int DIAL_V202    	 = 2;
	public static final int DIAL_V205    	 = 3;
	public static final int DIAL_V207    	 = 4;
	public static final int DIAL_V208   	 = 5;
	
	
	public static final int E_WALN       = 0;
	public static final int E_ETH        = 1;
	
}
