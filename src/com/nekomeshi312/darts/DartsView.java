package com.nekomeshi312.darts;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DartsView extends View {
	public static final String LOG_TAG = "DartsView";
	
	private DartsPartsOutboard mOutboard = null;
	private DartsPartsFanShape mFanShape[] = null;
	private DartsPartsBull mBull = null;
	private Context mContext = null;
	public DartsView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public DartsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public DartsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(mOutboard != null) mOutboard.draw(canvas);
		if(mFanShape != null) {
			for(int i = 0;i < mFanShape.length;i++) mFanShape[i].draw(canvas);
		}
		if(mBull != null) mBull.draw(canvas);
	}
	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if(MyDebug.DEBUG) Log.d(LOG_TAG, "onShzeChanged width = " + w + " / " + oldw + " heigth = " + h + " / " + oldh);
		
		if(mOutboard == null){
			mOutboard = new DartsPartsOutboard(mContext, w, h);
		}
		else{
			mOutboard.setDrawAreaSize(w, h);
		}
		if(mFanShape == null) {
			mFanShape = new DartsPartsFanShape[20];
			for(int i = 0;i < mFanShape.length;i++) mFanShape[i] = new DartsPartsFanShape(mContext, w, h, i);
		}
		else{
			for(int i = 0;i <mFanShape.length;i++) mFanShape[i].setDrawAreaSize(w, h);
		}
		if(mBull == null){
			mBull = new DartsPartsBull(mContext, w, h);
		}
		else{
			mBull.setDrawAreaSize(w, h);
		}
		invalidate();
	}
	public void setDartsActivePos(byte dartsPos){
		if(mBull == null) return;
		if(mFanShape == null) return;

		if(dartsPos == DartsADKCtrl.ADK_INNER_BULL ||
			dartsPos == DartsADKCtrl.ADK_OUTER_BULL){
			for(int i = 0;i < mFanShape.length;i++){
				mFanShape[i].setShowState(DartsPartsFanShape.POS_OFF);
			}
			if(dartsPos == DartsADKCtrl.ADK_INNER_BULL){
				mBull.setShowState(DartsPartsBull.POS_INNER_BULL);				
			}
			else{
				mBull.setShowState(DartsPartsBull.POS_OUTER_BULL);
			}
		}
		else{
			mBull.setShowState(DartsPartsBull.POS_OFF);
			final byte angle = (byte) ((dartsPos & 0xff) >> 2);
			final byte pos = (byte) (dartsPos & 0x03) ;
			for(byte i = 0;i < mFanShape.length;i++){
				if(i != angle){
					mFanShape[i].setShowState(DartsPartsFanShape.POS_OFF);
				}
				else if(pos == DartsPartsFanShape.POS_NORMAL ||
						pos == DartsPartsFanShape.POS_DOUBLE_RING ||
						pos == DartsPartsFanShape.POS_TRIPLE_RING){
					mFanShape[i].setShowState(pos);
				}
				else{
					mFanShape[i].setShowState(DartsPartsFanShape.POS_OFF);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}

}
