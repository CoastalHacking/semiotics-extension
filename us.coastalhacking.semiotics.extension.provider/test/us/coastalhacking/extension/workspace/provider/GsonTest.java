package us.coastalhacking.extension.workspace.provider;

import org.junit.Test;
import org.osgi.dto.DTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;

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
	
	static class TestDTO extends DTO {
		public String test;
		public TestDTO nested;
	}
}
