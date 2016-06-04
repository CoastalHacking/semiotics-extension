package us.coastalhacking.semiotics.extension.workspace.provider;

import org.junit.Test;
import org.osgi.dto.DTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.opensemantics.semiotics.model.assessment.AssessmentFactory;
import io.opensemantics.semiotics.model.assessment.Http;
import us.coastalhacking.semiotics.extension.emf.provider.EmfAdapterProvider;

import org.junit.Assert;
import org.junit.Ignore;

/*
 * Test DTO and JSON (de)serialization
 */
public class GsonTest {

	@Test
	public void testSimpleGsonDto() {
		TestDTO dto = new TestDTO();
		dto.test = "foobar";
		Gson gson = new GsonBuilder().create();
		String actual = gson.toJson(dto);
		String expected = "{\"test\":\"foobar\"}";
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(expected, gson.toJson(gson.fromJson(expected, TestDTO.class)));
	}
	
	@Test
	public void testNestedGsonDto() {
		TestDTO dto1 = new TestDTO();
		TestDTO dto2 = new TestDTO();
		dto1.test = "foo";
		dto2.test = "bar";
		dto1.nested = dto2;
		Gson gson = new GsonBuilder().create();
		String actual = gson.toJson(dto1);
		String expected = "{\"test\":\"foo\",\"nested\":{\"test\":\"bar\"}}";
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(expected, gson.toJson(gson.fromJson(expected, TestDTO.class)));
	}

	@Test
	public void testJsonToEmf() {
		final String json = "{\"request\":\"foo\",\"response\":\"bar\"}";
		Gson gson = new GsonBuilder().create();
		final TestHttpDTO dto = gson.fromJson(json, TestHttpDTO.class);
		EmfAdapterProvider provider = new EmfAdapterProvider();
		Http http = provider.toHttp(dto);
		String actual = http.getRequest();
		Assert.assertTrue("foo".equals(actual));
		actual = http.getResponse();
		Assert.assertTrue("bar".equals(actual));
	}

	public static class TestDTO extends DTO {
		public String test;
		public TestDTO nested;
	}
	
	public static class TestHttpDTO extends DTO {
		public String request;
		public String response;
	}
}
