package com.nekomeshi312.darts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class DartsPartsOutboard extends DartsPartsBase {
	private static final int OUT_BOARD_SCORE = 0;
	//扇型エリアのスコア。一番上から時計回りに
	private static final int FAN_AREA_SCORE[] = {20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 
												3, 19, 7, 16, 8, 11, 14, 9, 12, 5};

	private Paint mPaint = new Paint();
	private Paint mPaintScoreText = new Paint();
	private FontMetrics mScoreFontMetrics;
	DartsPartsOutboard(Context context, int width, int height) {
		super(context, width, height);
		// TODO Auto-generated constructor stub

		int color = mContext.getResources().getColor(R.color.outboard);
	    mPaint.setColor(color);
	    mPaint.setAntiAlias(true);
		color = mContext.getResources().getColor(R.color.outboard_score);	    
	    mPaintScoreText.setColor(color);
	    mPaintScoreText.setAntiAlias(true);
	    mPaintScoreText.setTextSize((float) (mDrawScale/30.0));//サイズはエイヤで
	    mScoreFontMetrics = mPaintScoreText.getFontMetrics();
	}	

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawCircle(mCenterX, mCenterY, (float) (0.5*mDrawScale), mPaint);
		for(int i = 0;i < 20;i++){
			final double angle = (-360.0/20.0*(double)i - 180.0) * Math.PI/180.0;
			final float textCenterX = (float) (mCenterX +  Math.sin(angle)*mDrawScale*0.5*0.95);
			final float textCenterY = (float) (mCenterY +  Math.cos(angle)*mDrawScale*0.5*0.95);
			String textScore = String.valueOf(FAN_AREA_SCORE[i]);
			// 文字列の幅を取得
			final float textWidth = mPaintScoreText.measureText(textScore);
			// 中心にしたいX座標から文字列の幅の半分を引く
			final float baseX = textCenterX - textWidth / 2;
			// 中心にしたいY座標からAscentとDescentの半分を引く
			final float baseY = textCenterY - (mScoreFontMetrics.ascent + mScoreFontMetrics.descent) / 2;
			canvas.drawText(textScore, baseX, baseY, mPaintScoreText);
		}
	}
	@Override
	public void setShowState(int pos) {
		// TODO Auto-generated method stub
		//do nothing.
	}

	@Override
	public int getScore(int pos) {
		// TODO Auto-generated method stub
		return OUT_BOARD_SCORE;
	}

}
