/*=============================================================================

    @(#)$Id: NmeaGpsStateMachine.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CAbstractStateMachine/CNmeaGpsStateMachine class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CAbstractStateMachine/CNmeaGpsStateMachine class define file
	Decode NMEA recode state change

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEAGPSSTATEMACHINE_H
#define CXX_NMEAGPSSTATEMACHINE_H
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
	CAbstractStateMachine class

	An abstract state machine

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CAbstractStateMachine
{
public:

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CAbstractStateMachine();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CAbstractStateMachine();

	/**
	 * Initialize the state machine
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual VOID Initialize(VOID);

	/**
	 * Start the state machine
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual VOID Start(VOID);

	/**
	 * Run the state machine
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual VOID Run(VOID);

	/**
	 * Stop the state machine
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual VOID Stop(VOID);

	/**
	 * Stop the state machine
	 *
	 * @param  NONE
	 *
	 * @return BOOL : the state machine is on working or not
	 */
	virtual BOOL IsWorking(VOID);

	/**
	 * Switch state to next
	 *
	 * @param  INT iNextState [IN]: next state
	 *
	 * @return BOOL : switch successful or not
	 */
	virtual BOOL SwitchToNextState(INT iNextState);

	/**
	 * Get state machine current state
	 *
	 * @param  NONE
	 *
	 * @return INT : current state
	 */
	virtual INT GetCurrentState(VOID);

protected:

private:
	
	// Members
	BOOL	m_bIsWorking;			// sign state machine is on working
	INT		m_iCurState;			// state of the machine
};

/*===========================================================================*/
/**
	CNmeaGpsStateMachine class

	An NMEA recode GPS signal state machine

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaGpsStateMachine : public CAbstractStateMachine
{
public:

	/**
	 * Run the state machine
	 *
	 * @param  CNmeaDecodeObject* pcNmeaObj [IN]: current get NMEA recode
	 *
	 * @return running right or not
	 */
	BOOL Run(CNmeaDecodeObject* pcNmeaObj);

	/**
	 * Check the packet is beginning or not
	 *
	 * @param  NONE
	 *
	 * @return check result
	 */
	BOOL IsPacketBegin(VOID);

	/**
	 * Check the packet is ending or not
	 *
	 * @param  NONE
	 *
	 * @return check result
	 */
	BOOL IsPacketEnd(VOID);

	/**
	 * Clear the state to NONE
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	VOID ClearState(VOID);

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CNmeaGpsStateMachine();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaGpsStateMachine();

protected:

private:

	// GPS state machine all state
	enum GpsMachineState
	{
		GPS_MACHINE_STATE_NONE = 0,
		GPS_MACHINE_STATE_GGA,				// Get a GGA recode 
		GPS_MACHINE_STATE_RMC,				// Get a RMC recode
		GPS_MACHINE_STATE_GSA,				// Get a GSA recode
		GPS_MACHINE_STATE_GSV,				// Get a GSV recode
		GPS_MACHINE_STATE_GGA_RMC,			// Get GGA and RMC recode
		GPS_MACHINE_STATE_GGA_GSA,			// Get GGA and GSA recode
		GPS_MACHINE_STATE_RMC_GSA,			// Get RMC and GSA recode
		GPS_MACHINE_STATE_GGA_RMC_GSA,		// Get GGA, RMC and GSA recode
	};

	INT		m_iSatelliteCnt;				// current satellite count
	INT		m_iGSVTotalCnt;					// GSV recode totally count
	INT		m_iGSVAlreadyGet;				// GSV already get count
};

#endif  // end of CXX_NMEAGPSSTATEMACHINE_H
