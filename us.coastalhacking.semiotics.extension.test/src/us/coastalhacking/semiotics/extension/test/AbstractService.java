package us.coastalhacking.semiotics.extension.test;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractService {
	
	protected <T> T getService(Class<T> clazz, BundleContext context) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		//return st.getService();
		return st.waitForService(1000);
	}

}
