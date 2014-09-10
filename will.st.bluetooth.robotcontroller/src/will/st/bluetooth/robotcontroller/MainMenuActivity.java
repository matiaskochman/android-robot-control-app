package will.st.bluetooth.robotcontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {

	public static final String SELECTED_CONTROLLER_FRAGMENT = "will.st.bluetooth.robot."
			+ "controller.CONTROLLER";
	private static final String TAG = "ROBOT_CONTROLLER";
	private ImageButton xButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In MainMenuActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_main_menu);
		xButton = (ImageButton) findViewById(R.id.x_button);
		xButton.setOnClickListener(new XButtonListener());
	}

	class XButtonListener implements OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "...In MainMenuActivity startXController(View view)...");

			Intent controllerActivityIntent = new Intent(
					getApplicationContext(), ControllerActivity.class);
			// This will inform the next activity which ControllerFragment to
			// use.
			controllerActivityIntent.putExtra(SELECTED_CONTROLLER_FRAGMENT,
					"XControllerFragment");

			Log.d(TAG, "In MainMenuActivity startXController(View view),"
					+ " starting ControllerActivity.");
			startActivity(controllerActivityIntent);
		}
	}
}
