package will.st.bluetooth.robot.controller;

import will.st.bluetooth.robotcontroller.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CrabToyFragment extends Fragment {
	int fragVal;
	
	static CrabToyFragment init(int val) {
		CrabToyFragment crabToyFrag = new CrabToyFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		crabToyFrag.setArguments(args);
		return crabToyFrag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_crab_toy, container,
				false);
		return layoutView;
	}
	
	
}