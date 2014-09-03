package will.st.bluetooth.robotcontroller;

import java.io.OutputStream;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class XControllerFragment extends ControllerFragment {

	private static final String TAG = "bluetooth1";

	ImageButton forwardsButton;
	ImageButton reverseButton;
	ImageButton leftButton;
	ImageButton rightButton;

	public XControllerFragment(OutputStream outStream) {
		super(outStream);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "...In XControllerFragment "
				+ "onCreate(Bundle savedInstanceState)...");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "...In XControllerFragment onCreateView()...");
		View rootView = inflater.inflate(R.layout.fragment_x_controller,
				container, false);

		forwardsButton = (ImageButton) rootView.findViewById(R.id.forwards);
		forwardsButton.setOnTouchListener(new forwardsListener());

		reverseButton = (ImageButton) rootView.findViewById(R.id.reverse);
		reverseButton.setOnTouchListener(new reverseListener());

		rightButton = (ImageButton) rootView.findViewById(R.id.right);
		rightButton.setOnTouchListener(new rightListener());

		leftButton = (ImageButton) rootView.findViewById(R.id.left);
		leftButton.setOnTouchListener(new leftListener());

		return rootView;
	}

	// Send directional commands to remote device. Data is sent when a button is
	// pressed and when it is released.
	class forwardsListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "...In forwardsListener onTouch"
					+ "(View view, MotionEvent event)...");
			view.performClick();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				sendData("1");
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				sendData("0");
			}
			return true;
		}
	}

	class reverseListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "...In forwardsListener onTouch"
					+ "(View view, MotionEvent event)...");
			view.performClick();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				sendData("2");
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				sendData("0");
			}
			return true;
		}
	}

	class rightListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "...In forwardsListener onTouch"
					+ "(View view, MotionEvent event)...");
			view.performClick();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				sendData("3");
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				sendData("0");
			}
			return true;
		}
	}

	class leftListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "...In forwardsListener onTouch"
					+ "(View view, MotionEvent event)...");
			view.performClick();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				sendData("4");
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				sendData("0");
			}
			return true;
		}
	}
}
