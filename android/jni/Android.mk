LOCAL_PATH := $(call my-dir)
#include $(CLEAR_VARS)
#LOCAL_MODULE := libicudata
#LOCAL_SRC_FILES := v8/libicudata.a
#include $(PREBUILT_STATIC_LIBRARY)
#include $(CLEAR_VARS)
#LOCAL_MODULE := libicui18n
#LOCAL_SRC_FILES := v8/libicui18n.a
#include $(PREBUILT_STATIC_LIBRARY)
#include $(CLEAR_VARS)
#LOCAL_MODULE := libicuuc
#LOCAL_SRC_FILES := v8/libicuuc.a
#include $(PREBUILT_STATIC_LIBRARY)
#include $(CLEAR_VARS)
#LOCAL_MODULE := libv8_libbase.arm
#LOCAL_SRC_FILES := v8/libv8_libbase.arm.a
#include $(PREBUILT_STATIC_LIBRARY)
#include $(CLEAR_VARS)
#LOCAL_MODULE := libv8_snapshot
#LOCAL_SRC_FILES := v8/libv8_snapshot.a
#include $(PREBUILT_STATIC_LIBRARY)
include $(CLEAR_VARS)
LOCAL_MODULE := libappsupport
LOCAL_CFLAGS := -Werror
LOCAL_CPP_EXTENSION := .cpp .cc
LOCAL_C_INCLUDE := $(LOCAL_PATH)
LOCAL_SRC_FILES := appsupport.cpp
LOCAL_LDLIBS := -llog -lstdc++
#LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libv8_libbase.arm.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libv8_base.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libv8_snapshot.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libv8_nosnapshot.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libv8_libbase.arm.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libicui18n.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libicuuc.a
LOCAL_LDLIBS += $(LOCAL_PATH)/v8/libicudata.a
LOCAL_LDLIBS += $(LOCAL_PATH)/libstlport_static.a

include $(BUILD_SHARED_LIBRARY)
