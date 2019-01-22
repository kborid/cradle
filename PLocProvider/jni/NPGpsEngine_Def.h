/*=============================================================================

    @(#)$Id: NPGpsEngine_Def.h 3438 2009-03-19 10:51:58Z ooue $
    @(#) GPSEngine定義ファイル

    (c)  PIONEER CORPORATION  2008
    25-1 Nishi-machi Yamada Kawagoe-shi Saitama-ken 350-8555 Japan
    All Rights Reserved.

=============================================================================*/

/*===========================================================================*/
/** @file
    GPSEngine定義ファイル

    @attention C++ 専用です C では使用出来ません
*/
/*===========================================================================*/
#ifndef CXX_NPGPSENGINE_DEF_H
#define CXX_NPGPSENGINE_DEF_H
#ifndef __cplusplus
#   error ERROR: This file requires C++ compilation (use a .cpp suffix)
#endif

/*=============================================================================
   インクルードファイルの読み込み                                            */

/*=============================================================================
    定数宣言                                                                 */

// エラーコード（0x20003200-0x200032FF）
const NP_ERROR  NP_ERROR_GPSENGINE_SYNC_CREATE		=   0x20003200; // GPS同期オブジェクト生成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_MEMORY_OPEN		=   0x20003201; // GPS共有メモリーオープン失敗
const NP_ERROR  NP_ERROR_GPSENGINE_MEMORY_SYNC		=   0x20003202; // GPS共有メモリー同期オブジェクト失敗
const NP_ERROR  NP_ERROR_GPSENGINE_SNSMSG_CREATE	=   0x20003203; // SNSデータ送信オブジェクト作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPSEVENT_CREATE	=   0x20003204; // GPS取得イベント作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_SNSEVENT_CREATE	=   0x20003205; // SNS取得イベント作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPSTHEARD_CREATE	=   0x20003206; // GPSスレッド作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_SNSTHEARD_CREATE	=   0x20003207; // SNSスレッド作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPS_SEND		=   0x20003208; // GPS情報送信失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPS_FAIL_DATA	=   0x20003209; // GPS送信情報異常
const NP_ERROR  NP_ERROR_GPSENGINE_SEND_TIMEOUT		=   0x2000320a; // GPS情報送信失敗(TIMEOUT)
const NP_ERROR  NP_ERROR_GPSENGINE_SNS_SEND		=   0x2000320b; // SNS情報送信失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPS_INITIALIZE	=   0x2000320c; // GPSスレッドイニシャライズエラー
const NP_ERROR  NP_ERROR_GPSENGINE_SNS_INITIALIZE	=   0x2000320d; // SNSスレッドイニシャライズエラー
const NP_ERROR  NP_ERROR_GPSENGINE_LOGTHEARD_CREATE	=   0x2000320e; // LOGスレッド作成失敗
const NP_ERROR  NP_ERROR_GPSENGINE_LOG_INITIALIZE	=   0x2000320f; // LOGスレッドイニシャライズエラー
const NP_ERROR  NP_ERROR_GPSENGINE_WRITE_LOG		=   0x20003210; // LOG書込み失敗
const NP_ERROR  NP_ERROR_GPSENGINE_GPS_SEND_MEMORY	=   0x20003211; // GPS共有メモリ取得失敗

const NP_ERROR  NP_ERROR_GPSENGINE_GPSAPI		=   0x20003250; // GPSAPI設定エラー
const NP_ERROR  NP_ERROR_GPSENGINE_NMEATYPE		=   0x20003251; // NMEA設定エラー
const NP_ERROR  NP_ERROR_GPSENGINE_COMOPEN		=   0x20003252; // COMオープンエラー
const NP_ERROR  NP_ERROR_GPSENGINE_PORT_SET		=   0x20003253; // ポート設定エラー
const NP_ERROR  NP_ERROR_GPSENGINE_EVENT_OPEN		=   0x20003254; // イベントオープンエラー

// 測地変換用定義
#ifndef PI
#define		PI		3.141592653589793
#endif
#define		D2R		(PI/180.0)
#define		R2D		(180.0/PI)

#define		A_TKY		6377397.155		// ベッセル楕円体長半径[m]
#define		F_TKY		(1.0/299.152813)	// ベッセル楕円体扁平度
#define		A_WGS84		6378137.0
#define		E2_WGS84	6.69437999013e-3
#define		F_WGS84		(1.0/298.257223563)
#define		ES_WGS84	(2.0 * F_WGS84 - F_WGS84 * F_WGS84)
#define		ADB_WGS84       (1.0 / (1.0 - F_WGS84)) // "a divided by b"

#define		DX_TKY		146.43
#define		DY_TKY		-507.89
#define		DZ_TKY		-681.46

/*** センサドライバデータ用マクロ定義値 ***/
#define V_BUFF_SIZE	3	/* Voltage Buffer Size */
#define P_BUFF_SIZE	5	/* Pulse Buffer Size */
#define SDB_SIZE	4	/* Senser Dirver Block Data Size */
#define YAW_GYRO_V	0	/* Yaw Gyro Voltage Access Pointer */
#define PITCH_GYRO_V	1	/* Pitch Gyro Voltage Access Pointer */
#define ROLL_GYRO_V	2	/* Roll Gyro Voltage Access Pointer */
#define X_G_SNS_V	0	/* X-axis G Sensor Voltage Access Pointer */
#define Y_G_SNS_V	1	/* Y-axis G Sensor Voltage Access Pointer */
#define Z_G_SNS_V	2	/* Z-axis G Sensor Voltage Access Pointer */

// 測地系
const	BYTE WGS84_DATUM 	= 47;	// 47:LocalPrmTable[47] WGS84
const	BYTE TOKYO_DATUM 	= 40;	// 40:LocalPrmTable[40] Tokyo Datum

// LOG種別定義
const	BYTE GPS_NORMAL_LOG	= 0;	// 通常ログ
const	BYTE GPS_EXP_LOG	= 1;	// 拡張ログ

// LOG状態定義
const	BYTE GPS_LOG_CONDITION_STOP  = 0;// ログ未実行状態
const	BYTE GPS_LOG_CONDITION_RUN   = 1;// ログ実行状態
const	BYTE GPS_LOG_CONDITION_ERROR = 2;// ログ実行エラー状態
const	BYTE GPS_LOG_CONDITION_FULL  = 3;// ログ保存先容量オーバ

// SDドライブマウント状態定義
const	BYTE SD_MOUNT_NG	= 0;	// SDドライブマウントNG
const	BYTE SD_MOUNT_OK	= 1;	// SDドライブマウントOK

// VersionNumberサイズ定義
const	BYTE VER_NUM_SIZE	= 32;	// VersionNumberサイズ

// GPSデータ用マクロ定義値
const	BYTE GPS_SATELLITE	= 20;	// 測位使用衛星最大数
const	BYTE MAX_GPS_SATELLITE	= 32;	// GPS衛星最大数

#pragma pack(4)							// alignment by 4 byte begin

// 時刻
typedef struct  {
	INT	iYear;		// 年
	INT	iMon;		// 月
	INT	iDay;		// 日
} sGpsTime;

// GPS衛星情報
typedef struct{
	BYTE			bUseSatellite;		// [GSV] 測位使用衛星フラグ(0:未使用,1:使用)
	BYTE			bySatelliteID;		// [GSV] 衛星番号
	// padding 2 bytes here
	FLOAT			fElevation;			// [GSV] 衛星仰角(deg)
	FLOAT			fAzimuth;			// [GSV] 衛星方位(deg)
	FLOAT			fSNR;				// [GSV] 信号強度
} sGpsSatelliteData;

typedef struct{
	BYTE			byUTCTime[6];							// [RMC] 年月日時分秒（年は下２桁、BCD形式）
	// padding 2 bytes here
	LONG			lLat;									// [GGA] 緯度(1/256秒)
	LONG			lLon;									// [GGA] 経度(1/256秒)
	FLOAT			fSpeed;									// [RMC] 速度(km/h)
	FLOAT			fAngle;									// [RMC] 方位(deg)
	BYTE			byFixDim;								// [GSA] 測位次元(0:測位不可,2:2次元測位,3:3次元測位)
	// padding 3 bytes here
	BYTE			byUseSatelliteID[GPS_SATELLITE];		// [GSA] 測位使用衛星番号
	FLOAT			fPDOP;									// [GSA] PDOP
	FLOAT			fHDOP;									// [GSA] HDOP
	FLOAT			fVDOP;									// [GSA] VDOP
	BYTE			bySatellite;							// [GGA] 測位使用衛星数
	// padding 3 bytes here
	FLOAT			fAltitude;								// [GGA] 高度(m)
	LONG			lLaxis;									// [PEXGP] 誤差楕円長軸半径(m)
	LONG			lSaxis;									// [PEXGP] 誤差楕円短軸半径(m)
	FLOAT			fLaxisAngle;							// [PEXGP] 誤差楕円長軸傾き(deg)
	LONG			lFixTime;								// [PEXGP] GPS測位時間（測位データ取得時間）
	BYTE			byMaxUra;								// [PEXGP] ユーザ距離精度の最大値（SAフラグの代用）
	BYTE			byMultiPathLevel;						// [PEXGP] マルチパスフラグ
	// padding 2 bytes here
	FLOAT			fClimb;									// [PEXGP] 垂直速度[m/s]
	sGpsSatelliteData	sSatelliteInfo[MAX_GPS_SATELLITE];	// [GSV] 衛星情報
	BYTE			bGpsFlag;								// ハンドラ受信確認フラグ
	// padding 3 bytes here
} GPS_DATA;

/* センサドライバデータ 50ms単位のブロックデータ */
typedef struct  {
	FLOAT	GyroVoltage[V_BUFF_SIZE];	/* Gyro Voltage [V] */
	FLOAT	GSnsVoltage[V_BUFF_SIZE];	/* G Sensor Voltage [V] */
	FLOAT	Temperature;			/* temperature [℃] */
	SHORT	SpPulseCnt;			/* Speed Pulse counts */
	BYTE	SpPulseCntSt;			/* Speed Pulse Status */
	DWORD	SpPeriod[P_BUFF_SIZE];		/* Length of Speed Pulse */
	BYTE	SpPeriodSt[P_BUFF_SIZE];	/* Length of Speed Pulse Status */
	BYTE	ReverseSense;			/* Reverse Signal */
} sSnsDriverBlock;

/* センサドライバデータ 構造体 */
typedef struct  {
	BYTE		TransmittedCnt;		/* transmitted Counter */
	sSnsDriverBlock	SDB[SDB_SIZE];		/* 50ms単位のDRデータ */
	FLOAT		BackupVoltage;		/* Backup Voltage[V] */
	DWORD		MsecTime;		/* Time [msec] */
	BYTE		SelfDiagData;		/* Self-diagnosis data */
	BOOL		DataCondition;		/* データの状態を示すステータス */
    						/* 本ステータスが、FALSEを示す場合にはDRセンテンスの利用不可 */
} sSnsDriverData;

const BYTE  SNS_STATUS_BIT_GYRO     =   0x01;
const BYTE  SNS_STATUS_BIT_GSNS     =   0x02;
const BYTE  SNS_STATUS_BIT_TSNS     =   0x04;
const BYTE  SNS_STATUS_BIT_PSNS     =   0x08;
const BYTE  SNS_STATUS_BIT_PULSE    =   0x10;
const BYTE  SNS_STATUS_BIT_BACK     =   0x20;

enum SNS_DATA_TYPE {
    SNS_DATA_GYRO = 0,
    SNS_DATA_GSNS,

    SNS_DATA_NUM
};

// SNS_DATA for new
typedef struct {
    FLOAT       m_fGyroData[3];             // Data from gyroscope
    FLOAT       m_fGsnsData[3];             // Data from Gsensor
    LONGLONG    m_llTimeStamp[SNS_DATA_NUM];// data time stamp
    FLOAT       m_fPeriod[SNS_DATA_NUM];    // sensor data period
    FLOAT       m_fTemperature;             // Data from temperature sensor
    FLOAT       m_fPressure;                // Data from pressure sensor
    WORD        m_wPulseCount;              // Data from pulse line
    BYTE        m_byReverseSig;             // Signal from reverse line
    BYTE        m_bySnsValidStatus;         // sensor valid status
} SNS_DATA_BLOCK;

const WORD SNS_BLOCK_SIZE = 2;

// SNS_DATA for new
typedef struct {
    BYTE        m_byTransmittedCnt;        // Sensor data transmitted counter
    BYTE        m_byReserve[3];             // reserve
    DWORD       m_dwFixTime;               // Sensor data fix time
    SNS_DATA_BLOCK  m_sDataBlock[SNS_BLOCK_SIZE];   // sensor data block
} SNS_DATA;

// GPS raw data struct
struct GPS_RAW_DATA {
    NP_BOOL         m_bFixUpdate;                           ///< GPS update flag
    DOUBLE          m_dbLon;                                ///< GPS longititude
    DOUBLE          m_dbLat;                                ///< GPS Latitude
    FLOAT           m_fAltitude;                            ///< GPS altitude
    FLOAT           m_fSpeed;                               ///< GPS speed
    FLOAT           m_fBearing;                             ///< GPS bearing
    FLOAT           m_fAdjPDOP;                             ///< pdop
    FLOAT           m_fHDOP;                                ///< hdop
    FLOAT           m_fVDOP;                                ///< vdop
    DWORD           m_dwLaxis;                              ///< error laxis
    DWORD           m_dwSaxis;                              ///< error saxis
    FLOAT           m_fLaxisAngle;                          ///< error laxis angle
    DWORD           m_dwFixTime;                            ///< data fix time
    BYTE            m_byGpsDim;                             ///< GPS dimension
    DWORD           m_dwSvNum;                              ///< satellite number
    DWORD           m_dwUsedSvNum;                          ///< satellite number for fix
    BYTE            m_byGpsSigStatus;                       ///< GPS signal status
};

//kevin add temprory
typedef struct {
	WORD		utcYY;
	WORD		utcMon;
	WORD		utcDD;
	WORD		utcHH;
	WORD		utcMM;
	BOOL		available;
	DWORD		utcSS;
} UTCTIME;

#pragma pack()							// alignment by 4 byte end

/* ------------------------------------------------------------------
【メモ】
 sSnsDriverData構造体のDataConditionは、Gpsエンジンにて拡張する情報
 DRセンテンスに何らかのエラー(※1参照)がある場合に、エラー状態を示す。
 GPSエンジンでは、ドライバデータ用の構造体をクリアする。
 その後、ハンドラへ通知を行ない、ハンドラでは、エラー状態であることを、
 DataConditionにより判断し、処理を行うものとする。
 ※1
 パケット長エラー、チェックサムエラー、パケット内のデータが不備の場合
 などを検知すること
------------------------------------------------------------------ */
/******************************************************************************

    【更新履歴】
    @(#)$Log:NpGpsEngine_Def.h,v$
    @(#)2009/03/19 20:00:00  H.Ooue
    @(#)エラーコード追加
    @(#)
    @(#)2009/01/29 18:00:00  H.Ooue
    @(#)不要定義値削除
    @(#)
    @(#)2009/01/20 21:00:00  H.Ooue
    @(#)LOG設定結果定義削除
    @(#)LOG状態定義追加
    @(#)コメント修正
    @(#)
    @(#)2008/12/26 19:00:00  H.Ooue
    @(#)Debug用コード削除
    @(#)
    @(#)2008/12/22 19:00:00  H.Ooue
    @(#)LOG設定結果定義追加
    @(#)VER_NUM_SIZE定義追加
    @(#)変数名、構造体名、定義名変更修正
    @(#)
    @(#)2008/12/11 16:00:00  H.Ooue
    @(#)エラーコード追加
    @(#)拡張ログ関連定義追加
    @(#)
    @(#)2008/11/19 12:00:00  H.Ooue
    @(#)構造体メンバの型修正(GPS_DATA:nUseSateliteID)
    @(#)
    @(#)2008/11/17 12:00:00  H.Ooue
    @(#)新規登録
    @(#)

******************************************************************************/
#endif // CXX_NPGPSENGINE_DEF_H
