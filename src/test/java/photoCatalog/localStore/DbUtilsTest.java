package photoCatalog.localStore;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class DbUtilsTest {

	@Test
	public void testGetDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParams() {
		String actual = DbUtils.getParams(3);
		String expected = "?, ?, ?";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetFieldValue() {
		fail("Not yet implemented");
	}

}
