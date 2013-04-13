package com.nekomeshi312.darts;


import java.io.IOException;

import com.android.future.usb.UsbAccessory;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;


public class DartsADKCtrl extends ADKCtrlBase {
	//的の位置の番号は
	//上位6bitが(12時の位置)から時計回りに 0x00, 0x01,・・・0x14, 
	//扇型の位置は下位2bitが 0x01:通常 0x02:double ring 0x03:triple ring 
	//Outer Bullは0xfd, Inner　Bull は0xfe 何も刺さっていない状態は0x00
	public static final byte ADK_OUTER_BULL = (byte) 0xfd;
	public static final byte ADK_INNER_BULL = (byte) 0xfe;
	public static final byte ADK_NO_ACTIVE = (byte) 0x00;

	//ADKから届く番号は、下位4bitがキースキャンの列選択番号(0x01～0x07)
	//上位4bitが行検知番号(0x01～0x0a)
	//何も刺さっていない状態は0x00, QUITは0xff
	private static final byte [][] ADK_KEY_TABLE = {//8x11のADK->Androidの番号変換matrix
		//     0           1           2           3           4           5           6           7
		{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},//0
		{(byte)0x00, (byte)0x46, (byte)0x47, (byte)0x45, (byte)0xfe, (byte)0x41, (byte)0x43, (byte)0x42},//1
		{(byte)0x00, (byte)0x4a, (byte)0x4b, (byte)0x49, (byte)0xfd, (byte)0x3d, (byte)0x3f, (byte)0x3e},//2
		{(byte)0x00, (byte)0x4e, (byte)0x4f, (byte)0x4d, (byte)0x00, (byte)0x39, (byte)0x3b, (byte)0x3a},//3
		{(byte)0x00, (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00, (byte)0x35, (byte)0x37, (byte)0x36},//4
		{(byte)0x00, (byte)0x1a, (byte)0x1b, (byte)0x19, (byte)0x00, (byte)0x1d, (byte)0x1f, (byte)0x1e},//5
		{(byte)0x00, (byte)0x16, (byte)0x17, (byte)0x15, (byte)0x00, (byte)0x21, (byte)0x23, (byte)0x22},//6
		{(byte)0x00, (byte)0x12, (byte)0x13, (byte)0x11, (byte)0x00, (byte)0x25, (byte)0x27, (byte)0x26},//7
		{(byte)0x00, (byte)0x0e, (byte)0x0f, (byte)0x0d, (byte)0x00, (byte)0x29, (byte)0x2b, (byte)0x2a},//8
		{(byte)0x00, (byte)0x0a, (byte)0x0b, (byte)0x09, (byte)0x00, (byte)0x2d, (byte)0x2f, (byte)0x2e},//9
		{(byte)0x00, (byte)0x06, (byte)0x07, (byte)0x05, (byte)0x00, (byte)0x31, (byte)0x33, (byte)0x32} //A
	};
	public interface OnDartsPosReceived{
		public void onDartsPosReceived(byte dartsPos);
	}
	private static final String LOG_TAG = "DartsADKCtrl";
	private class ReceiveDartsRunnable implements Runnable{
		private volatile boolean mRunning = true;
		private Object mLock = new Object();
		private int mDataLength;
		byte mArrivedData[];
		@Override
		public void run() {
			// TODO Auto-generated method stub

			byte[] buffer = new byte[128];
			while(mRunning){
				try {
					int ret = mInputStream.read(buffer);
					boolean quitCommand = false;
					boolean arrowRemoved = false;
					for(int i = 0;i < ret;i++){
						if(buffer[i] == CMD_QUIT){
							quitCommand = true;
							break;
						}
						if(buffer[i] == ARROW_REMOVED ){
							arrowRemoved = true;
							break;
						}
					}
					if(arrowRemoved) continue;
					if(quitCommand){
						mRunning = false;
						break;
					}
					synchronized(mLock) {
						mDataLength = ret;
						mArrivedData = new byte[ret];
						for(int i = 0;i < ret;i++) mArrivedData[i] = buffer[i];
					}
					if(mOnDartsPosReceived != null){
						mHandler.post(new Runnable(){
							@Override
							public void run() {//callback先でUIをいじるかもしれないのでUIスレッドで呼んでやる
								// TODO Auto-generated method stub
								synchronized(mLock) {
									for(int i = 0;i < mDataLength;i++){
										final int col = (mArrivedData[i] & 0x0f);
										final int row = (((0xff & mArrivedData[i]) >> 4) & 0xff);
										if(MyDebug.DEBUG) Log.d(LOG_TAG, "arrived data = " + (0xff & mArrivedData[i]) + "col = " + col + "row = " + row);
										mOnDartsPosReceived.onDartsPosReceived(ADK_KEY_TABLE[row][col]);
									}
								}
							}
						});
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mRunning = false;
				}
			}
			Log.i(LOG_TAG, "ReceiveDartsRunnable Quit");
		}
		public void stop(){
			mRunning = false;
		}
		
	}
	private static final byte CMD_QUIT = (byte) 0xFF;//ADKから届く的の位置の番号と重ならないように
	private static final byte ARROW_REMOVED = (byte) 0x00;//ADKから届く的の位置の番号と重ならないように
	
	private Handler mHandler;
	private OnDartsPosReceived mOnDartsPosReceived = null;
	private ReceiveDartsRunnable mReceiveDartsRunnable = null;
	private Thread mReceiveDartsThread = null;

	DartsADKCtrl(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		mHandler = new Handler();
	}

	public void setOnDartsPosReceived(OnDartsPosReceived posReceived){
		mOnDartsPosReceived = posReceived;
	}
	/* (non-Javadoc)
	 * @see com.nekomeshi312.darts.ADKCtrlBase#openAccessory(com.android.future.usb.UsbAccessory)
	 */
	@Override
	protected boolean openAccessory(UsbAccessory accessory) {
		// TODO Auto-generated method stub
		boolean opened = super.openAccessory(accessory);
		if(opened){
			mReceiveDartsRunnable = new ReceiveDartsRunnable();
			
			mReceiveDartsThread = new Thread(mReceiveDartsRunnable);
			mReceiveDartsThread.start();
			
		}
		return opened;
	}

	/* (non-Javadoc)
	 * @see com.nekomeshi312.darts.ADKCtrlBase#closeAccessory()
	 */
	@Override
	public void closeAccessory() {
		// TODO Auto-generated method stub
		if(null != mReceiveDartsRunnable && mReceiveDartsThread.isAlive()){
			//終了するときにQuitコマンドをADKに送って、同じコマンドを返してもらう
			//そうしないとmReceiveDartsRunnableのmInputStreamが抜けられない。
			if(mOutputStream != null){
				byte[] cmd = new byte[1];
				cmd[0] = CMD_QUIT;
				try {
					mOutputStream.write(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mReceiveDartsRunnable.stop();
		}
		super.closeAccessory();
	}

}
