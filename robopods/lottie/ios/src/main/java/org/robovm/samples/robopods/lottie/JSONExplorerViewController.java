package org.robovm.samples.robopods.lottie;

import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.*;

import java.io.File;
import java.util.List;

public class JSONExplorerViewController extends UIViewController {
    public interface Callback {
        void onCompletion(String json);
    }
    private Callback completionBlock;

    private UITableView tableView;
    private List<String> jsonFiles;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        this.getView().setBackgroundColor(UIColor.white());

        jsonFiles = NSBundle.getMainBundle().findResourcesPaths("json", null);

        tableView = new UITableView(getView().getBounds());
        tableView.setDelegate(new Delegate());
        tableView.setDataSource(new DataSource());
        tableView.registerReusableCellClass(UITableViewCell.class, "cell");
        getView().addSubview(tableView);

        getNavigationItem().setLeftBarButtonItem(new UIBarButtonItem("Close", UIBarButtonItemStyle.Done, barButtonItem -> closePressed()));
    }

    @Override
    public void viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();
        tableView.setFrame(getView().getBounds());
    }


    void setCompletion(Callback cb) {
        this.completionBlock = cb;
    }

    private class DataSource extends UITableViewDataSourceAdapter {
        @Override
        public long getNumberOfRowsInSection(UITableView tableView, long section) {
            return jsonFiles.size();
        }

        @Override
        public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
            UITableViewCell cell = tableView.dequeueReusableCell("cell", indexPath);
            String fileURL = jsonFiles.get(indexPath.getRow());
            cell.getTextLabel().setText(new File(fileURL).getName());
            return cell;
        }
    }

    private class Delegate extends UITableViewDelegateAdapter {
        @Override
        public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
            if (completionBlock != null) {
                String fileURL = jsonFiles.get(indexPath.getRow());
                completionBlock.onCompletion(new File(fileURL).getName());
            }
        }
    }

    private void closePressed() {
        if (completionBlock != null) {
            completionBlock.onCompletion(null);
        }
    }
}
