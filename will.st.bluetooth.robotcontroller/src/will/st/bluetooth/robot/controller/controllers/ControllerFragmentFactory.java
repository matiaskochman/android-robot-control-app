package will.st.bluetooth.robot.controller.controllers;

import java.io.OutputStream;


public class ControllerFragmentFactory {
	public static ControllerFragment createControllerFragment(String type,
			OutputStream outputStream) {
		ControllerFragment controllerFragment = null;
		if (type.equals("XControllerFragment")) {
			controllerFragment = new XControllerFragment(outputStream);
		}
		return controllerFragment;

	}
}
