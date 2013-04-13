package com.nekomeshi312.darts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DartsPartsBull extends DartsPartsBase {
	private static final int BULL_BASE_SCORE = 25;

	//public static final int POS_OFF = 0;
	public static final int POS_OUTER_BULL = 1;
	public static final int POS_INNER_BULL = 2;
	private int mShowState = POS_OFF;
	
	private Paint mPaintOuterOn = new Paint();
	private Paint mPaintOuterOff = new Paint();
	private Paint mPaintInnerOn = new Paint();
	private Paint mPaintInnerOff = new Paint();

	DartsPartsBull(Context context, int width, int height) {
		super(context, width, height);
		// TODO Auto-generated constructor stub
		final int colorOuterOn = mContext.getResources().getColor(R.color.target1_on);
		final int colorOuterOff = mContext.getResources().getColor(R.color.target1_off);
		final int colorInnerOn = mContext.getResources().getColor(R.color.target0_on);
		final int colorInnerOff = mContext.getResources().getColor(R.color.target0_off);
		
		mPaintOuterOn.setAntiAlias(true);
		mPaintOuterOn.setColor(colorOuterOn);
		mPaintOuterOff.setAntiAlias(true);
		mPaintOuterOff.setColor(colorOuterOff);
		mPaintInnerOn.setAntiAlias(true);
		mPaintInnerOn.setColor(colorInnerOn);
		mPaintInnerOff.setAntiAlias(true);
		mPaintInnerOff.setColor(colorInnerOff);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mShowState == POS_OUTER_BULL){
			canvas.drawCircle(mCenterX, mCenterY, (float) (0.5*mDrawScale*0.1), mPaintOuterOn);
		}
		else{
			canvas.drawCircle(mCenterX, mCenterY, (float) (0.5*mDrawScale*0.1), mPaintOuterOff);
		}
		if(mShowState == POS_INNER_BULL){
			canvas.drawCircle(mCenterX, mCenterY, (float) (0.5*mDrawScale*0.05), mPaintInnerOn);
		}
		else{
			canvas.drawCircle(mCenterX, mCenterY, (float) (0.5*mDrawScale*0.05), mPaintInnerOff);
		}

	}
	@Override
	public void setShowState(int pos) {
		// TODO Auto-generated method stub
		mShowState = pos;
	}

	@Override
	public int getScore(int pos) {
		// TODO Auto-generated method stub
		return BULL_BASE_SCORE*mShowState;
	}

}
