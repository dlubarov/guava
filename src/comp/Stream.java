package comp;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

public final class Stream {
    private final InputStream source;
    private final StringBuffer buffer;
    private final int pos;

    public Stream(InputStream source, StringBuffer buffer, int pos) {
        this.source = source;
        this.buffer = buffer;
        this.pos = pos;
    }

    public Stream(InputStream source) {
        this(source, new StringBuffer(), 0);
    }

    private char readChar() throws IOException {
        int res;
        try {
            res = source.read();
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
        if (res == -1) {
            source.close();
            throw new NoSuchElementException();
        }
        return (char) res;
    }

    public char head() throws IOException {
        while (buffer.length() <= pos)
            buffer.append(readChar());
        return buffer.charAt(pos);
    }

    public String next(int n) throws IOException {
        // TODO: read into array for efficiency
        while (buffer.length() < pos + n)
            buffer.append(readChar());
        return buffer.substring(pos, pos + n);
    }

    public Stream advance(int n) {
        return new Stream(source, buffer, pos + n);
    }

    public Stream tail() {
        return new Stream(source, buffer, pos + 1);
    }

    public boolean equals(Object o) {
        try {
            Stream that = (Stream) o;
            return source.equals(that.source) && pos == that.pos;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return source.hashCode() ^ pos;
    }

    public String toString() {
        return String.format("LazyList of %s, pos=%d, buf=%d", source, pos, buffer.length());
    }
}
