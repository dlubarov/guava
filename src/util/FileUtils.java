package util;

import java.io.*;

public final class FileUtils {
    private FileUtils() {}

    public static byte[] readFile(File f) throws IOException {
        InputStream in = new FileInputStream(f);
        byte[] result = new byte[(int) f.length()];
        int pos = 0;
        while (pos < result.length) {
            int n = in.read(result, pos, result.length - pos);
            if (n == -1)
                throw new IOException("file changed while reading?");
            pos += n;
        }
        return result;
    }

    public static String readFileAsUTF8(File f) throws IOException {
        return new String(readFile(f), "UTF-8");
    }
}
