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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((dateTaken == null) ? 0 : dateTaken.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + height;
		result = prime * result + ((localPath == null) ? 0 : localPath.hashCode());
		result = prime * result + rotate;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((universalId == null) ? 0 : universalId.hashCode());
		result = prime * result + width;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (dateModified == null) {
			if (other.dateModified != null)
				return false;
		} else if (!dateModified.equals(other.dateModified))
			return false;
		if (dateTaken == null) {
			if (other.dateTaken != null)
				return false;
		} else if (!dateTaken.equals(other.dateTaken))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (height != other.height)
			return false;
		if (localPath == null) {
			if (other.localPath != null)
				return false;
		} else if (!localPath.equals(other.localPath))
			return false;
		if (rotate != other.rotate)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (universalId == null) {
			if (other.universalId != null)
				return false;
		} else if (!universalId.equals(other.universalId))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
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
