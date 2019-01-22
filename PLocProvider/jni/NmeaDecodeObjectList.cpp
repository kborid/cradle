/*=============================================================================

	@(#)$Id: NmeaDecodeObjectList.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecodeObjectList class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeObjectList class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"

#ifndef CXX_NMEADECODEOBJECTLIST_H
#	include "NmeaDecodeObjectList.h"
#endif

#ifndef CXX_NMEADECODEFACTORY_H
#	include "NmeaDecodeFactory.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Constructor
CNmeaDecodeObjectList::CNmeaDecodeObjectList()
{
	// Initialize all members
	for (INT iCnt = 0; iCnt < OBJECT_LIST_LENGTH; iCnt++)
	{
		m_pcaObjectList[iCnt] = NULL;
	}

	m_iListHead = 0;
	m_iListTail = 0;
	m_bIsFull	= FALSE;
}

// Destructor
CNmeaDecodeObjectList::~CNmeaDecodeObjectList()
{
	// Clear the list
	for (INT iCnt = 0; iCnt < OBJECT_LIST_LENGTH; iCnt++)
	{
		m_pcaObjectList[iCnt] = NULL;
	}

	m_iListHead = 0;
	m_iListTail = 0;
	m_bIsFull	= FALSE;
}

// Push the object to the end of the list
BOOL CNmeaDecodeObjectList::Push(CNmeaDecodeObject* pcObject)
{
	if (IsFull() == TRUE)
	{
		// The list is FULL
		// Can not push a object
		return FALSE;
	}

	// Move the tail position
	m_pcaObjectList[m_iListTail] = pcObject;
	m_iListTail = (m_iListTail + 1) % OBJECT_LIST_LENGTH;

	// FULL
	if (m_iListTail == m_iListHead)
	{
		m_bIsFull = TRUE;
	}

	return TRUE;
}

// Pop the object of the head of the list 
BOOL CNmeaDecodeObjectList::Pop(CNmeaDecodeObject* &rpcObject)
{
	if (IsEmpty() == TRUE)
	{
		// The list is EMPTY
		// Can not pop a object
		return FALSE;
	}

	rpcObject = m_pcaObjectList[m_iListHead];
	m_pcaObjectList[m_iListHead] = NULL;
	m_iListHead = (m_iListHead + 1) % OBJECT_LIST_LENGTH;

	// Not FULL
	if (m_bIsFull == TRUE)
	{
		m_bIsFull = FALSE;
	}

	return TRUE;
}

// Check the list empty or not
BOOL CNmeaDecodeObjectList::IsEmpty(VOID)
{
	// Check the head and tail pointer
	if (m_iListHead == m_iListTail
		&& m_bIsFull != TRUE)
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}

// Check the list full or not
BOOL CNmeaDecodeObjectList::IsFull(VOID)
{
	// Check next tail position and head
	if (m_iListHead == m_iListTail
		&& m_bIsFull == TRUE)
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}