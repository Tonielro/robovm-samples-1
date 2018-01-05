package org.robovm.samples.robopods.onesignal;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.uikit.NSTextAlignment;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;

public class ViewController extends UIViewController {

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        // Get the view of this view controller.
        UIView view = getView();

        // Setup background.
        view.setBackgroundColor(UIColor.white());

        // Setup label.
        UILabel label = new UILabel(new CGRect(20, 250, 280, 44));
        label.setTextAlignment(NSTextAlignment.Center);
        label.setText("Waiting for Push notification");
        view.addSubview(label);

    }
}
