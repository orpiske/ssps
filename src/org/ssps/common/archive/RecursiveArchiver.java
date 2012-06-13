package org.ssps.common.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("rawtypes")
public class RecursiveArchiver extends DirectoryWalker {
	private static Logger logger = Logger.getLogger(RecursiveArchiver.class);

	private ArchiveOutputStream outputStream;
	
	private boolean stripPath;

	public RecursiveArchiver(final ArchiveOutputStream outputStream) {
		super();

		this.outputStream = outputStream;
	}
	
	public RecursiveArchiver(final ArchiveOutputStream outputStream, boolean stripPath) {
		super();

		this.outputStream = outputStream;
		setStripPath(stripPath);
	}

	@SuppressWarnings("unchecked")
	public List archive(final File startDirectory) throws IOException {
		List results = new ArrayList();

		walk(startDirectory, results);
		return results;
	}

	private String stripPath(final String path) {
		int pos = path.indexOf("installroot");
		return path.substring(pos);

	}

	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {

		String path;
		if (isStripPath()) { 
			 path = stripPath(directory.getPath());
		}
		else {
			path = directory.getPath();
		}
		
		logger.trace("Archiving " + path);
		TarArchiveEntry entry = new TarArchiveEntry(directory, path);

		outputStream.putArchiveEntry(entry);
		outputStream.closeArchiveEntry();

		return true;
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		
		String path;
		if (isStripPath()) { 
			 path = stripPath(file.getPath());
		}
		else {
			path = file.getPath();
		}

		logger.trace("Archiving" + path);

		TarArchiveEntry entry = new TarArchiveEntry(file, path);

		outputStream.putArchiveEntry(entry);
		IOUtils.copy(new FileInputStream(file), outputStream);

		outputStream.closeArchiveEntry();
	}

	public boolean isStripPath() {
		return stripPath;
	}

	public void setStripPath(boolean stripPath) {
		this.stripPath = stripPath;
	}

}
