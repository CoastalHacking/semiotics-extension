package us.coastalhacking.semiotics.extension.launcher.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import us.coastalhacking.semiotics.extension.launcher.Launcher;
import us.coastalhacking.semiotics.extension.launcher.LauncherException;

/**
 * This test needs to be ran as a JUnit test, not an OSGi Test Launcher
 * since it's testing the launcher.
 * 
 * The test also needs to reference the actual launcher jar, not just
 * the class files.
 * 
 * @author jonpasski
 *
 */
public class LauncherITTest {

	/**
	 * Test the starting and stopping of the launcher. Do not test
	 * the workspace itself. That should occur separately.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStartStop() {
		Launcher launcher = new Launcher();
		Map<String, String> config = new HashMap<>();
		List<String> extras = new ArrayList<>();
		extras.add("us.coastalhacking.semiotics.extension.workspace.api;version=1.0.0");
		try {
			launcher.startOsgi(extras, config);
			launcher.stopOsgi();
		} catch (LauncherException e) {
			Assert.fail("OSGi failed to start or stop");
		}
	}

}
