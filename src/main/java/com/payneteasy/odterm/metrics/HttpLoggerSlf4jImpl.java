package com.payneteasy.odterm.metrics;

import com.payneteasy.http.server.log.IHttpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpLoggerSlf4jImpl implements IHttpLogger {

    private static final Logger LOG = LoggerFactory.getLogger( HttpLoggerSlf4jImpl.class );

    @Override
    public void debug(String s, Object... objects) {
        LOG.debug(s, objects);
    }

    @Override
    public void error(String s) {
        LOG.debug(s);
    }

    @Override
    public void error(String s, Exception e) {
        LOG.error(s, e);
    }
}
