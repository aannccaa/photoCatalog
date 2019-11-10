package photoCatalog.localStore;

import java.nio.file.Paths;

import org.junit.rules.TemporaryFolder;

public abstract class DbManagerBaseTest {

	protected String getTempDbFileName(TemporaryFolder tempFolder, String fileName) {
		String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
		String dbFileName = Paths.get(tempFolderPath, fileName).toString();
		return dbFileName;
	}

	
}
