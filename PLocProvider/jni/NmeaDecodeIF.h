/*=============================================================================

    @(#)$Id: NmeaDecodeIF.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeIF decode process interface class define file

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
#ifndef CXX_NMEADECODEIF_H
#define CXX_NMEADECODEIF_H
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

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDeocdeIF class

	Interface of NMEA information decode

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeIF
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
	virtual BOOL ReceiveNmeaInfo(NmeaReceiveSource eRecSrc, const VOID* pData, DWORD dwSize) = 0;

	/**
	 * Get GPS data
	 *
	 * @param	GPS_DATA &rsGpsData	[OUT]:
	 *
	 * @return BOOL : can get a total packet or not
	 */
	virtual BOOL GetGpsData(GPS_DATA &rsGpsData) = 0;

	/**
	 * Get instance of the decode process
	 *
	 * @param  NONE
	 *
	 * @return CNmeaDecodeFactory* an abstract pointer
	 */
	static CNmeaDecodeIF* Instance(VOID);

	/**
	 * Destroy all the decode process interface instance
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
	CNmeaDecodeIF();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeIF();

};

#endif  // end of CXX_NMEADECODEIF_H
