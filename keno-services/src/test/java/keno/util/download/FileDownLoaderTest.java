package keno.util.download;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileDownLoaderTest {
	
	public static final String SOURCE = "http://www.szerencsejatek.hu/xls/keno.csv";
	public static final String TARGET = System.getProperty("user.home")
			+ File.separator + "keno.csv";
	
	@Test
	public void testDownload() {
		FileDownloader fd = new FileDownloader(SOURCE, TARGET);
		assertTrue(fd.download());
		File target = new File(TARGET);
		assertTrue(target.exists());
		assertTrue(target.canRead());
		assertTrue(target.isFile());
		assertTrue(target.length() > 0);
	}

}
