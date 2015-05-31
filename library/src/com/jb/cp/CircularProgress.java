package com.jb.cp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jb.circular_progress.R;

public class CircularProgress extends View {
	// background
	private GradientDrawable background;
	// progress
	private CircularProgressDrawable mProgressDrawable;

	private int mPaddingProgress;
	private int mStrokeWidth;
	private int mSize;
	private int mMaxProgress;
	private int mProgress;

	// progress background color
	private int mColorProgress;
	// progress indicator color
	private int mColorIndicator;
	// progress indicator background color
	private int mColorIndicatorBackground;

	@SuppressLint("NewApi")
	public CircularProgress(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	public CircularProgress(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public CircularProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CircularProgress(Context context) {
		super(context);
		init(context, null);
	}

	public void setProgress(int progress) {
		mProgress = progress;
		postInvalidate();
	}
	
	public void smoothToProgress(int progress){
		//TODO
	}

	private void init(Context context, AttributeSet attributeSet) {
		mMaxProgress = 100;
		mProgress = 0;
		initAttributes(context, attributeSet);
	}

	private void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet,
				R.styleable.CircularProgress);
		if (attr == null) {
			return;
		}

		try {
			mPaddingProgress = attr.getDimensionPixelSize(
					R.styleable.CircularProgress_padding_progress, 0);
			mStrokeWidth = attr.getDimensionPixelSize(
					R.styleable.CircularProgress_stroke_width, 4);
			mSize = attr.getDimensionPixelSize(
					R.styleable.CircularProgress_size, 64);

			int blue = getColor(R.color.blue);
			int white = getColor(R.color.white);
			int grey = getColor(R.color.grey);

			mColorProgress = attr.getColor(
					R.styleable.CircularProgress_color_progress, white);
			mColorIndicator = attr.getColor(
					R.styleable.CircularProgress_color_indicator, blue);
			mColorIndicatorBackground = attr.getColor(
					R.styleable.CircularProgress_color_indicator_background,
					grey);

			background = (GradientDrawable) context.getResources()
					.getDrawable(R.drawable.background).mutate();
			background.setStroke(mStrokeWidth, mColorIndicatorBackground);
			background.setColor(mColorProgress);
			background.setCornerRadius(mSize / 2);
			setBackgroundCompat(background);
		} finally {
			attr.recycle();
		}

	}

	/**
	 * Set the View's background. Masks the API changes made in Jelly Bean.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundCompat(Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			setBackground(drawable);
		} else {
			setBackgroundDrawable(drawable);
		}
	}

	protected int getColor(int id) {
		return getResources().getColor(id);
	}

	protected TypedArray getTypedArray(Context context,
			AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawProgress(canvas);
	}

	private void drawProgress(Canvas canvas) {
		if (mProgressDrawable == null) {
			int offset = (getWidth() - getHeight()) / 2;
			int size = getHeight() - mPaddingProgress * 2;
			mProgressDrawable = new CircularProgressDrawable(size,
					mStrokeWidth, mColorIndicator);
			int left = offset + mPaddingProgress;
			mProgressDrawable.setBounds(left, mPaddingProgress, left,
					mPaddingProgress);
		}
		float sweepAngle = (360f / mMaxProgress) * mProgress;
		mProgressDrawable.setSweepAngle(sweepAngle);
		mProgressDrawable.draw(canvas);
	}

}
