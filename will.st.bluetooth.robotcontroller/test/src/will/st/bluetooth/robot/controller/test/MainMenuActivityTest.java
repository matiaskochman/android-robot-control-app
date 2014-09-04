package will.st.bluetooth.robot.controller.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import will.st.bluetooth.robotcontroller.MainMenuActivity;
import will.st.bluetooth.robotcontroller.R;

@Config(emulateSdk = 18, manifest= "../AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MainMenuActivityTest {

    @Test
    public void shouldHaveHappySmiles() throws Exception {
    	String connecting = new MainMenuActivity().getResources().getString(R.string.connecting);
        assertThat(connecting, equalTo("Connecting..."));
    }

}