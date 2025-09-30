package me.passwordmanager.file;

import lombok.Getter;
import me.passwordmanager.file.exception.*;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class AtomicFileWrapper {

    public static final String BACKUP_TMP_FILENAME = ".%s.tmp";
    public static final String BACKUP_COMPLETE_FILENAME = ".%s.backup";

    @Getter
    private final String filePath;

    private FileChannel fileChannel;
    private FileLock fileLock;

    public AtomicFileWrapper(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return FileUtils.getFileName(getFilePath());
    }

    public String getWorkingDirectory() {
        return FileUtils.getAbsoluteDirectoryPath(getFilePath());
    }

    private String getTmpFilePath() {
        return getWorkingDirectory() + String.format(BACKUP_TMP_FILENAME, getFileName());
    }

    private String getBackupFilePath() {
        return getWorkingDirectory() + String.format(BACKUP_COMPLETE_FILENAME, getFileName());
    }

    public void open() throws IOException {
        fileChannel = FileChannel.open(Paths.get(getFilePath()), StandardOpenOption.READ, StandardOpenOption.WRITE);
        if((fileLock = fileChannel.tryLock()) == null) throw new AtomicFileLockedException();
        if(backupExists()) throw new AtomicFileCorruptedException(true);
        readContent(Channels.newInputStream(fileChannel));
    }

    private boolean backupExists() {
        return new File(getBackupFilePath()).exists();
    }


    public void restore() throws IOException {
        var backup = FileUtils.getFile(getBackupFilePath());
        StreamUtils.writeAll(Channels.newOutputStream(fileChannel), new FileInputStream(backup));
        if(!backup.delete()) throw new AtomicFileBackupDeleteException();
    }

    public void checkFilePath() throws IOException {
        var file = new File(getFilePath());
        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isFile()) throw new IncorrectFileTypeException();
    }

    protected abstract void writeContent(OutputStream outputStream) throws IOException;
    protected abstract void readContent(InputStream inputStream) throws IOException;

    public void save() throws IOException {
        var tmp = FileUtils.getFile(getTmpFilePath());
        writeContent(new FileOutputStream(tmp));

        if(!tmp.renameTo(new File(getBackupFilePath())))
            throw new AtomicFileBackupCreateFeedback();

        writeContent(Channels.newOutputStream(fileChannel));

        if(!tmp.delete()) throw new AtomicFileBackupDeleteException();
    }

    public void close() throws IOException {
        fileLock.close();
        fileChannel.close();
    }

}
