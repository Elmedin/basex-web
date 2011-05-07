package org.basex.web.servlet;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Map.Entry;
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
import org.eclipse.jetty.http.HttpException;

/**
 * This class handles GET or POST requests and prepares a Map with the
 * respective values.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public abstract class PrepareParamsServlet extends HttpServlet {
  /** Version. */
  private static final long serialVersionUID = 8548004356377035911L;
  /** the web root: *TODO* this path might be configurable in future version. */
  private final String fPath;

  /** Constructor. */
  public PrepareParamsServlet() {
    try {
      fPath = new File("src/main/webapp").getCanonicalPath();
    } catch(final IOException e) {
      // should never happen
      throw new IOError(e);
    }
  }

  @Override
  protected final void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {

    final Map get = getMap(req);
    final Map post = getPost(req);

    try {
      get(resp, requestedFile(req.getRequestURI()), get, post);
    } catch(final HttpException e) {
      resp.sendError(e.getStatus(), e.getReason());
    }
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
   * @param resp response object
   * @param f requested file
   * @param get GET map
   * @param post POST map
   * @throws IOException I/O exception
   */
  public abstract void get(final HttpServletResponse resp, final File f,
      final Map get, final Map post) throws IOException;

  /**
   * Checks whether the file exists.
   * @param file file
   * @return File object
   * @throws HttpException HTTP exception
   */
  private File requestedFile(final String file) throws HttpException {
    final File f = new File(fPath, file);
    if(!f.exists()) throw new HttpException(HttpServletResponse.SC_NOT_FOUND,
        "The file '" + file + "' doesn't exist on the server.");
    try {
      final File canon = f.getCanonicalFile();
      if(!canon.toString().startsWith(fPath)) throw new HttpException(
          HttpServletResponse.SC_FORBIDDEN, "The requested file '"
          + file + "' isn't below the server root.");
      return canon;
    } catch(final IOException ioe) {
      // TODO too much information / unsafe?
      throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          ioe.getLocalizedMessage());
    }
  }

}
