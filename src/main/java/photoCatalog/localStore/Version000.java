package photoCatalog.localStore;

import java.sql.Connection;

public class Version000 extends DbUpgrade {

	public Version000(Connection connection) {
		super(connection);
	}

	@Override
	public void upgrade() throws Exception {
		DbUtils db = new DbUtils(this.connection);
		String sql;
		sql = "PRAGMA foreign_keys = 1";
		db.executeUpdate(sql);
		sql = "CREATE TABLE DBPROPERTIES(name STRING PRIMARY KEY, value STRING)";
		db.executeUpdate(sql);

		sql = "INSERT INTO DBPROPERTIES(name, value) VALUES ('OLDVERSION', '0')";
		db.executeUpdate(sql);

		// temporar setam versiunea cu -1, sa detectam un upgrade neterminat
		sql = "INSERT INTO DBPROPERTIES(name, value) VALUES ('DBVERSION', '-1')";
		db.executeUpdate(sql);

		sql = "CREATE TABLE PHOTOS ("
				+ " universalid TEXT NOT NULL"
				+ ",title TEXT"
				+ ",description TEXT"
				+ ",filename TEXT"
				+ ",localpath TEXT"
				+ ",datetaken TEXT"
				+ ",datemodified TEXT"
				+ ",width INTEGER"
				+ ",height INTEGER"
				+ ",rotate INTEGER"
				+ ",PRIMARY KEY (universalid)"
				+ ")";
		db.executeUpdate(sql);

		sql = "CREATE TABLE PHOTOIDS ("
				+ " provider TEXT NOT NULL"
				+ ",photoid TEXT NOT NULL"
				+ ",universalid TEXT NOT NULL"
				+ ",PRIMARY KEY (provider, photoid)"
				+ ",FOREIGN KEY (universalid) REFERENCES PHOTOS(universalid)"
				+ " ON DELETE RESTRICT ON UPDATE RESTRICT"
				+ ")";
		
		
		db.executeUpdate(sql);

		// setam versiuna reala
		setVersion(1);
	}

}
