/*=============================================================================

    @(#)$Id: NmeaDecodeProcess.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeProcess class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeProcess class define file
	NMEA recode decode process

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODEPROCESS_H
#define CXX_NMEADECODEPROCESS_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif
#ifndef CXX_NEWTYPEDEFINE_H
#	include "AIL_NewTypesDefine.h"
#endif
#ifndef CXX_NPGPSENGINE_DEF_H
#	include "NPGpsEngine_Def.h"
#endif
/*===========================================================================*/
/*  included files                                                           */

#ifndef CXX_NMEADECODEDEFINE_H
#   include "NmeaDecodeDefine.h"
#endif

#ifndef CXX_NMEADECODEFACTORY_H
#	include "NmeaDecodeFactory.h"
#endif

#ifndef CXX_NMEADECODEOBJECT_H
#	include "NmeaDecodeObject.h"
#endif

#ifndef CXX_NMEADECODEOBJECTLIST_H
#	include "NmeaDecodeObjectList.h"
#endif

#ifndef CXX_NMEADECODSTATEMACHINE_H
#	include "NmeaDecodeStateMachine.h"
#endif

#ifndef CXX_AIL_SYNCOBJ_H
#	include "AIL_SyncObj.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDecodeProcess class

	NMEA recode decode process
	
	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
// Class declaration
class AIL_SyncObj;

class CNmeaDecodeProcess
{
public:

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CNmeaDecodeProcess();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeProcess();

	/**
	 * Decode NMEA recode in one line
	 *
	 * @param  CHAR* pStrData [IN]: input NMEA recode
	 *
	 * @return BOOL : Decode result
	 */
	BOOL DecodeNmeaRecode(const CHAR* pStrData);

	/**
	 * Decode NMEA recode in one line
	 *
	 * @param  GPS_DATA &rsGpsData [OUT]: GPS data
	 *
	 * @return BOOL : can get GPS data of a complete packet
	 */
	BOOL GetGpsData(GPS_DATA &rsGpsData);
	
private:

	CNmeaDecodeFactory*			m_pcFactory;		// pointer to the NMEA recode factory
	CNmeaDecodeStateMachine*	m_pcStatMachine;	// pointer to the state machine

	CNmeaDecodeObjectList		m_cObjectList;		// NMEA recode object list to save

	// Data output
	AIL_SyncObj					m_cDataSync;		// synchronize object for get data
	BOOL						m_bIsCompletePakct;	// Current is a complete packet or not
	GPS_DATA					m_sGpsData;			// Current packet GPS data

	// Debug
	DWORD						m_dwCounter;		// just for debug counter

	/**
	 * Check sum of the data in NMEA recode in one line
	 *
	 * @param  CHAR* pStrData [IN]: input NMEA recode
	 *
	 * @return BOOL : check sum result 
	 */
	BOOL CheckSumNmeaRecode(const CHAR* pStrData);

	/**
	 * Install the GPS data by object
	 *
	 * @param  GPS_DATA &rsGpsData [OUT]: the installation result
	 *
	 * @return BOOL : install process successful or not
	 */
	BOOL InstallGpsData(GPS_DATA &rsGpsData);

};

#endif  // end of CXX_NMEADECODEPROCESS_H
