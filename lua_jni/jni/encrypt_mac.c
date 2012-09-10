#include"encrypt.h"

JNIEXPORT jstring JNICALL Java_com_encrypt_Encrypt_stringFromEncrypt(JNIEnv *env, jobject obj, jstring path, jstring username, jstring password)
{
    unsigned short nRand = 0,nSum = 0,nKey  = 0;
    int   i = 0;
    const char *strUsername = (*env)->GetStringUTFChars(env, username, 0);
    const char *strPassword = (*env)->GetStringUTFChars(env, password, 0);
    const char *strpath     = (*env)->GetStringUTFChars(env, path, 0);
    char cTmp[256];
    char cRes[256];

    memset(cRes,'\0',256);
    //初始化lua
    lua_State *L = lua_open();
    //载入lua标准库
    luaL_openlibs(L);
    //注册函数
    luaopen_bit(L);
    //载入执行脚本
    i = luaL_loadfile(L,strpath);
    if (i)
    {
             lua_close(L);
             return (*env)->NewStringUTF(env, "Do you have encrypt.luac in correct path?");
    }

    i = lua_pcall(L, 0, 0, 0);
    if (i)
    {
             lua_close(L);
             return (*env)->NewStringUTF(env, "Fuck you!");
    }
    //播种
    srand((unsigned)time(0));
    //随机数
    nRand = rand();
    for (i = 0;i < strlen(strPassword);i++)
    {
        nSum += strPassword[i];
    }
    nKey = nSum ^ nRand;
    
    //通过函数名取出函数地址压入栈
    lua_getglobal( L,"xxx");
    lua_pushstring(L,strUsername);
    lua_pushstring(L,strPassword);
    lua_pushnumber(L,nRand);

    i = lua_pcall(L, 3, 1, 0);
    if (i)
    {
             lua_close(L);
             return (*env)->NewStringUTF(env, "Fuck you!");
    }
    if (lua_isstring(L, -1) )
    {
             strcpy(cRes,lua_tostring(L, -1));
    }
    else
    {
            return (*env)->NewStringUTF(env, "It's not this type!Are you understand?");
            lua_close(L);
    }
    lua_close(L);
    //转化
    sprintf(cTmp,"%04x",nKey);
    for (i = 0;i < 4;i++)
    {
          cRes[i + 28] = cTmp[i];
    }
    return (*env)->NewStringUTF(env, cRes);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
        //声明变量
        JNIEnv* env = NULL;
        //获取JNI环境对象
        if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) 
        {
                //LOGE("ERROR: GetEnv failed\n");
                return JNI_ERR;
        }
        assert(env != NULL);

        return JNI_VERSION_1_4;
}

