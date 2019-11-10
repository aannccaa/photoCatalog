package photoCatalog.localStore;

import java.io.File;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TestDbManager extends DbManagerBaseTest{

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void testGetConnectionURL() {
		String dbFileName = getTempDbFileName();
		String actual = DbUtils.getConnectionURL(dbFileName);
		String expected = "jdbc:sqlite:" + dbFileName;
		Assert.assertEquals(expected, actual);
	}

	private String getTempDbFileName() {
		return super.getTempDbFileName(tempFolder, "a.db");
	}

	@Test
	public void testOpenDb() {
		String dbFileName = getTempDbFileName();
		File dbFile = new File(dbFileName);
		// la inceput nu trebuie sa existe fisierul
		Assert.assertTrue(!dbFile.exists());
		DbManager dbManager = new DbManager(dbFileName);
		try {
			dbManager.openDb();
			Assert.assertNotNull(dbManager.getConnection());
			Assert.assertTrue(dbFile.exists());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetDbVersionEmpty() {
		String dbFileName = getTempDbFileName();
		File dbFile = new File(dbFileName);
		// la inceput nu trebuie sa existe fisierul
		Assert.assertTrue(!dbFile.exists());
		DbManager dbManager = new DbManager(dbFileName);
		try {
			dbManager.openDb();
			int expected = DbManager.EMPTY_VERSION;
			int actual = dbManager.getDbVersion();
			Assert.assertEquals(expected, actual);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

}
