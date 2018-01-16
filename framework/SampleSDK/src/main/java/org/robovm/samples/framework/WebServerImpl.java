package org.robovm.samples.framework;

import fi.iki.elonen.SimpleWebServer;
import org.robovm.apple.foundation.NSObject;

import java.io.File;
import java.io.IOException;

/**
 * simple webserver api implementation
 */
public class WebServerImpl extends NSObject implements Api.WebServer {
    private SimpleWebServer server;

    @Override
    public void start() {
        if (server != null) {
            System.out.println("WebServer already started ");
            return;
        }
        server = new SimpleWebServer(null, 8081, new File("/"), false);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
