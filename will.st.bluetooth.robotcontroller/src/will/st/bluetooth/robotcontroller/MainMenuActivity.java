package will.st.bluetooth.robotcontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainMenuActivity extends Activity {

	private static final String TAG = "bluetooth1";
	
	// Name for the String which we will send via intent.
	public final static String CONTROLLER_FRAGMENT = "Controller fragment name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In MainMenuActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_main_menu);
	}

	// Called when the user clicks the X image button.
	public void startXController(View view) {
		Log.d(TAG, "...In MainMenuActivity startXController(View view)...");

		Intent controllerActivityIntent = new Intent(this,
				ControllerActivity.class);
		// This will inform the next activity which ControllerFragment to use.
		controllerActivityIntent.putExtra(CONTROLLER_FRAGMENT,
				"will.st.bluetooth.robotcontroller.XControllerFragment");

		Log.d(TAG, "In MainMenuActivity startXController(View view),"
				+ " starting ControllerActivity.");
		startActivity(controllerActivityIntent);

	}
}
