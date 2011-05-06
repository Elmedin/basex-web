package org.basex.web.xquery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.basex.core.Context;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.item.map.Map;

/**
 * Provides static methods to access BaseX.
 *@author michael
 *
 */
public final class BaseXClient {
    /**
     *Do not construct me.
     */
    private BaseXClient() {
    }

    /** Query Context. */
    private static final Context CTX = new Context();

    /**
     *This Method reads and returns the result of a whole query.
     *
     *@param f the filename
     * @param get GET map
     * @param post POST map
     *@return the query result
     */
    public static String query(final File f, final Map get, final Map post) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return e2.getMessage();
        }
        final StringBuffer sb = new StringBuffer();
        try {
            while (br.ready()) {
                sb.append(br.readLine());
                sb.append(" ");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.getMessage();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return exec(sb.toString(), get, post);

    }

    /**
     *Executes a query string.
     * @param qu query string
     * @param get GET map
     * @param post POST map
     *@return the query result.
     */
    public static String exec(final String qu, final Map get, final Map post) {
        try {
            System.err.format("===\n%s\n=====", qu);
            QueryProcessor qp = new QueryProcessor(qu, CTX);
            qp.bind("GET", get);
            qp.bind("POST", post);
            return qp.execute().toString();
        } catch (QueryException e) {
            return "<div class=\"error\">" + e.getMessage() + "</div>";
        }
    }
}
