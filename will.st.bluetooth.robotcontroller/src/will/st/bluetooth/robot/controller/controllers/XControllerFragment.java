package will.st.bluetooth.robot.controller.controllers;

import java.io.OutputStream;

import will.st.bluetooth.robotcontroller.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class XControllerFragment extends ControllerFragment {

	private static final String TAG = "ROBOT_CONTROLLER";
	
	private ImageButton mForwardsButton;
	private ImageButton mReverseButton;
	private ImageButton mLeftButton;
	private ImageButton mRightButton;

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

		mForwardsButton = (ImageButton) rootView.findViewById(R.id.forwards);
		mForwardsButton.setOnTouchListener(new forwardsListener());

		mReverseButton = (ImageButton) rootView.findViewById(R.id.reverse);
		mReverseButton.setOnTouchListener(new reverseListener());

		mRightButton = (ImageButton) rootView.findViewById(R.id.right);
		mRightButton.setOnTouchListener(new rightListener());

		mLeftButton = (ImageButton) rootView.findViewById(R.id.left);
		mLeftButton.setOnTouchListener(new leftListener());

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
			Log.d(TAG, "...In reverseListener onTouch"
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
			Log.d(TAG, "...In rightListener onTouch"
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
			Log.d(TAG, "...In leftListener onTouch"
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
