package us.coastalhacking.semiotics.extension.workspace.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import us.coastalhacking.semiotics.extension.workspace.api.Workspace;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceIntegrationTest {

	private final BundleContext context = FrameworkUtil.getBundle(Workspace.class).getBundleContext();

	@Before
	public void before() {
		// TODO add test setup here
	}

	@After
	public void after() {
		// TODO add test clear up here
	}

	@Test
	public void testWorkspace() {
  	Assert.assertNotNull(context);
	}

	<T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

}