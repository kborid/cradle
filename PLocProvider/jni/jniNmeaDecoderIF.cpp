# include "jniNmeaDecoderIF.h"

#include <android/log.h>
#ifndef CXX_NMEADECODEIF_H
#	include "NmeaDecodeIF.h"
#endif

JNIEXPORT jboolean JNICALL Java_com_pioneer_cradle_PLocProvider_jni_jniNmeaDecoderIF_SendCradleData
  (JNIEnv * env, jobject, jbyteArray data, jint length)
  {
  	jbyte* pjbyData = new jbyte[length];

	if (pjbyData == NULL)
	{
		return 0;
	}

	// get buffer from java
	env->GetByteArrayRegion(data, 0, length, pjbyData);

	if (CNmeaDecodeIF::Instance()->ReceiveNmeaInfo(NMEA_RECEIVE_SOURCE_ANDROID, pjbyData, length) == TRUE)
	{
		return 1;
	}
	else
	{
		return 0;
	}
  }

	JNIEXPORT void JNICALL Java_com_pioneer_cradle_PLocProvider_jni_jniNmeaDecoderIF_getLocationInfo
  (JNIEnv * env, jobject obj, jobject gpsdata)
  {
		// get buffer from java
	// get cradle information
	GPS_DATA data;
	if (CNmeaDecodeIF::Instance()->GetGpsData(data) == NP_FALSE)
	{
		// Fault return size 0
		return;
	}

	// Successful
	jclass clazz = env->GetObjectClass(gpsdata);
	jmethodID method_setLocation = env->GetMethodID(clazz, "setLocation", "(JJFFFI[I)V");

	jintArray timeArray = env->NewIntArray(6);

	jint time[6];
	for(int i=0; i<6; i++) {
		time[i] = (jint)data.byUTCTime[i];
	}
	env->SetIntArrayRegion(timeArray, 0, 6, time);

	env->CallVoidMethod(gpsdata, method_setLocation,
	            (jlong)data.lLat, (jlong)data.lLon,
	            (jfloat)data.fAltitude,
	            (jfloat)data.fSpeed, (jfloat)data.fAngle,
	            data.byFixDim, timeArray);

  }

	JNIEXPORT jobjectArray JNICALL Java_com_pioneer_cradle_PLocProvider_jni_jniNmeaDecoderIF_getSatelliteInfo
  (JNIEnv * env, jobject obj)
  {
		GPS_DATA data;
		if (CNmeaDecodeIF::Instance()->GetGpsData(data) == NP_FALSE)
		{
			// Fault return size 0
			return NULL;
		}


		jclass satelliteclass = env->FindClass("com/pioneer/PLocProviderKit/util/SatelliteData");

		jmethodID constructorID = env->GetMethodID(satelliteclass,"<init>","(I)V");
		jmethodID method_setInfo = env->GetMethodID(satelliteclass, "setInfo", "(ZFFF)V");


		INT sateliteCount = 0;
		for(int i=0; i<MAX_GPS_SATELLITE; i++){
			sGpsSatelliteData satelliteData =  data.sSatelliteInfo[i];
			if(satelliteData.bUseSatellite != 0){
				sateliteCount++;
			}
		}

		jobjectArray result = env->NewObjectArray(sateliteCount, satelliteclass, NULL);
		int index = 0;

		for(int i=0; i<MAX_GPS_SATELLITE; i++){
			sGpsSatelliteData satelliteData =  data.sSatelliteInfo[i];
			if(satelliteData.bUseSatellite != 0){
				jobject obj = env->NewObject(satelliteclass,constructorID, satelliteData.bySatelliteID);
				env->CallVoidMethod(obj, method_setInfo,
											(jboolean)satelliteData.bUseSatellite,
											(jfloat)satelliteData.fElevation,
											(jfloat)satelliteData.fAzimuth,
											(jfloat)satelliteData.fSNR
											);
				env->SetObjectArrayElement(result, index, obj);
				index ++ ;
			}
		}

		return result;

  }



