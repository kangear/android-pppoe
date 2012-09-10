#ifndef _ENCRYPT_H
#define _ENCRYPT_H

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <assert.h>

#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"

#ifdef __cplusplus
extern "C" {
#endif

LUALIB_API int luaopen_bit(lua_State *L);

#ifdef __cplusplus
}
#endif

#endif
