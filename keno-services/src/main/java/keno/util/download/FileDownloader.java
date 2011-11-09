package keno.util.download;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class FileDownloader {
	
	private static final Logger LOGGER = Logger.getLogger(FileDownloader.class);
	
	private String source;
	private String target;

	public FileDownloader(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public boolean download() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			URL fileUrl = new URL(source);
			URLConnection urlConn = fileUrl.openConnection();
			inputStream = urlConn.getInputStream();
			outputStream = new BufferedOutputStream(new FileOutputStream(target));
			
			int bytesRead = 0;
			int bytesWritten = 0;
			byte[] buffer = new byte[1024];

			LOGGER.info("Downloading file '" + source + "'.");
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
				bytesWritten += bytesRead;
			}
			LOGGER.info("File '" + source + "' downloaded successfully to '" + target
					+ "' (" + bytesWritten + "bytes).");
			return true;
		} catch (MalformedURLException e) {
			LOGGER.error("Malformed URL error.", e);
			return false;
		} catch (IOException e) {
			LOGGER.error("IO error occured.", e);
			return false;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				
			}
		}
	}

}
