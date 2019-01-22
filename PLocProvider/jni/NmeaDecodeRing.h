/*=============================================================================

    @(#)$Id: NmeaDecodeRing.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeRing class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeRing class define file

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODERING_H
#define CXX_NMEADECODERING_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif
#ifndef CXX_NEWTYPEDEFINE_H
#define CXX_NEWTYPEDEFINE_H
#	include "AIL_NewTypesDefine.h"
#endif

#ifndef CXX_NPGPSENGINE_DEF_H
#	include "NPGpsEngine_Def.h"
#endif
/*===========================================================================*/
/*  included files                                                           */

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDecodeRing class

	NMEA decode ring buffer

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeRing
{
public:

	/**
	 * Constructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	CNmeaDecodeRing();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeRing();

	/**
	 * Receive NMEA information
	 *
	 * @param	const VOID* pData	[IN]: input NMEA information
	 *			DWORD dwSize		[IN]: input data size
	 *
	 * @return BOOL : Decode result
	 */
	BOOL ReceiveNmeaInfo(const VOID* pData, DWORD dwSize);

private:

	static const DWORD NMEA_DECODE_SEARCH_BUF_SIZE = 2048;	// NMEA decode search buffer size
	CHAR	m_pSearchBuffer[NMEA_DECODE_SEARCH_BUF_SIZE];	// NMEA decode search buffer
	DWORD	m_dwUsingBufSize;								// using buffer size

	static const DWORD NMEA_DECODE_SEND_BUF_SIZE = 256 + 1;	// NMEA decode send buffer size
	CHAR	m_pSendBuffer[NMEA_DECODE_SEND_BUF_SIZE];		// NMEA decode send buffer
};

#endif  // end of CXX_NMEADECODERING_H
