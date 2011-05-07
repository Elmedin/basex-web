package org.basex.web.servlet.impl;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.basex.query.item.map.Map;
import org.basex.web.parser.InlineXQuery;
import org.basex.web.servlet.PrepareParamsServlet;
import org.eclipse.jetty.http.HttpException;

/**
 * Parses HTML files with inline XQuery.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public class Html extends PrepareParamsServlet {

  /**
   * Constructor.
   * @throws IOException if the server root can't be resolved
   */
  public Html() throws IOException {
    super(new File("src/main/webapp"));
  }

  /** This is me, your version. */
  private static final long serialVersionUID = -7694236920689548933L;

  @Override
  public final void get(final HttpServletRequest request,
      final HttpServletResponse response, final Map get, final Map post)
      throws IOException {
    response.setContentType("text/html");

    final String uri = request.getRequestURI();
    final File f;
    try {
      f = requestedFile(uri);
    } catch(final HttpException e) {
      response.sendError(e.getStatus(), e.getReason());
      return;
    }

    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(new InlineXQuery(f).eval(get, post));
  }

}
