package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbManager {

	public static final int EMPTY_VERSION = 0;
	public static final int IN_UPDATE_VERSION = -1;
	public static final int CURRENT_VERSION = 1;



	private String dbFileName;
	private Connection connection;

	public DbManager(String dbFileName) {
		this.dbFileName = dbFileName;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void openDb() throws SQLException {
		if (this.connection != null) {
			return;
		}
		String dbUrl = DbUtils.getConnectionURL(dbFileName);
		this.connection = DriverManager.getConnection(dbUrl);
		String sql = "PRAGMA encoding = 'UTF-8'";
		DbUtils.executeUpdate(connection, sql);				
	}

	public int getDbVersion() throws SQLException {
		String sql = "SELECT name FROM sqlite_master WHERE type='table' and name = 'DBPROPERTIES'";
		ResultSet rs = DbUtils.executeQuery(this.connection, sql);
		if (!rs.next()) {
			return EMPTY_VERSION;
		} else {
			sql = "SELECT value FROM DBPROPERTIES WHERE name = 'DBVERSION";
			rs = DbUtils.executeQuery(this.connection, sql);
			if (!rs.next()) {
				throw new SQLException("Invalid db");
			}
			// returnez prima coloana din rezultat
			int version = Integer.parseInt(rs.getString(1));
			return version;
		}
	}

	public void upgrade() throws Exception {
		// obtinem versiunea db
		int dbVersion = getDbVersion();
		if (dbVersion < 0) {
			throw new SQLException("DB is corrupted, last upgrade is incomplete");
		}
		// obtinem lista de upgradeuri pe care o filtram de la versiunea db in sus
		ArrayList<DbUpgrade> upgrades = this.getUpgrades();
		// daca versiuna db < versiuna curenta
		for (int version = dbVersion; version < CURRENT_VERSION; version++) {
			DbUpgrade upgrade = upgrades.get(version);
			// executam fiecare upgrade
			upgrade.upgrade();
		}
	}

	private ArrayList<DbUpgrade> getUpgrades() throws Exception {
		if (this.connection == null) {
			throw new Exception("db not opened");
		}
		ArrayList<DbUpgrade> result = new ArrayList<DbUpgrade>();
		result.add(new Version000(this.connection));
		return result;
	}

}
