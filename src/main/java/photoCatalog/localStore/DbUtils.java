package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
		try {
			// rezultatul e numarul de linii modificate in baza
			int result = statement.executeUpdate(sql);
			return result;
		} finally {
			DbUtils.close(statement);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		return executeUpdate(this.connection, sql);
	}

	public static ZonedDateTime getDate(String fieldValue) {
		if (fieldValue == null) {
			return null;
		}
		LocalDateTime l = LocalDateTime.parse(fieldValue, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		ZonedDateTime zu = ZonedDateTime.of(l, ZoneOffset.UTC);

		ZonedDateTime zz = zu.withZoneSameInstant(ZoneId.systemDefault());
		return zz;
	}

	public static String getParams(int number) {
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (int i = 0; i < number; i++) {
			sb.append(sep);
			sb.append('?');
			sep = ", ";
		}
		return sb.toString();
	}

	public static String getFieldValue(ZonedDateTime date) {
		if (date == null) {
			return null;
		}
		ZonedDateTime utc = date.withZoneSameInstant(ZoneOffset.UTC);

		LocalDateTime utcL = utc.toLocalDateTime();

		DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

		String s = utcL.format(f);
		return s;
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO log
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO log
				e.printStackTrace();
			}
		}
	}

}
