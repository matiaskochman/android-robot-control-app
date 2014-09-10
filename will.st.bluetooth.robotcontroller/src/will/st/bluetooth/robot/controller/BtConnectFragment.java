package will.st.bluetooth.robot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import will.st.bluetooth.robotcontroller.R;

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

	private static final String TAG = "ROBOT_CONTROLLER";
	private static final int REQUEST_ENABLE_BT = 1;

	private BtConnectionMadeListener parentActivity;

	// UUID and MAC address required to make a Bluetooth connection.
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address;

	// An AsyncTask to have our Bluetooth connection in the background.
	private BtConnectTask connectionThread;

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
		try {
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
		connectionThread = new BtConnectTask();
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

	private class BtConnectTask extends AsyncTask<Void, Void, String> {
		private Exception connectionFailedException = null;

		@Override
		protected String doInBackground(Void... params) {

			Log.d(TAG, "...In doInBackground(Void... params)...");

			if (btAdapter == null) {
				try {
					checkDeviceSupportsBluetooth();
				} catch (ConnectionFailedException ex) {
					connectionFailedException = ex;
					return null;
				}
				if (isCancelled())
					return null;
			}

			Log.d(TAG, "In doInBackground(Void... params),"
					+ " enabling Bluetooth.");
			enableBluetooth();

			if (isCancelled())
				return null;

			if (address == null) {
				Log.d(TAG, "In doInBackground(Void... params),"
						+ " getting remote device MAC address.");
				try {
					obtainRemoteDeviceMacAddress();
				} catch (ConnectionFailedException ex) {
					connectionFailedException = ex;
					return null;
				}
				if (isCancelled())
					return null;

			}

			Log.d(TAG, "In doInBackground(Void... params),"
					+ " setting up Bluetooth socket for remote device.");
			BluetoothDevice device = btAdapter.getRemoteDevice(address);
			try {
				btSocket = createBluetoothSocket(device);
			} catch (ConnectionFailedException ex) {
				connectionFailedException = ex;
				return null;
			}
			if (isCancelled()) {
				closeConnection();
				return null;
			}

			Log.d(TAG, "In doInBackground(Void... params),"
					+ " establishing Bluetooth connection.");
			try {
				int connectionResult = establishBtConnection();
			} catch (ConnectionFailedException ex) {
				connectionFailedException = ex;
				return null;
			}

			if (isCancelled()) {
				closeConnection();
				return null;
			}

			Log.d(TAG, "In doInBackground(Void... params),"
					+ " creating on output stream.");
			try {
				createOutputStream();
			} catch (ConnectionFailedException ex) {
				connectionFailedException = ex;
				return null;
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

			if (connectionFailedException != null) {
				ControllerActivity parentActivity = (ControllerActivity) getParentActivity();

				Context context = parentActivity.getApplicationContext();
				CharSequence text = connectionFailedException.getMessage();
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				// Return to main menu.
				Log.d(TAG,
						"In onPostExecute(), connection failed so finishing activity");
				parentActivity.finish();
			}

			if (result != null) {
				connectionMade = true;
				parentActivity.onConnectionMade(btSocket, outStream);
			}
		}

		private int checkDeviceSupportsBluetooth()
				throws ConnectionFailedException {
			Log.d(TAG, "In checkDeviceSupportsBluetooth(), checking"
					+ "if device supports bluetooth...");
			btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter == null) {
				throw new ConnectionFailedException(
						"Device does not support Bluetooth");
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

				try {
					Log.d(TAG, "In enableBluetooth(), going to sleep");
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}

		private void obtainRemoteDeviceMacAddress()
				throws ConnectionFailedException {

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
						}
					}
					if (address == null) {
						Log.d(TAG, "In obtainRemoteDeviceMacAddress() and"
								+ "remote device MAC address  not found...");
						throw new ConnectionFailedException("Connection Failed");
					}
				}
				// Stop resource intensive device discovery.
				btAdapter.cancelDiscovery();
			}
		}

		private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
				throws ConnectionFailedException {
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
					throw new ConnectionFailedException("Connection Failed");
				}
			}
			
			try {
				return device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException ex) {
				throw new ConnectionFailedException("Connection failed");
			}
		}

		private int establishBtConnection() throws ConnectionFailedException {
			Log.d(TAG, "In establishBtConnection() and connecting");
			try {
				btSocket.connect();
			} catch (IOException e) {
				Log.e(TAG, "In establishBtConnection() and Connection failed",
						e);
				throw new ConnectionFailedException("Connection Failed");
			}
			return 0;
		}

		private void createOutputStream() throws ConnectionFailedException {
			Log.d(TAG, "In createOutputStream()  and creating data output"
					+ "stream to remote device...");
			try {
				outStream = btSocket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "In createOutputStream() and"
						+ "output stream creation failed:", e);
				throw new ConnectionFailedException("Connection Failed");
			}
		}
	}

	public BtConnectionMadeListener getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(BtConnectionMadeListener parentActivity) {
		this.parentActivity = parentActivity;
	}

}