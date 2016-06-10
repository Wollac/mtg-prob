package probability.utils;

import com.google.common.io.Files;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;

import probability.messages.Messages;

public abstract class ReadOrWriteDefaultIO {

    private final Logger _log;

    protected ReadOrWriteDefaultIO(Logger log) {

        _log = Objects.requireNonNull(log);
    }

    protected boolean readOrWriteDefault(File file, Charset charset) {

        boolean readSuccess = read(file, charset);

        if (readSuccess) {
            return true;
        }

        return write(file, charset);
    }

    private boolean read(File file, Charset charset) {

        if (!file.exists()) {

            return false;
        }

        boolean readSuccess;
        try (Reader reader = Files.newReader(file, charset)) {

            readSuccess = read(reader);
        } catch (IOException e) {

            readSuccess = false;
            _log.error(Messages.get().readException(file.getName(), e.getLocalizedMessage()));
        }

        return readSuccess;
    }

    private boolean write(File file, Charset charset) {


        if (file.exists()) {

            return false;
        }

        boolean writeSuccess;
        try (Writer writer = Files.newWriter(file, charset)) {

            _log.warn(Messages.get().writeDefault(file.getName()));

            writeDefault(writer);
            writeSuccess = true;

        } catch (IOException e) {

            writeSuccess = false;
            _log.error(Messages.get().writeException(file.getName(), e.getLocalizedMessage()));
        }


        return writeSuccess;
    }

    abstract protected boolean read(Reader reader) throws IOException;

    abstract protected void writeDefault(Writer writer) throws IOException;
}
