package will.st.bluetooth.robotcontroller.test;

import org.junit.Test;

import will.st.bluetooth.robot.controller.MainMenuActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainMenuActivityUiTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	private MainMenuActivity activity;
	private ImageButton goButton;
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
        goButton = (ImageButton) activity.findViewById(
        		will.st.bluetooth.robotcontroller.R.id.go_button);
    }
	
	public void testPreconditions() {
	    assertNotNull("activity is null", activity);
	    assertNotNull("xButton is null”, xButton");
	}
	
	@Test
	public void testGoButtonShouldBeCentralOnScreen() {
		ViewAsserts.assertOnScreen(activityLayout, goButton);
		ViewAsserts.assertHorizontalCenterAligned(activityLayout, goButton);
		ViewAsserts.assertVerticalCenterAligned(activityLayout, goButton);
	}
	
}
