package org.robovm.samples.framework;

import org.robovm.apple.foundation.NSObject;
import org.robovm.rt.VM;

/**
 * Class that implements MainClass protocol/Interace
 * This class will be exposed by SampleSDK_instance() call in objc world
 * Use it as Facade to get access to other Framework functionalities
 */
public class SampleSDKImpl extends NSObject implements Api.SampleSDK {
    /**
     * singleton that will keep reference to Framework main class
     */
    private static SampleSDKImpl frameworkInstance = new SampleSDKImpl();

    private static Api.WebServer webServerInstance = new WebServerImpl();

    /**
     * IMPORTANT: keep this method as it is. As when libframeworksupport is used this metthod will be called
     * right after VM initialization to provide
     */
    public static NSObject instantiate() {
        return frameworkInstance;
    }

    @Override
    public Api.Calculator createCalculator() {
        return new CalculatorImpl();
    }

    @Override
    public void sayHello() {
        System.out.println("Hello world from RoboVM framework");
    }

    @Override
    public String roboVmVersion() {
        return VM.vmVersion();
    }

    @Override
    public Api.WebServer webServer() {
        return webServerInstance;
    }
}
