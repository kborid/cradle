/*=============================================================================

	@(#)$Id: NmeaDecodeRing.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecodeRing class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeRing class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"
#include <android/log.h>

#ifndef CXX_NMEADECODERING_H
#	include "NmeaDecodeRing.h"
#endif

#ifndef CXX_NMEADECODE_H
#	include "NmeaDecode.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Constructor
CNmeaDecodeRing::CNmeaDecodeRing() :
	m_dwUsingBufSize(0)
{
	memset(m_pSearchBuffer, 0, sizeof(m_pSearchBuffer));
	memset(m_pSendBuffer, 0, sizeof(m_pSendBuffer));
}

/*===========================================================================*/
// Destructor
CNmeaDecodeRing::~CNmeaDecodeRing()
{

}


/*===========================================================================*/
// Receive NMEA information
BOOL CNmeaDecodeRing::ReceiveNmeaInfo(const VOID* pData, DWORD dwSize)
{
	// Check input
	if (pData == NULL
		|| dwSize <= 0)
	{
		return FALSE;
	}

	INT iCount = 0;
	INT iNextLog = 0;

	// Check the remain buffer
	if (m_dwUsingBufSize + dwSize > NMEA_DECODE_SEARCH_BUF_SIZE)
	{
		// Calculate the cancel data size
		DWORD dwCancelSize = m_dwUsingBufSize + dwSize - NMEA_DECODE_SEARCH_BUF_SIZE;
		// Move enough space for new data
		memmove(m_pSearchBuffer, m_pSearchBuffer + dwCancelSize, NMEA_DECODE_SEARCH_BUF_SIZE - dwCancelSize);
		// Clear the moved buffer
		memset(m_pSearchBuffer + NMEA_DECODE_SEARCH_BUF_SIZE - dwCancelSize, 0, dwCancelSize);
		// Reset using buffer size
		m_dwUsingBufSize = m_dwUsingBufSize - dwCancelSize;
	}

	// Adding new data to search buffer
	memcpy(m_pSearchBuffer + m_dwUsingBufSize, pData, dwSize);

	// Begin to search a total NMEA log
	for(iCount = 0; iCount < static_cast<INT>(m_dwUsingBufSize + dwSize); iCount++)
	{
		if (m_pSearchBuffer[iCount] == '$')
		{
			iNextLog = iCount;
		}
		else if(m_pSearchBuffer[iCount] == '\n')
		{
			// Clear send buffer
			memset(m_pSendBuffer, 0, sizeof(m_pSendBuffer));

			// Copy data to send buffer
			memcpy(m_pSendBuffer, m_pSearchBuffer + iNextLog, iCount - iNextLog + 1);

			// Next log head position save
			iNextLog = iCount + 1;

			// Analysis NMEA log by decode process
			CNmeaDecode::Instance()->DecodeNmeaRecode(static_cast<const CHAR*>(m_pSendBuffer));

			// Check get a full packet
			if (CNmeaDecode::Instance()->GetFullPacketFlag() == TRUE)
			{
				// One packet once
				CNmeaDecode::Instance()->ResetFullPacketFlag();
				break;
			}
		}
	}

	// Calculate using buffer size
	m_dwUsingBufSize = m_dwUsingBufSize + dwSize - iNextLog;
	// Move remain data to the head
	memmove(m_pSearchBuffer, m_pSearchBuffer + iNextLog, m_dwUsingBufSize);
	// Clear empty buffer
	memset(m_pSearchBuffer + m_dwUsingBufSize, 0, NMEA_DECODE_SEARCH_BUF_SIZE - m_dwUsingBufSize);

	// Always return true
	return NP_TRUE;
}
