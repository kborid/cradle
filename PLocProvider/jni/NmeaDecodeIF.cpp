/*=============================================================================

	@(#)$Id: NmeaDecodeIF.cpp 25636 2011-05-04 09:07:25Z guoxianing $
	@(#) CNmeaDecodeIF class concrete

	(c)  PIONEER CORPORATION  2011
	PSET Shanghai China
	All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
	CNmeaDecodeIF interface class concrete file

	@attention only for C++
*/
/*===========================================================================*/

/*===========================================================================*/
/*  included files                                                           */

//#include "stdafx.h"

#ifndef CXX_NMEADECODEIF_H
#	include "NmeaDecodeIF.h"
#endif

#ifndef CXX_NMEADECODE_H
#	include "NmeaDecode.h"
#endif

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  static parameter set                                                     */

/*===========================================================================*/
// Get instance of the decode process
CNmeaDecodeIF* CNmeaDecodeIF::Instance(VOID)
{
	return CNmeaDecode::Instance();
}

/*===========================================================================*/
// Destroy all the decode process interface instance
VOID CNmeaDecodeIF::Destroy(VOID)
{
	CNmeaDecode::Destroy();
}

/*===========================================================================*/
// Constructor
CNmeaDecodeIF::CNmeaDecodeIF()
{

}

/*===========================================================================*/
// Destructor
CNmeaDecodeIF::~CNmeaDecodeIF()
{

}
