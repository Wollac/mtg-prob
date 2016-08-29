package probability.utils;

import com.google.common.io.Files;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;

public abstract class ReadOrWriteDefaultIO {

    private final File _file;

    private final Charset _charset;

    protected ReadOrWriteDefaultIO(File file, Charset charset) {

        _file = Objects.requireNonNull(file);
        _charset = Objects.requireNonNull(charset);
    }

    protected ReadOrWriteDefaultIO(String fileName, Charset charset) {

        this(new File(fileName), charset);
    }

    public void readOrWriteDefault() throws IOException {

        if (_file.exists()) {

            read(_file, _charset);
        } else {

            Logger.debug("File {} does not exist", _file.getPath());
            write(_file, _charset);
        }

    }

    private void read(File file, Charset charset) throws IOException {

        try (Reader reader = Files.newReader(file, charset)) {

            read(reader);
        } catch (FileNotFoundException e) {

            throw new IOException(e);
        }
    }

    private void write(File file, Charset charset) throws IOException {

        try (Writer writer = Files.newWriter(file, charset)) {

            writeDefault(writer);
        }
    }

    public String getFileName() {

        return _file.getName();
    }

    abstract protected void read(Reader reader) throws IOException;

    abstract protected void writeDefault(Writer writer) throws IOException;
}
