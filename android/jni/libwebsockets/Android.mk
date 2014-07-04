LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    := libwebsockets
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_SRC_FILES := \
		base64-decode.c \
		client.c \
		client-handshake.c \
		client-parser.c \
		context.c \
		getifaddrs.c \
		handshake.c \
		libwebsockets.c \
		lws-plat-unix.c \
		minilex.c \
		output.c \
		parsers.c \
		pollfd.c \
		server.c \
		server-handshake.c \
		service.c \
		sha-1.c \

#		daemonize.c \
		extension.c \
		extension-deflate-frame.c \
		extension-deflate-stream.c \
		libev.c \
		lws-plat-win.c \
		ssl.c \
		ssl-http2.c

LOCAL_LDLIBS := -lz

include $(BUILD_STATIC_LIBRARY)