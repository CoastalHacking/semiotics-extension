package us.coastalhacking.semiotics.extension.test;

import java.io.IOException;
import java.util.Set;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESUsersession;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.client.exceptions.ESServerNotFoundException;
import org.eclipse.emf.emfstore.client.exceptions.ESServerStartFailedException;
import org.eclipse.emf.emfstore.common.ESSystemOutProgressMonitor;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.util.tracker.ServiceTracker;

import us.coastalhacking.semiotics.extension.workspace.api.AuthenticationException;
import us.coastalhacking.semiotics.extension.workspace.api.Workspace;
import us.coastalhacking.semiotics.extension.workspace.api.WorkspaceException;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceIntegrationTest {

	private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	
	private ESServer server;
	private ESWorkspace workspace;
	private Workspace provider;
	private static final String projectName = "TestProject";
	private static final String localServerName = "Local Server";
	
	@Before
	public void before() {
  	Assert.assertNotNull(context);
  	try {
			provider = getService(Workspace.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(provider);

		try {
			workspace = ESWorkspaceProvider.INSTANCE.getWorkspace();
			deleteServers();
			deleteLocalProjects();
			server = ESServer.FACTORY.createAndStartLocalServer();
			final ESUsersession session = server.login("super", "super");
			final ESLocalProject project = workspace.createLocalProject(projectName);
			project.shareProject(session, new ESSystemOutProgressMonitor());
		} catch (IOException| ESException | ESServerNotFoundException | ESServerStartFailedException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	}

	@After
	public void after() {
		try {
			ESServer.FACTORY.stopLocalServer();
			if (workspace != null) {
				deleteLocalProjects();
				deleteServers();
				// TODO commented out because of bug 487904
				// Assert.assertNull(workspace.getLocalProjectByName(projectName));
			}
		} catch (IOException| ESException | ESServerNotFoundException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	
	/*
	 * Test Declarative Service
	 */
	@Test
	public void testWorkspaceProvider() {
  	try {
  		provider.login("localhost", 8080, "super", "super");
			Assert.assertTrue(provider.isProject(projectName));
			Set<String> projects = provider.getProjects();
			Assert.assertTrue(projects.contains(projectName));

			//final ESUsersession session = server.login("super", "super");
			//final ESLocalProject project = workspace.createLocalProject("TestProject");
			//project.shareProject(session, new ESSystemOutProgressMonitor());

		} catch (AuthenticationException | WorkspaceException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	<T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		//return st.getService();
		return st.waitForService(1000);
	}

	void deleteServers() throws ESServerNotFoundException {
		Assert.assertNotNull(workspace);
		for (final ESServer oldServer : workspace.getServers()) {
				workspace.removeServer(oldServer);
		}
	}

	/*
	 * Because of bug 487904, residual project directories accumlate 
	 * and aren't deleted by ECP. This in turn makes ECP think it has
	 * projects that exist on disk, which don't contain any project
	 * information. Trying to delete said projects results in an NPE.
	 * Therefore, we catch on NPE only here and continue. This
	 * also means we cannot trust the count or output of .getLocalProjects()
	 * until the bug is resolved.
	 * 
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=487904 
	 */
	void deleteLocalProjects() throws IOException, ESException {

		for (final ESLocalProject oldProject : workspace.getLocalProjects()) {
			try {
				// Cannot call ELocalProject.delete since it also breaks
				// ESWorkspace.getLocalProjectByName
				// oldProject.delete(new ESSystemOutProgressMonitor());
			} catch (NullPointerException e) {
				// swallow
			}
		}
	}
}