package probability.rules.parser;

/** Makes a character sequence accessible as a stack without creating new instances. */
class StringStack implements CharSequence {

    private final char[] _value;

    private final int _count;

    private int _offset;

    StringStack(String str) {

        _count = str.length();
        _value = new char[_count];
        str.getChars(0, _count, _value, 0);

        _offset = 0;
    }

    /** Pops the first {@code n} characters of the string. */
    public void pop(int n) {

        if (n < 0 || n > length()) {
            throw new StringIndexOutOfBoundsException(n);
        }
        _offset += n;
    }

    @Override
    public int length() {

        assert _offset <= _count;
        return _count - _offset;
    }

    @Override
    public char charAt(int index) {

        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return _value[_offset + index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {

        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (end > length()) {
            throw new StringIndexOutOfBoundsException(end);
        }
        if (start > end) {
            throw new StringIndexOutOfBoundsException(end - start);
        }
        return new String(_value, _offset + start, end - start);
    }

    @Override
    public String toString() {

        return new String(_value, _offset, length());
    }
}
