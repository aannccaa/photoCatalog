package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteException;

import photoCatalog.model.Photo;

public class PhotoDataAccess {
	// fields in Photos table (final as their name is not changeable)
	public static final String universalid = "universalid";
	public static final String title = "title";
	public static final String description = "description";
	public static final String filename = "filename";
	public static final String localpath = "localpath";
	public static final String datetaken = "datetaken";
	public static final String datemodified = "datemodified";
	public static final String width = "width";
	public static final String height = "height";
	public static final String rotate = "rotate";

	private static ArrayList<String> photoFields;

	/**
	 * get the list of fields of PHOTOS TABLE
	 * 
	 * @return
	 */
	public static ArrayList<String> getPhotoFields() {
		if (photoFields == null) {
			photoFields = getPhotoFields(null);
		}
		return photoFields;
	}

	/**
	 * get the list of fields of PHOTOS TABLE
	 * 
	 * @return
	 */
	public static ArrayList<String> getPhotoFields(String tablePrefix) {
		String prefix = tablePrefix == null ? "" : tablePrefix + ".";
		ArrayList<String> fields = new ArrayList<String>();

		fields.add(prefix + universalid);
		fields.add(prefix + title);
		fields.add(prefix + description);
		fields.add(prefix + filename);
		fields.add(prefix + localpath);
		fields.add(prefix + datetaken);
		fields.add(prefix + datemodified);
		fields.add(prefix + width);
		fields.add(prefix + height);
		fields.add(prefix + rotate);

		return fields;
	}

	private Connection connection;

	public PhotoDataAccess(Connection connection) {
		this.connection = connection;
	}

	/**
	 * return a photo from db
	 * 
	 * @param universalId
	 * @return photo from db, or null
	 * @throws SQLException
	 */
	public Photo getPhoto(String universalId) throws SQLException {
		String fields = String.join(",", getPhotoFields());
		String sql = "SELECT " + fields + " FROM PHOTOS WHERE universalid=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ResultSet rs = null;
		try {
			// setez param (numerotati de la 1)
			ps.setString(1, universalId);
			rs = ps.executeQuery();

			// if select returned nothing
			if (!rs.next()) {
				return null;
			}
			Photo result = load(rs);

			return result;
		} finally {
			DbUtils.close(rs);
			DbUtils.close(ps);
		}
	}

	public Photo getPhotoByProviderID(String provider, String photoId) throws SQLException {
		String fields = String.join(",", getPhotoFields("P"));
		String sql = "SELECT " + fields + " FROM " +
				"PHOTOIDS I " +
				"JOIN PHOTOS P ON P.universalid=I.universalid " +
				"WHERE I.provider = ? and I.photoid = ? ";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ResultSet rs = null;
		try {
			// setez param (numerotati de la 1)
			ps.setString(1, provider);
			ps.setString(2, photoId);
			rs = ps.executeQuery();

			// if select returned nothing
			if (!rs.next()) {
				return null;
			}
			Photo result = load(rs);

			return result;
		} finally {
			DbUtils.close(rs);
			DbUtils.close(ps);
		}
	}

	/**
	 * Asociaza un universalId cu perechea provider/photoId
	 * Daca exista deja un alt universalId asociat, exceptie
	 * @param universalId
	 * @param provider
	 * @param photoId
	 * @throws SQLException
	 */
	public void setPhotoId(String universalId, String provider, String photoId) throws SQLException {

		ResultSet rs = null;
		String sql = "INSERT INTO PHOTOIDS (provider, photoid, universalid) VALUES (?, ?, ?)";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		int result;
		try {
			ps.setString(1, provider);
			ps.setString(2, photoId);
			ps.setString(3, universalId);
			try {
				result = ps.executeUpdate();
				if (result == 1) {
					return;
				}
			} catch (SQLException e) {
				// SQLLiteError 19 : CONSTRAINT VIOLATION
				// Violare de cheie
				if (e.getErrorCode() != 19) {
					throw e;
				}
			}
			DbUtils.close(ps);
			sql = "SELECT universalid FROM PHOTOIDS WHERE provider = ? AND photoid = ?";
			ps = this.connection.prepareStatement(sql);
			ps.setString(1, provider);
			ps.setString(2, photoId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String inDbUniversalId = rs.getString(1);
				if (!inDbUniversalId.equals(universalId)) {
					throw new SQLException("different photoId in db");
				}
			}
		} finally {
			DbUtils.close(rs);
			DbUtils.close(ps);
		}

	}

	/**
	 * insert photo in db
	 * 
	 * @param photo
	 * @throws SQLException
	 */
	public void insertPhoto(Photo photo) throws SQLException {
		ArrayList<String> photoFields = getPhotoFields();
		String fields = String.join(",", photoFields);
		// paramsString are forma ?, ?, ?, ... ?
		String paramsString = DbUtils.getParams(photoFields.size());
		String sql = "INSERT INTO PHOTOS" +
				" (" + fields + ") " +
				"VALUES (" + paramsString + ")";

		PreparedStatement ps = this.connection.prepareStatement(sql);
		try {
			// setez params (numerotati de la 1)
			ps.setString(photoFields.indexOf(universalid) + 1, photo.getUniversalId());
			ps.setString(photoFields.indexOf(title) + 1, photo.getTitle());
			ps.setString(photoFields.indexOf(description) + 1, photo.getDescription());
			ps.setString(photoFields.indexOf(filename) + 1, photo.getFileName());
			ps.setString(photoFields.indexOf(localpath) + 1, photo.getLocalPath());
			ps.setString(photoFields.indexOf(datetaken) + 1, DbUtils.getFieldValue(photo.getDateTaken()));
			ps.setString(photoFields.indexOf(datemodified) + 1, DbUtils.getFieldValue(photo.getDateModified()));
			ps.setInt(photoFields.indexOf(width) + 1, photo.getWidth());
			ps.setInt(photoFields.indexOf(height) + 1, photo.getHeight());
			ps.setInt(photoFields.indexOf(rotate) + 1, photo.getRotate());

			// result is an integer (nb of rows inserted)
			int rows = ps.executeUpdate();
			if (rows != 1) {
				throw new SQLException("photo not inserted in db");
			}
		} finally {
			DbUtils.close(ps);
		}
	}

	public void updatePhoto(Photo photo) throws SQLException {
		ArrayList<String> photoFields = getPhotoFields();
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (String f : photoFields) {
			sb.append(sep);
			sb.append(f);
			sb.append("=?");
			sep = ",";
		}
		String fields = sb.toString();

		String sql = "UPDATE PHOTOS SET " + fields + " WHERE universalid = ?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		try {
			// setez params (numerotati de la 1)
			ps.setString(photoFields.indexOf(universalid) + 1, photo.getUniversalId());
			ps.setString(photoFields.indexOf(title) + 1, photo.getTitle());
			ps.setString(photoFields.indexOf(description) + 1, photo.getDescription());
			ps.setString(photoFields.indexOf(filename) + 1, photo.getFileName());
			ps.setString(photoFields.indexOf(localpath) + 1, photo.getLocalPath());
			ps.setString(photoFields.indexOf(datetaken) + 1, DbUtils.getFieldValue(photo.getDateTaken()));
			ps.setString(photoFields.indexOf(datemodified) + 1, DbUtils.getFieldValue(photo.getDateModified()));
			ps.setInt(photoFields.indexOf(width) + 1, photo.getWidth());
			ps.setInt(photoFields.indexOf(height) + 1, photo.getHeight());
			ps.setInt(photoFields.indexOf(rotate) + 1, photo.getRotate());

			ps.setString(photoFields.size() + 1, photo.getUniversalId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("photo not updated !!");
			}
		} finally {
			DbUtils.close(ps);
		}
	}

	private Photo load(ResultSet rs) throws SQLException {
		Photo photo = new Photo();
		photo.setUniversalId(rs.getString(universalid));
		photo.setTitle(rs.getString(title));
		photo.setDescription(rs.getString(description));
		photo.setFileName(rs.getString(filename));
		photo.setLocalPath(rs.getString(localpath));
		photo.setDateTaken(DbUtils.getDate(rs.getString(datetaken)));
		photo.setDateModified(DbUtils.getDate(rs.getString(datemodified)));
		photo.setWidth(rs.getInt((width)));
		photo.setHeight(rs.getInt((height)));
		photo.setRotate(rs.getInt((rotate)));
		return photo;
	}

	public void deletePhoto(String universalId) throws SQLException {
		String sql = "DELETE FROM PHOTOS WHERE universalid=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		try {
			ps.setString(1, universalId);
			int rows = ps.executeUpdate();

			// result is an integer (nb of rows deleted)
			if (rows != 1) {
				throw new SQLException("photo not deleted from db");
			}
		} finally {
			DbUtils.close(ps);
		}

	}

}
