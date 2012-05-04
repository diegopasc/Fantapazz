package it.fantapazz.connector.cache;

import it.fantapazz.ConfigSettings;
import it.fantapazz.utility.FileUtility;
import it.fantapazz.utility.URLConnectionInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cache is a simple module that is responsible to save
 * URL content and to retrieve cached contents 
 * 
 * @author Michele Mastrogiovanni
 */
public class Cache {

	private static final Log logger = LogFactory.getLog(Cache.class);
	
	private static final int MAX_FILE_NAME_LENGTH = 100;
	
	private static Cache cache;
	
	private boolean enabled;
	
	private Cache() {
		enabled = true;
	}
	
	public static Cache instance() {
		if ( cache == null )
			cache = new Cache();
		return cache;
	}
	
	public void setEnabled(boolean flag) {
		enabled = flag;
	}

	public InputStream read(String url) {
		return read(url, true);
	}
	
	public static void main(String[] args) {
		Cache cache = new Cache();
		cache.inspect();
	}
	
	public void inspect() {
		
		File cacheDir = new File("cache");

		inspect(cacheDir, cacheDir);
		
	}
	
	private void inspect(File cacheDir, File directory) {
		File[] list = directory.listFiles();
		for ( File file : list ) {
			// System.out.println(file.getAbsolutePath());
			if ( ! file.exists() )
				continue;
			if ( file.isFile() ) {
				if ( file.getName().endsWith(".cache")) {
					String url = getURLFromCacheFile(cacheDir, file);
					System.out.println("URL: " + url + " : " + file.getAbsolutePath());
				}
			}
			else if ( file.isDirectory() ) {
				inspect(cacheDir, file);
			}
		}
	}

	public InputStream read(String url, boolean useCache) {
		
		logger.debug("GET: " + url);

		// Cache disabled
		if ( ! enabled ) {
			return forward(url);
		}

		// Cache dir
		File cacheDir = new File("cache");
		
		// Cache entry
		CacheFile cacheFile = cacheFileName(cacheDir, url);

		// New url
		url += "&r=" + new Random().nextLong();

		if ( useCache ) {
			
			// Cache cannot be served
			if ( cacheFile.isLocked() || ! cacheFile.exists() ) {

				// Cache does not exists
				logger.debug("Cache does not exists");

			}
			else {

				try {
					InputStream in = serveFileFromCache(cacheFile);
					logger.debug("Served form cache: " + url);
					return in;
				} catch (FileNotFoundException e) {
					logger.error("Error getting file from cache: " + url, e);
				} 

			}
		}
		
		if ( cacheFile.lock() ) {
			
			InputStream in = null;

			try {
				in = serveFileAndCacheIt(cacheFile, url);
			} catch (FileNotFoundException e) {
				logger.error("Error cache file does not exitst: " + url, e);
			}
			
			cacheFile.unlock();
			
			if ( in != null ) {
				logger.debug("Served form cache: " + url);
				return in;
			}
			
		}
		
		return forward(url);
		
	}

	private InputStream serveFileFromCache(CacheFile cacheFile) throws FileNotFoundException {
		return new FileInputStream(cacheFile.getCache());
	}

	private InputStream serveFileAndCacheIt(CacheFile cacheFile, String url) throws FileNotFoundException {
		
		// Get direct input stream
		InputStream in = forward(url);
				
		// Save in cache the result
		try {
			FileOutputStream out = new FileOutputStream(cacheFile.getCache());
			FileUtility.joinStreams(in, out);
			out.close();
		} catch (IOException e) {
			logger.error("Cannot save file", e);
			return forward(url);
		} 
		
		return serveFileFromCache(cacheFile);
		
	}

	private InputStream forward(String url) {
		// New url
		url += "&r=" + new Random().nextLong();
		URLConnectionInputStream in = new URLConnectionInputStream(url);
		if ( ConfigSettings.instance().isProxyEnabled()) {
			in.setupProxy(ConfigSettings.instance().getProxyHost(), ConfigSettings.instance().getProxyPort(), ConfigSettings.instance().getProxyUsername(), ConfigSettings.instance().getProxyPassword());
		}
		return in;
	}
	
	public String getURLFromCacheFile(File cacheDir, File cacheFile) {
		try {
			String url = getRealName(cacheDir, cacheFile);
			// System.out.println("Real Name: " + url);
			url = url.substring(0, url.length() - 6);
			// System.out.println(url);
			String fileID = new String(Base64.decodeBase64(url.getBytes()), "UTF-8");
			return fileID;
		}
		catch (UnsupportedEncodingException e) {
			logger.error("Bad encoding", e );
		}
		return null;
	}

	public CacheFile cacheFileName(File cacheDir, String url) {
		try {
			logger.debug("Cache URL (not encoded): " + url);
			byte[] bytes = url.getBytes("UTF-8");
			String fileID = new String(Base64.encodeBase64(bytes), "UTF-8");
			File lockFile = makeFileShorter(cacheDir, fileID + ".lock");
			File cacheFile = makeFileShorter(cacheDir, fileID + ".cache");
			return new CacheFile(fileID, cacheFile, lockFile);
		}
		catch (UnsupportedEncodingException e) {
			logger.error("Bad encoding", e );
		}
		return null;
	}
	
	public static String getRealName(File directory, File file) {
		String path = "";
		File parent = file;
		while ( ! directory.equals(parent) ) {
			if ( parent.isFile() ) {
				path = parent.getName() + path;
			}
			else if ( parent.isDirectory()) {
				path = parent.getName().substring(1) + path;
			}
			parent = parent.getParentFile();
		}
		return path;
	}

	public static File makeFileShorter(File directory, String fileName) {
		
		if (fileName.length() <= MAX_FILE_NAME_LENGTH) {
			directory.mkdirs();
			return new File(directory, fileName);
		}

		String directoryName = "_" + fileName.substring(0, MAX_FILE_NAME_LENGTH);
		String restFileName = fileName.substring(MAX_FILE_NAME_LENGTH, fileName.length());

		File newDirectory = new File(directory + "/" + directoryName);

		newDirectory.mkdirs();

		return makeFileShorter(newDirectory, restFileName);
	}
	
}
