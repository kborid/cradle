/*=============================================================================

    @(#)$Id: NmeaDecodeFactory.h 25636 2011-05-04 09:07:25Z guoxianing $
    @(#) CNmeaDecodeFactory/NmeaDecodeConcreteFactory class define

    (c)  PIONEER CORPORATION  2011
    PSET Shanghai China
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    CNmeaDecodeFactory/NmeaDecodeConcreteFactory class define file

    @attention only for C++
*/
/*===========================================================================*/
#ifndef CXX_NMEADECODEFACTORY_H
#define CXX_NMEADECODEFACTORY_H
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
	CNmeaDecodeFactory class

	NMEA decode process factory abstraction

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeFactory
{
public:

	// ALL interface
	/**
	 * Create the NMEA recode object
	 *
	 * @param  CHAR *pchRecode	[IN]: the NMEA recode in one line
	 *
	 * @return CNmeaDecodeObject* : new pointer to a NMEA decode object
	 */
	virtual CNmeaDecodeObject* CreateNmeaDecodeObejct(const CHAR *pStrData) = 0;

	/**
	 * Destroy the NMEA recode object
	 *
	 * @param  CNmeaDecodeObject* pcNmeaDecodeObject [IN]: need to destroy object
	 *
	 * @return NONE
	 */
	virtual VOID DestroyNmeaDecodeObejct(CNmeaDecodeObject* pcNmeaDecodeObject) = 0;

	/**
	 * Get instance of the factory abstraction
	 *
	 * @param  NONE
	 *
	 * @return CNmeaDecodeFactory* an abstract pointer
	 */
	static CNmeaDecodeFactory* Instance(VOID);

	/**
	 * Destroy the factory interface instance
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
	CNmeaDecodeFactory();

	/**
	* Destructor
	*
	* @param  NONE
	*
	* @return NONE
	*/
	virtual ~CNmeaDecodeFactory();

	INT m_iObjectCount;		// object counter

private:

};

/*===========================================================================*/
/**
	CNmeaDecodeConcreteFactory class

	NMEA decode process concrete factory

	@author $Author: guoxianing $
	@version $Revision: 25636 $
	@date $Date:: 2011-05-04 17:07:25 #$
*/
/*===========================================================================*/
class CNmeaDecodeConcreteFactory : public CNmeaDecodeFactory
{
public:

	/**
	 * Create the NMEA recode object
	 *
	 * @param  CHAR *pchRecode	[IN]: the NMEA recode in one line
	 *
	 * @return CNmeaDecodeObject* : new pointer to a NMEA decode object
	 */
	virtual CNmeaDecodeObject* CreateNmeaDecodeObejct(const CHAR *pStrData);

	/**
	 * Destroy the NMEA recode object
	 *
	 * @param  CNmeaDecodeObject* pcNmeaDecodeObject [IN]: need to destroy object
	 *
	 * @return NONE
	 */
	virtual VOID DestroyNmeaDecodeObejct(CNmeaDecodeObject* pcNmeaDecodeObject);

	/**
	 * Get the factory instance
	 *
	 * @param  NONE
	 *
	 * @return CNmeaDecodeConcreteFactory* the pointer
	 */
	static CNmeaDecodeConcreteFactory* Instance(VOID);

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
	CNmeaDecodeConcreteFactory();

	/**
	 * Destructor
	 *
	 * @param  NONE
	 *
	 * @return NONE
	 */
	virtual ~CNmeaDecodeConcreteFactory();

private:

	// Static member to recode instance pointer
	static CNmeaDecodeConcreteFactory* s_pcInstance;

};

#endif  // end of CXX_NMEADECODEFACTORY_H
