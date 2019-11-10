package photoCatalog.model;

import java.time.ZonedDateTime;

public class Photo {

	private String universalId;
	private String title;
	private String description;
	private String fileName;
	private String localPath;
	private ZonedDateTime dateTaken;
	private ZonedDateTime dateModified;
	private int width;
	private int height;
	private int rotate;
	public String getUniversalId() {
		return universalId;
	}
	public void setUniversalId(String universalId) {
		this.universalId = universalId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public ZonedDateTime getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(ZonedDateTime dateTaken) {
		this.dateTaken = dateTaken;
	}
	public ZonedDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(ZonedDateTime dateModified) {
		this.dateModified = dateModified;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getRotate() {
		return rotate;
	}
	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	

}
