#ifndef _HAD_ZIPCONF_H
#define _HAD_ZIPCONF_H

/*
   zipconf.h -- platform specific include file

   This file was generated automatically by ./make_zipconf.sh
   based on config.h.
 */

#define LIBZIP_VERSION "0.9.3"
#define LIBZIP_VERSION_MAJOR 0
#define LIBZIP_VERSION_MINOR 9
#define LIBZIP_VERSION_MICRO 3

#include <inttypes.h>

typedef signed char int8_t;
#define ZIP_INT8_MAX SCHAR_MAX

typedef uint8_t zip_uint8_t;
#define ZIP_UINT8_MAX UINT8_MAX

typedef  short zip_int16_t;
#define ZIP_INT16_MIN SSHRT_MIN
#define ZIP_INT16_MAX SSHRT_MAX

typedef uint16_t zip_uint16_t;
#define ZIP_UINT16_MAX UINT16_MAX

typedef  int zip_int32_t;
#define ZIP_INT32_MIN SINT_MIN
#define ZIP_INT32_MAX SINT_MAX

typedef uint32_t zip_uint32_t;
#define ZIP_UINT32_MAX UINT32_MAX

typedef int64_t zip_int64_t;
#define ZIP_INT64_MIN INT64_MIN
#define ZIP_INT64_MAX INT64_MAX

typedef uint64_t zip_uint64_t;
#define ZIP_UINT64_MAX UINT64_MAX


#endif /* zipconf.h */
