/*=============================================================================

	@(#)$Id: NmeaGpsStateMachine.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaGpsStateMachine class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeObject class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"

#ifndef CXX_NMEAGPSSTATEMACHINE_H
#	include "NmeaGpsStateMachine.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Constructor
CAbstractStateMachine::CAbstractStateMachine() :
	m_bIsWorking(FALSE),
	m_iCurState(0)
{

}

// Destructor
CAbstractStateMachine::~CAbstractStateMachine()
{
	m_bIsWorking = FALSE;
	m_iCurState = 0;
}

// Initialize the state machine
VOID CAbstractStateMachine::Initialize(VOID)
{
	// Initialize all members
	m_bIsWorking = FALSE;
	m_iCurState = 0;
}

// Start the state machine
VOID CAbstractStateMachine::Start(VOID)
{
	// Start the state machine
	m_bIsWorking = TRUE;
}

// Run the state machine
VOID CAbstractStateMachine::Run(VOID)
{
	// Nothing to do
}

// Stop the state machine
VOID CAbstractStateMachine::Stop(VOID)
{
	// Stop the state machine
	m_bIsWorking = FALSE;
}

// Stop the state machine
BOOL CAbstractStateMachine::IsWorking(VOID)
{
	return m_bIsWorking;
}

// Switch state to next
BOOL CAbstractStateMachine::SwitchToNextState(INT iNextState)
{
	// Check state machine working or not
	if (m_bIsWorking == TRUE)
	{
		// Switch to next state
		m_iCurState = iNextState;
		return TRUE;
	}
	else
	{
		// State machine is not working
		return FALSE;
	}
}

// Get state machine current state
INT CAbstractStateMachine::GetCurrentState(VOID)
{
	return m_iCurState;
}

/*===========================================================================*/
// Run the state machine
BOOL CNmeaGpsStateMachine::Run(CNmeaDecodeObject* pcNmeaObj)
{
	BOOL bRunRet = TRUE;

	switch (pcNmeaObj->GetNmeaType())
	{
	case NMEA_DECODE_TYPE_GGA:
		{
			// Get satellite count from GGA record
			m_iSatelliteCnt = static_cast<CGGADecodeObject *>(pcNmeaObj)->GetSatelliteCnt();

			// Switch to GGA state
			SwitchToNextState(GPS_MACHINE_STATE_GGA);
		}
		break;
	case NMEA_DECODE_TYPE_RMC:
		{
			switch (GetCurrentState())
			{
			case GPS_MACHINE_STATE_GGA:
				SwitchToNextState(GPS_MACHINE_STATE_GGA_RMC);
				break;
			case GPS_MACHINE_STATE_GGA_GSA:
				SwitchToNextState(GPS_MACHINE_STATE_GGA_RMC_GSA);
				break;
			default:
				// Other state ERROR
				SwitchToNextState(GPS_MACHINE_STATE_NONE);
				bRunRet = FALSE;
				break;
			}
		}
		break;
	case NMEA_DECODE_TYPE_GSA:
		{
			// Get satellite count from GSA record
			INT iSatCnt = 0;
			for (INT iChannelCnt = 1; iChannelCnt <= CGSADecodeObject::SATELLITE_CHANNEL_MAX; iChannelCnt++)
			{
				INT iPRNCode = static_cast<CGSADecodeObject *>(pcNmeaObj)->GetSatelliteInChannel(iChannelCnt);

				if (iPRNCode != 0)
				{
					iSatCnt++;
				}
			}

			// Check satellite count between GGA and GSV
			if (iSatCnt != m_iSatelliteCnt)
			{
				// ERROR
				SwitchToNextState(GPS_MACHINE_STATE_NONE);
				bRunRet = FALSE;
				break;
			}

			// Switch to next state
			switch (GetCurrentState())
			{
			case GPS_MACHINE_STATE_GGA:
				SwitchToNextState(GPS_MACHINE_STATE_GGA_GSA);
				break;
			case GPS_MACHINE_STATE_GGA_RMC:
				SwitchToNextState(GPS_MACHINE_STATE_GGA_RMC_GSA);
				break;
			default:
				// Other state ERROR
				SwitchToNextState(GPS_MACHINE_STATE_NONE);
				bRunRet = FALSE;
				break;
			}
		}
		break;
	case NMEA_DECODE_TYPE_GSV:
		{
			// Get object pointer
			CGSVDecodeObject* pGSVObj = static_cast<CGSVDecodeObject *>(pcNmeaObj);

			// First get GSV reset the total count
			if (m_iGSVAlreadyGet == 0)
			{
				m_iGSVTotalCnt = pGSVObj->GetRecodeTotal();
			}
			else
			{
				if (m_iGSVTotalCnt != pGSVObj->GetRecodeTotal())
				{
					// ERROR
					SwitchToNextState(GPS_MACHINE_STATE_NONE);
					bRunRet = FALSE;
					break;
				}
			}

			m_iGSVAlreadyGet = pGSVObj->GetRecodeIndex();
		}
		break;
	default:
		// Nothing to do
		break;
	}

	return bRunRet;
}

// Check the packet is beginning or not
BOOL CNmeaGpsStateMachine::IsPacketBegin(VOID)
{
	// Check state and receive satellite information count
	// Packet begin with GGA and not GSV or GSV
	if ((GetCurrentState() == GPS_MACHINE_STATE_GGA && m_iGSVAlreadyGet == 0)
		|| (GetCurrentState() == GPS_MACHINE_STATE_NONE && m_iGSVAlreadyGet == 1))
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}

// Check the packet is ending or not
BOOL CNmeaGpsStateMachine::IsPacketEnd(VOID)
{
	// Check state and receive satellite information count
	if (GetCurrentState() == GPS_MACHINE_STATE_GGA_RMC_GSA
		&& m_iGSVTotalCnt == m_iGSVAlreadyGet)
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}

// Clear the state to NONE
VOID CNmeaGpsStateMachine::ClearState(VOID)
{
	// Clear
	SwitchToNextState(GPS_MACHINE_STATE_NONE);

	// Reset GSV already get count
	m_iGSVAlreadyGet = 0;
}

// Constructor
CNmeaGpsStateMachine::CNmeaGpsStateMachine() :
	CAbstractStateMachine(),
	m_iSatelliteCnt(0),
	m_iGSVTotalCnt(0),
	m_iGSVAlreadyGet(0)
{
	
}

// Destructor
CNmeaGpsStateMachine::~CNmeaGpsStateMachine()
{
	
}