/*=============================================================================

	@(#)$Id: NmeaDecodeFactory.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) GpsSnsNmeaFactory class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeFactory class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"
#include <android/log.h>
#ifndef CXX_NMEADECODEFACTORY_H
#	include "NmeaDecodeFactory.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Get the factory interface instance
CNmeaDecodeFactory* CNmeaDecodeFactory::Instance(VOID)
{
	return CNmeaDecodeConcreteFactory::Instance();
}

// Destroy the factory interface instance
VOID CNmeaDecodeFactory::Destroy(VOID)
{
	CNmeaDecodeConcreteFactory::Destroy();
}

// Constructor
CNmeaDecodeFactory::CNmeaDecodeFactory() :
	m_iObjectCount(0)
{
	// Nothing to do
}

// Destructor
CNmeaDecodeFactory::~CNmeaDecodeFactory()
{
	// Nothing to do
}


/*===========================================================================*/
// Get the NMEA recode object
CNmeaDecodeObject* CNmeaDecodeConcreteFactory::CreateNmeaDecodeObejct(const CHAR *pStrData)
{
	CNmeaDecodeObject* pcTemp = NULL;
	// Create different object by check input
	if (memcmp(pStrData, "$GPGGA", 6) == 0)
	{
		pcTemp = new CGGADecodeObject(pStrData);
	}
	else if (memcmp(pStrData, "$GPRMC", 6) == 0)
	{
		pcTemp = new CRMCDecodeObject(pStrData);
	}
	else if (memcmp(pStrData, "$GPGSA", 6) == 0)
	{
		pcTemp = new CGSADecodeObject(pStrData);
	}
	else if (memcmp(pStrData, "$GPGSV", 6) == 0)
	{
		pcTemp = new CGSVDecodeObject(pStrData);
	}
	else
	{
		// Nothing to do
	}

	// Create object successful increase the counter
	if (pcTemp != NULL)
	{
		m_iObjectCount++;
	}

	return pcTemp;
}

// Destroy the NMEA recode object
VOID CNmeaDecodeConcreteFactory::DestroyNmeaDecodeObejct(CNmeaDecodeObject* pcNmeaDecodeObject)
{
	// Check input
	if (pcNmeaDecodeObject != NULL)
	{
		// Release the space
		delete pcNmeaDecodeObject;
		pcNmeaDecodeObject = NULL;

		// decrease the counter
		m_iObjectCount--;
	}
}

// CGpsSnsNmeaFactory instance declare and create
CNmeaDecodeConcreteFactory* CNmeaDecodeConcreteFactory::s_pcInstance = NULL;

// Get the factory interface instance
CNmeaDecodeConcreteFactory* CNmeaDecodeConcreteFactory::Instance(VOID)
{
	// TODO: ATTENTION
	// Need to Synchronized

	if (s_pcInstance == NULL)
	{
		s_pcInstance = new CNmeaDecodeConcreteFactory;
	}

	return s_pcInstance;
}

// Destroy the factory interface instance
VOID CNmeaDecodeConcreteFactory::Destroy(VOID)
{
	if (s_pcInstance != NULL)
	{
		// delete the instance
		delete s_pcInstance;
		s_pcInstance = NULL;
	}
}

// Constructor
CNmeaDecodeConcreteFactory::CNmeaDecodeConcreteFactory() :
	CNmeaDecodeFactory()
{
	// Nothing to do
}

// Destructor
CNmeaDecodeConcreteFactory::~CNmeaDecodeConcreteFactory()
{
	// Nothing to do
}
