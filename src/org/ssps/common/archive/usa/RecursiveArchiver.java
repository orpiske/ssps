package org.ssps.common.archive.usa;

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

	public RecursiveArchiver(final ArchiveOutputStream outputStream) {
		super();
		
		this.outputStream = outputStream;
	}

	@SuppressWarnings("unchecked")
	public List archive(final File startDirectory) throws IOException {
		List results = new ArrayList();
		
		walk(startDirectory, results);
		return results;
	}

	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {

		logger.trace("Archiving " + directory.getName());
		TarArchiveEntry entry = new TarArchiveEntry(directory);

		outputStream.putArchiveEntry(entry);
		outputStream.closeArchiveEntry();

		return true;
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		logger.trace("Archiving " + file.getCanonicalPath());
		TarArchiveEntry entry = new TarArchiveEntry(file);

		outputStream.putArchiveEntry(entry);
		IOUtils.copy(new FileInputStream(file), outputStream);

		outputStream.closeArchiveEntry();
	}

}
