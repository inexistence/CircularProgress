package com.jb.cp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jb.circular_progress.R;

public class CircularProgress extends View {
	// background
	private GradientDrawable background;
	// progress
	private CircularProgressDrawable mProgressDrawable;
	private CircularAnimatedDrawable mAnimatedDrawable;

	private boolean mIndeterminateProgressMode;

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

	private void init(Context context, AttributeSet attributeSet) {
		mMaxProgress = 100;
		mProgress = 0;
		mPaddingProgress = 0;
		initAttributes(context, attributeSet);
	}

	private void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet,
				R.styleable.CircularProgress);
		if (attr == null) {
			return;
		}

		try {
			// mPaddingProgress = attr.getDimensionPixelSize(
			// R.styleable.CircularProgress_padding_progress, 0);
			mStrokeWidth = attr.getDimensionPixelSize(
					R.styleable.CircularProgress_stroke_width, 4);
			// mSize = attr.getDimensionPixelSize(
			// R.styleable.CircularProgress_size, 64);
			mIndeterminateProgressMode = attr.getBoolean(
					R.styleable.CircularProgress_indeterminate, false);

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
			// background.setCornerRadius(mSize / 2);
			setBackgroundCompat(background);
		} finally {
			attr.recycle();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		mSize = width < height ? width : height;
		if (background != null) {
			background.setCornerRadius(mSize / 2);
		}

		int sizeSpec = MeasureSpec.makeMeasureSpec(mSize, MeasureSpec.EXACTLY);

		super.onMeasure(sizeSpec, sizeSpec);
	}

	/**
	 * instantly move to that value
	 * 
	 * @param progress
	 */
	public void setInstantProgress(int progress) {
		mProgress = progress;
		invalidate();
	}

	public int getProgress() {
		return mProgress;
	}

	private final float DEFAULT_ANIM_SPEED = 0.06f;
	private Animator mAnimator;

	/**
	 * smoothly animate to that value with default speed 0.06f
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		this.setProgress(progress, DEFAULT_ANIM_SPEED);
	}

	/**
	 * smoothly animate to that value
	 * 
	 * @param progress
	 * @param speed
	 *            default speed is 0.06f
	 */
	public void setProgress(int progress, float speed) {
		if (mAnimator != null) {
			mAnimator.cancel();
			mAnimator = null;
		}
		long duration = (long) (Math.abs(progress - mProgress) / speed);
		ValueAnimator progressAnimation = ValueAnimator.ofInt(this.mProgress,
				progress);
		progressAnimation.setDuration(duration);
		progressAnimation
				.setInterpolator(new AccelerateDecelerateInterpolator());
		progressAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						CircularProgress.this.setInstantProgress(value);
					}
				});
		mAnimator = progressAnimation;
		progressAnimation.start();
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

	public boolean isIndeterminateProgressMode() {
		return mIndeterminateProgressMode;
	}

	public void setIndeterminateProgressMode(boolean indeterminateProgressMode) {
		mIndeterminateProgressMode = indeterminateProgressMode;
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
		if (mIndeterminateProgressMode) {
			drawIndeterminateProgress(canvas);
		} else {
			drawProgress(canvas);
		}
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

	private void drawIndeterminateProgress(Canvas canvas) {
		if (mAnimatedDrawable == null) {
			int offset = (getWidth() - getHeight()) / 2;
			mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator,
					mStrokeWidth);
			int left = offset + mPaddingProgress;
			int right = getWidth() - offset - mPaddingProgress;
			int bottom = getHeight() - mPaddingProgress;
			int top = mPaddingProgress;
			mAnimatedDrawable.setBounds(left, top, right, bottom);
			mAnimatedDrawable.setCallback(this);
			mAnimatedDrawable.start();
		} else {
			mAnimatedDrawable.draw(canvas);
		}
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return who == mAnimatedDrawable || super.verifyDrawable(who);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.mProgress = mProgress;
		savedState.mIndeterminateProgressMode = mIndeterminateProgressMode;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			mProgress = savedState.mProgress;
			mIndeterminateProgressMode = savedState.mIndeterminateProgressMode;
			super.onRestoreInstanceState(savedState.getSuperState());
			setProgress(mProgress);
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	static class SavedState extends BaseSavedState {

		private boolean mIndeterminateProgressMode;
		private int mProgress;

		public SavedState(Parcelable parcel) {
			super(parcel);
		}

		private SavedState(Parcel in) {
			super(in);
			mProgress = in.readInt();
			mIndeterminateProgressMode = in.readInt() == 1;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(mProgress);
			out.writeInt(mIndeterminateProgressMode ? 1 : 0);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
