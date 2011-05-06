package org.basex.web.xquery;

import java.io.File;
import java.io.IOException;
import org.basex.core.Context;
import org.basex.io.IOFile;
import org.basex.io.TextInput;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.item.map.Map;

/**
 * Provides static methods to access BaseX.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class BaseXClient {
  /** Do not construct me. */
  private BaseXClient() { /* void */ }

  /** Query Context. */
  private static final Context CTX = new Context();

  /**
   * This Method reads and returns the result of a whole query.
   * @param f the filename
   * @param get GET map
   * @param post POST map
   * @return the query result
   * @throws IOException exception
   */
  public static String query(final File f, final Map get, final Map post)
      throws IOException {
    return exec(TextInput.content(new IOFile(f)).toString(), get, post);
  }

  /**
   * Executes a query string.
   * @param qu query string
   * @param get GET map
   * @param post POST map
   * @return the query result.
   */
  public static String exec(final String qu, final Map get, final Map post) {
    try {
      System.err.format("===\n%s\n=====", qu);
      QueryProcessor qp = new QueryProcessor(qu, CTX);
      qp.bind("GET", get);
      qp.bind("POST", post);
      return qp.execute().toString();
    } catch(QueryException e) {
      return "<div class=\"error\">" + e.getMessage() + "</div>";
    }
  }
}
