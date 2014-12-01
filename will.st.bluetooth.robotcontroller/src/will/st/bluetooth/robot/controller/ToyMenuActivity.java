package will.st.bluetooth.robot.controller;

import will.st.bluetooth.robotcontroller.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ToyMenuActivity extends FragmentActivity {

	public static final String SELECTED_CONTROLLER_FRAGMENT = "will.st.bluetooth.robot."
			+ "controller.CONTROLLER";
	private static final String TAG = "ROBOT_CONTROLLER";

	static final int ITEMS = 2;
	ToyPagerAdapter mAdapter;
	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In ToyMenuActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_toy_menu);
		mAdapter = new ToyPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

	public static class ToyPagerAdapter extends FragmentPagerAdapter {
		public ToyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return TwoWheelToyFragment.init(position);
			case 1:
				return CrabToyFragment.init(position);
			default:
				return CrabToyFragment.init(position);
			}
		}
	}
}
