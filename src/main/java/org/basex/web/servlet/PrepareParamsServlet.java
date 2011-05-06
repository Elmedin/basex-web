package org.basex.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.query.QueryException;
import org.basex.query.item.Atm;
import org.basex.query.item.Str;

/**
 * This class handles GET or POST requests and prepares a HashMap with the
 * respective values. It then calls the
 * {@link #get(HttpServletRequest, HttpServletResponse)} method of its
 * imeplementing class.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 *
 */
public abstract class PrepareParamsServlet extends HttpServlet {
    /** Version. */
    private static final long serialVersionUID = 8548004356377035911L;
    /** The GET map. */
    private final HashMap<String, String> getVars =
            new HashMap<String, String>();
    /** The POST array. */
    private final HashMap<String, String> postVars =
            new HashMap<String, String>();
    /**
     * the web root: *TODO* this path might be configurable in future version.
     */
    protected static String fPath = "src/main/webapp";

    /**
     * Returns the GET & POST maps. *TODO* implement $_POST
     *
     * @return XQuery String with the supplied data.
     */
    protected final String pr() {
        org.basex.query.item.map.Map get = org.basex.query.item.map.Map.EMPTY;
        int count = getVars.size();
        try {

            for (Entry<String, String> entr : getVars.entrySet()) {
                System.out.println(entr.getValue());
                get = get.insert(Str.get(entr.getKey()),
                        Atm.get(entr.getValue()), null);
                if (count-- == 1) {
                    break;
                }
            }
        } catch (QueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.err.println(get.toString());
        return "";
    }

    @Override
    protected final void doGet(final HttpServletRequest req,
            final HttpServletResponse resp) throws IOException,
            ServletException {
        doGetOrPost(req, resp, getVars);
        get(req, resp);
    }

    /**
     * Fills the right map with its data.
     * @param req
     *            request.
     * @param resp
     *            respone (ignored).
     * @param map
     *            the map to fill.
     */
    private void doGetOrPost(final HttpServletRequest req,
            final HttpServletResponse resp, final HashMap<String, String> map) {
        emptyMaps();

        @SuppressWarnings("unchecked")
        Map<String, String[]> pars = req.getParameterMap();
        for (String key : pars.keySet()) {
            String[] vals = req.getParameterValues(key);
            if (null != vals) {
                System.out.println("Var: " + vals[0]);
                map.put(key, join(vals, ","));
            }
        }

    }

    /**
     * Resets the parameter maps.
     */
    private void emptyMaps() {
        getVars.clear();
        postVars.clear();
    }

    @Override
    public final void doPost(final HttpServletRequest req,
            final HttpServletResponse resp) throws IOException {
        doGetOrPost(req, resp, postVars);
    }

    /**
     * Performs the actual get, this is needed to allow
     * {@link PrepareParamsServlet} collecting the parameters before delegating
     * the actual get or post of the implementation.
     * @param req
     *            request
     * @param resp
     *            response object
     * @throws ServletException
     *             servlet
     * @throws IOException
     *             io
     */
    public abstract void get(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException;

    /**
     * Joins a String with delimiter.
     *
     * @param strs
     *            String array
     * @param delim
     *            Delimiter
     * @return the joined String
     */
    private String join(final String[] strs, final String delim) {
        final StringBuilder sb = new StringBuilder();
        if (strs.length > 1) {
            sb.append("(");
        }
        for (int p = 0; p < strs.length; p++) {
            sb.append('"');
            sb.append(strs[p]);
            sb.append('"');
            if (p == strs.length - 1) {
                break;
            }
            sb.append(",");
        }
        if (strs.length > 1) {
            sb.append(")");
        }

        return sb.toString();
    }
}
