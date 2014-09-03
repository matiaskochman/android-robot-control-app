package will.st.bluetooth.robotcontroller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BtConnectFragment extends Fragment {

	private static final String TAG = "bluetooth1";
	private static final int REQUEST_ENABLE_BT = 1;

	// ControllerActivity which this fragment is attached to.
	private BtConnectionMadeListener parentActivity;

	// UUID and MAC address required to make a Bluetooth connection.
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address;

	// An AsyncTask to make our Bluetooth connection in the background
	private ConnectBtTask connectionThread;
	
	private boolean connectionMade;

	// These are used establish a connection and send data via Bluetooth.
	private BluetoothAdapter btAdapter;
	private BluetoothSocket btSocket;
	private OutputStream outStream;

	// Container Activity must implement this callback interface.
	public interface BtConnectionMadeListener {
		public void onConnectionMade(BluetoothSocket btSocket,
				OutputStream outStream);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "...In BtConnectFragment"
				+ " onAttach(Activity activity)...");
		// This makes sure that the container activity has implemented
		// the callback interface.
		try {
			Log.d(TAG, "...In BtConnectFragment"
					+ "onAttach(Activity activity), setting callback activity.");
			parentActivity = (BtConnectionMadeListener) activity;
		} catch (ClassCastException e) {
			Log.e(TAG, "In BtConnectFragment onAttach, container activity"
					+ " must implement BtConnectionMadeListener.", e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "...In BtConnectFragment onCreateView...");
		View rootView = inflater.inflate(R.layout.fragment_controller_connect,
				container, false);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "...In BtConnectFragment onResume...");
		connectionMade = false;
		connectionThread = new ConnectBtTask();
		connectionThread.execute();
	}

	public void onPause() {
		super.onPause();
		Log.d(TAG, "...In BtConnectFragment onPause()...");
		// Make sure Bluetooth connection is closed properly if app is paused
		// during connection.
		if (connectionMade == false) {
			closeConnection();
		}
		connectionThread.cancel(true);
	}

	private void closeConnection() {
		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "In closeConnection(),),"
						+ " failed to flush output stream.", e);
			}
			outStream = null;
		}

		Log.d(TAG, "In closeConnection()," + " closing bluetooth socket.");
		if (btSocket != null) {
			try {
				btSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "In closeConnection(),"
						+ " failed to close bluetooth socket.", e);
			}
			btSocket = null;
		}
	}

	public BtConnectionMadeListener getParentActivity() {
		return parentActivity;
	}

	private class ConnectBtTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			Log.d(TAG, "...In doInBackground(Void... params)...");

			if (btAdapter == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " getting local Bluetooth adapter.");
				btAdapter = BluetoothAdapter.getDefaultAdapter();
				int checkResult = checkDeviceSupportsBluetooth();
				if (checkResult == -1)
					return "Bluetooth not supported";
			}

			if (isCancelled())
				return null;

			if (btSocket == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " enabling Bluetooth.");
				enableBluetooth();
			}

			if (isCancelled())
				return null;

			if (address == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " getting remote device MAC address.");
				obtainRemoteDeviceMacAddress();
			}

			if (isCancelled())
				return null;

			if (btSocket == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " setting up Bluetooth socket for remote device.");
				BluetoothDevice device = btAdapter.getRemoteDevice(address);
				try {
					btSocket = createBluetoothSocket(device);
				} catch (IOException e) {
					Log.e(TAG, "In doInBackground(Void... params) "
							+ "and socket creation failed.", e);
				}
			}

			if (isCancelled()) {
				closeConnection();
				return null;
			}

			if (outStream == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " establishing Bluetooth connection.");
				int connectionResult = establishBtConnection();
				if (connectionResult == -1)
					return "Connection Failed";
				if (isCancelled()) {
					closeConnection();
					return null;
				}

				Log.d(TAG, "In doInBackground(Void... params),"
						+ " creating on output stream.");
				createOutputStream();
			}

			if (isCancelled()) {
				closeConnection();
				return null;
			}

			Log.d(TAG, "In doInBackground(Void... params),"
					+ " bluetooth setup complete.");
			return "Connection made";

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "In onPostExecute(String result)");

			switch (result) {
			case "Connection Failed":
				Log.d(TAG,
						"In onPostExecute(String result), connection failed.");
				onConnectionFailed();
				break;
			case "Connection made":
				Log.d(TAG, "In onPostExecute(String result), connection made!");
				connectionMade = true;
				parentActivity.onConnectionMade(btSocket, outStream);
				break;
			case "Bluetooth not supported":
				Log.d(TAG,
						"In onPostExecute(String result), Bluetooth not supported");
				bluetoothNotSupported();
				break;
			}
		}

		private int checkDeviceSupportsBluetooth() {
			Log.d(TAG, "In checkDeviceSupportsBluetooth(), checking"
					+ "if device supports bluetooth...");
			if (btAdapter == null) {
				Log.d(TAG, "In checkDeviceSupportsBluetooth(), device"
						+ "does not support bluetooth");
				return -1;
			}
			return 0;
		}

		private void enableBluetooth() {
			Log.d(TAG, "In enableBluetooth() and prompting user"
					+ "to enable bluetooth if necessary...");
			if (!btAdapter.isEnabled()) {
				// Create and start an intent for user to enable Bluetooth, wait
				// for it to be enabled before moving on.
				Log.d(TAG, "In enableBluetooth(),"
						+ " prompting user to enable Bluetooth");
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				while (!btAdapter.isEnabled()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}

		private void obtainRemoteDeviceMacAddress() {
			if (address == null) {
				Log.d(TAG, "...In obtainRemoteDeviceMacAddress()...");
				Set<BluetoothDevice> pairedDevices = btAdapter
						.getBondedDevices();

				Log.d(TAG, "In obtainRemoteDeviceMacAddress() and getting"
						+ "remote device MAC address");
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						if (device.getName().equals("fish")) {
							address = device.getAddress();
						} else {
							Log.d(TAG, "In obtainRemoteDeviceMacAddress() and"
									+ "remote device MAC address  not found...");
						}
					}
				}
				// Stop resource intensive device discovery.
				btAdapter.cancelDiscovery();
			}
		}

		private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
				throws IOException {
			Log.d(TAG, "...In createBluetoothSocket(BluetoothDevice device)...");
			// Create a Bluetooth socket using MAC address and serial port UUID
			// of remote device.
			if (Build.VERSION.SDK_INT >= 10) {
				try {
					final Method m = device.getClass().getMethod(
							"createInsecureRfcommSocketToServiceRecord",
							new Class[] { UUID.class });
					return (BluetoothSocket) m.invoke(device, MY_UUID);
				} catch (Exception e) {
					Log.e(TAG,
							"In createBluetoothSocket(BluetoothDevice device), could"
									+ " not create Insecure RFComm Connection",
							e);
				}
			}
			return device.createRfcommSocketToServiceRecord(MY_UUID);
		}

		private int establishBtConnection() {
			Log.d(TAG, "In establishBtConnection() and connecting");
			try {
				btSocket.connect();
			} catch (IOException e) {
				Log.e(TAG, "In establishBtConnection() and Connection failed",
						e);
				return -1;
			}
			return 0;
		}

		private void createOutputStream() {
			Log.d(TAG, "In createOutputStream()  and creating data output"
					+ "stream to remote device...");
			try {
				outStream = btSocket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "In createOutputStream() and"
						+ "output stream creation failed:", e);
			}
		}

		private void onConnectionFailed() {
			Log.d(TAG, "..In onConnectFailed()...");
			ControllerActivity parentActivity = (ControllerActivity) getParentActivity();

			Context context = parentActivity.getApplicationContext();
			CharSequence text = "Bluetooth connection failed";
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			// Return to main menu.
			Log.d(TAG, "In onConnectFailed(), finishing activity");
			parentActivity.finish();
		}

		private void bluetoothNotSupported() {
			Log.d(TAG, "..In bluetoothNotSupported()...");
			ControllerActivity parentActivity = (ControllerActivity) getParentActivity();

			Context context = parentActivity.getApplicationContext();
			CharSequence text = "Bluetooth not supported by device.";
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

			// Return to main menu
			Log.d(TAG, "In bluetoothNotSupported(), finishing activity");
			parentActivity.finish();
		}
	}
}
