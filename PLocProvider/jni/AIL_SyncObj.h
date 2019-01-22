/*
 * @(#)$Id: AIL_SyncObj.h 15202 2010-10-18 10:13:22Z zhouguang $
 * @(#) Declaration file of class AIL_SyncObj
 *
 * (c)  PIONEER CORPORATION  2010
 * 25-1 Nishi-machi Yamada Kawagoe-shi Saitama-ken 350-8555 Japan
 * All Rights Reserved.
 */
#ifndef CXX_AIL_SYNCOBJ_H
#define CXX_AIL_SYNCOBJ_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif
/*
#ifndef CXX_PUTILLIBAPI_H
#   include "putillibAPI.h"
#endif
*/
#ifndef CXX_AIL_NEWTYPESDEFINE_H
#   include "AIL_NewTypesDefine.h"
#endif
#if !defined(_WIN32) && !defined(_WIN32_WCE)
#   include <pthread.h>
#endif //_LINUX

// Class declaration
class AIL_SyncObj;

/**
 *  class AIL_SyncObj
 *
 * The class of Synchronizing object
 *
 * @author $Author: zhouguang $
 * @version $Revision: 15202 $
 * @date $Date:: 2010-10-18 18:13:22 #$
 */
class AIL_SyncObj
{
protected:
#if defined(_WIN32) || defined(_WIN32_WCE)
	CRITICAL_SECTION cs;
#else
	pthread_mutex_t mutex;
	pthread_mutexattr_t attr;
#endif //_LINUX

private:
	AIL_SyncObj(const AIL_SyncObj& src){}
	AIL_SyncObj& operator = (const AIL_SyncObj& src){return *this;}

public:
	/**
	* Construction.
	*/
	AIL_SyncObj();

	/**
	* Destruction.
	*/
	virtual	~AIL_SyncObj();

	/**
	* Synchronize start.
	*/
	VOID SyncStart();

	/**
	* Try synchronize start
	*
	* @return NP_BOOL : NP_TRUE means synchronize succeed, and NP_FALSE failed.
	*/
	NP_BOOL TrySyncStart();

	/**
	* Synchronize end.
	*/
	VOID SyncEnd();
};

#endif // CXX_AIL_SYNCOBJ_H
/* EOF */
