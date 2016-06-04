package us.coastalhacking.semiotics.extension.test.emf.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.dto.DTO;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import io.opensemantics.semiotics.model.assessment.Http;
import us.coastalhacking.semiotics.extension.emf.api.EmfAdapter;
import us.coastalhacking.semiotics.extension.test.AbstractService;

@RunWith(MockitoJUnitRunner.class)
public class EmfAdapterProviderIntegrationTest extends AbstractService {

	private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	private EmfAdapter provider;
	
	@Before
	public void before() {
  	Assert.assertNotNull(context);
  	try {
  		provider = getService(EmfAdapter.class, context);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(provider);
	}

	@Test
	public void testProvider() {
		TestDTO dto = new TestDTO();
		String expected = "foobar";
		dto.request = expected;
		dto.response = expected;
		final Http http = provider.toHttp(dto);
		Assert.assertEquals(expected, http.getRequest());
		Assert.assertEquals(expected, http.getResponse());
	}
	
	public class TestDTO extends DTO {
		public String request;
		public String response;
	}
}
