package will.st.bluetooth.robotcontroller.test;

import org.junit.Test;

import will.st.bluetooth.robot.controller.MainMenuActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainMenuActivityUiTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	private MainMenuActivity activity;
	private ImageButton xButton;
	private RelativeLayout activityLayout;
	
	public MainMenuActivityUiTest() {
		super(MainMenuActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        
        setActivityInitialTouchMode(true);
        
        activity = getActivity();
        activityLayout = (RelativeLayout) activity.findViewById(
        		will.st.bluetooth.robotcontroller.R.id.main_menu_relative_layout);
        xButton = (ImageButton) activity.findViewById(
        		will.st.bluetooth.robotcontroller.R.id.x_button);
    }
	
	public void testPreconditions() {
	    assertNotNull("activity is null", activity);
	    assertNotNull("xButton is null”, xButton");
	}
	
	@Test
	public void testXButtonShouldBeCentralOnScreen() {
		ViewAsserts.assertOnScreen(activityLayout, xButton);
		ViewAsserts.assertHorizontalCenterAligned(activityLayout, xButton);
		ViewAsserts.assertVerticalCenterAligned(activityLayout, xButton);
	}
	
}
