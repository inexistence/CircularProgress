package com.jb.cp;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class CircularProgressDrawable extends Drawable {

	private Paint mPaint;
	private RectF mRectF;
	private Path mPath;

	private float mSweepAngle;
	private float mStartAngle;
	private int mSize;
	private int mStrokeWidth;
	private int mStrokeColor;

	private int mOpacity;

	public CircularProgressDrawable(int size, int strokeWidth, int strokeColor) {
		mSize = size;
		mStrokeWidth = strokeWidth;
		mStrokeColor = strokeColor;
		mStartAngle = -90;
		mSweepAngle = 0;
		mOpacity = 1;
	}

	@Override
	public void draw(Canvas canvas) {
		final Rect bounds = getBounds();

		if (mPath == null) {
			mPath = new Path();
		}
		mPath.reset();
		mPath.addArc(getRect(), mStartAngle, mSweepAngle);
		mPath.offset(bounds.left, bounds.top);
		canvas.drawPath(mPath, getPaint());
	}

	@Override
	public void setAlpha(int alpha) {
		getPaint().setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		getPaint().setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return mOpacity;
	}

	public void setOpacity(int opacity) {
		mOpacity = opacity;
	}

	public int getSize() {
		return mSize;
	}

	public void setSweepAngle(float sweepAngle) {
		mSweepAngle = sweepAngle;
	}

	private Paint getPaint() {
		if (mPaint == null) {
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mStrokeWidth);
			mPaint.setColor(mStrokeColor);
		}
		return mPaint;
	}

	private RectF getRect() {
		if (mRectF == null) {
			int index = mStrokeWidth / 2;
			mRectF = new RectF(index, index, getSize() - index, getSize()
					- index);
		}
		return mRectF;
	}

}
