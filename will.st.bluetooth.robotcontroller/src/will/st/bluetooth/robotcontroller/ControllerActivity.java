package will.st.bluetooth.robotcontroller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ControllerActivity extends FragmentActivity implements
		BtConnectFragment.BtConnectionMadeListener {

	private final static String TAG = "ROBOT_CONTROLLER";
	// Tells us which controller fragment to use
	private static String selectedControllerFragment;

	private ControllerFragment controlFrag;
	private BtConnectFragment connectFrag;
	// These are used to send data via Bluetooth.
	private OutputStream outStream;
	private BluetoothSocket btSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In ControllerActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_controller);

		selectedControllerFragment = getIntent().getStringExtra(
				MainMenuActivity.SELECTED_CONTROLLER_FRAGMENT);

		connectFrag = new BtConnectFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "...In ControllerActivity onResume()...");
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.controller_activity_frame_layout, connectFrag,
						"connect").commit();
		getSupportFragmentManager().executePendingTransactions();

	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d(TAG, "...In ControllerActivity onPause()...");

		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to flush output stream.", e);
			}
			outStream = null;
		}

		if (btSocket != null) {
			try {
				btSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to close socket.", e);
			}
			btSocket = null;
			finish();
		}
	}

	// Call to swap our BtConnectFragment for a ControllerFragment once
	// connectFrag establishes a connection.
	public void onConnectionMade(BluetoothSocket btSocket,
			OutputStream outStream) {
		Log.d(TAG, "...In ControllerActivity onConnectionMade()...");

		this.btSocket = btSocket;
		this.outStream = outStream;

		controlFrag = ControllerFragmentFactory.createControllerFragment(
				selectedControllerFragment, outStream);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.controller_activity_frame_layout, controlFrag,
						"control").commit();
	}

	public void setConnectFrag(BtConnectFragment connectFrag) {
		this.connectFrag = connectFrag;
	}

	public static void setSpecifiedControllerFragment(
			String specifiedControllerFragment) {
		ControllerActivity.selectedControllerFragment = specifiedControllerFragment;
	}
}