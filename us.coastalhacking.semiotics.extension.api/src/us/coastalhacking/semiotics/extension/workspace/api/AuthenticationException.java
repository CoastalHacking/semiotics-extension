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


/**
 * <p>
 * Represents an invalid server configuration or server credentials 
 * </p>
 * 
 * @author jonpasski
 *
 */
public class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4350365252611240826L;

	/**
	 * @param message
	 */
	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

}
