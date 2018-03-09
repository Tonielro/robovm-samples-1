package org.robovm.samples.robopods.spottie;

import org.robovm.apple.coreanimation.CADisplayLink;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSRunLoop;
import org.robovm.apple.foundation.NSRunLoopMode;
import org.robovm.apple.uikit.*;
import org.robovm.pods.lottie.LOTAnimationView;

public class AnimationExplorerViewController extends UIViewController {

    enum ViewBackgroundColor {
        ViewBackgroundColorWhite,
        ViewBackgroundColorBlack,
        ViewBackgroundColorGreen,
        ViewBackgroundColorNone
    }

    private ViewBackgroundColor currentBGColor;
    private UIToolbar toolbar;
    private UISlider slider;
    private LOTAnimationView laAnimation;

    private void setCurrentBGColor(ViewBackgroundColor bgColor) {
        currentBGColor = bgColor;
        switch (currentBGColor) {
            case ViewBackgroundColorWhite:
                getView().setBackgroundColor(UIColor.white());
                break;
            case ViewBackgroundColorBlack:
                getView().setBackgroundColor(UIColor.black());
                break;
            case ViewBackgroundColorGreen:
                getView().setBackgroundColor(new UIColor(50.f/255.f, 207.f/255.f, 193.f/255.f, 1.f));
                break;
            case ViewBackgroundColorNone:
                getView().setBackgroundColor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        setCurrentBGColor(ViewBackgroundColor.ViewBackgroundColorWhite);
        toolbar = new UIToolbar();

        UIBarButtonItem open = new UIBarButtonItem(UIBarButtonSystemItem.Bookmarks, barButtonItem -> open());
        UIBarButtonItem flx1 = new UIBarButtonItem(UIBarButtonSystemItem.FlexibleSpace);
        UIBarButtonItem play = new UIBarButtonItem(UIBarButtonSystemItem.Play, this::play);
        UIBarButtonItem flx2 = new UIBarButtonItem(UIBarButtonSystemItem.FlexibleSpace);
        UIBarButtonItem loop = new UIBarButtonItem(UIBarButtonSystemItem.Refresh, this::loop);
        UIBarButtonItem flx3 = new UIBarButtonItem(UIBarButtonSystemItem.FlexibleSpace);
        UIBarButtonItem zoom = new UIBarButtonItem(UIBarButtonSystemItem.Add, barButtonItem -> setZoom());
        UIBarButtonItem flx4 = new UIBarButtonItem(UIBarButtonSystemItem.FlexibleSpace);
        UIBarButtonItem bgcolor = new UIBarButtonItem(UIBarButtonSystemItem.Compose, barButtonItem -> setBGColor());
        toolbar.setItems(new NSArray<>(open, flx1, loop, flx2, play, flx3, zoom, flx4, bgcolor));
        getView().addSubview(toolbar);

        slider = new UISlider();
        slider.addOnValueChangedListener(control -> sliderChanged());
        slider.setMinimumValue(0);
        slider.setMaximumValue(1);
        getView().addSubview(slider);

        resetAllButtons();

        // just create empty one to eliminate NPE
        laAnimation = new LOTAnimationView();
    }

    private void resetAllButtons() {
        slider.setValue(0);
        for (UIBarButtonItem button : toolbar.getItems()) {
            resetButton(button, false);
        }
    }

    private void resetButton(UIBarButtonItem button, boolean highlighted) {
        button.setTintColor(highlighted ? UIColor.red() : new UIColor(50.f/255.f, 207.f/255.f, 193.f/255.f, 1.f));
    }

    @Override
    public void viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();

        CGRect b = getView().getBounds();
        toolbar.setFrame(new CGRect(0, b.getHeight() - 44, b.getWidth(), 44));
        CGSize sliderSize = slider.getSizeThatFits(b.getSize());
        sliderSize.setHeight(sliderSize.getHeight() + 12);
        slider.setFrame(new CGRect(10,  toolbar.getFrame().getMinY() - sliderSize.getHeight(), b.getWidth() - 20, sliderSize.getHeight()));
        if (laAnimation != null)
            laAnimation.setFrame(new CGRect(0, 0, b.getWidth(), slider.getFrame().getMinY()));
    }

    private void sliderChanged() {
        laAnimation.setAnimationProgress(slider.getValue());
    }

    private void open() {
        showJSONExplorer();
    }

    private void setZoom() {
        switch (laAnimation.getContentMode()) {
            case ScaleAspectFit:
                laAnimation.setContentMode(UIViewContentMode.ScaleAspectFill);
                showMessage("Aspect fill");
            break;
            case ScaleAspectFill:
                laAnimation.setContentMode(UIViewContentMode.ScaleToFill);
                showMessage("Scale Fill");
            break;
            case ScaleToFill:
                laAnimation.setContentMode(UIViewContentMode.ScaleAspectFit);
                showMessage("Aspect Fit");
            break;
            default:
                break;
        }
    }

    private void setBGColor() {
        ViewBackgroundColor current = ViewBackgroundColor.values()[currentBGColor.ordinal() + 1];
        if (current == ViewBackgroundColor.ViewBackgroundColorNone) {
            current = ViewBackgroundColor.ViewBackgroundColorWhite;
        }
        setCurrentBGColor(current);
    }


    private void showJSONExplorer() {
        JSONExplorerViewController vc = new JSONExplorerViewController();
        vc.setCompletion(selectedAnimation -> {
            if (selectedAnimation != null) {
                loadAnimation(selectedAnimation);
            }
            dismissViewController(true, null);
        });

        UINavigationController navController = new UINavigationController(vc);
        presentViewController(navController, true, null);
    }

    private void loadAnimation(String selectedAnimation) {
        laAnimation.removeFromSuperview();
        laAnimation = null;
        resetAllButtons();

        laAnimation = new LOTAnimationView(selectedAnimation);
        laAnimation.setContentMode(UIViewContentMode.ScaleAspectFit);
        getView().addSubview(laAnimation);
        getView().setNeedsDisplay();
    }

    private void play(UIBarButtonItem button) {
        if (laAnimation.isAnimationPlaying()) {
            resetButton(button, false);
            laAnimation.pause();
        } else {
            CADisplayLink displayLink = new CADisplayLink(displayLink1 -> updateSlider());
            displayLink.addToRunLoop(NSRunLoop.getCurrent(), NSRunLoopMode.Common);
            resetButton(button, true);
            laAnimation.playWithCompletion(animationFinished -> {
                displayLink.invalidate();
                resetButton(button, false);
            });
        }
    }

    private void updateSlider() {
        slider.setValue((float) laAnimation.getAnimationProgress());
    }

    private void loop(UIBarButtonItem button) {
        laAnimation.setLoopAnimation(laAnimation.isLoopAnimation());
        resetButton(button, laAnimation.isLoopAnimation());
        showMessage(laAnimation.isLoopAnimation() ? "Loop Enabled" : "Loop Disabled");
    }

    private void showMessage(String message) {
        UILabel messageLabel = new UILabel();
        messageLabel.setTextAlignment(NSTextAlignment.Center);
        messageLabel.setBackgroundColor(UIColor.black().addAlpha(0.5));
        messageLabel.setTextColor(UIColor.white());
        messageLabel.setText(message);

        CGSize messageSize = messageLabel.getSizeThatFits(getView().getBounds().getSize());
        messageSize.setWidth(messageSize.getWidth() + 14);
        messageSize.setHeight(messageSize.getHeight() + 14);
        messageLabel.setFrame(new CGRect(10, 30, messageSize.getWidth(), messageSize.getHeight()));
        messageLabel.setAlpha(0);
        getView().addSubview(messageLabel);
        UIView.animate(0.3, () -> messageLabel.setAlpha(1), v -> {
            messageLabel.setAlpha(0);
            messageLabel.removeFromSuperview();
        });
    }
}
