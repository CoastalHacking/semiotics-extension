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
 * Entry point for Extension API for servers and projects. 
 * </p>
 * 
 * @author jonpasski
 *
 */
@ProviderType
public interface Workspace {

	/**
	 * <p>
	 * Obtain a reference to a server. Validation of url or port
	 * occurs when authenticating to the server, not here.
	 * </p>
	 * 
	 * @param url The URL of the server
	 * @param port The port of the server
	 * @return An unvalidated server reference
	 */
	Server getServer(String url, int port);
}
