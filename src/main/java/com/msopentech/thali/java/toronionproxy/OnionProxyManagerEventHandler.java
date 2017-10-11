
package com.msopentech.thali.java.toronionproxy;

import net.sf.T0rlib4j.controller.network.NetLayerStatus;
import net.sf.T0rlib4j.controller.network.ServiceDescriptor;
import net.sf.T0rlib4j.freehaven.tor.control.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OnionProxyManagerEventHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OnionProxyManagerEventHandler.class);
    private ServiceDescriptor hs;
    private NetLayerStatus listener;
    private boolean hsPublished;

    public void setHStoWatchFor(ServiceDescriptor hs, NetLayerStatus listener) {
        if (hs == this.hs && hsPublished) {
            listener.onConnect(hs);
            return;
        }
        this.listener = listener;
        this.hs = hs;
        hsPublished = false;
    }

    public void circuitStatus(String status, String id, List<String> path, Map<String, String> info) {
        String msg = "CircuitStatus: " + id + " " + status;
        String purpose = info.get("PURPOSE");
        if (purpose != null) msg += ", purpose: " + purpose;
        String hsState = info.get("HS_STATE");
        if (hsState != null) msg += ", state: " + hsState;
        String rendQuery = info.get("REND_QUERY");
        if (rendQuery != null) msg += ", service: " + rendQuery;
        if (!path.isEmpty()) msg += ", path: " + shortenPath(path);
        LOG.info(msg);
    }

    @Override
    public void circuitStatus(String status, String circID, String path) {
        LOG.info("streamStatus: status: " + status + ", circID: " + circID + ", path: " + path);
    }

    public void streamStatus(String status, String id, String target) {
        LOG.info("streamStatus: status: " + status + ", id: " + id + ", target: " + target);
    }

    public void orConnStatus(String status, String orName) {
        LOG.info("OR connection: status: " + status + ", orName: " + orName);
    }

    public void bandwidthUsed(long read, long written) {
        LOG.info("bandwidthUsed: read: " + read + ", written: " + written);
    }

    public void newDescriptors(List<String> orList) {
        Iterator<String> iterator = orList.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
        }
        LOG.info("newDescriptors: " + stringBuilder.toString());
    }

    //fetch Exit Node
    public void message(String severity, String msg) {
        LOG.info("message: severity: " + severity + ", msg: " + msg);
    }

    public void unrecognized(String type, String msg) {
        LOG.info("unrecognized: type: " + type + ", msg: " + msg);
    }

    private String shortenPath(List<String> path) {
        StringBuilder s = new StringBuilder();
        for (String id : path) {
            if (s.length() > 0) s.append(',');
            s.append(id.substring(1, 7));
        }
        return s.toString();
    }

}
