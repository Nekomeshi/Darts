package com.nekomeshi312.darts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class DartsPartsFanShape extends DartsPartsBase {
	//扇型エリアのスコア。一番上から時計回りに
	private static final int FAN_AREA_SCORE[] = {20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 
												3, 19, 7, 16, 8, 11, 14, 9, 12, 5};
	//public static final int POS_OFF = 0;
	public static final int POS_NORMAL = 1;
	public static final int POS_DOUBLE_RING = 2;
	public static final int POS_TRIPLE_RING = 3;
	private int mShowState = POS_OFF;
	private int mPosAngle = 0;
	private float mArgAngleStart;
	private static final float ARG_SWEEP_ANGLE = 360.0f/20.0f;

	private RectF mRectTripleRing;
	private RectF mRectOuterNormal;
	private RectF mRectDoubleRing;
	private RectF mRectInnerNormal;
	
	private Paint mPaintPosRingOn;
	private Paint mPaintPosRingOff;
	private Paint mPaintPosNormalOn;
	private Paint mPaintPosNormalOff;

	private static final float TRIPLE_RING_SCALE = 0.9f;
	private static final float OUTER_NORMAL_SCALE = 0.8f;
	private static final float DOUBLE_RING_SCALE = 0.55f;
	private static final float INNER_NORMAL_SCALE = 0.45f;
	
	DartsPartsFanShape(Context context, int width, int height) {
		this(context, width, height, 0);
		// TODO Auto-generated constructor stub
	}
	DartsPartsFanShape(Context context, int width, int height, int posAngle) {
		super(context, width, height);
		// TODO Auto-generated constructor stub
		mPosAngle = posAngle;
		mArgAngleStart = (float) (-90.0 + ARG_SWEEP_ANGLE * (posAngle - 0.5)) ;

		int colorOn[] = {mContext.getResources().getColor(R.color.target0_on), 
							mContext.getResources().getColor(R.color.target1_on),
								mContext.getResources().getColor(R.color.target2_on)
						};
		int colorOff[] = {mContext.getResources().getColor(R.color.target0_off), 
								mContext.getResources().getColor(R.color.target1_off),
									mContext.getResources().getColor(R.color.target2_off)
						};
		final int colorTableRing[] = {2, 1, 0, 1};
		final int colorTableNormal[] = {0, 2, 1, 2};
		mPaintPosRingOn = new Paint();
		mPaintPosRingOn.setAntiAlias(true);
		mPaintPosRingOn.setColor(colorOn[colorTableRing[posAngle%4]]);
		mPaintPosRingOff = new Paint();
		mPaintPosRingOff.setAntiAlias(true);
		mPaintPosRingOff.setColor(colorOff[colorTableRing[posAngle%4]]);
		mPaintPosNormalOn = new Paint();
		mPaintPosNormalOn.setAntiAlias(true);
		mPaintPosNormalOn.setColor(colorOn[colorTableNormal[posAngle%4]]);
		mPaintPosNormalOff = new Paint();
		mPaintPosNormalOff.setAntiAlias(true);
		mPaintPosNormalOff.setColor(colorOff[colorTableNormal[posAngle%4]]);
	
		float halfwidth = (float) (mDrawScale*0.5);
		mRectTripleRing = new RectF( mCenterX - halfwidth*TRIPLE_RING_SCALE, 
									mCenterY - halfwidth*TRIPLE_RING_SCALE, 
									mCenterX + halfwidth*TRIPLE_RING_SCALE, 
									mCenterY + halfwidth*TRIPLE_RING_SCALE); 
		mRectOuterNormal = new RectF( mCenterX - halfwidth*OUTER_NORMAL_SCALE, 
									mCenterY - halfwidth*OUTER_NORMAL_SCALE, 
									mCenterX + halfwidth*OUTER_NORMAL_SCALE, 
									mCenterY + halfwidth*OUTER_NORMAL_SCALE); 
		mRectDoubleRing = new RectF( mCenterX - halfwidth*DOUBLE_RING_SCALE, 
									mCenterY - halfwidth*DOUBLE_RING_SCALE, 
									mCenterX + halfwidth*DOUBLE_RING_SCALE, 
									mCenterY + halfwidth*DOUBLE_RING_SCALE); 
		mRectInnerNormal = new RectF( mCenterX - halfwidth*INNER_NORMAL_SCALE, 
									mCenterY - halfwidth*INNER_NORMAL_SCALE, 
									mCenterX + halfwidth*INNER_NORMAL_SCALE, 
									mCenterY + halfwidth*INNER_NORMAL_SCALE); 
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mShowState == POS_TRIPLE_RING){
			canvas.drawArc(mRectTripleRing, mArgAngleStart, ARG_SWEEP_ANGLE, true, mPaintPosRingOn);
		}
		else{
			canvas.drawArc(mRectTripleRing, mArgAngleStart, ARG_SWEEP_ANGLE, true, mPaintPosRingOff);
		}
		if(mShowState == POS_NORMAL){
			canvas.drawArc(mRectOuterNormal, mArgAngleStart, ARG_SWEEP_ANGLE, true,mPaintPosNormalOn);
		}
		else{
			canvas.drawArc(mRectOuterNormal, mArgAngleStart, ARG_SWEEP_ANGLE, true,mPaintPosNormalOff);
		}
		if(mShowState == POS_DOUBLE_RING){
			canvas.drawArc(mRectDoubleRing, mArgAngleStart, ARG_SWEEP_ANGLE, true, mPaintPosRingOn);
		}
		else{
			canvas.drawArc(mRectDoubleRing, mArgAngleStart, ARG_SWEEP_ANGLE, true, mPaintPosRingOff);
		}
		if(mShowState == POS_NORMAL){
			canvas.drawArc(mRectInnerNormal, mArgAngleStart, ARG_SWEEP_ANGLE, true,mPaintPosNormalOn);
		}
		else{
			canvas.drawArc(mRectInnerNormal, mArgAngleStart, ARG_SWEEP_ANGLE, true,mPaintPosNormalOff);
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
		return FAN_AREA_SCORE[mPosAngle]*mShowState;
	}
}
