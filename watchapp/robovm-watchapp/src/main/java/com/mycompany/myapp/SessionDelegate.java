package com.mycompany.myapp;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.watchconnectivity.WCSession;
import org.robovm.apple.watchconnectivity.WCSessionActivationState;
import org.robovm.apple.watchconnectivity.WCSessionDelegateAdapter;
import org.robovm.objc.block.VoidBlock1;

public class SessionDelegate extends WCSessionDelegateAdapter {
    interface Callback<T> {
        void invoke(T data);
    }

    public Callback<NSDictionary<NSString,?>> onMessageReceived = msg -> {};
    public Callback<String> onLog = msg -> {};


    @Override
    public void activationDidComplete(WCSession session, WCSessionActivationState activationState, NSError error) {
        onLog.invoke("activationDidCompleteWith activationState = " + activationState +
                "  error = " + error);
    }

    @Override
    public void sessionDidBecomeInactive(WCSession session) {
        onLog.invoke("sessionDidBecomeInactive");
    }

    @Override
    public void sessionDidDeactivate(WCSession session) {
        onLog.invoke("sessionDidDeactivate");
    }

    @Override
    public void didReceiveMessage(WCSession session, NSDictionary<NSString, ?> message) {
        onMessageReceived.invoke(message);
    }

    @Override
    public void didReceiveMessage(WCSession session, NSDictionary<NSString, ?> message,
                                  VoidBlock1<NSDictionary<NSString, ?>> replyHandler) {
        onMessageReceived.invoke(message);
        replyHandler.invoke(new NSDictionary<>(new NSString("msg"), new NSString("ok")));
    }
}
