package will.st.bluetooth.robotcontroller.test;

import org.junit.Test;

import will.st.bluetooth.robotcontroller.MainMenuActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ImageButton;

public class MainMenuActivityTest extends
		ActivityUnitTestCase<MainMenuActivity> {
	
	private Intent MainMenuActivityIntent;
	private ImageButton xButton;

	public MainMenuActivityTest() {
		super(MainMenuActivity.class);
	}

	@Test
	protected void setUp() throws Exception {
		super.setUp();
		MainMenuActivityIntent = new Intent(getInstrumentation().getTargetContext(),
				MainMenuActivity.class);
	}

	@Test
	public void testXButtonClickShouldStartAnIntent() {
		startActivity(MainMenuActivityIntent, null, null);
		
		xButton = (ImageButton) getActivity().findViewById(
				will.st.bluetooth.robotcontroller.R.id.x_button);	
		assertNotNull("xButton is null", xButton);
		
		// Start intent to launch next activity.
		xButton.performClick();

		// Check that the intent was made.
		Intent ControllerActivityIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", ControllerActivityIntent);

		// Check that the extra message was passed into the intent
		String extra = ControllerActivityIntent
				.getStringExtra(MainMenuActivity.CONTROLLER_FRAGMENT);
		String expectedExtra = "will.st.bluetooth.robotcontroller.XControllerFragment";
		assertEquals("Wrong extra message given to intent", extra,
				expectedExtra);
	}
}
