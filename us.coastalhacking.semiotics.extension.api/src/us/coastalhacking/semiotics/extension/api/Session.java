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
package us.coastalhacking.semiotics.extension.api;

import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <p>
 * The session provides access to a project or projects available
 * to this extension
 * </p>
 * 
 * @author jonpasski
 * @see Server
 */
@ProviderType
public interface Session {

	/**
	 * <p>
	 * Obtain all currently projects available to the extension
	 * </p>
	 * 
	 * @return A set of projects, or null if no projects exist 
	 * @throws SessionException if the underlying session is invalid
	 */
	Set<Project> getProjects() throws SessionException;
	
	/**
	 * <p>
	 * Obtain a specific project given its name
	 * </p>
	 * 
	 * @param name
	 * 	the name of the project
	 * @return A project or null if no projects exist with the given name
	 * @throws SessionException if the underlying session is invalid
	 */
	Project getProject(String name) throws SessionException;

}
