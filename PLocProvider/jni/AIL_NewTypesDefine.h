/*
 * @(#)$Id: AIL_NewTypesDefine.h 18807 2010-11-02 07:24:44Z zhouguang $
 * @(#) Declaration file of New Types Definitions
 *
 * (c)  PIONEER CORPORATION  2010
 * 25-1 Nishi-machi Yamada Kawagoe-shi Saitama-ken 350-8555 Japan
 * All Rights Reserved.
 */
#ifndef CXX_AIL_NEWTYPESDEFINE_H
#define CXX_AIL_NEWTYPESDEFINE_H

#define COMP_OPT_PCORE
#define _FOR_ANDROID_
#define _UNICODE

#ifdef COMP_OPT_PCORE //compiling in Package

#if defined(_LINUX) || defined(_FOR_ANDROID_) || defined(_FOR_IPHONE_)
/*=============================================================================*/
/*include of Linux                                                             */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <ctype.h>
#include <wchar.h>
#include <errno.h>
#include <unistd.h>
#include <utime.h>

/*=============================================================================*/
/*redefinition of Linux                                                        */
typedef signed char BOOL;
typedef void *HANDLE;
typedef int	HWND;
typedef unsigned char BYTE;
typedef int	INT;
typedef short SHORT;
typedef unsigned int UINT;
typedef unsigned short UINT16;
typedef unsigned short USHORT, WORD;
typedef unsigned long ULONG, DWORD, *PDWORD;
typedef long LONG;
typedef int64_t INT64,__int64;
typedef int64_t LONGLONG;
typedef uint64_t ULONGLONG;
typedef float		FLOAT;
//typedef double      DOUBLE;

typedef void *PVOID;
typedef void *LPVOID;
typedef const void *LPCVOID;
typedef BYTE *PBYTE;
typedef INT *PINT;

typedef unsigned char UCHAR;
typedef char CHAR, *PCHAR, *LPSTR, *PSTR;
typedef const CHAR	*LPCSTR;
typedef wchar_t WCHAR;
typedef WCHAR *LPCWSTR, *PCWSTR;

#if defined(UNICODE) || defined(_UNICODE)
typedef wchar_t TCHAR, *PTCHAR;
typedef const WCHAR *PCTSTR, *LPCTSTR;
typedef WCHAR *LPTSTR;
#define __TEXT(quote) L##qutoe
#else //UNICODE || _UNICODE
typedef CHAR TCHAR, *PTCHAR;
typedef const CHAR  *PCTSTR, *LPCTSTR;
typedef CHAR  *LPTSTR;
#define __TEXT(quote) quote
#endif //UNICODE || _UNICODE

#define TEXT(quote) __TEXT(quote)
#define _T(x)	TEXT(x)

#define MAXLONG 0x7fffffffL
#define MAXWORD 0xffff

#ifndef MAX_PATH
#   define MAX_PATH 260
#   define _MAX_PATH MAX_PATH
#endif

#if defined(UNICODE) || defined(_UNICODE)
#   define _tcschr wcschr
#   define _tcslen wcslen
#   define _tcscpy wcscpy
#   define _tcsncpy wcsncpy
#   define _tcschr wcschr
#   define _tcsrchr wcsrchr
#   define _tcscmp wcscmp
#   define _tcsncmp wcsncmp
#   define _stprintf swprintf
#   define _sntprintf swprintf
#   define _vsntprintf vswprintf
#   define lstrcmp wcscmp
#   define lstrcmpi wcscmp
#   define lstrcpy wcscpy
#   define _wcsupr wcsupr
#   define _wcslwr wcslwr
#   define _wcsicmp wcscasecmp
#   define _wcsnicmp wcsncasecmp
#else //UNICODE || _UNICODE
#   define _tscchr strchr
#   define _tcslen strlen
#   define _tcscpy strcpy
#   define _tcsncpy strncpy
#   define _tcschr strchr
#   define _tcsrchr strrchr
#   define _tcscmp strcmp
#   define _tcsncmp strncmp
#   define _stprintf sprintf
#   define _snprintf snprintf
#   define _sntprintf snprintf
#   define _vsntprintf vsnprintf
#   define lstrcmp strcmp
#   define lstrcmpi strcmp
#   define lstrcpy strcpy
#   define _strupr strupr
#   define _strlwr strlwr
#   define _stricmp strcasecmp
#   define _strnicmp strncasecmp
#endif //UNICODE || _UNICODE
#define _vsnprintf vsnprintf
#define _vsnwprintf vswprintf

#ifndef CONST
#   define CONST const
#endif

#ifndef VOID
#   define VOID void
#endif

#ifndef FALSE
#   define FALSE 0
#endif

#ifndef TRUE
#   define TRUE 1
#endif

#define INFINITE 0xffffffffL

/*=============================================================================*/
/*redefinition of Linux                                                        */
#ifdef __cplusplus
extern "C" {
#endif
void OutputDebugStringA(LPCSTR lpOutputString);
void OutputDebugStringW(LPCWSTR lpOutputString);
void Sleep(DWORD sec);
#ifdef __cplusplus
}
#endif

#if defined(UNICODE) || defined(_UNICODE)
#	define OutputDebugString  OutputDebugStringW
#else
#	define OutputDebugString  OutputDebugStringA
#endif//UNICODE || _UNICODE

#endif// _LINUX

/*=============================================================================*/
/*redefine for NewTypeDefine.h                                                 */
typedef long NP_ERROR;
typedef double DOUBLE;
#ifdef __cplusplus
typedef bool NP_BOOL;
const NP_BOOL NP_TRUE  = true;
const NP_BOOL NP_FALSE = false;
#endif

#ifndef PAI
#   define PAI (3.141592654F)
#endif

/*=============================================================================*/
/*redefine for NChar.h                                                         */
#ifdef NSTRING_UNICODE

typedef WCHAR NCHAR;
#define NTEXT(C) L##C
#define NCHARstrcat		wcscat
#define NCHARstrchr		wcschr
#define NCHARstrcmp		wcscmp
#define NCHARstrcpy		wcscpy
#define NCHARstrcspn	wcscspn
#define NCHARstrlen		wcslen
#define NCHARstrncat	wcsncat
#define NCHARstrncmp	wcsncmp
#define NCHARstrncpy	wcsncpy
#define NCHARstrpbrk	wcspbrk
#define NCHARstrrchr	wcsrchr
#define NCHARstrspn		wcsspn
#define NCHARstrstr		wcsstr
#define NCHARstrtok		wcstok
#define NCHARisalnum	iswalnum
#define NCHARisalpha	iswalpha
#define NCHARiscntrl	iswcntrl
#define NCHARisdigit	iswdigit
#define NCHARisgraph	iswgraph
#define NCHARislower	iswlower
#define NCHARisprint	iswprint
#define NCHARispunct	iswpunct
#define NCHARisspace	iswspace
#define NCHARisupper	iswupper
#define NCHARisxdigit	iswxdigit
#define NCHARtolower	towlower
#define NCHARtoupper	towupper
#define NCHARsprintf	swprintf
#define NCHARatoi		_wtoi
#define NCHARatol		_wtol
#define NCHARstrtod		wcstod
#define NCHARstrtol		wcstol
#define NCHARstrtoul	wcstoul
#define NCHARstrlwr		_wcslwr
#define NCHARstrupr		_wcsupr
#define NCHARstricmp	_wcsicmp
#define NCHARstrnicmp	_wcsnicmp

#else //NSTRING_UNICODE

typedef char NCHAR;
#define NTEXT(C) C
#define NCHARstrcat		strcat
#define NCHARstrchr		strchr
#define NCHARstrcmp		strcmp
#define NCHARstrcpy		strcpy
#define NCHARstrcspn	strcspn
#define NCHARstrlen		strlen
#define NCHARstrncat	strncat
#define NCHARstrncmp	strncmp
#define NCHARstrncpy	strncpy
#define NCHARstrpbrk	strpbrk
#define NCHARstrrchr	strrchr
#define NCHARstrspn		strspn
#define NCHARstrstr		strstr
#define NCHARstrtok		strtok

#define NCHARisalnum	isalnum
#define NCHARisalpha	isalpha
#define NCHARiscntrl	iscntrl
#define NCHARisdigit	isdigit
#define NCHARisgraph	isgraph
#define NCHARislower	islower
#define NCHARisprint	isprint
#define NCHARispunct	ispunct
#define NCHARisspace	isspace
#define NCHARisupper	isupper
#define NCHARisxdigit	isxdigit
#define NCHARtolower	tolower
#define NCHARtoupper	toupper

#define NCHARsprintf	sprintf
#define NCHARatoi		atoi
#define NCHARatol		atol
#define NCHARstrtod		strtod
#define NCHARstrtol		strtol
#define NCHARstrtoul	strtoul

#define NCHARstrlwr		_strlwr
#define NCHARstrupr		_strupr

///Perform a lowercase comparison of strings.
#define NCHARstricmp	_stricmp
///Compare characters of two strings without regard to case.
#define NCHARstrnicmp	_strnicmp

#endif //NSTRING_UNICODE

#endif //COMP_OPT_PCORE

/*=============================================================================*/
/*redefine for others                                                          */
#ifndef MAX
#   define MAX(a,b)	(((a)>(b))?(a):(b))
#endif

#ifndef MIN
#   define MIN(a,b)	(((a)<(b))?(a):(b))
#endif

#if defined(_WIN32) || defined(_WIN32_WCE)
typedef WCHAR UCHAR16;
#else
typedef UINT16 UCHAR16;
#endif

#endif // CXX_AIL_NEWTYPESDEFINE_H
/* EOF */
