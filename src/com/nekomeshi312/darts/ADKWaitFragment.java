package com.nekomeshi312.darts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class ADKWaitFragment extends SherlockFragment {
	private static final String LOG_TAG = "ADKWaitFragment";

	public static ADKWaitFragment newInstance(){
		ADKWaitFragment fragment = new ADKWaitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(MyDebug.DEBUG)Log.i(LOG_TAG, "onCreateView");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_adk_wait, container, false);       
		return root;
	}

}
