package com.dart.archive.pdfparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class CommandHelper {

	private static final String PDFIMAGES_COMMAND = "pdfimages";
	private static final String IMAGEMAGICK_COMMAND = "convert";	

	public int extractImages(final File src, final File dest) {
		if (!dest.exists()) {
			dest.mkdirs();
		}
		String pdfName = FilenameUtils.getBaseName(src.getName());
		File destination = new File(dest, pdfName);
		try {
			return runCommand(PDFIMAGES_COMMAND, "-p", src.getAbsolutePath(), destination.getAbsolutePath());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int convertPpmToJpg(File dir) {
		List<File> files = listPpmImages(dir, true);
		int result = 0;
		for (File file : files) {
			File dest = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + ".jpg");
			try {
				result += runCommand(IMAGEMAGICK_COMMAND, file.getAbsolutePath(), dest.getAbsolutePath());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileUtils.deleteQuietly(file);
		}
		return result;
	}

	public int runCommand(String... args) throws InterruptedException, IOException {
		return new ProcessBuilder(args).start().waitFor();
	}
	
	public List<File> listJpegImages(File dir, boolean recursive) {
		return new ArrayList<File>(FileUtils.listFiles(dir, new String[] {"jpg", "JPG", "JPEG", "jpeg"}, recursive));
	}
	
	public List<File> listImages(File dir, boolean recursive) {
		return new ArrayList<File>(FileUtils.listFiles(dir, new String[] {"jpg", "JPG", "JPEG", "jpeg", "ppm", "PPM"}, recursive));
	}
	
	public List<File> listPpmImages(File dir, boolean recursive) {
		return new ArrayList<File>(FileUtils.listFiles(dir, new String[] {"ppm", "PPM"}, recursive));
	}
	
	
}
