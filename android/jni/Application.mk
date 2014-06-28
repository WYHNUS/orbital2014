APP_ABI := armeabi armeabi-v7a
NDK_TOOLCHAIN_VERSION := 4.8
APP_STL := gnustl_static
APP_CPPFLAGS += -std=c++11
APP_CPPFLAGS += -frtti
APP_CPPFLAGS += -fexceptions
APP_CPPFLAGS += -DANDROID
APP_OPTIM := debug