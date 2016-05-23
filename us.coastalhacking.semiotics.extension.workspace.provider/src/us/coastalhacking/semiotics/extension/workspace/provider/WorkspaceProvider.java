package us.coastalhacking.semiotics.extension.workspace.provider;

import java.util.Collection;
import java.util.Set;

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
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#login(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String url, int port, String name, String password) throws AuthenticationException, WorkspaceException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isProject(String name) throws WorkspaceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(Collection<?> objects, String project) throws WorkspaceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getProjects() throws WorkspaceException {
		// TODO Auto-generated method stub
		return null;
	}

}
