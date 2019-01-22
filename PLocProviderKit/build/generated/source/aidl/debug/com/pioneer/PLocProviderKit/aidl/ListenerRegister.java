/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace\\cradle\\PLocProviderKit\\src\\com\\pioneer\\PLocProviderKit\\aidl\\ListenerRegister.aidl
 */
package com.pioneer.PLocProviderKit.aidl;
public interface ListenerRegister extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.pioneer.PLocProviderKit.aidl.ListenerRegister
{
private static final java.lang.String DESCRIPTOR = "com.pioneer.PLocProviderKit.aidl.ListenerRegister";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.pioneer.PLocProviderKit.aidl.ListenerRegister interface,
 * generating a proxy if needed.
 */
public static com.pioneer.PLocProviderKit.aidl.ListenerRegister asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.pioneer.PLocProviderKit.aidl.ListenerRegister))) {
return ((com.pioneer.PLocProviderKit.aidl.ListenerRegister)iin);
}
return new com.pioneer.PLocProviderKit.aidl.ListenerRegister.Stub.Proxy(obj);
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
case TRANSACTION_pRegisterListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.pioneer.PLocProviderKit.aidl.pOnCallBackListener _arg1;
_arg1 = com.pioneer.PLocProviderKit.aidl.pOnCallBackListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.pRegisterListener(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_pUnregisterListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.pioneer.PLocProviderKit.aidl.pOnCallBackListener _arg1;
_arg1 = com.pioneer.PLocProviderKit.aidl.pOnCallBackListener.Stub.asInterface(data.readStrongBinder());
boolean _result = this.pUnregisterListener(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_pGetExtDeviceConnectionStatus:
{
data.enforceInterface(descriptor);
int _result = this.pGetExtDeviceConnectionStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_pGetLatestLocation:
{
data.enforceInterface(descriptor);
android.location.Location _result = this.pGetLatestLocation();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getConnectedDevice:
{
data.enforceInterface(descriptor);
android.bluetooth.BluetoothDevice _result = this.getConnectedDevice();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_order:
{
data.enforceInterface(descriptor);
android.os.Bundle _arg0;
if ((0!=data.readInt())) {
_arg0 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.os.Bundle _result = this.order(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.pioneer.PLocProviderKit.aidl.ListenerRegister
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
/**
     * Called when the service has a new value for you.
     * @value current state of BT in SPP Service
     */
@Override public boolean pRegisterListener(java.lang.String packageName, com.pioneer.PLocProviderKit.aidl.pOnCallBackListener ll) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeStrongBinder((((ll!=null))?(ll.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_pRegisterListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean pUnregisterListener(java.lang.String packageName, com.pioneer.PLocProviderKit.aidl.pOnCallBackListener ll) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeStrongBinder((((ll!=null))?(ll.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_pUnregisterListener, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int pGetExtDeviceConnectionStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pGetExtDeviceConnectionStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.location.Location pGetLatestLocation() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.location.Location _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pGetLatestLocation, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.location.Location.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.bluetooth.BluetoothDevice getConnectedDevice() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.bluetooth.BluetoothDevice _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getConnectedDevice, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.os.Bundle order(android.os.Bundle order) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.Bundle _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((order!=null)) {
_data.writeInt(1);
order.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_order, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.os.Bundle.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_pRegisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_pUnregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_pGetExtDeviceConnectionStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_pGetLatestLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getConnectedDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_order = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * Called when the service has a new value for you.
     * @value current state of BT in SPP Service
     */
public boolean pRegisterListener(java.lang.String packageName, com.pioneer.PLocProviderKit.aidl.pOnCallBackListener ll) throws android.os.RemoteException;
public boolean pUnregisterListener(java.lang.String packageName, com.pioneer.PLocProviderKit.aidl.pOnCallBackListener ll) throws android.os.RemoteException;
public int pGetExtDeviceConnectionStatus() throws android.os.RemoteException;
public android.location.Location pGetLatestLocation() throws android.os.RemoteException;
public android.bluetooth.BluetoothDevice getConnectedDevice() throws android.os.RemoteException;
public android.os.Bundle order(android.os.Bundle order) throws android.os.RemoteException;
}
