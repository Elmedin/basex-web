package org.basex.web.servlet;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.basex.query.QueryException;
import org.basex.query.item.Atm;
import org.basex.query.item.Item;
import org.basex.query.item.Seq;
import org.basex.query.item.Str;
import org.basex.query.item.map.Map;
import org.basex.util.Token;

/**
 * This class handles GET or POST requests and prepares a Map with the
 * respective values.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public abstract class PrepareParamsServlet extends HttpServlet {
  /** Version. */
  private static final long serialVersionUID = 8548004356377035911L;
  /**
   * the web root: *TODO* this path might be configurable in future version.
   */
  protected static String fPath = "src/main/webapp";

  @Override
  protected final void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException, ServletException {
    final Map get = getMap(req);
    final Map post = getPost(req);
    get(req, resp, get, post);
  }

  /**
   * Populates the POST variables map.
   * @param req request
   * @return POST map
   */
  @SuppressWarnings("unused")
  private Map getPost(final HttpServletRequest req) {
    return Map.EMPTY;
  }

  /**
   * Populates the GET variables map.
   * @param req request
   * @return GET map
   */
  private Map getMap(final HttpServletRequest req) {
    @SuppressWarnings("unchecked")
    java.util.Map<String, String[]> pars = req.getParameterMap();

    Map get = Map.EMPTY;
    for(final Entry<String, String[]> e : pars.entrySet()) {
      final Str key = Str.get(e.getKey());
      final String[] v = e.getValue();
      final Item[] val = new Item[v.length];
      for(int i = val.length; --i >= 0;) val[i] = new Atm(Token.token(v[i]));
      try {
        get = get.insert(key, Seq.get(val, val.length), null);
      } catch(final QueryException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
    return get;
  }

  @Override
  public final void doPost(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    // TODO
  }

  /**
   * Performs the actual get, this is needed to allow
   * {@link PrepareParamsServlet} collecting the parameters before delegating
   * the actual get or post of the implementation.
   * @param req request
   * @param resp response object
   * @param get GET map
   * @param post POST map
   * @throws ServletException servlet
   * @throws IOException io
   */
  public abstract void get(final HttpServletRequest req,
      final HttpServletResponse resp, final Map get, final Map post)
  throws ServletException, IOException;
}
