package photoCatalog.localStore;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class DbUtilsTest {

	@Test
	public void getDateTest_TODO() {
		fail("Not yet implemented");
	}

	@Test
	public void getParamsTest() {
		String actual = DbUtils.getParams(3);
		String expected = "?, ?, ?";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getFieldValueTest_TODO() {
		fail("Not yet implemented");
	}

}
