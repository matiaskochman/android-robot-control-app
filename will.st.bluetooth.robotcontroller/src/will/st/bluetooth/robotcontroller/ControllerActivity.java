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

	private final static String TAG = "bluetooth1";
	// Tells us which controller fragment to use
	private static String specifiedControllerFragment;

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
		
		// Receive intent, store the specified ControllerFragment.
		specifiedControllerFragment = getIntent().getStringExtra(
				MainMenuActivity.getFragmentKey());
		
		connectFrag = new BtConnectFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "...In ControllerActivity onResume()...");
		
		// Start and show a BTConnectFragment.
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

		Log.d(TAG, "In ControllerActivity onPause(),"
				+ " Flushing data output stream.");
		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to flush output stream.", e);
			}
			outStream = null;
		}

		Log.d(TAG, "In ControllerActivity onPause(),"
				+ " closing bluetooth socket.");
		if (btSocket != null) {
			try {
				btSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "In ControllerActivity onPause(),"
						+ " failed to close socket.", e);
			}
			btSocket = null;
		}
	}

	// Called to swap our ConnectFragment for a ControllerFragment once 
	// connectFrag establishes a connection.
	public void onConnectionMade(BluetoothSocket btSocket,
			OutputStream outStream) {
		Log.d(TAG, "...In ControllerActivity onConnectionMade()...");
		this.btSocket = btSocket;
		this.outStream = outStream;

		// We need to instantiate a subclass of ControllerFragment, but do not
		// know which subclass until runtime.
		Class<? extends ControllerFragment> controllerFragmentSubclass = null;
		Constructor<?> cons = null;

		// Get the required subclass of ControllerFragment based on
		// information from the intent which started this activity.
		try {
			controllerFragmentSubclass = Class.forName(
					specifiedControllerFragment).asSubclass(
					ControllerFragment.class);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "In ControllerActivity onConnectionMade(),"
					+ " could not find specifeid ControllerFragment.", e);
		}

		// Get this subclass's constructor.
		try {
			cons = controllerFragmentSubclass
					.getConstructor(OutputStream.class);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "In ControllerActivity onConnectionMade(),"
					+ " could not get ControllerFragment constructor.", e);
			e.printStackTrace();
		}

		// Create an instance of this subclass.
		try {
			controlFrag = (ControllerFragment) cons.newInstance(outStream);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			Log.e(TAG, "In ControllerActivity onConnectionMade(),"
					+ " could not instantiate given ControllerFragment.", e);
			e.printStackTrace();
		}

		// Replace the BtConnectionFragment with our instance of the
		// ControllerFragment subclass.
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
		ControllerActivity.specifiedControllerFragment = specifiedControllerFragment;
	}
}