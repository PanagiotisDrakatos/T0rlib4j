
package com.msopentech.thali.java.toronionproxy;


import java.io.File;

public class JavaOnionProxyManager extends OnionProxyManager {
    public JavaOnionProxyManager(OnionProxyContext onionProxyContext) {
        super(onionProxyContext);
    }

    @Override
    protected boolean setExecutable(File f) {
        return f.setExecutable(true);
    }

}
