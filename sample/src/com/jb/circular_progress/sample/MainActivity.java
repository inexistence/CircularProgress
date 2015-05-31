package com.jb.circular_progress.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

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
						// mProgress.setInstantProgress(20);
						mProgress.setProgress(100);
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
}
