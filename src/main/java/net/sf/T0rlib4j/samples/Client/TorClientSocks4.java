package net.sf.T0rlib4j.samples.Client;

import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import com.msopentech.thali.java.toronionproxy.OnionProxyManager;
import com.msopentech.thali.java.toronionproxy.Utilities;
import net.sf.T0rlib4j.controller.network.JavaTorRelay;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TorClientSocks4 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String fileStorageLocation = "torfiles";
        OnionProxyManager onionProxyManager = new JavaOnionProxyManager(
                new JavaOnionProxyContext(new File(fileStorageLocation)));

        int totalSecondsPerTorStartup = 4 * 60;
        int totalTriesPerTorStartup = 5;
        // Start the Tor Onion Proxy
        if (onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup) == false) {
            return;
        }
        // Start a hidden service listener
        int hiddenServicePort = 80;
        int localPort = onionProxyManager.getIPv4LocalHostSocksPort();
        String OnionAdress = "d2lz63pgzqms2xxp.onion";


        Socket clientSocket = Utilities.socks4aSocketConnection(OnionAdress, hiddenServicePort, "127.0.0.1", localPort);

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.flush();

        out.writeObject("helloooooooooooooooooooooooo frommmmmmm hereeeeeeeeeeeeeeeeeeeeeeeeeeee");
        out.flush();
    }
}
