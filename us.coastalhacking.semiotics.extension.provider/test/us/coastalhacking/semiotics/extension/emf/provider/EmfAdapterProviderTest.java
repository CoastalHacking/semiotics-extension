package us.coastalhacking.semiotics.extension.emf.provider;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.osgi.dto.DTO;

import io.opensemantics.semiotics.model.assessment.Http;


/**
 * Test creation of Http EMF objects
 */
public class EmfAdapterProviderTest {

	EmfAdapterProvider provider;
	
	@Before
	public void setup() {
		provider = new EmfAdapterProvider();
	}
	
	@Test
	public void testToHttpRequestOnly() {
		final MyHttpRequestDTO req = new MyHttpRequestDTO();
		String expected = "requestOnly";
		req.request = expected;
		final Http http = provider.toHttp(req);
		String actual = http.getRequest();
		assertTrue(expected.equals(actual));
		assertNull(http.getResponse());
	}

	@Test
	public void testToHttpResponseOnly() {
		final MyHttpResponseDTO res = new MyHttpResponseDTO();
		String expected = "responseOnly";
		res.response = expected;
		final Http http = provider.toHttp(res);
		String actual = http.getResponse();
		assertTrue(expected.equals(actual));
		assertNull(http.getRequest());
	}

	@Test
	public void testToHttpRequestResponse() {
		final MyHttpReqResDTO reqres = new MyHttpReqResDTO();
		String expected = "requestResponse";
		reqres.response = expected;
		reqres.request = expected;
		final Http http = provider.toHttp(reqres);
		String actual = http.getResponse();
		assertTrue(expected.equals(actual));
		actual = http.getRequest();
		assertTrue(expected.equals(actual));
	}

	@Test
	public void shouldBeNullNonPublicClass() {
		PrivateHttpDTO dto = new PrivateHttpDTO();
		String expected = "foobar";
		dto.request = expected;
		dto.response = expected;
		final Http http = provider.toHttp(dto);
		assertNull(http.getRequest());
		assertNull(http.getResponse());
	}

	@Test
	public void shouldBeNullNonPublicFields() {
		PrivateFieldDTO dto = new PrivateFieldDTO();
		String expected = "foobar";
		dto.request = expected;
		dto.response = expected;
		final Http http = provider.toHttp(dto);
		assertNull(http.getRequest());
		assertNull(http.getResponse());
	}

	@Test(expected=ClassCastException.class)
	public void shouldThrowRuntimeExceptionDifferentTypes() {
		NonStringDTO dto = new NonStringDTO();
		String expected = "foobar";
		dto.request = expected.getBytes();
		dto.response = expected.getBytes();
		final Http http = provider.toHttp(dto);
	}
	
	public static class MyHttpRequestDTO extends DTO {
		public String request;
	}
	
	public static class MyHttpResponseDTO extends DTO {
		public String response;
	}

	public static class MyHttpReqResDTO extends DTO {
		public String request;
		public String response;
	}
	
	private static class PrivateHttpDTO extends DTO {
		public String request;
		public String response;
	}
	
	public static class PrivateFieldDTO extends DTO {
		private String request;
		private String response;
	}

	public static class NonStringDTO extends DTO {
		public byte[] request;
		public byte[] response;
	}
}
