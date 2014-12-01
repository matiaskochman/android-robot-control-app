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

import will.st.bluetooth.robot.controller.MainMenuActivity;
import will.st.bluetooth.robot.controller.ToyMenuActivity;
import will.st.bluetooth.robotcontroller.R;
import android.content.Intent;

@Config(emulateSdk = 18, manifest = "../AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MainMenuActivityTest {

	private final ActivityController<MainMenuActivity> mActivityController = Robolectric
			.buildActivity(MainMenuActivity.class);

	@Test
	public void testGoButtonClickShouldFireCorrectIntent() {
		// Create, start and resume activity under test.
		MainMenuActivity activity = mActivityController.create().start().resume().get();
		// Click button to start next activity.
		activity.findViewById(R.id.go_button).performClick();

		ShadowActivity shadowActivity = Robolectric.shadowOf_(activity);

		Intent expectedIntent = new Intent(activity.getApplicationContext(),
				ToyMenuActivity.class);
		assertThat(shadowActivity.getNextStartedActivity(),
				equalTo(expectedIntent));
	}
}