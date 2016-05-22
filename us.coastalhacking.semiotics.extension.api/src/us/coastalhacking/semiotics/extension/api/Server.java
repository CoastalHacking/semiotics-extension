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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <p>
 * A server establishes a session, which provides access to projects
 * </p>
 * 
 * @author jonpasski
 *
 */
@ProviderType
public interface Server {
	
	/**
	 * <p>
	 * Authenticate to a server with a user name and password
	 * </p>
	 * 
	 * @param name API account name
	 * @param password API password
	 * @return A valid server exception
	 * @throws AuthenticationException If the server url, port, or credentials are incorrect
	 */
	Session login(String name, String password) throws AuthenticationException;
}
