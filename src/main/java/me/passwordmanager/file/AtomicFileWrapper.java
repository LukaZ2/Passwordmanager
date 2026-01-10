package me.passwordmanager.file;

import lombok.Getter;
import me.passwordmanager.file.exception.*;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Abstract class for handling an atomic file.
 */
public abstract class AtomicFileWrapper {

    /**
     * Temporary backup file format.
     */
    public static final String BACKUP_TMP_FILENAME = ".%s.tmp";
    /**
     * Complete backup file format.
     */
    public static final String BACKUP_COMPLETE_FILENAME = ".%s.backup";

    @Getter
    private final String filePath;

    private FileChannel fileChannel;
    private FileLock fileLock;

    /**
     * Instantiates a new Atomic file wrapper.
     *
     * @param filePath the file path.
     */
    public AtomicFileWrapper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Create the file.
     *
     * @throws IOException thrown when the file cannot be created.
     */
    public void createNew() throws IOException {
        FileUtils.getFile(getFilePath(), true);
    }

    /**
     * Gets file name.
     *
     * @return the file name string.
     */
    public String getFileName() {
        return FileUtils.getFileName(getFilePath());
    }

    /**
     * Gets working directory.
     *
     * @return the working directory string.
     */
    public String getWorkingDirectory() {
        return FileUtils.getAbsoluteDirectoryPath(getFilePath());
    }

    private String getTmpFilePath() {
        return getWorkingDirectory() + "/" + String.format(BACKUP_TMP_FILENAME, getFileName());
    }

    private String getBackupFilePath() {
        return getWorkingDirectory() + "/" + String.format(BACKUP_COMPLETE_FILENAME, getFileName());
    }

    /**
     * Open and read the contents of the atomic file.
     *
     * @throws IOException thrown when the file cannot be read.
     */
    public void open() throws IOException {
        fileChannel = FileChannel.open(Paths.get(getFilePath()), StandardOpenOption.READ, StandardOpenOption.WRITE);
        if((fileLock = fileChannel.tryLock()) == null) throw new AtomicFileLockedException();
        if(backupExists()) throw new AtomicFileCorruptedException(true);
//        readContent(Channels.newInputStream(fileChannel));
    }

    public void readContentWrapped() throws IOException {
        readContentWrapped(Channels.newInputStream(fileChannel));
        refresh();
    }

    private boolean backupExists() {
        return new File(getBackupFilePath()).exists();
    }


    /**
     * Restore the atomic file from a complete backup in case the last save was interrupted.
     *
     * @throws IOException thrown when the data cannot be written.
     */
    public void restore() throws IOException {
        var backup = FileUtils.getFile(getBackupFilePath());
        StreamUtils.writeAll(Channels.newOutputStream(fileChannel), new FileInputStream(backup));
        if(!backup.delete()) throw new AtomicFileBackupDeleteException();
    }

    /**
     * Check if file specified at filePath exists, and is a real file.
     *
     * @throws FileNotFoundException      thrown when the file does not exist.
     * @throws IncorrectFileTypeException thrown when the file is not a real file.
     */
    public void checkFilePath() throws FileNotFoundException, IncorrectFileTypeException {
        var file = new File(getFilePath());
        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isFile()) throw new IncorrectFileTypeException();
    }

    /**
     * Write content to the atomic file. Must be overwritten.
     *
     * @param outputStream the output stream to write data to.
     * @throws IOException thrown when the data cannot be written.
     */
    protected abstract void writeContent(OutputStream outputStream) throws IOException;

    /**
     * Read content of the atomic file. Must be overwritten.
     *
     * @param inputStream the input stream to read data from.
     * @throws IOException thrown when data cannot be read.
     */
    protected abstract void readContentWrapped(InputStream inputStream) throws IOException;

    /**
     * Save the file atomically. Creates a temporary backup first, then writes content to the file and deletes the backup.
     *
     * @throws IOException thrown when data cannot be written.
     */
    public void save() throws IOException {

        var tmp = FileUtils.getFile(getTmpFilePath(), true);
        writeContent(new FileOutputStream(tmp));

        var backup = new File(getBackupFilePath());
        if(!tmp.renameTo(backup))
            throw new AtomicFileBackupCreateFeedback();
        fileChannel.truncate(0);
        writeContent(Channels.newOutputStream(fileChannel));

        if(!backup.delete()) throw new AtomicFileBackupDeleteException();

        refresh();

    }

    /**
     * Close the file.
     *
     * @throws IOException thrown when the file couldn't be closed.
     */
    public void close() throws IOException {
        fileLock.close();
        fileChannel.close();
    }

    public void refresh() throws IOException {
        if(fileChannel != null && fileChannel.isOpen()) close();
        open();
    }

}
