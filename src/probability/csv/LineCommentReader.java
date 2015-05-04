package probability.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * A simple Reader that parses the input line by line, removing empty lines and
 * lines starting with single line comment tags.
 * 
 * Extending StringReader is fine, as we are only overwriting the constructor.
 */
class LineCommentReader extends StringReader {

	private static final String[] DEFAULT_COMMENT_TAGS = { "#", "//" };

	public LineCommentReader(Reader in, String... commentTags)
			throws IOException {

		super(initString(in, commentTags));
	}

	public LineCommentReader(Reader in) throws IOException {

		this(in, DEFAULT_COMMENT_TAGS);
	}

	private static String initString(Reader in, String[] commentTags)
			throws IOException {

		BufferedReader reader = new BufferedReader(in);

		StringBuilder builder = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			line = line.trim();

			if (line.isEmpty()) {
				continue;
			}

			if (startsWithComment(line, commentTags)) {
				continue;
			}

			builder.append(line + '\n');

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
