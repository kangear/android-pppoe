LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_MODULE       := rp-pppoe
LOCAL_SRC_FILES:=\
    pppoe.c \
    if.c \
    debug.c \
    common.c \
    ppp.c \
    discovery.c \


LOCAL_STATIC_LIBRARIES :=

LOCAL_MODULE_PATH    := $(TARGET_OUT_OPTIONAL_EXECUTABLES)
LOCAL_MODULE_TAGS    := eng
LOCAL_C_INCLUDES        += $(LOCAL_PATH)/src
LOCAL_CFLAGS               += '-DVERSION="3.10"' \
   		             '-DPPPD_PATH="/system/bin/pppd"'

include $(BUILD_SHARED_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))