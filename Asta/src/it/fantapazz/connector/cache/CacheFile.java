package it.fantapazz.connector.cache;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheFile {

	private static final Log logger = LogFactory.getLog(CacheFile.class);

	private String ID;
	
	private File cache;
	
	private File lock;

	public CacheFile(String iD, File cache, File lock) {
		super();
		ID = iD;
		this.cache = cache;
		this.lock = lock;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public File getCache() {
		return cache;
	}

	public void setCache(File cache) {
		this.cache = cache;
	}
	
	public void remove() {
		cache.delete();
	}
	
	public boolean isLocked() {
		return lock.exists();
	}
	
	public boolean exists() {
		return cache.exists();
	}
	
	public boolean lock() {

		// Take lock on filesystem
		boolean created;

		try {
			created = lock.createNewFile();
		} catch (IOException e) {
			logger.error("Error in creation on lock file", e);
			return false;
		}

		return created;

	}

	public void unlock() {
		lock.delete();
	}

}
