package will.st.bluetooth.robot.controller;

import java.io.IOException;
import java.io.OutputStream;

import will.st.bluetooth.robot.controller.controllers.ControllerFragment;
import will.st.bluetooth.robot.controller.controllers.ControllerFragmentFactory;
import will.st.bluetooth.robotcontroller.R;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ControllerActivity extends FragmentActivity implements
		BtConnectFragment.BtConnectionMadeListener {

	private final static String TAG = "ROBOT_CONTROLLER";
	
	// Tells us which controller fragment to use
	private static String sSelectedControllerFragment;

	private ControllerFragment mControllerFragment;
	private BtConnectFragment mBtConnectFragment;
	// These are used to send data via Bluetooth.
	private OutputStream mOutputStream;
	private BluetoothSocket mBluetoothSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In ControllerActivity "
				+ "onCreate(Bundle savedInstanceState)...");
		setContentView(R.layout.activity_controller);

		sSelectedControllerFragment = getIntent().getStringExtra(
				ToyMenuActivity.SELECTED_CONTROLLER_FRAGMENT);

		mBtConnectFragment = BtConnectFragment.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "...In ControllerActivity onResume()...");
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.controller_activity_frame_layout, mBtConnectFragment,
						"connect").commit();
		getSupportFragmentManager().executePendingTransactions();

	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d(TAG, "...In ControllerActivity onPause()...");

		if (mOutputStream != null) {
			try {
				mOutputStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to flush output stream.", e);
			}
			mOutputStream = null;
		}

		if (mBluetoothSocket != null) {
			try {
				mBluetoothSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to close socket.", e);
			}
			mBluetoothSocket = null;
			finish();
		}
	}

	// Call to swap our BtConnectFragment for a ControllerFragment once
	// connectFrag establishes a connection.
	public void onConnectionMade(BluetoothSocket btSocket,
			OutputStream outputStream) {
		Log.d(TAG, "...In ControllerActivity onConnectionMade()...");

		this.mBluetoothSocket = btSocket;
		this.mOutputStream = outputStream;

		mControllerFragment = ControllerFragmentFactory.createControllerFragment(
				sSelectedControllerFragment, outputStream);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.controller_activity_frame_layout, mControllerFragment,
						"control").commit();
	}

	public void setConnectFrag(BtConnectFragment connectFrag) {
		this.mBtConnectFragment = connectFrag;
	}

	public static void setSpecifiedControllerFragment(
			String specifiedControllerFragment) {
		ControllerActivity.sSelectedControllerFragment = specifiedControllerFragment;
	}
}