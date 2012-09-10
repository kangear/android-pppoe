// LuaVM.cpp : 定义 DLL 应用程序的入口点。
//

#include "stdafx.h"


#ifdef _MANAGED
#pragma managed(push, off)
#endif

#define    DECLCALAPI extern "C" __declspec(dllexport)
#define    MAX_CHAR   256
#include   "Encrypt_mac.h"

DECLCALAPI int Encrypt300_mac(const char* path,const char* username,const char* password,char* Outbuf,int buflen)
{
	short iError = 0;
	unsigned short nRand = 0,nSum = 0,nKey = 0;
	char cTmp[MAX_CHAR];
	string str_encrypt = "";
	//播种
	srand((unsigned)time(0));

	//随机数
	nRand = rand();
	for (int i = 0;i <(int)strlen(password);i++)
	{
		nSum += password[i];
	}
	nKey = nSum ^ nRand;

	//初始化lua
	lua_State *L = lua_open();
	//载入lua标准库
	luaL_openlibs(L);
	//注册函数
	luaopen_bit(L);
	//载入执行脚本
	iError = luaL_loadfile(L,path);
	if (iError)
	{
		cout << "Load script Failed!" << lua_tostring(L, -1)<< endl;
		lua_close(L);
		strcpy_s(Outbuf,buflen,"Load script Failed!");
		return -1;
	}
	iError = lua_pcall(L, 0, 0, 0);
	if (iError)
	{
		cout << "Run Failed!"<< lua_tostring(L, -1)<< iError<< endl;
		lua_close(L);
		strcpy_s(Outbuf,buflen,"Run Failed!");
		return -1;
	}
	//通过函数名取出函数地址压入栈
	lua_getglobal( L,"xxx");
	lua_pushstring(L,username);
	lua_pushstring(L,password);
	lua_pushnumber(L,nRand);
	iError = lua_pcall(L, 3, 1, 0);
	if (iError)
	{
		cout << "Run Failed!"<< lua_tostring(L, -1)<< iError<< endl;
		lua_close(L);
		strcpy_s(Outbuf,buflen,"Run Failed!");
		return -1;
	}
	if (lua_isstring(L, -1) )
	{
		str_encrypt = lua_tostring(L, -1);
	}
	else
	{
		cout << "Wrong Return Values!" << endl;
		lua_close(L);	
		strcpy_s(Outbuf,buflen,"Wrong Return Values!");
		return -1;
	}
	lua_close(L);	
	//格式化字符
	sprintf_s(cTmp,MAX_CHAR,"%04x",nKey);

	for (int j = 0;j < 4;j++)
	{
		str_encrypt.at(j + 28) = cTmp[j];
	}
	//str_encrypt =  string("~ghca") + str_encrypt;
	return strcpy_s(Outbuf,buflen,str_encrypt.c_str());
}


BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}
#ifdef _MANAGED
#pragma managed(pop)
#endif

