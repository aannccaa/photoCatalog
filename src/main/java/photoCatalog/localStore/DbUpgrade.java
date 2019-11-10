package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DbUpgrade {

	protected Connection connection;

	public DbUpgrade(Connection connection) {
		this.connection = connection;
	}

	public abstract void upgrade() throws Exception;



	protected void setVersion(int version) throws SQLException {
		String sql = "UPDATE DBPROPERTIES SET value = " + version + " WHERE name = 'DBVERSION'";
		int result = DbUtils.executeUpdate(this.connection, sql);
		// daca nu am exact o modificare in baza, atunci e o problema
		if (result != 1) {
			throw new SQLException("Cannot set DBVERSION");
		}
	}

}
