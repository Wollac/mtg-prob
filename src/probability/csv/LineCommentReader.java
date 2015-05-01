package probability.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class LineCommentReader extends StringReader {

	private static final String[] DEFAULT_COMMENT = { "#", "//" };

	public LineCommentReader(Reader in, String... comments) throws IOException {

		super(initString(in, comments));
	}

	public LineCommentReader(Reader in) throws IOException {

		this(in, DEFAULT_COMMENT);
	}

	private static String initString(Reader in, String[] comments)
			throws IOException {

		BufferedReader reader = new BufferedReader(in);

		StringBuilder builder = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			line = line.trim();

			if (line.isEmpty()) {
				continue;
			}

			if (startsWithComment(line, comments)) {
				continue;
			}

			builder.append(line + '\n');

		}

		return builder.toString();
	}

	private static boolean startsWithComment(String line, String[] comments) {

		for (String comment : comments) {
			if (line.endsWith(comment)) {
				return true;
			}
		}

		return false;
	}
}
