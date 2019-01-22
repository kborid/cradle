/*=============================================================================

    @(#)$Id: NmeaDecodeObject.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeObject class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeObject class define file
	And all concrete class include GGA/RMC/GSA/GSV

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODEOBJECT_H
#define CXX_NMEADECODEOBJECT_H
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

/*===========================================================================*/
/*  static parameter announce                                                */

/*===========================================================================*/
/*  structure define announce                                                */

/*===========================================================================*/
/**
	CNmeaDecodeObject class

	NMEA decode process every recode object
	
	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeObject
{
public:

	/**
	 * Constructor
	 *
	 * @param  CHAR *pStrData	[IN]: NMEA recode data
	 *
	 * @return NONE
	 */
	CNmeaDecodeObject(const CHAR *pStrData);

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeObject();

	/**
	 * Get current NMEA recode type
	 *
	 * @param  NONE
	 *
	 * @return NmeaDecodeTypeDef : current NMEA recode type
	 */
	virtual NmeaDecodeTypeDef GetNmeaType(VOID) = 0;

	/**
	 * Is valid or not
	 *
	 * @param  NONE
	 *
	 * @return BOOL : result
	 */
	virtual BOOL IsValid(VOID);

protected:

	BOOL m_bIsValid;		// decode process normal

	/**
	 * Get filed total count in the record
	 *
	 * @param	CHAR *pStrData	[IN]: input NMEA recode
	 *
	 * @return	INT : the total count of fields in the record
	 */
	INT GetFieldTotalCnt(const CHAR *pStrData);

	/**
	 * Get filed recode
	 *
	 * @param	CHAR *pStrData		[IN]: input NMEA recode
	 *			INT iFieldNum		[IN]: need to get field count
	 *			CHAR* pStrField		[OUT]: output field recode 
	 *
	 * @return BOOL : can get field recode or not
	 */
	BOOL GetField(const CHAR *pStrData, INT iFieldNum, CHAR* pStrField);

	INT m_iCurFieldOffset;	// current field offset to the data pointer
	INT m_iCurFieldNum;		// current field number for save last check

protected:

private:

};

/*===========================================================================*/
/**
	CGGADecodeObject class

	$GPGGA decode process

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CGGADecodeObject : public CNmeaDecodeObject
{
public:

	/**
	 * Constructor
	 *
	 * @param  CHAR *pStrData	[IN]: NMEA recode data
	 *
	 * @return NONE
	 */
	CGGADecodeObject(const CHAR *pStrData);

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CGGADecodeObject();

	/**
	 * Get current NMEA recode type
	 *
	 * @param  NONE
	 *
	 * @return NmeaDecodeTypeDef : current NMEA recode type
	 */
	virtual NmeaDecodeTypeDef GetNmeaType(VOID);

	/**
	 * Get current longitude in the GGA record
	 *
	 * @param  NONE
	 *
	 * @return	current longitude
	 */
	DOUBLE GetLongitude(VOID);

	/**
	 * Get current latitude in the GGA record
	 *
	 * @param  NONE
	 *
	 * @return	current latitude
	 */
	DOUBLE GetLatitude(VOID);

	/**
	 * Get current altitude in the GGA record
	 *
	 * @param  NONE
	 *
	 * @return	current altitude
	 */
	FLOAT GetAltitude(VOID);

	/**
	 * Get current satellite count in GGA record
	 *
	 * @param  NONE
	 *
	 * @return	current satellite count on receive
	 */
	INT GetSatelliteCnt(VOID);

protected:

private:

	DOUBLE		m_dLongitude;		// longitude	[sec]
	DOUBLE		m_dLatitude;		// latitude		[sec]
	FLOAT		m_fAltitude;		// altitude		[m]
	INT			m_iSatellite;		// satellite number in using

};

/*===========================================================================*/
/**
	CRMCDecodeObject class

	$GPRMC decode process

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CRMCDecodeObject : public CNmeaDecodeObject
{
public:

	// Define time struct
	struct STime
	{
		WORD wYear;			// year				(20XX)	 
		BYTE byMonth;		// month			(1 ~ 12)
		BYTE byDay;			// day				(1 ~ 31)
		BYTE byHour;		// hour				(0 ~ 59)
		BYTE byMinute;		// minute			(0 ~ 59)
		BYTE bySecond;		// second			(0 ~ 59)
		BYTE Revert1;		
		WORD wMilliSec;		// millisecond		(0 ~ 999)
		WORD Revert2;
	};

public:

	/**
	 * Constructor
	 *
	 * @param  CHAR *pStrData	[IN]: NMEA recode data
	 *
	 * @return NONE
	 */
	CRMCDecodeObject(const CHAR *pStrData);

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CRMCDecodeObject();

	/**
	 * Get current NMEA recode type
	 *
	 * @param  NONE
	 *
	 * @return NmeaDecodeTypeDef : current NMEA recode type
	 */
	virtual NmeaDecodeTypeDef GetNmeaType(VOID);

	/**
	 * Get current time in RMC record
	 *
	 * @param  NONE
	 *
	 * @return	current time
	 */
	STime GetTime(VOID);

	/**
	 * Get current speed in RMC record
	 *
	 * @param  NONE
	 *
	 * @return	current speed
	 */
	FLOAT GetSpeed(VOID);

	/**
	 * Get current angle in RMC recode
	 *
	 * @param  NONE
	 *
	 * @return	current angle
	 */
	FLOAT GetAngle(VOID);

protected:

private:

	// Time information
	STime	m_sTime;			// time struct
	FLOAT	m_fSpeed;			// speed			[km/h]
	FLOAT	m_fAngle;			// direction angle	[deg] (E is 0 in clock ward)

};

/*===========================================================================*/
/**
	CGSADecodeObject class

	$GPGSA decode process

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CGSADecodeObject : public CNmeaDecodeObject
{
public:

	static const INT SATELLITE_CHANNEL_MAX = 12;	// total channel count to receive signal

	// DOP value struct
	struct SDOP
	{
		FLOAT fPDOP;		// position DOP
		FLOAT fHDOP;		// horizontal DOP 
		FLOAT fVDOP;		// vertical DOP
	};

public:

	/**
	 * Constructor
	 *
	 * @param  CHAR *pStrData	[IN]: NMEA recode data
	 *
	 * @return NONE
	 */
	CGSADecodeObject(const CHAR *pStrData);

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CGSADecodeObject();

	/**
	 * Get current NMEA recode type
	 *
	 * @param  NONE
	 *
	 * @return NmeaDecodeTypeDef : current NMEA recode type
	 */
	virtual NmeaDecodeTypeDef GetNmeaType(VOID);

	/**
	 * Get current dimension in GSA recode
	 *
	 * @param  NONE
	 *
	 * @return	current dimension
	 */
	BYTE GetDimension(VOID);

	/**
	 * Get current DOP in RMC recode
	 *
	 * @param  NONE
	 *
	 * @return	current DOP
	 */
	SDOP GetDOP(VOID);

	/**
	 * Get satellite PRN code in different channel
	 *
	 * @param  INT iChannelCnt	[IN]: channel count 1 ~ 12
	 *
	 * @return PRN code of input channel, 0 is invalid value
	 */
	INT GetSatelliteInChannel(INT iChannelCnt);

protected:

private:

	// Receive information
	BYTE	m_byDimension;							// GPS dimension
	SDOP	m_sPDOP;								// position DOP
	BYTE	m_pbySatList[SATELLITE_CHANNEL_MAX];	// receive satellite PNG code in every channel

};

/*===========================================================================*/
/**
	CGSVDecodeObject class

	$GPGSV decode process

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CGSVDecodeObject : public CNmeaDecodeObject
{
public:

	static const INT SATELLITE_INFO_MAX = 4;			// information MAX count in a GSV record

	struct SGSVInfo
	{
		BOOL	bIsValid;		// valid
		INT		iPRGCode;		// PRN code (01 ~ 32)
		INT		iElevation;		// satellite elevation (00 ~ 90) [deg]
		INT		iAzimuth;		// satellite azimuth (00 ~ 359) [deg]
		INT		iSNR;			// satellite signal strength (00 ~ 99) [dbHz]
	};

public:

	/**
	 * Constructor
	 *
	 * @param  CHAR *pStrData	[IN]: NMEA recode data
	 *
	 * @return NONE
	 */
	CGSVDecodeObject(const CHAR *pStrData);

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CGSVDecodeObject();

	/**
	 * Get current NMEA recode type
	 *
	 * @param  NONE
	 *
	 * @return NmeaDecodeTypeDef : current NMEA recode type
	 */
	virtual NmeaDecodeTypeDef GetNmeaType(VOID);

	/**
	 * Get GSV recode totally count
	 *
	 * @param  NONE
	 *
	 * @return	current satellite count on receive
	 */
	INT GetRecodeTotal(VOID);

	/**
	 * Get current recode index
	 *
	 * @param  NONE
	 *
	 * @return	current satellite count on receive
	 */
	INT GetRecodeIndex(VOID);

	/**
	 * Get satellite information in GSV record by index
	 *
	 * @param  NONE
	 *
	 * @return	satellite information
	 */
	SGSVInfo GetSatInfo(INT iIndex);

protected:

private:

	// Satellite information
	INT				m_iRecordTotal;						// GSV record total count (1 ~ 3)
	INT				m_iRecordIndex;						// GSV record index in this time (1 ~ 3)
	INT				m_iSatCnt;							// current satellite totally count (00 ~ 12)
	SGSVInfo		m_sSatInfo[SATELLITE_INFO_MAX];		// satellite information in a GSV record
	INT				m_iSatInfoCnt;						// Current satellite information count
};

#endif  // end of CXX_NMEADECODEOBJECT_H
