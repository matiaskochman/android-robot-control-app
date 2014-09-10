package will.st.bluetooth.robotcontroller;

import java.io.IOException;
import java.io.OutputStream;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class ControllerFragment extends Fragment {

	private static final String TAG = "ROBOT_CONTROLLER";
	protected OutputStream mOutputStream = null;
	
	public ControllerFragment(OutputStream outputStream) {
		this.mOutputStream = outputStream;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "...In ControllerFragment onPause()...");
		mOutputStream = null;
		
	}
	
	// Sends a String over Bluetooth as a series of bytes.
	protected void sendData(String message) {
		Log.d(TAG, "...In ControllerFragment sendData()...");
		byte[] msgBuffer = message.getBytes();
		try {
			mOutputStream.write(msgBuffer);
		} catch (IOException e) {
			Log.e(TAG,
					"In sendData(String message) an exception occurred during write",
					e);
		}
	}
}
