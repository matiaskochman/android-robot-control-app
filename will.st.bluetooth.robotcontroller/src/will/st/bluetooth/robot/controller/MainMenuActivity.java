package will.st.bluetooth.robot.controller;

import will.st.bluetooth.robotcontroller.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {

	private static final String TAG = "ROBOT_CONTROLLER";
	
	private ImageButton mGoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In MainMenuActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_main_menu);
		mGoButton = (ImageButton) findViewById(R.id.go_button);
		mGoButton.setOnClickListener(new GoButtonListener());
	}

	class GoButtonListener implements OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "...In GoButtonListener, onClick()...");

			Intent controllerActivityIntent = new Intent(
					getApplicationContext(), ToyMenuActivity.class);

			Log.d(TAG, "In GoButtonListener, onClick(),"
					+ " starting ControllerActivity.");
			startActivity(controllerActivityIntent);
		}
	}
}
