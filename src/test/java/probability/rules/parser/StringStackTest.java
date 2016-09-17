package probability.rules.parser;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringStackTest {

    private final String STRING = "Test String";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testNullInput() {

        exception.expect(NullPointerException.class);
        new StringStack(null);
    }

    @Test
    public void testEmptyInput() {

        StringStack stack = new StringStack("");

        assertEquals("", stack);
    }

    @Test
    public void testPopEmptyInput() {

        StringStack stack = new StringStack("");

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.pop(1);
    }

    @Test
    public void testOriginal() {

        StringStack stack = new StringStack(STRING);

        assertEquals(STRING, stack);
    }

    @Test
    public void testPopNegative() {

        StringStack stack = new StringStack(STRING);

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.pop(-1);
    }

    @Test
    public void testPop() {

        StringStack stack = new StringStack(STRING);

        stack.pop(1);
        assertEquals(STRING.substring(1), stack);

        stack.pop(1);
        assertEquals(STRING.substring(2), stack);
    }

    @Test
    public void testPopEntireString() {

        StringStack stack = new StringStack(STRING);

        stack.pop(STRING.length());
        assertEquals("", stack);

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.charAt(0);
    }

    @Test
    public void testSubSequenceOriginal() {

        StringStack stack = new StringStack(STRING);

        int start = 0;
        int end = STRING.length();

        Assert.assertEquals(STRING.subSequence(start, end), stack.subSequence(start, end));
    }

    @Test
    public void testSubSequenceAfterPop() {

        StringStack stack = new StringStack(STRING);

        int start = 1;
        int end = 8;

        stack.pop(1);

        Assert.assertEquals(STRING.subSequence(start + 1, end + 1), stack.subSequence(start, end));
    }

    @Test
    public void testSubSequenceInvalidStart() {

        StringStack stack = new StringStack(STRING);

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.subSequence(-1, STRING.length());
    }

    @Test
    public void testSubSequenceInvalidEnd() {

        StringStack stack = new StringStack(STRING);

        stack.pop(1);

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.subSequence(0, STRING.length());
    }

    @Test
    public void testSubSequenceInvalidSequence() {

        StringStack stack = new StringStack(STRING);

        exception.expect(StringIndexOutOfBoundsException.class);
        stack.subSequence(1, 0);
    }

    private static void assertEquals(String expectedString, StringStack stack) {

        Assert.assertEquals(expectedString.length(), stack.length());

        // compare using charAt
        Assert.assertTrue(expectedString.contentEquals(stack));

        // compare using toString
        Assert.assertEquals(expectedString, stack.toString());
    }
}
