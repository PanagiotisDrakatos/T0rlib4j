package net.sf.T0rlib4j.controller.network;

import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JavaTorRelay {
    private JavaOnionProxyManager onionProxyManager;

    private static final int TOTAL_SEC_PER_STARTUP = 4 * 60;
    private static final int TRIES_PER_STARTUP = 5;

    private static final String PROXY_LOCALHOST = "127.0.0.1";


    private static final Logger LOG = LoggerFactory.getLogger(JavaTorRelay.class);

    public JavaTorRelay(File torDirectory) throws IOException {
        onionProxyManager = new JavaOnionProxyManager(new JavaOnionProxyContext(torDirectory));
        this.initTor();
    }


    public ServiceDescriptor createHiddenService(final int localPort, final int servicePort) throws IOException {
        return createHiddenService(localPort, servicePort, null);
    }

    public ServiceDescriptor createHiddenService(final int localPort, final int servicePort,
                                                 final NetLayerStatus listener) throws IOException {
        LOG.debug("Publishing Hidden Service. This will at least take half a minute...");
        final String hiddenServiceName = onionProxyManager.publishHiddenService(servicePort, localPort);
        final ServiceDescriptor serviceDescriptor = new ServiceDescriptor(hiddenServiceName,
                localPort, servicePort);
        if (listener != null)
            onionProxyManager.attachHiddenServiceReadyListener(serviceDescriptor, listener);
        return serviceDescriptor;
    }

    public ServiceDescriptor createHiddenService(int port, NetLayerStatus listener)
            throws IOException {
        return createHiddenService(port, port, listener);
    }

    public int getLocalPort() throws IOException {
        return onionProxyManager.getIPv4LocalHostSocksPort();
    }

    public void initTor()
            throws IOException {

        LOG.debug("Trying to start tor in directory {}", onionProxyManager.getWorkingDirectory());

        try {
            if (!onionProxyManager.startWithRepeat(TOTAL_SEC_PER_STARTUP, TRIES_PER_STARTUP)) {
                throw new IOException("Could not Start Tor.");
            } else {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            onionProxyManager.stop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public void addHiddenServiceReadyListener(ServiceDescriptor serviceDescriptor,
                                              NetLayerStatus listener) throws IOException {
        onionProxyManager.attachHiddenServiceReadyListener(serviceDescriptor, listener);
    }

    public void ShutDown() throws IOException {
        onionProxyManager.stop();
    }

}
