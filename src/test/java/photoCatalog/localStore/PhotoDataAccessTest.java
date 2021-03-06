package photoCatalog.localStore;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import photoCatalog.model.Photo;

public class PhotoDataAccessTest extends DbManagerBaseTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Connection connection;

	@Before
	public void setUp() throws Exception {
		this.connection = this.openDb();
	}

	@After
	public void tearDown() throws Exception {
		this.connection.close();
		String fileName = this.getTempDbFileName();
		File f = new File(fileName);
		f.delete();
	}

	@Test
	public void testGetPhoto() {
		try {
			this.insertPhotoNr1();
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		String universalID = "nr1";
		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		Photo actual = null;
		try {
			actual = pda.getPhoto(universalID);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		Photo expected = createPhotoNr1();
		Assert.assertEquals(expected, actual);
	}

	private void insertPhotoNr1() throws SQLException {
		Photo photo1 = createPhotoNr1();
		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		pda.insertPhoto(photo1);
	}

	@Test
	public void testInsertPhoto() {

		Photo photo = createPhotoNr1();

		PhotoDataAccess pda = new PhotoDataAccess(this.connection);

		try {
			pda.insertPhoto(photo);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Photo photo2 = null;
		try {
			photo2 = pda.getPhoto(photo.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(photo, photo2);
	}

	private Photo createPhotoNr1() {
		ZonedDateTime dateTaken = ZonedDateTime.of(2011, 10, 5, 14, 35, 59, 0, ZoneId.systemDefault());
		ZonedDateTime dateModified = ZonedDateTime.of(2016, 11, 6, 17, 39, 50, 3000, ZoneId.systemDefault());
		String localPath = "2011/10/05";
		Photo photo = new Photo();
		photo.setUniversalId("nr1");
		photo.setDescription("photo 1");
		photo.setTitle("dsc1.jpg");
		photo.setDateTaken(dateTaken);
		photo.setDateModified(dateModified);
		photo.setHeight(300);
		photo.setWidth(400);
		photo.setFileName("dsc1.jpg");
		photo.setRotate(90);
		photo.setLocalPath(localPath);
		return photo;
	}

	private Photo createPhotoNr2() {
		ZonedDateTime dateTaken = ZonedDateTime.of(2001, 10, 5, 14, 35, 59, 0, ZoneId.systemDefault());
		ZonedDateTime dateModified = ZonedDateTime.of(2006, 11, 6, 17, 39, 50, 3000, ZoneId.systemDefault());
		String localPath = "2001/10/05";
		Photo photo = new Photo();
		photo.setUniversalId("nr2");
		photo.setDescription("photo 2");
		photo.setTitle("dsc2.jpg");
		photo.setDateTaken(dateTaken);
		photo.setDateModified(dateModified);
		photo.setHeight(600);
		photo.setWidth(800);
		photo.setFileName("dsc2.jpg");
		photo.setRotate(180);
		photo.setLocalPath(localPath);
		return photo;
	}

	private Photo createPhotoNr1Modified() {
		Photo photo1 = createPhotoNr1();
		Photo photo2 = createPhotoNr2();
		photo2.setUniversalId(photo1.getUniversalId());
		return photo2;
	}

	@Test
	public void testUpdatePhoto() {
		Photo photo1 = createPhotoNr1();

		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		try {
			pda.insertPhoto(photo1);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		Photo photo1Modified = createPhotoNr1Modified();
		photo1Modified.setUniversalId(photo1.getUniversalId());

		try {
			pda.updatePhoto(photo1Modified);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		Photo photo1ModifiedFromDb = null;
		try {
			photo1ModifiedFromDb = pda.getPhoto(photo1.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(photo1Modified, photo1ModifiedFromDb);
	}

	@Test
	public void deletePhotoTest1() {
		Photo photo1 = createPhotoNr1();

		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		Photo photoInDb = null;

		try {
			pda.insertPhoto(photo1);
			photoInDb = pda.getPhoto(photo1.getUniversalId());

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		// check photo1 was inserted in db
		Assert.assertEquals(photo1, photoInDb);

		// delete photo1 from db
		try {
			pda.deletePhoto(photo1.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		try {
			photoInDb = pda.getPhoto(photo1.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		// the photo in db should no longer exist
		Assert.assertEquals(photoInDb, null);
	}

	// pt o foto care are un potoid flickr in PHOTOIDS, de testat ca nu se poate
	// sterge foto din PHOTOS
	@Test
	public void deletePhotoTest2() {
		Photo photo1 = createPhotoNr1();

		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		Photo photoInDb = null;

		try {
			pda.insertPhoto(photo1);
			photoInDb = pda.getPhoto(photo1.getUniversalId());

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		// check photo1 was inserted in db
		Assert.assertEquals(photo1, photoInDb);

		String flickrPhotoId = "1";
		// insert photoid
		try {
			pda.setPhotoId(photo1.getUniversalId(), PhotoDataAccess.Flickr, flickrPhotoId);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		// delete photo1 from db
		try {
			pda.deletePhoto(photo1.getUniversalId());
			Assert.fail("problema, nu trebuia stearsa poza pt care are un photoid asociat");
		} catch (SQLException e) {
			//ok
		}

		// sterg photoid, dupa care trebuie sa pot sterge inregistrearea
		try {
			pda.deletePhotoId(PhotoDataAccess.Flickr, flickrPhotoId);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

		// trebuie sa pot sterge poza
		try {
			pda.deletePhoto(photo1.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

		try {
			photoInDb = pda.getPhoto(photo1.getUniversalId());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

		// the photo in db should no longer exist
		Assert.assertEquals(photoInDb, null);
	}

	@Test
	public void setPhotoIdViolationTest() {
		PhotoDataAccess pda = new PhotoDataAccess(this.connection);
		try {
			DbUtils.executeUpdate(connection, "INSERT INTO PHOTOS (universalid) VALUES('1')");
			// perechea provider si photoid e PK
			pda.setPhotoId("1", "flickr", "1");
			try {
				pda.setPhotoId("2", "flickr", "1");
				// linia precedenta trebuie sa dea exceptie, pe linia asta nu se poate ajunge
				Assert.fail("Not allowed to modify universalid reference for photoid");
			} catch (SQLException e) {
				// este ok sa am exceceptie
			}

		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

	private String getTempDbFileName() {
		return this.getTempDbFileName(tempFolder, "a.db");
	}

	private Connection openDb() throws Exception {
		String dbFileName = getTempDbFileName();
		File dbFile = new File(dbFileName);
		// la inceput nu trebuie sa existe fisierul
		if (dbFile.exists()) {
			throw new SQLException("db exist");
		}
		DbManager dbManager = new DbManager(dbFileName);

		dbManager.openDb();
		dbManager.upgrade();

		return dbManager.getConnection();
	}

}
