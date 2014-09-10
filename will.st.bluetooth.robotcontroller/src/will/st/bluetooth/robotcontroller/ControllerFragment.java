package will.st.bluetooth.robotcontroller;

import java.io.IOException;
import java.io.OutputStream;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class ControllerFragment extends Fragment {

	private static final String TAG = "ROBOT_CONTROLLER";
	
	// Used to send data via Bluetooth.
	protected OutputStream outStream = null;
	
	public ControllerFragment(OutputStream outStream) {
		this.outStream = outStream;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "...In ControllerFragment onPause()...");
		outStream = null;
		
	}
	
	// Sends a String over Bluetooth as a series of bytes.
	protected void sendData(String message) {
		Log.d(TAG, "...In ControllerFragment sendData()...");
		byte[] msgBuffer = message.getBytes();
		try {
			outStream.write(msgBuffer);
		} catch (IOException e) {
			Log.e(TAG,
					"In sendData(String message) an exception occurred during write",
					e);
		}
	}
}
