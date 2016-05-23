package us.coastalhacking.semiotics.extension.launcher;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import us.coastalhacking.semiotics.extension.workspace.api.AuthenticationException;
import us.coastalhacking.semiotics.extension.workspace.api.Workspace;
import us.coastalhacking.semiotics.extension.workspace.api.WorkspaceException;

/*
 * The launcher cannot leak any ServiceReferences, so this
 * method delegates and keeps the references.
 */
public class Launcher implements Workspace {

	private Framework framework;
	private Workspace workspace;

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#login(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String url, int port, String name, String password) throws AuthenticationException, WorkspaceException {
		checkFramework();
		checkWorkspace();

		workspace.login(url, port, name, password);
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#getProjects()
	 */
	@Override
	public Set<String> getProjects() throws WorkspaceException {
		checkFramework();
		checkWorkspace();

		// Create a new set based on the existing to hopefully not to leak a reference
		// TODO Needs to be tested
		return workspace.getProjects().stream().sorted().collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#isProject(java.lang.String)
	 */
	@Override
	public boolean isProject(String name) throws WorkspaceException {
		checkFramework();
		checkWorkspace();

		return workspace.isProject(name);
	}

	/*
	 * (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.workspace.api.Workspace#add(java.util.Collection, java.lang.String)
	 */
	@Override
	public void add(Collection<?> objects, String project) throws WorkspaceException {
		checkFramework();
		checkWorkspace();

		if (workspace.isProject(project)) {
			workspace.add(objects, project);
		} else {
			throw new WorkspaceException(String.format("Project does not exist: %s", project));
		}
	}

	private void checkFramework() throws WorkspaceException {
		if (framework == null || framework.getState() != Bundle.ACTIVE) {
			throw new WorkspaceException("OSGi framework is not in ACTIVE state.");
		}
	}

	private void checkWorkspace() throws WorkspaceException {
		if (workspace == null)
			throw new WorkspaceException("No workspace provider available");
	}
	
	/**
	 * Starts the OSGi framework
	 * 
	 * @param config
	 * 	OSGi configurations to pass to the framework prior to start
	 * @throws LauncherException
	 * 	when an exception occurs starting the OSGi framework 
	 */
	public void startOsgi(List<String> extras, Map<String, String> config) throws LauncherException {
		FrameworkFactory frameworkFactory = getFrameworkFactory();
		if (frameworkFactory != null) {
			Map<String, String> myConfig = new HashMap<String, String>(config);
			myConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, getExtras(extras));
			myConfig.put(Constants.FRAMEWORK_STORAGE_CLEAN, "onFirstInit");
			framework = frameworkFactory.newFramework(myConfig);
			try {
				System.out.print("Starting OSGi framework...");
				framework.start();
				System.out.println(" started.");
				startBundles();
				getWorkspace();
			} catch (BundleException e) {
				// TODO log
				System.out.println(" error:");
				e.printStackTrace();
				throw new LauncherException(e);
			}
		} else {
			// TODO log
		}
	}

	private String getExtras(List<String> extras) {
		List<String> myExtras = new ArrayList<>(extras);
		// TODO: hardcoded hack around; can't call bundleContext.getBundle().getHeader();
		// something like so - http://stackoverflow.com/a/1273432
		myExtras.add("us.coastalhacking.semiotics.extension.workspace.api;version=1.0.0");
		return myExtras.stream().collect(Collectors.joining(","));
	}

	/**
	 * Stops the OSGi framework
	 * 
	 * @throws LauncherException
	 * 	when an exception occurs stopping the OSGi framework
	 */
	public void stopOsgi() throws LauncherException {
		if (framework != null) {
			try {
				System.out.println("Stopping OSGi framework...");
				framework.stop();
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new LauncherException(e);
			}
		}	
	}

	private FrameworkFactory getFrameworkFactory() throws LauncherException {
		FrameworkFactory frameworkFactory = null;
		try {
			ClassLoader loader = getClass().getClassLoader();
			Iterator<FrameworkFactory> iter = ServiceLoader.load(FrameworkFactory.class, loader).iterator();
			if (iter.hasNext()) {
				frameworkFactory = iter.next();
			} else {
				// TODO log
				throw new LauncherException("Could not find factory via loader: " + loader.toString());
			}
		} catch (Exception ex) {
			// Some extentions throws an exception if no implementation is found
			// TODO: log
			throw new LauncherException(ex);
		}
		return frameworkFactory;
	}

	private void startBundles() throws LauncherException {
		if (framework != null) {
			BundleContext context = framework.getBundleContext();
			Bundle[] bundles = context.getBundles();

			List<Bundle> installedBundles = new LinkedList<Bundle>();
			Map<String, Bundle> installedBundleLocations = new HashMap<String, Bundle>();
			for (Bundle bundle : bundles) {
				installedBundleLocations.put(bundle.getLocation(), bundle);
			}

			for (String jar : getBundles()) {
				// May have been previously installed
				// Still needs to be started
				if (installedBundleLocations.keySet().contains(jar)) {
					installedBundles.add(installedBundleLocations.get(jar));
					continue;
				}

				Bundle bundle;
				try {
					System.out.println(String.format("Installing bundle: %s", jar));
					bundle = context.installBundle(jar);
					System.out.println(String.format("Adding installed bundle to be started: %s", bundle));
					installedBundles.add(bundle);
				} catch (BundleException e) {
					System.out.println(String.format("Could not install bundle: %s", jar));
					// TODO : handle better
					// A bundle may have been already installed
					// via a transitive load
					e.printStackTrace();
					// throw new LauncherException(e);
				}
			}

			for (Bundle bundle : installedBundles) {
				try {
					if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
						System.out.println(String.format("Starting bundle: %s", bundle));
						bundle.start();
					} else {
						System.out.println(String.format("Ignoring starting bundle: %s", bundle));
					}
				} catch (BundleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// throw new LauncherException(e);
					try {
						// TODO log
						System.out.println(String.format("Attempting to uninstall bundle: %s", bundle));
						bundle.uninstall();
						System.out.println("Uninstall successful.");
					} catch (BundleException e1) {
						// TODO Auto-generated catch block
						System.out.println("Uninstall failed.");
						e1.printStackTrace();
					}
				}
			}
		} else {
			// TODO log
		}
	}

	private List<String> getBundles() throws LauncherException {
		List<String> results = new ArrayList<String>();
		// http://stackoverflow.com/a/15331935
		JarURLConnection urlcon;
		try {
			URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
			// TODO hack
			if (location.getProtocol().equals("file")) {
				location = new URL(String.format("jar:%s!/", location.toString()));
			}
			urlcon = (JarURLConnection) (location.openConnection());

			// TODO: filter on 'bundles' directory
			try (JarFile jar = urlcon.getJarFile()) {
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					String entry = entries.nextElement().getName();
					if (entry.endsWith(".jar")) {
						String jarEntry = String.format("%s%s", location.toString(), entry);
						results.add(jarEntry);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new LauncherException(e);
		}
		return results;
	}

	private void getWorkspace() {
		BundleContext context = framework.getBundleContext();
		ServiceReference<Workspace> svcRef = context.getServiceReference(Workspace.class);
		workspace = context.getService(svcRef);
	}
}
