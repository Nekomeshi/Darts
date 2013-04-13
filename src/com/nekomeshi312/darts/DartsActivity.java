package com.nekomeshi312.darts;


import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
public class DartsActivity extends SherlockFragmentActivity
						implements DartsADKCtrl.OnDartsPosReceived{

	private static final String LOG_TAG = "DartsActivity";
	private DartsADKCtrl mAccessory = null;
	private SherlockFragment mCurrentFragment = null;

	private static final String DARTS_FRAG_TAG = "mDartsFragment";
	private static final String ADK_WAIT_FRAG_TAG = "mDartsFragment";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_darts);
	
		replaceToWaitFragment();
		
		mAccessory = new DartsADKCtrl(this);
		mAccessory.setOnConnectionChangeListener(new ADKCtrlBase.OnConnectionChangeListener(){
			@Override
	       	public void onDetouched() {
				// TODO Auto-generated method stub
				replaceToWaitFragment();
				if(MyDebug.DEBUG)Log.d(LOG_TAG, "ADB detouched");
	        }
	        @Override
	        public void onAttached() {
	        	// TODO Auto-generated method stub
	        	replaceToDartFragment();
				if(MyDebug.DEBUG)Log.d(LOG_TAG, "ADB atached");
        	}
        });			
		mAccessory.setOnDartsPosReceived(this);
	}
	private void replaceToWaitFragment(){
		mCurrentFragment = ADKWaitFragment.newInstance();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.layout_base, mCurrentFragment, ADK_WAIT_FRAG_TAG);
		ft.commit();		
	}
	private void replaceToDartFragment(){
		mCurrentFragment = DartsFragment.newInstance();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.layout_base, mCurrentFragment, DARTS_FRAG_TAG);
		ft.commit();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAccessory.closeAccessory();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mAccessory.unregisterReceiver();		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mAccessory.registerReceiver();
		if (!mAccessory.isAccessoryOpen()) {
			mAccessory.findAndOpenAccessory();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.darts, menu);
		return true;
	}
	@Override
	public void onDartsPosReceived(byte dartsPos) {
		// TODO Auto-generated method stub
		if(mCurrentFragment != null && mCurrentFragment instanceof DartsFragment){
			DartsFragment frag = (DartsFragment)mCurrentFragment;
			frag.setDartsActivePos(dartsPos);
		}
	}

}
