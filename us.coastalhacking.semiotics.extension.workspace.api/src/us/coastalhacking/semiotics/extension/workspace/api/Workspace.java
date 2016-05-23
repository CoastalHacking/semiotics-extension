/*******************************************************************************
 * Copyright 2016 Coastal Hacking
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package us.coastalhacking.semiotics.extension.workspace.api;

import java.util.Collection;
import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;


/**
 * <p>
 * The workspace provides access to a project or projects available
 * to this extension
 * </p>
 * 
 * @author jonpasski
 * @see Server
 */
@ProviderType
public interface Workspace {
	
	/**
	 * <p>
	 * Authenticate to a workspace with a user name and password
	 * </p>
	 * 
	 * @param url
	 * 		URL that specifies server
	 * @param port
	 * 		port of server
	 * @param name
	 * 		API account name
	 * @param password
	 * 		API password
	 * @return A valid server or null if none found
	 * @throws AuthenticationException If the server url, port, or credentials are incorrect
	 */
	void login(String url, int port, String name, String password) throws AuthenticationException, WorkspaceException;
	
	/**
	 * <p>
	 * Obtain all currently projects available to the extension
	 * </p>
	 * 
	 * @return A set of projects, or null if no projects exist 
	 * @throws WorkspaceException if the underlying session is invalid
	 */
	Set<String> getProjects() throws WorkspaceException;
	
	/**
	 * <p>
	 * Obtain a specific project given its name
	 * </p>
	 * 
	 * @param name
	 * 	the name of the project
	 * @return A project or null if no projects exist with the given name
	 * @throws WorkspaceException if the underlying session is invalid
	 */
	boolean isProject(String name) throws WorkspaceException;

	/**
	 * <p>
	 * Add extension-specific objects to this project. This method
	 * is not idempotent! Calling it repeatedly may add multiple copies
	 * of the same object. It is the responsibility of the provider
	 * to insert these objects correctly into the project.
	 * </p>
	 * 
	 * @param objects
	 * 	Extension-specific objects to add to this project
	 * @param project
	 *  Project to which objects are added
	 * @throws WorkspaceException if the underlying session is invalid
	 */
	void add(Collection<?> objects, String project) throws WorkspaceException;
}
