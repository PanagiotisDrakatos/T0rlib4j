package net.sf.T0rlib4j.samples.Client;

import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import com.msopentech.thali.java.toronionproxy.OnionProxyManager;
import com.msopentech.thali.java.toronionproxy.Utilities;
import com.runjva.sourceforge.jsocks.protocol.Authentication;
import com.runjva.sourceforge.jsocks.protocol.Socks5Proxy;
import com.runjva.sourceforge.jsocks.protocol.UserPasswordAuthentication;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TorClientSocks5withAuth {
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
        String OnionAdress = "doqj3fyb5qjka7bb.onion";
        Authentication auth = new UserPasswordAuthentication("username", "password");

        Socks5Proxy proxy = onionProxyManager.SetupSocks5Proxy(localPort);
        proxy.setAuthenticationMethod(2, auth);


        Socket clientSocket = Utilities.Socks5connection(proxy, OnionAdress, localPort);

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.flush();
        out.writeObject("i am workingg");
        out.flush();
    }
}
