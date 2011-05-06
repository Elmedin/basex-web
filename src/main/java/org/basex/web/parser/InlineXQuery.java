package org.basex.web.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.basex.web.xquery.BaseXClient;

/**
 * Parses and substitutes inline XQuery with a query result.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 *
 */
public final class InlineXQuery {

    /** GET/Post Params. */
    private final String pr;
    /** GET/Post Params. */
    private final String file;
    /**
     * Parses and executes inline xquery.
     * @param f file to query
     * @param prepend get/post params
     */
    public InlineXQuery(final String f, final String prepend) {
        this.pr = prepend;
        this.file = f;
    }

    /**
     * Denotes start of query.
     */
    static final char[] START = "<?xq".toCharArray();
    /**
     * Denotes end of query.
     */
    static final char[] END = "?>".toCharArray();

    /**
     * Parses HTML for <?xq ... ?>. *TODO* parse queries that contain ?>
     * intermediate.
     *
     * @return the result of the parse
     * @throws IOException on error.
     */
    public  String parse() throws IOException {
        final BufferedReader fis =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                        file)));
        final StringBuilder sb = new StringBuilder(128);
        while (fis.ready()) {
            consumeXQ(fis, sb);
            sb.append((char) fis.read());
        }
        return sb.toString();
    }

    /**
     * Tries to consume <?xq starting tag.
     *
     * @param fis
     *            file input stream
     * @param sb
     *            resultbuffer to fill.
     * @throws IOException on error.
     */
    private  void consumeXQ(final BufferedReader fis,
            final StringBuilder sb) throws IOException {
        int apos = -1;
        fis.mark(START.length);
        final StringBuilder curQuery = new StringBuilder();
        boolean inqry = false;
        while (fis.ready() && ((char) fis.read()) == START[++apos]) {
            if (apos == START.length - 1) {
                inqry = true;
                break;
            }
        }
        if (inqry) {
            while (!consumeEnd(fis) && fis.ready()) {
                curQuery.append((char) fis.read());
            }

            sb.append(BaseXClient.exec(curQuery.toString(), this.pr));
        } else {
            fis.reset();
        }
    }

    /**
     * Tries to consume an ?> end of a xquery block.
     * @param fis file input stream
     * @return true if end detected
     * @throws IOException on err
     */
    private static boolean consumeEnd(final BufferedReader fis)
            throws IOException {
        int epos = -1;
        fis.mark(END.length);
        while (fis.ready() && ((char) fis.read()) == END[++epos]) {
            if (epos == END.length - 1) {
                return true;
            }

        }
        fis.reset();
        return false;
    }
}
