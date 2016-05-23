package us.coastalhacking.semiotics.extension.launcher;

/**
 * 
 * Exceptions associated to starting or stopping the embedded OSGi framework
 * 
 * @author jonpasski
 *
 */
public class LauncherException extends Exception {

	private static final long serialVersionUID = -362920480803224095L;

	/**
	 * @param message
	 */
	public LauncherException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public LauncherException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
