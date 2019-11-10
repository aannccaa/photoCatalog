package photoCatalog.localStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import photoCatalog.model.Photo;

public class PhotoDataAccess {
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

	public static ArrayList<String> getPhotoFields() {
		if (photoFields == null) {
			photoFields = new ArrayList<String>();

			photoFields.add(universalid);
			photoFields.add(title);
			photoFields.add(description);
			photoFields.add(filename);
			photoFields.add(localpath);
			photoFields.add(datetaken);
			photoFields.add(datemodified);
			photoFields.add(width);
			photoFields.add(height);
			photoFields.add(rotate);
		}
		return photoFields;
	}

	private Connection connection;

	public PhotoDataAccess(Connection connection) {
		this.connection = connection;
	}

	public Photo getPhoto(String universalId) throws SQLException {
		String fields = String.join(",", getPhotoFields());
		String sql = "SELECT " + fields + " from PHOTOS WHERE universalid=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		// setez param (numerotati de la 1)
		ps.setString(1, universalId);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		}
		Photo result = load(rs);

		return result;
	}

	public void insertPhoto(Photo photo) throws SQLException {
		String fields = String.join(",", photoFields);
		String paramsString = DbUtils.getParams(photoFields.size());
		String sql = "INSERT INTO PHOTOS ( " + fields + ") VALUES (" + paramsString + ")";
		PreparedStatement ps = this.connection.prepareStatement(sql);

		setParams(photo, ps);

		ps.executeUpdate();
	}

	public void UpdatePhoto(Photo photo) throws SQLException {
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (String f : photoFields) {
			sb.append(sep);
			sb.append(f);
			sb.append("=?");
		}
		String fields = sb.toString();

		String sql = "UPDATE PHOTOS SET " + fields + " WHERE universalid = ?";
		PreparedStatement ps = this.connection.prepareStatement(sql);

		setParams(photo, ps);
		ps.setString(photoFields.size() + 1, photo.getUniversalId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("photo not updated !!");
		}
	}

	private void setParams(Photo photo, PreparedStatement ps) throws SQLException {
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

}
