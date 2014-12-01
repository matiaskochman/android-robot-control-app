package will.st.bluetooth.robot.controller;

import will.st.bluetooth.robotcontroller.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ToyMenuActivity extends FragmentActivity {

	public static final String SELECTED_CONTROLLER_FRAGMENT = "will.st.bluetooth.robot."
			+ "controller.CONTROLLER";
	private static final String TAG = "ROBOT_CONTROLLER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In ToyMenuActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_toy_menu);
	}
}
