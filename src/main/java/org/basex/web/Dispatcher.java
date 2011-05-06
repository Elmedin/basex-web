package org.basex.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class has been here for testing purposes and will be deleted soon.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 *
 */
public class Dispatcher extends HttpServlet {

    /** This is me, your version. */
    private static final long serialVersionUID = -7694236920689548933L;

    @Override
    protected final void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println(
                "session=" + request.getSession(true).getId());
    }

}
