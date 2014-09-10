package will.st.bluetooth.robot.controller.test;

import static org.junit.Assert.assertTrue;

import java.io.OutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import will.st.bluetooth.robot.controller.BtConnectFragment;
import will.st.bluetooth.robot.controller.ControllerActivity;
import android.bluetooth.BluetoothSocket;
import android.support.v4.app.FragmentManager;

@Config(emulateSdk = 18, manifest = "../AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ControllerActivityTest {

	private final ActivityController<ControllerActivity> mActivityController = Robolectric
			.buildActivity(ControllerActivity.class);

	@Test
	public void BtConnectFragmentShouldBeShownAfterResume() {
		BtConnectFragment mockBtConnectFragment = Mockito
				.mock(BtConnectFragment.class);
		// Create, start and resume activity under test.
		ControllerActivity activity = mActivityController.create().start().get();
		activity.setConnectFrag(mockBtConnectFragment);
		activity = mActivityController.resume().get();

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		assertTrue(fragmentManager.findFragmentByTag("connect") != null);
		assertTrue(fragmentManager.findFragmentByTag("control") == null);
	}

	@Test
	public void ControllerFragmentShouldBeShownAfterOnConnectionMade() {
		BtConnectFragment mockBtConnectFragment = Mockito
				.mock(BtConnectFragment.class);
		// Create, start and resume activity under test.
		ControllerActivity activity = mActivityController.create().start().get();
		activity.setConnectFrag(mockBtConnectFragment);
		activity = mActivityController.resume().get();
		
		activity.setSpecifiedControllerFragment("XControllerFragment");
		activity.onConnectionMade(Mockito.mock(BluetoothSocket.class), Mockito.mock(OutputStream.class));
		
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		assertTrue(fragmentManager.findFragmentByTag("control") != null);
		assertTrue(fragmentManager.findFragmentByTag("control").isAdded());
		assertTrue(fragmentManager.findFragmentByTag("connect") == null);
		
	}

}
