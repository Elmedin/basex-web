package org.basex.web.servlet.impl;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.basex.query.item.map.Map;
import org.basex.web.parser.InlineXQuery;
import org.basex.web.servlet.PrepareParamsServlet;

/**
 * Parses HTML files with inline XQuery.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public class Html extends PrepareParamsServlet {


    /** This is me, your version. */
    private static final long serialVersionUID = -7694236920689548933L;

    @Override
    public final void get(final HttpServletRequest request,
        final HttpServletResponse response, final Map get, final Map post)
        throws IOException {
        response.setContentType("text/html");

        final String uri = request.getRequestURI();
        final File f = requestedFile(uri);
        if(!f.exists()) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND,
              "The file '" + uri + "' can't be found on the server.");
        } else {
          response.setStatus(HttpServletResponse.SC_OK);
          response.getWriter().write(
                  new InlineXQuery(f).eval(get, post));
        }
    }


}
