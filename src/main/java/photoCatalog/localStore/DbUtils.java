package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {

	private Connection connection;

	public DbUtils(Connection connection) {
		this.connection = connection;
	}

	public static final String getConnectionURL(String fileName) {
		String result = "jdbc:sqlite:" + fileName;
		return result;
	}

	public static ResultSet executeQuery(Connection connection, String sql) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		return rs;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return executeQuery(this.connection, sql);
	}

	public static int executeUpdate(Connection connection, String sql) throws SQLException {
		Statement statement = connection.createStatement();
		// rezultatul e numarul de linii modificate in baza
		int result = statement.executeUpdate(sql);
		return result;
	}

	public int executeUpdate(String sql) throws SQLException {
		return executeUpdate(this.connection, sql);
	}

}
