package probability.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;


public abstract class ReadOrWriteDefaultIO {

    protected ReadOrWriteDefaultIO(File file, Charset charset) {

        boolean readSuccess = read(file, charset);

        if (!readSuccess) {
            write(file, charset);
        }
    }

    private boolean read(File file, Charset charset) {

        boolean readSuccess = false;
        try (Reader reader = Files.newReader(file, charset)) {

            readSuccess = read(reader);
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
        }

        return readSuccess;
    }

    private void write(File file, Charset charset) {

        if (file.isFile() && !file.exists()) {

            try (Writer writer = Files.newWriter(file, charset)) {

                writeDefault(writer);
            } catch (IOException e) {
                System.err.println("Could not write file: " + e.getMessage());
            }
        }
    }

    abstract protected boolean read(Reader reader) throws IOException;

    abstract protected void writeDefault(Writer writer) throws IOException;
}
