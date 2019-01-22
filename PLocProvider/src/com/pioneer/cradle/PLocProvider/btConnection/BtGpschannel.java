package com.pioneer.cradle.PLocProvider.btConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.pioneer.cradle.PLocProvider.ServiceCallbackControl;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;
import com.pioneer.cradle.nmea.NMEA0183Kit;

/*
 * This class is working for control Bluetooth process,
 * other class call interface to do matching action.
 */
 class BtGpschannel {
	
	// Singleton
	private static BtGpschannel mInstance;
	
	
	/*
	 * Instance
	 */
	public static synchronized BtGpschannel instance() {
		if(null == mInstance) {
			mInstance = new BtGpschannel();
		}
		return mInstance;
	}
	
	
	/*
     * Constructor. Create a Bluetooth service
     */
    public BtGpschannel() {
    	// get current Bluetooth adapter
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        // set state to nothing to do
        mState = STATE_NONE;
        taskSyncCtler = new TaskSyncController();
        
    }

	// Debugging
	private static final String TAG = "duan";
	private static final boolean Debug = true;
	
	// Name for the SDP record when creating server socket
	private static final String NAME = "BtGpschannel";
	// Unique UUID for this application
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	
    /**************************Get device******************************/
	// Bluetooth process using
    private BluetoothAdapter 	mAdapter;
    private BluetoothDevice		mDevice;
	private int conntectNum = 0;
    
    private TaskSyncController taskSyncCtler;
    
    public BluetoothDevice getDevice() {
    	// Set device information name and address
    	if (mState == STATE_CONNECTED) {
    		return mDevice;
    	}
    	else {
    		return null;
    	}
    }
    
    /**************************State Machine******************************/
    // Constants that indicate the current connection state
    private int mState;
    public static final int STATE_NONE 			= 0;	// we're doing nothing
    public static final int STATE_ACCEPT		= 1;	// accept state
    public static final int STATE_CONNECTING	= 2;	// try to connect other remote device
    public static final int STATE_CONNECTED 	= 3; 	// now connected with a remote device
	
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    
    private synchronized void setState(int state, boolean needNotificate) {

    	// Check the state if same nothing to do
    	if (state == mState) {
    		return;
    	}
    	
    	mState = state;
    	ConnectLog.getInstance().writeGpslogfile("connect states changed, new state = "+state);
    	
    	if(needNotificate) {
    		ServiceCallbackControl.getInstance().deliveryRequiredEvent(state);
    	}
    }
    
    private void setState(int state) {
    	setState(state, true);
    }
    /*!!!!!this method should not be used to a certain state, just to enter some cases in thread
     *if need to set a certain state, please use setState(int state)
     *
     */
    private void setStateWithoutNotification(int state) {
    	setState(state, false);
    }
    /**
     * Return the current connection state. 
     */
    public synchronized int getState() {
    	return mState;
    }
    
    /*
     * CurrentBTIsEnable
     */
    public boolean isBTEnabled() {
    	return (mAdapter != null) && mAdapter.isEnabled();
    }
    
    /**
     * Start the AcceptThread to initiate a ServerSocket to accept a remote device.
     * The BluetoothServerSocket to accept connect request
     */
    private synchronized void accept(BluetoothDevice device) {
    	if (getState() != STATE_NONE){
    		return;
    	}

    	if (mConnectingThread != null) {
        	mConnectingThread.cancel();
        	mConnectingThread = null;
        }
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        if (mAcceptThread != null) {
        	mAcceptThread.cancel();
        	mAcceptThread = null;
        }
        if (mAcceptThread == null) {
	    	mAcceptThread = new AcceptThread(device);
	    	mAcceptThread.start();
	    	Log.i(TAG,"Start AcceptThread in accept()");
        }
        // Check valid
        if (mAcceptThread.isValid()) {
            setState(STATE_ACCEPT);
        }
        else {
        	// Reset state to none
        	setState(STATE_NONE);
        }
    }
    
    public synchronized boolean startToAccept(BluetoothDevice device_info) {
		if (mState == STATE_ACCEPT) {
			if (mAcceptThread != null && mAcceptThread.isValid()) {
//				setStateWithoutNotification(STATE_NONE);
//				accept(device_info);
				resetAccessCountDown();
				return (mState == STATE_ACCEPT);
			}
		} else if (mState == STATE_CONNECTED) {
			if (device_info.equals(getDevice())) {
				Log.i(TAG, "has connected=====");
				ConnectLog.getInstance().writeGpslogfile("into GPS channel state is connected, cancel connect, device = "+device_info);
				return true;
			} else {
				disconnect();
				return startToAccept(device_info);
			}
		} else if(mState == STATE_NONE) {
		
			BluetoothDevice device = device_info;
			// accept
			accept(device);
	
			ConnectLog.getInstance().writeGpslogfile("success to into GPS channel connect device = "+device);
			// Check start connecting result
			return (mState == STATE_ACCEPT);
		}
		return false;
	}
	private void resetAccessCountDown() {
		if(mAcceptThread != null && mAcceptThread.isValid()) {
			mAcceptThread.resetCountDOwn();
		}
		
	}


	/*
	 * Start to connect with a remote device
	 */
	public synchronized boolean startToConnect(BluetoothDevice device_info) {
		if(device_info == null) {
			return false;
		}
		
		if (mState == STATE_CONNECTING) {
			if(mConnectingThread != null && mConnectingThread.isValid() && device_info.equals(mConnectingThread.getDevice())){
				ConnectLog.getInstance().writeGpslogfile("into GPS channel state is connecting, cancel connect, device = "+device_info);
				return true;
			}
		}
		
		if(mState == STATE_CONNECTED && device_info.equals(getDevice())){
			ConnectLog.getInstance().writeGpslogfile("into GPS channel state is connected, cancel connect, device = "+device_info);
			return true;
		}
		
		ConnectLog.getInstance().writeGpslogfile("start to into GPS channel connect device = "+device_info);
		
		// Get device
		BluetoothDevice device = device_info;
		// start connecting
//		connect(device);
		accept(device);
		ConnectLog.getInstance().writeGpslogfile("success to into GPS channel connect device = "+device);
		// Check start connecting result
		return (mState == STATE_CONNECTING) ;
	}
	
	/*
	 * Start to connect with a remote device
	 */
	public synchronized boolean startToConnectWithStopCurrentConnect(BluetoothDevice device_info) {
		if(device_info == null) {
			return false;
		}
		
		if (mState == STATE_CONNECTING) {
			if(mConnectingThread != null && mConnectingThread.isValid() && device_info.equals(mConnectingThread.getDevice())){
				return true;
			}
		}
		
		// Get device
		BluetoothDevice device = device_info;
		// start connecting
//		connect(device);
		accept(device);
		ConnectLog.getInstance().writeGpslogfile("success to into GPS channel connect device = "+device);
		// Check start connecting result
		return (mState == STATE_CONNECTING) ;
	}
	
	/*
	 * Stop
	 */
	public synchronized void stop() {
		// Disconnect all
		disconnect();
	}
	
	/**************************Bluetooth Connect State Change******************************/
	private ConnectingThread	mConnectingThread;
	private ConnectedThread 	mConnectedThread;
	private AcceptThread		mAcceptThread;

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
//    private synchronized void connect(BluetoothDevice device) {
//        
//    	// Cancel all thread
//    	if (mState == STATE_CONNECTING) {
//    		// Cancel any thread currently running for connecting
//    		if (mConnectingThread != null) {
//    			mConnectingThread.cancel();
//    			mConnectingThread = null;
//    		}
//    	}
//
//        // Cancel any thread currently running a connection
//    	if (mState == STATE_CONNECTED) {
//	        if (mConnectedThread != null) {
//	        	mConnectedThread.cancel();
//	        	mConnectedThread = null;
//	        }
//    	}
//        
//    	// Start the thread to connect with the given device
//    	mConnectingThread = new ConnectingThread(device);
//    	// Check valid
//    	if (mConnectingThread.isValid()) {
//    		setState(STATE_CONNECTING);
//    		mConnectingThread.start();
//    	}
//    	else {
//    		// Reset state to none
//    		setState(STATE_NONE);
//    		mConnectingThread = null;
//    	}
//        
//    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param iLastState
     */
    private synchronized void connected(BluetoothSocket socket, int iLastState) {
    	
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        

        // Start the thread to manage the connection and perform transmissions
    	mConnectedThread = new ConnectedThread(socket, iLastState);
    	// Check valid
    	if (mConnectedThread.isValid()) {
    		
    		
    		mDevice = socket.getRemoteDevice();
    		if(mDevice != null) {
    			DeviceTools.addDevice(mDevice);
    		} else {
    		}
    		mConnectedThread.start();
    		setState(STATE_CONNECTED);
    		
    	}
    	else {
    		// Reset state to none
    		setState(STATE_NONE);
    		mConnectedThread = null;
    		return;
    	}


        // Set current connect device
 		
		// already switched to internal GPS need to
		// switch to external
    }
    
    /**
     * disconnect all
     */
    private synchronized void disconnect() {
    	
        // stop connected thread of input and output
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        
       
        if(mAcceptThread != null) {
        	mAcceptThread.cancel();
        	mAcceptThread = null;
        }
        
        // Cancel the thread
        if (mConnectingThread != null) {
        	mConnectingThread.cancel();
        	mConnectingThread = null;
        }
        
        // set state to none
        setState(STATE_NONE);
        
        if(taskSyncCtler.isSignal()) {
        	taskSyncCtler.raiseSignal();
        }
    }

    /**
     * Indicate that the connection was lost.
     */
    private synchronized void connectionLost(BluetoothDevice device) {
    	setStateWithoutNotification(STATE_NONE);
    	Log.i(TAG, "Retry begin========!!!");
    	retryAcceptConnect(device);
    }

	private void retryAcceptConnect(BluetoothDevice device) {
		startToAccept(device);
	}
    
	
    public void setConntectedNum(int num) {
    	if(num > 0) {
        	taskSyncCtler.raiseSignal();
    	} 
        conntectNum = num;
    }
    
    /**************************Bluetooth Connect******************************/
    /*
     * This thread runs while connecting or reading process has exception.
     */
    
    /*
     * This thread runs while connecting or reading process has exception.
     */
    private class AcceptThread extends Thread {
    	
    	private boolean mmbValid;
    	private boolean mmbQuit;
    	private BluetoothDevice device_info;
    	private String deviceName = null;
        private final BluetoothServerSocket mmServerSocket;
        int iExceptCnt;
        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }
        
    	public void resetCountDOwn() {
    		iExceptCnt = 0;
			
		}

		/*
    	 * Constructor for initialization
    	 */
    	public AcceptThread(BluetoothDevice device) {
    		
    		mmbQuit = false;
    		mmbValid = false;
    		device_info = device;
    		deviceName = device_info.getName();
    		BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                mmbValid = true;
            } catch (IOException e) {            	
            	try{
	            	mAdapter.disable();
	            	mAdapter.enable();
            	}catch (Exception e2){}

            	if (Debug) {
            		ConnectLog.getInstance().writeGpslogfile("listen() failed" + e);
            	}
            	// Stop
            	BtGpschannel.this.stop();
            	e.printStackTrace();
            }
            
            // Get server socket
            mmServerSocket = mmbValid ? tmp : null;	 	
    	}
    	
    	/*
    	 * thread run to wait for next connect
    	 */
    	public void run() {
    		// Check create successful or not
            if (!mmbValid) {
            	return;
            }
    		
    		setName("AcceptThread");
    		
    		BluetoothSocket socket = null;
    		iExceptCnt = 0;
    		final int EXCEPT_MAX_CNT = 6;
            while(true) {
            	// Check quit
            	if (mmbQuit) {
            		break;
            	}
            	
            	// accept
            	iExceptCnt++;
            	try {
            		ConnectLog.getInstance().writeGpslogfile("---Begin to listen");
            		Log.i(TAG,"Begin to listen");
                    // This is a blocking call and will only return on a
            		
            		
            		//check whether bluetooth is open
            		if(!isBTEnabled() ||
            				(device_info != null && device_info.getBondState() != BluetoothDevice.BOND_BONDED)) {
            			Log.i(TAG,"accepting thread bluetooth closed");
            			break;
            		}
            		//force update state to Accept
            		setState(STATE_ACCEPT);
            		Log.i(TAG,"Begin to accept");
                    // successful connection or an exception
                    socket = mmServerSocket.accept(10 * 1000);
                    
                    Log.i(TAG,"accept ok");
                    ConnectLog.getInstance().writeGpslogfile("accept over");
                } catch (IOException e) {
                	ConnectLog.getInstance().writeGpslogfile("---accept exception");
                	Log.i(TAG,"====accept exception====");
                	// Check quit
                	if (mmbQuit) {
                        break;
                	}
                	
                	if (Debug) {
                		Log.e(TAG, "accept() failed", e);
                	}
                	
                	
                	Log.i(TAG,"iExceptCnt = "+iExceptCnt);
                	// check exit the cycle or for next
                	if (iExceptCnt > EXCEPT_MAX_CNT) {
                		ConnectLog.getInstance().writeGpslogfile("---accept() break");
                		break;
                	}else {
                		ConnectLog.getInstance().writeGpslogfile("---accept() for next");
                		continue;
                	}
                }
                
				// If a connection was accepted
				if (socket != null) {
					String remoteDeviceName = socket.getRemoteDevice().getName();
					if (null != remoteDeviceName && null != deviceName) {
						if (remoteDeviceName.equals(deviceName)) {
							connected(socket, STATE_ACCEPT);
							mAcceptThread.cancel();
							mAcceptThread = null;
							return;
						} else {
							// Terminate new socket and start for next accept
							try {
								socket.close();
							} catch (IOException e) {
								if (Debug) {
									Log.e(TAG, "Could not close unwanted socket", e);
								}
							}
							continue;
						}
					}
				}
			}
            ConnectLog.getInstance().writeGpslogfile("---END mAcceptThread, accept failed");
            Log.i(TAG,"END mAcceptThread, accept failed");
            if(null != mAcceptThread) {
	            mAcceptThread.cancel();
	            mAcceptThread = null;
            }
            setState(STATE_NONE);
		}
    	
    	/*
    	 * Cancel
    	 */
    	public void cancel() {
    		mmbQuit = true;
    		ConnectLog.getInstance().writeGpslogfile("cancel accept");
    		try {
    			if (mmbValid) {
    				mmServerSocket.close();
    				mmbValid = false;
    			}
    		} catch (IOException e) {
    			if (Debug) {
    				Log.e(TAG, "close() of server failed", e);
    			}
    		}
    	}
    }
    
    /**
     * This thread runs while select a device in search list and 
     * connecting with it. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectingThread extends Thread {

    	private boolean mmbValid;
    	private boolean mmbQuit;
        private final BluetoothSocket mmSocket;
        
        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }
        
        public BluetoothDevice getDevice() {
        	return mmSocket.getRemoteDevice();
        }

        /*
         * Constructor for initialization
         */
        public ConnectingThread(BluetoothDevice device) {
            
        	mmbValid = false;
        	mmbQuit = false;
        	BluetoothSocket tmp = null;
            
            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
            	// Get socket
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                mmbValid = true;
            } catch (IOException e) {
            	// Log output
            	if (Debug) {
            		Log.e(TAG, "create() failed", e);
            	}
            }
            
            // Device and socket create
            mmSocket = mmbValid ? tmp : null;
        }

        /*
         * Try to connect with the remote device
         * @see java.lang.Thread#run()
         */
        public void run() {
        	
        	// Check valid
        	if (!mmbValid) {
        		return;
        	}
        	
        	ConnectLog.getInstance().writeGpslogfile("BEGIN connecting thread");
        	
        	setName("ConnectingThread");
        	
        	// Always cancel discovery because it will slow down a connection
//           	mAdapter.cancelDiscovery();

            // Check quit
            if (mmbQuit) {
                return;
            }
            
            // Make a connection to the BluetoothSocket
            try {
            	// This is a blocking call and will only return on a
            	// successful connection or an exception
            	mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    if (Debug) {
                    	Log.e(TAG, "unable to close() socket during connection failure", e2);
                    }
                }
                
            	// No quit turn to connect failed state
            	if (!mmbQuit) {
            		// Connection failed change to exception thread
            		if(mState != STATE_NONE) {
            			BtGpschannel.this.stop();
            		}
            	}
                return;
            }
            synchronized (BtGpschannel.this) {
            	mConnectingThread = null;
            }
            
            // Start the connected thread
            connected(mmSocket, STATE_CONNECTING);
        }
        
        /*
         * Cancel
         */
        public void cancel() {
        	mmbQuit = true;
            try {
            	if (mmbValid) {
            		mmSocket.close();
            	}
            } catch (IOException e) {
            	if (Debug) {
            		Log.e(TAG, "close() of connect socket failed", e);
            	}
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     */
    private class ConnectedThread extends Thread implements CradleCommand.WriteStream{
    	
    	private boolean mmbValid;
    	private boolean mmbQuit;
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        
        private boolean firstRecive = true;
        
        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }
        
        /*
         * Constructor for initialization
         */
        public ConnectedThread(BluetoothSocket socket, int iLastState) {
        	
        	mmbValid = false;
        	mmbQuit = false;
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams from BluetoothSocket
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
                mmbValid = true;
            } catch (IOException e) {
            	// Log output
            	if(Debug) {
            		Log.e(TAG, "temp sockets not created", e);
            	}
//            	loclistener.writeGpslogfile("socket connect check fail");
            }
            
            Log.d("test", "mmbValid = "+ mmbValid);
            // Create socket, input and output
            mmInStream = mmbValid ? tmpIn : null;
            mmOutStream = mmbValid ? tmpOut : null;
            
            // Sleep 1s
//        	SystemClock.sleep(1000);
            
        }
        
        /*
         * Main read and write cycle
         * @see java.lang.Thread#run()
         */
        public void run() {
        	
        	// Check valid
        	if (!mmbValid) {
        		// Close created socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                	if (Debug) {
                		Log.e(TAG, "close() of connect socket failed", e2);
                	}
                }
        		return;
        	}

        	setName("ConnectedThread");
        	
            
            // Buffer
            byte[] buffer = new byte[1024];
    		
            // Keep listening to the InputStream while connected
            while (true) {
            	
            	// Check quit
            	if (mmbQuit) {
                    break;
            	}
            	
            	if(conntectNum <= 0) {
            		taskSyncCtler.getSignal();	
            	}
                try {
                	
					int iLength = mmInStream.read(buffer);
					
					String nmeaData  = getDataString(buffer, iLength);
					
					
					NmeaLog.getInstance().writeGpslogfile(nmeaData);
					
					ServiceCallbackControl.getInstance().deliveryLocationEventFromNmea(nmeaData);
					
					// parse data and deliver to GpsSnsModule
					boolean decode1FrameAtLeast = NMEA0183Kit.receiveNmeaInfo(nmeaData);
					if(decode1FrameAtLeast) {
						ServiceCallbackControl.getInstance().deliveryLocationToOtherApp();
					}
					if(firstRecive) {
			        	String cradleMsg = NMEA0183Kit.getCradleMessage();
			        	
			        	if(TextUtils.isEmpty(cradleMsg) && SharedPreferenceData.USE_LAST_LOCATION.getBoolean()) {
			        		cradleMsg = SharedPreferenceData.CRADLE_BACKUP_MESSAGE.getString();
			        	}
			        	
			        	if(!TextUtils.isEmpty(cradleMsg)) {
			            	write(cradleMsg.getBytes());
			        	}
			        	
			        	CradleCommand.getInstance().setSetToCardle(this);
			        	
			        	firstRecive = false;
					} else {
						if(CradleCommand.getInstance().hasCommads()) {
							CradleCommand.getInstance().excute(this);
						}
					}
					
					
                } catch (IOException e) {
                	// Check quit
                	if (mmbQuit) {
                        break;
                	}
              
					String cradleMsg = NMEA0183Kit.getCradleMessage();
		        	
		        	if(!TextUtils.isEmpty(cradleMsg)) {
		        		SharedPreferenceData.CRADLE_BACKUP_MESSAGE.setValue(cradleMsg);
		        	}
		        	e.printStackTrace();

		        	if (!isBTEnabled()) {
		        		Log.i(TAG, "Close Bluetooth");
                		disconnect();
						break;
					} else if(isDeviceBonded(mmSocket.getRemoteDevice())){
						connectionLost(mmSocket.getRemoteDevice());
						Log.d("test", "connect Lost");
						break;
					}else{
						// connection lost
						Log.i(TAG, "Cancel pair");
						disconnect();
						break;
					}
                }
            }
            
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
        	
        	// Check quit
        	if (mmbQuit) {
                return;
        	}
        	try {
        		mmOutStream.write(buffer);
        	} catch (IOException e) {
	           	if (Debug) {
	           		Log.e(TAG, "Exception during write", e);
	           	}
        	}
        }

        /*
         * Cancel
         */
        public void cancel() {
        	mmbQuit = true;
            try {
            	if (mmbValid) {
            		mmInStream.close();
            		mmOutStream.close();
            	}
                mmSocket.close();
            } catch (IOException e2) {
            	if (Debug) {
            		Log.e(TAG, "close() of connect socket failed", e2);
            	}
            }
        }
    }
    
	private String getDataString(byte[] buff, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(buff, 0, temp, 0, length);
		return new String(temp);
	}

	private static final Object writeSync = new Object();	// synchronized object for write
	
	/*
	 * write feedback data to cradle by BT
	 */
	public void WriteToCradle(byte[] buffer, int size) {
		if (mState == STATE_CONNECTED && mConnectedThread != null) {
			synchronized (writeSync) {
				// Write data to Cradle
				mConnectedThread.write(buffer);
				// Sleep 20ms for next write
				SystemClock.sleep(20);
			}
		}
	}
	
	private Set<BluetoothDevice> getbondedDevices() {
		if(null != mAdapter) {
			return mAdapter.getBondedDevices();
		}
		else {
			return null;
		}
	}
	
	private boolean isDeviceBonded(BluetoothDevice dev) {
		String remoteAddress = dev.getAddress();
		Set<BluetoothDevice> bondedList = getbondedDevices();
		if(null == bondedList) {
			return false;
		}
		for(BluetoothDevice bondedDev: bondedList) {
			if(remoteAddress.equals(bondedDev.getAddress())) {
				return true;
			}
		}
		
		return false;
	}

	public class TaskSyncController {
		private boolean isSignal = false;
		
		public synchronized void getSignal() {
			try {
				isSignal = true;
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public synchronized void raiseSignal() {
			this.notifyAll();
			isSignal = false;
		}
		
		public synchronized boolean isSignal(){
			return isSignal;
		}
	}

}

