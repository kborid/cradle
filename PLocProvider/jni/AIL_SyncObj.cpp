/*
 * @(#)$Id: AIL_SyncObj.cpp 10467 2010-09-15 13:37:47Z zhouguang $
 * @(#) Implementation file of class AIL_SyncObj
 *
 * (c)  PIONEER CORPORATION  2010
 * 25-1 Nishi-machi Yamada Kawagoe-shi Saitama-ken 350-8555 Japan
 * All Rights Reserved.
 */
//#include "stdafx.h"

#ifndef CXX_AIL_SYNCOBJ_H
#	include "AIL_SyncObj.h"
#endif

AIL_SyncObj::AIL_SyncObj()
{
#if defined(_WIN32) || defined(_WIN32_WCE)
	::InitializeCriticalSection(&cs);
#else
	pthread_mutexattr_init(&attr);
	pthread_mutexattr_settype(&attr, PTHREAD_MUTEX_RECURSIVE);
	pthread_mutex_init(&mutex, &attr);
#endif
}

AIL_SyncObj::~AIL_SyncObj()
{
#if defined(_WIN32) || defined(_WIN32_WCE)
	::DeleteCriticalSection(&cs);
#else
	pthread_mutexattr_destroy(&attr);
	pthread_mutex_destroy(&mutex);
#endif
}

VOID
AIL_SyncObj::SyncStart()
{
#if defined(_WIN32) || defined(_WIN32_WCE)
	::EnterCriticalSection(&cs);
#else
	pthread_mutex_lock(&mutex);
#endif
}

NP_BOOL
AIL_SyncObj::TrySyncStart()
{
#if defined(_WIN32) || defined(_WIN32_WCE)
	BOOL blRet = ::TryEnterCriticalSection(&cs);
	return blRet?NP_TRUE:NP_FALSE;
#else
    int ret = pthread_mutex_trylock(&mutex);
	return (ret==0)?NP_TRUE:NP_FALSE;
#endif
}

VOID
AIL_SyncObj::SyncEnd()
{
#if defined(_WIN32) || defined(_WIN32_WCE)
	::LeaveCriticalSection(&cs);
#else
	pthread_mutex_unlock(&mutex);
#endif
}

/* EOF */
