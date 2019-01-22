/*=============================================================================

	@(#)$Id: NmeaDecode.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecode class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecode implement class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"
#include <android/log.h>
# define REMOVE_GPS_DATA

#ifndef CXX_NMEADECODE_H
#	include "NmeaDecode.h"
#endif

#ifndef REMOVE_GPS_DATA
#ifndef CXX_GPSSNSMODULE_H
#	include "GpsSnsModule.h"
#endif
#endif
/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Receive NMEA information
BOOL CNmeaDecode::ReceiveNmeaInfo(NmeaReceiveSource eRecSrc, const VOID* pData, DWORD dwSize)
{
	// Check input
	if (eRecSrc == NMEA_RECEIVE_SOURCE_NONE
		|| pData == NULL
		|| dwSize == 0)
	{
		return FALSE;
	}

	m_eRecSrc = eRecSrc;
	return m_cRingBuffer.ReceiveNmeaInfo(pData, dwSize);
}

/*===========================================================================*/
// Get GPS data
BOOL CNmeaDecode::GetGpsData(GPS_DATA &rsGpsData)
{
	return m_cMainProcess.GetGpsData(rsGpsData);
}

/*===========================================================================*/
// Decode NMEA recode in one line
BOOL CNmeaDecode::DecodeNmeaRecode(const CHAR* pStrData)
{
	return m_cMainProcess.DecodeNmeaRecode(pStrData);
}


#ifndef REMOVE_GPS_DATA
// Update GPS data
VOID CNmeaDecode::UpdateGpsData(const GPS_DATA &rsGpsData)
{
	WORD wDataSrc = MAILDATA_ERROR;

	switch (m_eRecSrc)
	{
	case NMEA_RECEIVE_SOURCE_ANDROID:
		wDataSrc = MAILDATA_ANDROID_BUILTIN_GPS_ONLY;
		break;
	case NMEA_RECEIVE_SOURCE_IPHONE:
		wDataSrc = MAILDATA_IPHONE_BUILTIN_GPS_ONLY;
		break;
	case NMEA_RECEIVE_SOURCE_CRADLE:
		wDataSrc = MAILDATA_GPS_ONLY_NMEA_TEXT;
		break;
	case NMEA_RECEIVE_SOURCE_OTHER:
		wDataSrc = MAILDATA_GPS_ONLY_NMEA_TEXT;
		break;
	default:
		// ERROR
		// Nothing to do
		return;
		break;
	}

	sGpsSnsMailData  mailData = {0};

	// Set mail data source
	mailData.nMailSrc = wDataSrc;

	// Set mail data GPS data from NMEA log
	mailData.sGpsData = rsGpsData;

	// Update to GpsSnsModule
	GpsSnsModule::Instance()->UpdateMailData(mailData);

	m_bFullPacketFlag = TRUE;
}
#endif
/*===========================================================================*/
// static pointer for singleton
CNmeaDecode* CNmeaDecode::s_pcInstance = NULL;

/*===========================================================================*/
// Get the decode process instance
CNmeaDecode* CNmeaDecode::Instance(VOID)
{
	// TODO: ATTENTION
	// Need to Synchronized

	if (s_pcInstance == NULL)
	{
		s_pcInstance = new CNmeaDecode;
	}

	return s_pcInstance;
}

/*===========================================================================*/
// Destroy the factory interface instance
VOID CNmeaDecode::Destroy(VOID)
{
	if (s_pcInstance != NULL)
	{
		delete s_pcInstance;
		s_pcInstance = NULL;
	}
}

/*===========================================================================*/
// Constructor
CNmeaDecode::CNmeaDecode() :
	m_cRingBuffer(),
	m_cMainProcess(),
	m_eRecSrc(NMEA_RECEIVE_SOURCE_NONE),
	m_bFullPacketFlag(FALSE)
{

}

/*===========================================================================*/
// Destructor
CNmeaDecode::~CNmeaDecode()
{

}
