package com.mycompany.myapp;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.*;
import org.robovm.apple.watchconnectivity.WCSession;

import java.util.Date;

public class MyViewController extends UIViewController {
    private final UIButton button;
    private final UILabel sent;
    private final UILabel received;
    private final UILabel log;

    public MyViewController() {
        // Get the view of this view controller.
        UIView view = getView();

        // Setup background.
        view.setBackgroundColor(UIColor.white());
        double maxX = view.getFrame().getWidth() - 20;

        // Setup button.
        button = new UIButton(UIButtonType.RoundedRect);
        button.setFrame(new CGRect(10, 40, maxX, 40));
        button.setBackgroundColor(UIColor.yellow());
        view.addSubview(button);

        // Setup labels.
        sent = new UILabel(new CGRect(10, 90, maxX, 20));
        view.addSubview(sent);
        received = new UILabel(new CGRect(10, 120, maxX, 20));
        view.addSubview(received);
        log = new UILabel(new CGRect(10, 150, maxX, 500));
        log.setNumberOfLines(0);
        view.addSubview(log);

        model = new Model();
        setupWatchConnectivity();
    }

    private void setupWatchConnectivity() {
        // subscribe for message
        ((SessionDelegate)WCSession.getDefaultSession().getDelegate()).onMessageReceived = msg ->
                DispatchQueue.getMainQueue().async(() -> model.msgReceived(msg));
        ((SessionDelegate)WCSession.getDefaultSession().getDelegate()).onLog = msg ->
                DispatchQueue.getMainQueue().async(() -> model.log(msg));

        // action once clicked
        button.addOnTouchUpInsideListener((control, event) -> {
            model.onSending();
            WCSession.getDefaultSession().sendMessage(
                    new NSDictionary<>(new NSString("msg"), new NSString(model.me)),
                    msg -> DispatchQueue.getMainQueue().async(model::msgSent),
                    err -> DispatchQueue.getMainQueue().async(() -> model.onError(err))
            );
        });
    }

    private final Model model;
    private class Model {
        private final String me = "Pong";
        private final String other = "Ping";
        private int sentCnt;
        private int receivedCnt;

        public Model() {
            button.setTitle("Wait, sending!", UIControlState.Disabled);
            button.setTitle(me + " it!", UIControlState.Normal);
        }

        void enableButton() {
            button.setEnabled(true);
        }

        void msgSent() {
            sentCnt += 1;
            sent.setText(me + " sent: " + sentCnt);
            enableButton();
        }

        void msgReceived(NSDictionary<NSString, ? > msg) {
            receivedCnt += 1;
            received.setText(other + " received: " + receivedCnt);
            log.setText(new Date() + ": Received " + msg);
        }

        void onError(NSError err) {
            log.setText(new Date() + ": Error " + err);
            enableButton();
        }

        void log(String msg) {
            log.setText(new Date() + ": " + msg);
        }

        void onSending() {
            button.setEnabled(false);
            log.setText("");
        }
    }
}
