package org.basex.web.servlet.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.query.item.map.Map;
import org.basex.web.servlet.PrepareParamsServlet;
import org.basex.web.xquery.BaseXClient;

/**
 * This class parses complete XQuery files or modules and returns their result.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 *
 */
public class XQuery extends PrepareParamsServlet {


    /** This is me, your version. */
    private static final long serialVersionUID = -7694236920689548933L;

    @Override
    public final void get(final HttpServletRequest request,
            final HttpServletResponse response, final Map get, final Map post)
    throws ServletException, IOException {
        response.setContentType("text/html");

        File f = requestedFile(request.getRequestURI().toString());
        if (!f.exists()) {
            throw new ServletException("File " + f.getName() + " not found");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(BaseXClient.query(f,
            get, post));
    }


}
