/*=============================================================================

	@(#)$Id: NmeaDecodeStateMachine.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecodeObject class concrete

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

#ifndef CXX_NMEADECODSTATEMACHINE_H
#	include "NmeaDecodeStateMachine.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Run the state machine
BOOL CNmeaDecodeStateMachine::Run(CNmeaDecodeObject* pcNmeaObj)
{
	return m_cNmeaGpsStateMachine.Run(pcNmeaObj);
}

// Check the packet is beginning or not
BOOL CNmeaDecodeStateMachine::IsPacketBegin(VOID)
{
	return m_cNmeaGpsStateMachine.IsPacketBegin();
}

// Check the packet is ending or not
BOOL CNmeaDecodeStateMachine::IsPacketEnd(VOID)
{
	return m_cNmeaGpsStateMachine.IsPacketEnd();
}

// Clear the state to NONE
VOID CNmeaDecodeStateMachine::ClearState(VOID)
{
	// Clear
	m_cNmeaGpsStateMachine.ClearState();
}

// CNmeaDecodeStateMachine instance declare and create
CNmeaDecodeStateMachine* CNmeaDecodeStateMachine::s_pcInstance = NULL;

// Get the state machine interface instance
CNmeaDecodeStateMachine* CNmeaDecodeStateMachine::Instance(VOID)
{
	// TODO: ATTENTION
	// Need to Synchronized

	if (s_pcInstance == NULL)
	{
		s_pcInstance = new CNmeaDecodeStateMachine;
	}

	return s_pcInstance;
}

// Destroy the factory interface instance
VOID CNmeaDecodeStateMachine::Destroy(VOID)
{
	if (s_pcInstance != NULL)
	{
		// delete the instance
		delete s_pcInstance;
		s_pcInstance = NULL;
	}
}

// Constructor
CNmeaDecodeStateMachine::CNmeaDecodeStateMachine()
{
	// Initialize the GPS state machine
	m_cNmeaGpsStateMachine.Initialize();

	// Start the GPS state machine
	m_cNmeaGpsStateMachine.Start();
}

// Destructor
CNmeaDecodeStateMachine::~CNmeaDecodeStateMachine()
{
	// Stop the GPS state machine
	m_cNmeaGpsStateMachine.Stop();
}
