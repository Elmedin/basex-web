package org.basex.web.xquery;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.XQuery;
import org.basex.query.QueryProcessor;

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
     *@param f
     *           the filename
     *@param prepend
     *           get & post params
     *@return the query result
     */
    public static String query(final String f, final String prepend) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return e2.getMessage();
        }
        final StringBuffer sb = new StringBuffer();
        try {
            while (br != null && br.ready()) {
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
        return exec(sb.toString(), prepend);

    }

    /**
     *Executes a query string.
     *     *@param data
     *           the query string
     *@param prepend
     *           get & post params
     *@return the query result.
     */
    public static String exec(final String data, final String prepend) {
        try {
            final String query = String.format("%s\n%s", prepend, data);
            System.err.format("===\n%s\n=====", query);
            QueryProcessor qp = new QueryProcessor(query, CTX);
            //qp.bind("", )
            return new XQuery(query).execute(CTX);
        } catch (BaseXException e) {
            return "<div class=\"error\">" + e.getMessage() + "</div>";
        }
    }
}
