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

import java.util.Collection;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <p>
 * A project allows extension to update the project with domain-specific objects
 * </p>
 * 
 * @author jonpasski
 *
 */
@ProviderType
public interface Project {
	
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
	 * @throws SessionException if the underlying session is invalid
	 */
	void add(Collection<?> objects) throws SessionException;

	/**
	 * <p>
	 * The name of this project
	 * </p>
	 * 
	 * @return the name of this project
	 * @see Session.getProject(String)
	 */
	String getName();
}
