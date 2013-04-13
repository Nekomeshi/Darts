package com.nekomeshi312.darts;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;


public class ADKCtrlBase {
	interface OnConnectionChangeListener{
		public void onAttached();
		public void onDetouched();
	}
	public class ADKDetouchException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ADKDetouchException(String msg){
			super(msg);
		}
	}
	
	private static final String LOG_TAG = "ADKCtrlBase";
	private static final String ACTION_USB_PERMISSION = ".ADKCtrlBase.USB_PERMISSION";
	private String mPermissionString;
	
	Activity mRootActivity = null;
	
	private UsbManager mUsbManager = null;

	private PendingIntent mPermissionIntent = null;
	private boolean mPermissionRequestPending = false;
	private UsbAccessory mAccessory = null;
	private ParcelFileDescriptor mFileDescriptor = null;
	protected FileInputStream mInputStream = null;
	protected FileOutputStream mOutputStream = null;
	
	private OnConnectionChangeListener mOnConnectionChangeListener = null;
	
	public void setOnConnectionChangeListener(OnConnectionChangeListener listener){
		mOnConnectionChangeListener = listener;
	}
	ADKCtrlBase(Activity activity){
		mRootActivity = activity;
		
		mUsbManager = UsbManager.getInstance(mRootActivity);

		if (mRootActivity.getLastNonConfigurationInstance() != null) {
			ADKCtrlBase a = (ADKCtrlBase)mRootActivity.getLastNonConfigurationInstance();
			mAccessory = (UsbAccessory)a.mAccessory;
			openAccessory(mAccessory);
		}		
	}
	/**
	 * ADK接続検出レシーバを登録する
	 */
	public void registerReceiver(){
		mPermissionString = mRootActivity.getPackageName() + ACTION_USB_PERMISSION;//パッケージ名だけでもいいけど
		if(MyDebug.DEBUG){
			Log.i(LOG_TAG, "ACTION_USB_PERMISSION = " + mPermissionString);
		}
    	mPermissionIntent = PendingIntent.getBroadcast(mRootActivity, 
    													0, 
    													new Intent(mPermissionString), 
    													0);
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(mPermissionString);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
    	
		mRootActivity.registerReceiver(mUsbReceiver, filter);
	}
	/**
	 * ADK登録レシーバを登録解除する
	 */
	public void unregisterReceiver(){
		mRootActivity.unregisterReceiver(mUsbReceiver);
	}
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(MyDebug.DEBUG)Log.i(LOG_TAG, action);
			if (mPermissionString.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} 
					else {
						Log.d(LOG_TAG, "permission denied for accessory "
								+ accessory);
						Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
					}
					mPermissionRequestPending = false;
				}
			}
			else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
					if(null != mOnConnectionChangeListener){
						mOnConnectionChangeListener.onDetouched();
					}
				}
			}
		}
	};
	public boolean isAccessoryOpen(){
		return (mInputStream != null && mOutputStream != null) ;
	}
	
	public boolean findAndOpenAccessory(){
		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
				Log.d(LOG_TAG, "accessory opened");
				return true;
			}
			else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
					Log.d(LOG_TAG, "permission pending");
					return true;
				}
			}
		} 
		else {
			Log.d(LOG_TAG, "mAccessory is null");
			return false;
		}
	}
	
	protected boolean openAccessory(UsbAccessory accessory) {
		if(null == mUsbManager)return false;
		if(null == accessory) return false;
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Log.d(LOG_TAG, "accessory opened");
			
			if(null != mOnConnectionChangeListener){
				mOnConnectionChangeListener.onAttached();
			}

			return true;
		}
		else {
			Log.d(LOG_TAG, "accessory open fail");
			return false;
		}
	}
	public void closeAccessory() {
//		enableControls(false);
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
				mInputStream.close();
				mOutputStream.close();
			}
			Log.d(LOG_TAG, "accessory detouched1");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			mFileDescriptor = null;
			mAccessory = null;
			mInputStream = null;
			mOutputStream = null;
		}
	}
}
