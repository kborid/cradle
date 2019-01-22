/*=============================================================================

    @(#)$Id: NmeaDecodeObjectList.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeObjectList class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeObjectList class define file

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODEOBJECTLIST_H
#define CXX_NMEADECODEOBJECTLIST_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif
#ifndef CXX_NEWTYPEDEFINE_H
#	include "AIL_NewTypesDefine.h"
#endif
/*===========================================================================*/
/*  included files                                                           */

#ifndef CXX_NMEADECODEDEFINE_H
#   include "NmeaDecodeDefine.h"
#endif

#ifndef CXX_NMEADECODEOBJECT_H
#	include "NmeaDecodeObject.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDecodeObjectList class

	NMEA decode process object list to install a packet data
	
	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeObjectList
{
public:

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CNmeaDecodeObjectList();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeObjectList();

	/**
	 * Push the object to the end of the list
	 *
	 * @param  CNmeaDecodeObject* pcObject [IN]: need to push object
	 *
	 * @return BOOL : push action successful or not
	 */
	BOOL Push(CNmeaDecodeObject* pcObject);

	/**
	 * Pop the object of the head of the list 
	 *
	 * @param  CNmeaDecodeObject* &rpcObject [OUT]: need to pop object
	 *
	 * @return BOOL : pop action successful or not
	 */
	BOOL Pop(CNmeaDecodeObject* &rpcObject);

	/**
	 * Check the list empty or not
	 *
	 * @param  NONE
	 *
	 * @return BOOL : the list is empty or not
	 */
	BOOL IsEmpty(VOID);

protected:

private:

	// All members
	static const INT	OBJECT_LIST_LENGTH = 10;
	CNmeaDecodeObject*	m_pcaObjectList[OBJECT_LIST_LENGTH];
	INT					m_iListHead;
	INT					m_iListTail;
	BOOL				m_bIsFull;

	/**
	 * Check the list full or not
	 *
	 * @param  NONE
	 *
	 * @return BOOL : the list is full or not
	 */
	BOOL IsFull(VOID);

};

#endif  // end of CXX_NMEADECODEOBJECTLIST_H
