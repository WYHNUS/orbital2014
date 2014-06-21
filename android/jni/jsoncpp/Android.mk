LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_CFLAGS := 

LOCAL_MODULE    := jsoncpp
LOCAL_SRC_FILES :=\
	json_reader.cpp \
	json_value.cpp \
	json_writer.cpp
	
	
include $(BUILD_STATIC_LIBRARY)