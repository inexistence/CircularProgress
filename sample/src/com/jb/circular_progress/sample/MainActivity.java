package com.jb.circular_progress.sample;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jb.cp.CircularProgress;

public class MainActivity extends ActionBarActivity {

	CircularProgress mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgress = (CircularProgress) findViewById(R.id.cp);
		findViewById(R.id.btn_sim_progress).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						simulateSuccessProgress(mProgress);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void simulateSuccessProgress(final CircularProgress cp) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(1500);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						cp.setProgress(value);
					}
				});
		widthAnimation.start();
	}
}
