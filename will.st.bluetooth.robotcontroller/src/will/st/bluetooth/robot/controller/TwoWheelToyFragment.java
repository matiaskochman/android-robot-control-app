package will.st.bluetooth.robot.controller;

import will.st.bluetooth.robotcontroller.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TwoWheelToyFragment extends Fragment {
	int fragVal;
	ImageButton twoWheelToyButton;

	static TwoWheelToyFragment init(int val) {
		TwoWheelToyFragment twoWheelToyFrag = new TwoWheelToyFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		twoWheelToyFrag.setArguments(args);
		return twoWheelToyFrag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_two_wheel_toy, container,
				false);
		twoWheelToyButton = (ImageButton) layoutView.findViewById(R.id.two_wheel_toy_button);
		twoWheelToyButton.setOnClickListener(new TwoWheelToyButtonListener());
		return layoutView;
	}
	
	class TwoWheelToyButtonListener implements OnClickListener {
		public void onClick(View v) {

			Intent controllerActivityIntent = new Intent(
					getActivity().getApplicationContext(), ControllerActivity.class);
			// This will inform the next activity which ControllerFragment to
			// use.
			controllerActivityIntent.putExtra(ToyMenuActivity.SELECTED_CONTROLLER_FRAGMENT,
					"XControllerFragment");
			startActivity(controllerActivityIntent);
		}
	}
}
