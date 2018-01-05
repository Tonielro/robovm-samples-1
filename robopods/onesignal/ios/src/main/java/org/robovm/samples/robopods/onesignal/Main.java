package org.robovm.samples.robopods.onesignal;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegateAdapter;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.pods.onesignal.OSNotificationDisplayType;
import org.robovm.pods.onesignal.OSSettings;
import org.robovm.pods.onesignal.OneSignal;

public class Main extends UIApplicationDelegateAdapter {
    // Replace '11111111-2222-3333-4444-0123456789ab' with your OneSignal App ID.
    public static final String ONESIGNAL_APP_ID = "11111111-2222-3333-4444-0123456789ab";
    private UIWindow window;
    private ViewController rootViewController;

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        // Set up the view controller.
        rootViewController = new ViewController();

        // Create a new window at screen size.
        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        // Set the view controller as the root controller for the window.
        window.setRootViewController(rootViewController);
        // Make the window visible.
        window.makeKeyAndVisible();

        // initialize one signal
        OneSignal.Init(launchOptions, ONESIGNAL_APP_ID, null, new OSSettings().setAutoPrompt(false));
        OneSignal.setInFocusDisplayType(OSNotificationDisplayType.Notification);


        // Recommend moving the below line to prompt for push after informing the user about
        //   how your app will use them.
        OneSignal.promptForPushNotificationsWithUserResponse(v -> System.out.println("User accepted notifications: " + v));

        // Call syncHashedEmail anywhere in your iOS app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);

        return true;
    }

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, Main.class);
        }
    }
}
