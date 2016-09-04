package probability.utils;

import com.google.common.io.CharStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * A simple Reader that parses the input line by line, removing empty lines and lines starting with
 * single line comment tags.
 * <p>
 * Extending BufferedReader is fine, as we are only overwriting the constructor.
 */
public final class LineCommentReader extends BufferedReader {

    private static final String[] DEFAULT_COMMENT_TAGS = {"#", "//"};

    private LineCommentReader(Readable r, String... commentTags)
            throws IOException {

        super(new StringReader(initString(r, commentTags)));
    }

    public LineCommentReader(Readable r) throws IOException {

        this(r, DEFAULT_COMMENT_TAGS);
    }

    private static String initString(Readable r, String[] commentTags)
            throws IOException {

        StringBuilder builder = new StringBuilder();

        for (String line : CharStreams.readLines(r)) {
            line = line.trim();

            if (!line.isEmpty() && !startsWithComment(line, commentTags)) {
                builder.append(line).append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    private static boolean startsWithComment(String line, String[] comments) {

        for (String comment : comments) {
            if (line.startsWith(comment)) {
                return true;
            }
        }

        return false;
    }
}
