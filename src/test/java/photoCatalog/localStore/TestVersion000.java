package photoCatalog.localStore;

import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TestVersion000 extends DbManagerBaseTest{

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	
	@Test
	public void testUpgrade() {
		String fileName = this.getTempDbFileName(tempFolder, "a.db");
		File f = new File(fileName);
		if (f.exists()) {
			Assert.fail("NOK, db exists !!");
		}
		String url = DbUtils.getConnectionURL(fileName);
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		Version000 v0 = new Version000(connection);
		try {
			v0.upgrade();
			String sql = "SELECT name FROM sqlite_master WHERE type='table' and name = 'PHOTOIDS'";
			ResultSet rs = DbUtils.executeQuery(connection, sql);
			if (!rs.next()) {
				Assert.fail("table PHOTOIDS doesn't exist");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}
