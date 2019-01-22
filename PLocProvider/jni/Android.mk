LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jniNmeaDecoder
LOCAL_LDLIBS := -llog
LOCAL_SRC_FILES := AIL_NewTypesDefine.h NmeaDecode.h NmeaDecodeFactory.cpp NmeaDecodeFactory.h NmeaDecodeIF.cpp NmeaDecodeIF.h NmeaDecodeObject.h  NmeaDecodeObject.cpp NmeaDecodeObjectList.cpp NmeaDecodeObjectList.h NmeaDecodeProcess.cpp NmeaDecodeProcess.h NmeaDecodeRing.cpp NmeaDecodeRing.h  NmeaDecode.cpp NmeaDecodeStateMachine.cpp NmeaDecodeStateMachine.h NmeaGpsStateMachine.cpp NmeaGpsStateMachine.h NmeaDecodeDefine.h NPGpsEngine_Def.h AIL_SyncObj.h AIL_SyncObj.cpp jniNmeaDecoderIF.h jniNmeaDecoderIF.cpp 

include $(BUILD_SHARED_LIBRARY)
