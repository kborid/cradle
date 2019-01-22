/*=============================================================================

    @(#)$Id: NmeaDecodeStateMachine.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeStateMachine class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeStateMachine class define file
	Decode NMEA recode state change

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODSTATEMACHINE_H
#define CXX_NMEADECODSTATEMACHINE_H
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

#ifndef CXX_NMEAGPSSTATEMACHINE_H
#	include "NmeaGpsStateMachine.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDecodeStateMachine class

	NMEA decode process state machine implement class

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeStateMachine
{
public:

	// ALL interface
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
	 * Get the factory instance
	 *
	 * @param  NONE
	 *
	 * @return CNmeaDecodeFactory* an abstract pointer
	 */
	static CNmeaDecodeStateMachine* Instance(VOID);

	/**
	 * Destroy the factory instance
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	static VOID Destroy(VOID);

protected:

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CNmeaDecodeStateMachine();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeStateMachine();

private:

	static CNmeaDecodeStateMachine* s_pcInstance;	// static member to recode instance pointer
	CNmeaGpsStateMachine m_cNmeaGpsStateMachine;	// NMEA recode GPS signal state machine

};

#endif  // end of CXX_NMEADECODSTATEMACHINE_H
