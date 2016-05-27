package us.coastalhacking.semiotics.extension.workspace.provider;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.emfstore.client.ESRemoteProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESUsersession;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.osgi.service.component.annotations.Component;

import us.coastalhacking.semiotics.extension.workspace.api.AuthenticationException;
import us.coastalhacking.semiotics.extension.workspace.api.Workspace;
import us.coastalhacking.semiotics.extension.workspace.api.WorkspaceException;

/**
 * @author jonpasski
 *
 */
@Component(name="us.coastalhacking.semiotics.extension.workspace")
public class WorkspaceProvider implements Workspace {

	/**
	 * 
	 */
	public WorkspaceProvider() {
	}

	private ESServer server;
	private ESUsersession session;
	
	/* (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#login(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String url, int port, String name, String password) throws AuthenticationException, WorkspaceException {
		server = ESServer.FACTORY.createServer(url, port, null);
		ESWorkspace workspace = ESWorkspaceProvider.INSTANCE.getWorkspace();
		workspace.addServer(server);
		try {
			session = server.login(name, password);
		} catch (ESException e) {
			throw new AuthenticationException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#isProject(java.lang.String)
	 */
	@Override
	public boolean isProject(String name) throws WorkspaceException {
		try {
			return server.getRemoteProjects(session).stream()
					.map(ESRemoteProject::getProjectName)
					.anyMatch(p -> p.equals(name));
		} catch (ESException e) {
			throw new WorkspaceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#add(java.util.Collection, java.lang.String)
	 */
	@Override
	public void add(Collection<?> objects, String project) throws WorkspaceException {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#getProjects()
	 */
	@Override
	public Set<String> getProjects() throws WorkspaceException {
		try {
			return server.getRemoteProjects(session).stream()
					.map(ESRemoteProject::getProjectName)
					.collect(Collectors.toSet());
		} catch (ESException e) {
			throw new WorkspaceException(e);
		}
	}

}
