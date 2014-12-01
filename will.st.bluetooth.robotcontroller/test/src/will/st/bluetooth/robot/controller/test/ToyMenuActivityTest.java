package will.st.bluetooth.robot.controller.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import will.st.bluetooth.robot.controller.ControllerActivity;
import will.st.bluetooth.robot.controller.ToyMenuActivity;
import will.st.bluetooth.robot.controller.TwoWheelToyFragment;
import will.st.bluetooth.robotcontroller.R;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

@Config(emulateSdk = 18, manifest = "../AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ToyMenuActivityTest {
	 	
		private static final String FRAGMENT_TAG = "fragment";

	    private ActivityController<ToyMenuActivity> mActivityController;
	    private FragmentActivity mActivity;
	    private TwoWheelToyFragment mFragment = new TwoWheelToyFragment();

	    /**
	     * Adds the fragment to a new blank activity, thereby fully
	     * initializing its view.
	     */
	    @Test
	    public void testTwoWheelToyButtonShouldFireCorrectIntent() {
	    	// Add a fragment to a new blank activity.
	        mActivityController = Robolectric.buildActivity(ToyMenuActivity.class);
	        mActivity = mActivityController.create().start().visible().get();
	        FragmentManager manager = mActivity.getSupportFragmentManager();
	        manager.beginTransaction()
	        		.add(mFragment, FRAGMENT_TAG).commit();
	        
	        // Now we can proceed to test the button click.
	        mFragment.getView().findViewById(R.id.two_wheel_toy_button).performClick();
	        
	        ShadowActivity shadowActivity = Robolectric.shadowOf_(mActivity);

			Intent expectedIntent = new Intent(mActivity.getApplicationContext(),
					ControllerActivity.class);
			expectedIntent.putExtra(ToyMenuActivity.SELECTED_CONTROLLER_FRAGMENT,
					"XControllerFragment");
			
			assertThat(shadowActivity.getNextStartedActivity(),
					equalTo(expectedIntent));
	    }
}
