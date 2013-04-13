package com.nekomeshi312.darts;

import android.content.Context;
import android.graphics.Canvas;

public abstract class DartsPartsBase {
	private static String LOG_TAG = "DartsPartsBase";
	public static final int POS_OFF = 0;

	protected double mDrawScale = 1.0;
	protected int mCenterX = 0;
	protected int mCenterY = 0;
	protected Context mContext;
	/**
	 * コンストラクタ
	 * @param drawScale 的全体を幅1.0として描画する際に、画面に合わせるためのスケール(例えば的全体の直径を512pixelで描画するなら512)
	 * @param score 的にあたった時のベースとなるスコア
	 */
	DartsPartsBase(Context context, int width, int height){
		mContext = context;
		setDrawAreaSize(width, height);
	}
	public abstract void draw(Canvas canvas);
	public abstract int getScore(int pos);
	public abstract void setShowState(int pos);
	public final void setDrawAreaSize(int width, int height){
		mDrawScale = Math.min(width,  height)*0.9;
		mCenterX = width/2;
		mCenterY = height/2;
	}
}
