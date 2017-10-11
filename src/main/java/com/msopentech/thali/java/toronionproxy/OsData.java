

package com.msopentech.thali.java.toronionproxy;

import java.io.IOException;
import java.util.Scanner;

public class OsData {
    public enum OsType {WINDOWS, LINUX_32, LINUX_64, MAC, ANDROID}
    private static OsType detectedType = null;

    public static OsType getOsType() {
        if (detectedType == null) {
            detectedType = actualGetOsType();
        }

        return detectedType;
    }

    /**
     * Yes, I should use a proper memoization abstract class but, um, next time.
     * @return Type of OS we are running on
     */
    protected static OsType actualGetOsType() {

        if (System.getProperty("java.vm.name").contains("Dalvik")) {
            return OsType.ANDROID;
        }

        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            return OsType.WINDOWS;
        } else if (osName.contains("Mac")) {
            return OsType.MAC;
        } else if (osName.contains("Linux")) {
            return getLinuxType();
        }
        throw new RuntimeException("Unsupported OS");
    }

    protected static OsType getLinuxType() {
        String [] cmd = { "uname", "-m" };
        Process unameProcess = null;
        try {
            String unameOutput;
            unameProcess = Runtime.getRuntime().exec(cmd);

            Scanner scanner = new Scanner(unameProcess.getInputStream());
            if (scanner.hasNextLine()) {
                unameOutput = scanner.nextLine();
            } else {
                throw new RuntimeException("Couldn't get output from uname call");
            }

            int exit = unameProcess.waitFor();
            if (exit != 0) {
                throw new RuntimeException("Uname returned error code " + exit);
            }

            if (unameOutput.compareTo("i686") == 0) {
                return OsType.LINUX_32;
            }
            if (unameOutput.compareTo("x86_64") == 0) {
                return OsType.LINUX_64;
            }
            throw new RuntimeException("Could not understand uname output, not sure what bitness");
        } catch (IOException e) {
            throw new RuntimeException("Uname failure", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Uname failure", e);
        } finally {
            if (unameProcess != null) {
                unameProcess.destroy();
            }
        }
    }
}
