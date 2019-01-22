/*=============================================================================

	@(#)$Id: NmeaDecodeProcess.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecodeProcess class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeProcess class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"
#include <Android/log.h>
#ifndef CXX_NMEADECODEDEFINE_H
#   include "NmeaDecodeDefine.h"
#endif

#ifndef CXX_NMEADECODEPROCESS_H
#	include "NmeaDecodeProcess.h"
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
CNmeaDecodeProcess::CNmeaDecodeProcess() :
	m_dwCounter(0)
{
	m_pcFactory = CNmeaDecodeFactory::Instance();
	m_pcStatMachine = CNmeaDecodeStateMachine::Instance();

	m_bIsCompletePakct = FALSE;
	memset(&m_sGpsData, 0, sizeof(m_sGpsData));
}

/*===========================================================================*/
// Destructor
CNmeaDecodeProcess::~CNmeaDecodeProcess()
{
	// Release all object in the object list
	CNmeaDecodeObject* pNmeaObject = NULL;

	while (m_cObjectList.Pop(pNmeaObject) == TRUE)
	{
		m_pcFactory->DestroyNmeaDecodeObejct(pNmeaObject);
	}

	// Release the factory instance
	CNmeaDecodeFactory::Destroy();
	m_pcFactory = NULL;

	// Release the state machine instance
	CNmeaDecodeStateMachine::Destroy();
	m_pcStatMachine = NULL;
}

/*===========================================================================*/
// Decode NMEA recode in one line
BOOL CNmeaDecodeProcess::DecodeNmeaRecode(const CHAR* pStrData)
{
	// Check input
	if (pStrData == NULL)
	{
		return FALSE;
	}

	// Adding counter
	m_dwCounter++;

	// Check the NMEA recode
	if (CheckSumNmeaRecode(pStrData) == FALSE)
	{
		return FALSE;
	}

	// Create current NMEA recode object
	CNmeaDecodeObject* pcNmeaObj = m_pcFactory->CreateNmeaDecodeObejct(pStrData);

	// Check pointer
	if (pcNmeaObj == NULL)
	{
		return FALSE;
	}

	// Check decode result
	if (pcNmeaObj->IsValid() == FALSE)
	{
		// Release the object
		m_pcFactory->DestroyNmeaDecodeObejct(pcNmeaObj);

		// Return
		return FALSE;
	}

	// State machine run to next state
	if (m_pcStatMachine->Run(pcNmeaObj) == TRUE)
	{
		// Check packet begin
		if (m_pcStatMachine->IsPacketBegin() == TRUE)
		{
			// Clear all the object in the list
			CNmeaDecodeObject* pcClearNmeaObj = NULL;

			while (m_cObjectList.Pop(pcClearNmeaObj) == TRUE)
			{
				// Release the object
				m_pcFactory->DestroyNmeaDecodeObejct(pcClearNmeaObj);
				pcClearNmeaObj = NULL;
			}

			// Start synchronize
			m_cDataSync.SyncStart();

			// Clear OLD GPS_DATA
			memset(&m_sGpsData, 0, sizeof(GPS_DATA));

			// Reset the flag
			m_bIsCompletePakct = FALSE;

			// End synchronize
			m_cDataSync.SyncEnd();
		}

		// Push current to the object list
		// To wait a complete packet to install a GPS data
		m_cObjectList.Push(pcNmeaObj);

		// Check packet end
		if (m_pcStatMachine->IsPacketEnd() == TRUE)
		{
			GPS_DATA sTempData = {0};

			// Install the GPS data
			if (InstallGpsData(sTempData) == TRUE)
			{
				// Start synchronize
				m_cDataSync.SyncStart();

				// Get a complete packet
				m_bIsCompletePakct = TRUE;
				m_sGpsData = sTempData;
				// End synchronize
				m_cDataSync.SyncEnd();

				// Update GPS data
//				CNmeaDecode::Instance()->UpdateGpsData(sTempData);

				// Clear state of state machine to NONE
				m_pcStatMachine->ClearState();
			}
			else
            {
                __android_log_print(ANDROID_LOG_WARN, "GPS_DATA",	"GPS_DATA InstallGpsData== false");
            }
		}
	}
	else
	{
		// Release the object
		m_pcFactory->DestroyNmeaDecodeObejct(pcNmeaObj);
	}

	return TRUE;
}

/*===========================================================================*/
// Get GPS data of a complete packet
BOOL CNmeaDecodeProcess::GetGpsData(GPS_DATA &rsGpsData)
{
	BOOL bRet = FALSE;

	// Start synchronize
	m_cDataSync.SyncStart();

	// Check get a complete packet
	if (m_bIsCompletePakct == TRUE)
	{
		rsGpsData = m_sGpsData;

		bRet = TRUE;
	}

	// End synchronize
	m_cDataSync.SyncEnd();

	return bRet;
}

/*===========================================================================*/
// Check sum of the data in NMEA recode in one line
BOOL CNmeaDecodeProcess::CheckSumNmeaRecode(const CHAR* pStrData)
{
	INT		iLength		= 0;		// string data length
	INT		iCount		= 0;		// count for cycle
	BYTE	chksum		= 0x00;		// check sum
	CHAR	chkCap[3]	= {0};		// check sum buffer capital letter
	CHAR	chkLow[3]	= {0};		// check sum buffer lower case letter

	// Check input
	if (pStrData == NULL)
	{
		return FALSE;
	}

	// Get the string length
	iLength = static_cast<INT>(strlen(pStrData));

	// Get the check sum in the string data (without the '$' at begin)
	for (iCount = 1; iCount < iLength; iCount++)
	{
		if (pStrData[iCount] == '*')
		{

			if (((iCount + 4) < iLength) || (((iCount + 4) == iLength) && pStrData[iCount + 3] == '\n'))
			{
				// Check sum
				sprintf(chkCap, "%02X", chksum);
				sprintf(chkLow, "%02x", chksum);

				if ((pStrData[iCount + 1] == chkCap[0] && pStrData[iCount + 2] == chkCap[1])
					|| (pStrData[iCount + 1] == chkLow[0] && pStrData[iCount + 2] == chkLow[1]))
				{
					return TRUE;
				}
				else
				{
					return FALSE;
				}
			}
			else
			{
				return FALSE;
			}
		}

		// Check sum
		chksum = chksum ^ (BYTE)pStrData[iCount];
	}

	// ERROR
	return FALSE;
}

/*===========================================================================*/
// Install the GPS data by object
BOOL CNmeaDecodeProcess::InstallGpsData(GPS_DATA &rsGpsData)
{
	CNmeaDecodeObject* pNmeaObject = NULL;

	// Check the object list
	if (m_cObjectList.IsEmpty() == TRUE)
	{
		return FALSE;
	}

	// Get object for the list
	while (m_cObjectList.Pop(pNmeaObject) == TRUE)
	{
		// Makeup the GPS data
		switch (pNmeaObject->GetNmeaType())
		{
		case NMEA_DECODE_TYPE_NONE:
			// Nothing to do
			break;
		case NMEA_DECODE_TYPE_GGA:
			{
				// Change to GGA object
				CGGADecodeObject *pcGGAObj = static_cast<CGGADecodeObject *>(pNmeaObject);

				// Get longitude
				rsGpsData.lLon = static_cast<LONG>(pcGGAObj->GetLongitude() * 256);

				// Get latitude
				rsGpsData.lLat = static_cast<LONG>(pcGGAObj->GetLatitude() * 256);

				// Get altitude
				rsGpsData.fAltitude = pcGGAObj->GetAltitude();

				// Get Longitude
				rsGpsData.bySatellite = static_cast<BYTE>(pcGGAObj->GetSatelliteCnt());

			}
			break;
		case NMEA_DECODE_TYPE_RMC:
			{
				// Change to RMC object
				CRMCDecodeObject *pcGGAObj = static_cast<CRMCDecodeObject *>(pNmeaObject);

				// Get time
				CRMCDecodeObject::STime sTime = pcGGAObj->GetTime();

				// UTC time in BCD type year
				rsGpsData.byUTCTime[0] = sTime.wYear % 100;

				// UTC time in BCD type month
				rsGpsData.byUTCTime[1] = sTime.byMonth;

				// UTC time in BCD type day
				rsGpsData.byUTCTime[2] = sTime.byDay;

				// UTC time in BCD type hour
				rsGpsData.byUTCTime[3] = sTime.byHour;

				// UTC time in BCD type minute
				rsGpsData.byUTCTime[4] = sTime.byMinute;

				// UTC time in BCD type second
				rsGpsData.byUTCTime[5] = sTime.bySecond;

				// Get speed
				rsGpsData.fSpeed = pcGGAObj->GetSpeed();

				// Get angle
				rsGpsData.fAngle = pcGGAObj->GetAngle();
			}
			break;
		case NMEA_DECODE_TYPE_GSA:
			{
				// Change to GSA object
				CGSADecodeObject *pcGGAObj = static_cast<CGSADecodeObject *>(pNmeaObject);

				// Get dimension
				rsGpsData.byFixDim = pcGGAObj->GetDimension();

				// Get satellite ID
				for (INT iChannelCnt = 1; iChannelCnt <= CGSADecodeObject::SATELLITE_CHANNEL_MAX; iChannelCnt++)
				{
					INT iPRNCode = pcGGAObj->GetSatelliteInChannel(iChannelCnt);

					if (iPRNCode != 0)
					{
						rsGpsData.byUseSatelliteID[iChannelCnt] = iPRNCode;
					}
				}

				// Get PDOP
				rsGpsData.fPDOP = pcGGAObj->GetDOP().fPDOP;

				// Get HDOP
				rsGpsData.fHDOP = pcGGAObj->GetDOP().fHDOP;

				// Get VDOP
				rsGpsData.fVDOP = pcGGAObj->GetDOP().fVDOP;
			}
			break;
		case NMEA_DECODE_TYPE_GSV:
			{
				// Change to GSV object
				CGSVDecodeObject *pcGGAObj = static_cast<CGSVDecodeObject *>(pNmeaObject);

				// Get satellite information
				// Get can use buffer
				INT iSatCnt = 0;
				for (iSatCnt = 0; iSatCnt < MAX_GPS_SATELLITE; iSatCnt++)
				{
					if (rsGpsData.sSatelliteInfo[iSatCnt].bUseSatellite != 1)
					{
						break;
					}
				}

				if (iSatCnt == MAX_GPS_SATELLITE)
				{
					// ERROR
				}
				else
				{
					INT iValidInfoCnt = 0;

					// Get information
					for (INT iIndex = 0; iIndex < CGSVDecodeObject::SATELLITE_INFO_MAX; iIndex++)
					{
						CGSVDecodeObject::SGSVInfo sSatInfo = pcGGAObj->GetSatInfo(iIndex);

						// Check valid flag
						if (sSatInfo.bIsValid == TRUE)
						{
							// Fix struct
							rsGpsData.sSatelliteInfo[iSatCnt + iValidInfoCnt].bUseSatellite = 1;	// Set to VALID
							rsGpsData.sSatelliteInfo[iSatCnt + iValidInfoCnt].bySatelliteID = static_cast<BYTE>(sSatInfo.iPRGCode);
							rsGpsData.sSatelliteInfo[iSatCnt + iValidInfoCnt].fElevation = static_cast<FLOAT>(sSatInfo.iElevation);
							rsGpsData.sSatelliteInfo[iSatCnt + iValidInfoCnt].fAzimuth = static_cast<FLOAT>(sSatInfo.iAzimuth);
							rsGpsData.sSatelliteInfo[iSatCnt + iValidInfoCnt].fSNR = static_cast<FLOAT>(sSatInfo.iSNR);

							// Increase valid information count
							iValidInfoCnt++;
						}
					}
				}
			}
			break;
		default:
			break;
		}

		// Release the object
		m_pcFactory->DestroyNmeaDecodeObejct(pNmeaObject);
	}

	return TRUE;
}
