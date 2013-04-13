package com.nekomeshi312.darts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class DartsFragment extends SherlockFragment {


	private static final String LOG_TAG = "DartsFragment";
	private Activity mParentActivity = null;
	private DartsView mDartsView = null;
	private byte mCurrentActiveDartsPos = DartsADKCtrl.ADK_NO_ACTIVE;
	
	public static DartsFragment newInstance(){
		DartsFragment fragment = new DartsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mParentActivity = activity;
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
	public View onCreateView(LayoutInflater inflater, 
							ViewGroup container,
							Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(MyDebug.DEBUG)Log.i(LOG_TAG, "onCreateView");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_darts, container, false);

		return root;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDartsView = (DartsView)view.findViewById(R.id.darts_view);
        Button clearButton = (Button) view.findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDartsActivePos((byte)0x00);
			}
		});
		if(savedInstanceState != null){
			byte dartsPos = (byte) (savedInstanceState.getByte("LastDartsPos") & 0xff);
			setDartsActivePos(dartsPos);
		}
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void setDartsActivePos(byte dartsPos){
		if(mDartsView != null){
			mDartsView.setDartsActivePos(dartsPos);	
			mDartsView.invalidate();
		}
		mCurrentActiveDartsPos = dartsPos;
	}
}

