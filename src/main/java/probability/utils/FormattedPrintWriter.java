package probability.utils;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.BreakIterator;
import java.util.Objects;

public class FormattedPrintWriter extends Writer {

  private static final String INDENTION_STRING = "  ";

  private static final String TITLE_STRING = "==";

  private final PrintWriter _writer;

  private final int _lineWidth;

  private String _prefix;

  private int _indentionLevel;

  public FormattedPrintWriter(Writer out, int lineWidth) {
    _writer = new PrintWriter(out);
    _lineWidth = lineWidth;
    _prefix = "";
  }

  public FormattedPrintWriter(OutputStream out, int lineWidth) {
    _writer = new PrintWriter(out, true);
    _lineWidth = lineWidth;
    _prefix = "";
  }

  private static String trimEnd(String s) {
    for (int i = s.length() - 1; i >= 0; i--) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return s.substring(0, i + 1);
      }
    }

    return s;
  }

  private static String capitalizeWords(String string) {

    StringBuilder sb = new StringBuilder(string.length());

    BreakIterator wb = BreakIterator.getWordInstance();
    wb.setText(string);

    int start = wb.first();
    for (int end = wb.next(); end != BreakIterator.DONE; start = end, end = wb.next()) {

      char firstChar = string.charAt(start);
      if (Character.isWhitespace(firstChar)) {
        sb.append(string.substring(start, end));
      } else {
        sb.append(Character.toTitleCase(firstChar)).append(string.substring(start + 1, end));
      }
    }

    return sb.toString();
  }

  public void setIndentionLevel(int level) {
    _indentionLevel = level;
  }

  public void setPrefixString(String prefix) {
    _prefix = Objects.requireNonNull(prefix);
  }

  public void println() {
    _writer.println(_prefix);
  }

  public void println(Object object) {
    String s = object.toString();

    final int usableWidth =
        _lineWidth - _prefix.length() - INDENTION_STRING.length() * _indentionLevel;

    int offset = 0;
    BreakIterator wb = BreakIterator.getLineInstance();
    wb.setText(s);

    for (int end = wb.next(); end != BreakIterator.DONE; end = wb.next()) {
      if (end - offset > usableWidth) {
        printlnNotWrapped(s.substring(offset, end));
        offset = end;
      }
    }

    printlnNotWrapped(s.substring(offset));
  }

  public void printlnNotWrapped(String string) {

    String trimmed = trimEnd(string);
    if (!trimmed.isEmpty()) {
      _writer.println(_prefix + Strings.repeat(INDENTION_STRING, _indentionLevel) + string);
    } else {
      _writer.println(_prefix);
    }
  }

  public void printlnTitle(String string) {

    String title = capitalizeWords(string.trim());

    final int length =
        Math.min(title.length(), _lineWidth - _prefix.length() - 2 * TITLE_STRING.length() - 2);

    _writer.println(_prefix + TITLE_STRING + ' ' + title.substring(0, length) + ' ' + TITLE_STRING);
  }

  @Override public void write(char[] cbuf, int off, int len) {
    throw new IllegalStateException();
  }

  @Override public void flush() throws IOException {
    _writer.flush();
  }

  @Override public void close() throws IOException {
    _writer.close();
  }
}
