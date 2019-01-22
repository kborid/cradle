/*=============================================================================

    @(#)$Id: NmeaDecodeDefine.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) NMEA decode process public define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    NMEA decode process public define

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODEDEFINE_H
#define CXX_NMEADECODEDEFINE_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif

/*===========================================================================*/
/*  included files                                                           */
/*
#ifndef CXX_NPBASELIB_H
#   include "NPBase.h"
#endif

#ifndef CXX_GPSSNSMODULE_H
#	include "NPGpsEngine_Def.h"
#endif

#ifndef CXX_NSYNCOBJECT_H
#	include "NSyncObject.h"
#endif

*/
/*===========================================================================*/
/*  static parameter announce                                                */

// Define NMEA receive source
enum NmeaReceiveSource
{
	NMEA_RECEIVE_SOURCE_NONE = 0,
	NMEA_RECEIVE_SOURCE_ANDROID,
	NMEA_RECEIVE_SOURCE_IPHONE,
	NMEA_RECEIVE_SOURCE_CRADLE,
	NMEA_RECEIVE_SOURCE_OTHER,
};

// Define NMEA recode type
enum NmeaDecodeTypeDef
{
	NMEA_DECODE_TYPE_NONE = 0,
	NMEA_DECODE_TYPE_GGA,
	NMEA_DECODE_TYPE_RMC,
	NMEA_DECODE_TYPE_GSA,
	NMEA_DECODE_TYPE_GSV,
};

/*===========================================================================*/
/*  structure define announce                                                */

#endif  // end of CXX_NMEADECODEDEFINE_H
