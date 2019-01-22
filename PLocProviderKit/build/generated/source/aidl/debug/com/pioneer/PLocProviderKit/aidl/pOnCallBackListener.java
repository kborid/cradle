/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace\\cradle\\PLocProviderKit\\src\\com\\pioneer\\PLocProviderKit\\aidl\\pOnCallBackListener.aidl
 */
package com.pioneer.PLocProviderKit.aidl;
public interface pOnCallBackListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.pioneer.PLocProviderKit.aidl.pOnCallBackListener
{
private static final java.lang.String DESCRIPTOR = "com.pioneer.PLocProviderKit.aidl.pOnCallBackListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.pioneer.PLocProviderKit.aidl.pOnCallBackListener interface,
 * generating a proxy if needed.
 */
public static com.pioneer.PLocProviderKit.aidl.pOnCallBackListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.pioneer.PLocProviderKit.aidl.pOnCallBackListener))) {
return ((com.pioneer.PLocProviderKit.aidl.pOnCallBackListener)iin);
}
return new com.pioneer.PLocProviderKit.aidl.pOnCallBackListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_onReceiveRemoteCtrl:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
this.onReceiveRemoteCtrl(_arg0);
return true;
}
case TRANSACTION_onReceiveLocationInfo:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
this.onReceiveLocationInfo(_arg0);
return true;
}
case TRANSACTION_onLocationChanged:
{
data.enforceInterface(descriptor);
android.location.Location _arg0;
if ((0!=data.readInt())) {
_arg0 = android.location.Location.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onLocationChanged(_arg0);
return true;
}
case TRANSACTION_onExtDeviceConnectStateChanged:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
this.onExtDeviceConnectStateChanged(_arg0);
return true;
}
case TRANSACTION_onSatelliteChanged:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
this.onSatelliteChanged(_arg0);
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.pioneer.PLocProviderKit.aidl.pOnCallBackListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onReceiveRemoteCtrl(int info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(info);
mRemote.transact(Stub.TRANSACTION_onReceiveRemoteCtrl, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onReceiveLocationInfo(java.lang.String nmea) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(nmea);
mRemote.transact(Stub.TRANSACTION_onReceiveLocationInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onLocationChanged(android.location.Location l) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((l!=null)) {
_data.writeInt(1);
l.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onLocationChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onExtDeviceConnectStateChanged(int connectState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(connectState);
mRemote.transact(Stub.TRANSACTION_onExtDeviceConnectStateChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSatelliteChanged(java.lang.String satellites) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(satellites);
mRemote.transact(Stub.TRANSACTION_onSatelliteChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onReceiveRemoteCtrl = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onReceiveLocationInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onLocationChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onExtDeviceConnectStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onSatelliteChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void onReceiveRemoteCtrl(int info) throws android.os.RemoteException;
public void onReceiveLocationInfo(java.lang.String nmea) throws android.os.RemoteException;
public void onLocationChanged(android.location.Location l) throws android.os.RemoteException;
public void onExtDeviceConnectStateChanged(int connectState) throws android.os.RemoteException;
public void onSatelliteChanged(java.lang.String satellites) throws android.os.RemoteException;
}
