/*=============================================================================

	@(#)$Id: NmeaDecode.h 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecode decode process implement class define file

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeIF decode process interface class define file
	Decode NMEA recode state change

	@attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODE_H
#define CXX_NMEADECODE_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif

/*===========================================================================*/
/*  included files    
                                                       */
#ifndef CXX_NEWTYPEDEFINE_H
#	include "AIL_NewTypesDefine.h"
#endif

#ifndef CXX_NMEADECODEDEFINE_H
#   include "NmeaDecodeDefine.h"
#endif

#ifndef CXX_NMEADECODEIF_H
#	include "NmeaDecodeIF.h"
#endif

#ifndef CXX_NMEADECODERING_H
#	include "NmeaDecodeRing.h"
#endif

#ifndef CXX_NMEADECODEPROCESS_H
#	include "NmeaDecodeProcess.h"
#endif

#ifndef CXX_NPGPSENGINE_DEF_H
#	include "NPGpsEngine_Def.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDeocde class

	Implement of NMEA information decode

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecode : public CNmeaDecodeIF
{
public:

	// All interface
	/**
	 * Receive NMEA information
	 *
	 * @param	NmeaReceiveSource eRecSrc	[IN]: input NMEA receive source
	 *			const VOID* pData			[IN]: input NMEA information
	 *			DWORD dwSize				[IN]: input data size
	 *
	 * @return BOOL : Decode result
	 */
	virtual BOOL ReceiveNmeaInfo(NmeaReceiveSource eRecSrc, const VOID* pData, DWORD dwSize);

	/**
	 * Get GPS data
	 *
	 * @param	GPS_DATA &rsGpsData	[OUT]:
	 *
	 * @return BOOL : can get a total packet or not
	 */
	virtual BOOL GetGpsData(GPS_DATA &rsGpsData);

	// Function used in the module
	/**
	 * Decode NMEA recode in one line
	 *
	 * @param  CHAR* pStrData [IN]: input NMEA recode
	 *
	 * @return BOOL : Decode result
	 */
	BOOL DecodeNmeaRecode(const CHAR* pStrData);

	/**
	 * Update GPS data
	 *
	 * @param  const GPS_DATA &rsGpsData [IN]: a total packet of GPS data
	 *
	 * @return NONE
	 */
	VOID UpdateGpsData(const GPS_DATA &rsGpsData);

	/**
	 * Get signing full packet flag
	 *
	 * @param  NONE
	 *
	 * @return BOOL : Yes/No
	 */
	BOOL GetFullPacketFlag(VOID) {return m_bFullPacketFlag;}

	/**
	 * Reset the full packet flag to invalid
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	VOID ResetFullPacketFlag(VOID) {m_bFullPacketFlag = FALSE;}

	/**
	 * Get instance of the decode process
	 *
	 * @param  NONE
	 *
	 * @return CNmeaDecodeFactory* an abstract pointer
	 */
	static CNmeaDecode* Instance(VOID);

	/**
	 * Destroy all the decode process instance
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
	CNmeaDecode();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecode();

private:

	// All members
	static CNmeaDecode*	s_pcInstance;					// static pointer for singleton

	CNmeaDecodeRing		m_cRingBuffer;					// NMEA decode ring buffer
	CNmeaDecodeProcess	m_cMainProcess;					// NMEA decode main process
	NmeaReceiveSource	m_eRecSrc;						// NMEA information receive source

	BOOL				m_bFullPacketFlag;				// a flag to sign got a full packet of GPS data
};

#endif  // end of CXX_NMEADECODE_H
