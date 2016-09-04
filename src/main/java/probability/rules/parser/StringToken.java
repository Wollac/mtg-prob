package probability.rules.parser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static probability.rules.parser.StringTokenizer.QUOTE_CHAR;

/**
 * A String token is the building block of a rule string.
 * It contains a type, the actual representation as a string and its position in the original input
 * string.
 */
public final class StringToken {

    private final TokenType _type;

    private final String _string;

    private final int _pos;

    StringToken(TokenType type, String string, int pos) {

        checkNotNull(type);
        checkNotNull(string);
        checkArgument(pos >= 0);

        _type = type;
        _string = string;
        _pos = pos;
    }

    /** Returns the type of the token. */
    public TokenType getType() {
        return _type;
    }

    /** Returns the string value of the token. */
    public String getString() {
        return _string;
    }

    /** Returns the position of the token in the input. */
    public int getPos() {
        return _pos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StringToken)) {
            return false;
        }
        StringToken other = (StringToken) obj;
        return Objects.equals(_type, other._type) && Objects.equals(_pos, other._pos) && Objects.equals(_string, other._string);
    }

    @Override
    public String toString() {
        return _string + ":" + _pos;
    }

    /** All feasible types of string tokens represented by their regular expression. */
    public enum TokenType {

        /** Opening parenthesis */
        OPEN_PARENTHESIS("\\("),
        /** Closing parenthesis */
        CLOSE_PARENTHESIS("\\)"),
        /** String enclosed in quotation marks, containing arbitrary characters */
        QUOTED_STRING(QUOTE_CHAR + "([^" + QUOTE_CHAR + "]*)" + QUOTE_CHAR),
        /** String containing no white spaces or quotations */
        STRING("[^\\s\\(\\)" + QUOTE_CHAR + "]+");

        private final Pattern _pattern;

        TokenType(String regex) {
            _pattern = Pattern.compile("^(" + regex + ")");
        }

        /** Matches the given input with the token type. */
        Matcher match(CharSequence input) {
            return _pattern.matcher(input);
        }
    }
}
