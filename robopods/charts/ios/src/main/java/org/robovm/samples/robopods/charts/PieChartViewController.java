package org.robovm.samples.robopods.charts;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.foundation.NSMutableArray;
import org.robovm.apple.foundation.NSMutableAttributedString;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSNumberFormatter;
import org.robovm.apple.foundation.NSNumberFormatterStyle;
import org.robovm.apple.foundation.NSRange;
import org.robovm.apple.uikit.NSAttributedStringAttributes;
import org.robovm.apple.uikit.NSLayoutAttribute;
import org.robovm.apple.uikit.NSLayoutConstraint;
import org.robovm.apple.uikit.NSLayoutRelation;
import org.robovm.apple.uikit.NSLineBreakMode;
import org.robovm.apple.uikit.NSMutableParagraphStyle;
import org.robovm.apple.uikit.NSParagraphStyle;
import org.robovm.apple.uikit.NSTextAlignment;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UISlider;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.apple.uikit.UITableViewDataSourceAdapter;
import org.robovm.apple.uikit.UITableViewDelegateAdapter;
import org.robovm.apple.uikit.UITextField;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.pods.charts.ChartColorTemplates;
import org.robovm.pods.charts.ChartDataEntry;
import org.robovm.pods.charts.ChartDefaultValueFormatter;
import org.robovm.pods.charts.ChartEasingOption;
import org.robovm.pods.charts.ChartHighlight;
import org.robovm.pods.charts.ChartLegend;
import org.robovm.pods.charts.ChartLegendHorizontalAlignment;
import org.robovm.pods.charts.ChartLegendOrientation;
import org.robovm.pods.charts.ChartLegendVerticalAlignment;
import org.robovm.pods.charts.ChartViewBase;
import org.robovm.pods.charts.ChartViewDelegate;
import org.robovm.pods.charts.PieChartData;
import org.robovm.pods.charts.PieChartDataEntry;
import org.robovm.pods.charts.PieChartDataSet;
import org.robovm.pods.charts.PieChartView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@CustomClass("PieChartViewController")
public class PieChartViewController extends UIViewController implements ChartViewDelegate {

    @IBOutlet private PieChartView chartView;
    @IBOutlet private UISlider sliderX;
    @IBOutlet private UISlider sliderY;
    @IBOutlet private UITextField sliderTextX;
    @IBOutlet private UITextField sliderTextY;
    private UITableView optionsTableView;

    private static String[] parties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F",
            "Party G", "Party H", "Party I", "Party J", "Party K", "Party L",
            "Party M", "Party N", "Party O", "Party P", "Party Q", "Party R",
            "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"};
    /** @noinspection unchecked*/
    private static List<Map<String, String>> options = Arrays.asList(
            new HashMap(){{put("key", "toggleValues"); put("label", "Toggle Y-Values");}},
            new HashMap(){{put("key", "toggleXValues"); put("label", "Toggle X-Values");}},
            new HashMap(){{put("key", "togglePercent"); put("label", "Toggle Percent");}},
            new HashMap(){{put("key", "toggleHole"); put("label", "Toggle Hole");}},
            new HashMap(){{put("key", "toggleIcons"); put("label", "Toggle Icons");}},
            new HashMap(){{put("key", "animateX"); put("label", "Animate X");}},
            new HashMap(){{put("key", "animateY"); put("label", "Animate Y");}},
            new HashMap(){{put("key", "animateXY"); put("label", "Animate XY");}},
            new HashMap(){{put("key", "spin"); put("label", "Spin");}},
            new HashMap(){{put("key", "drawCenter"); put("label", "Draw CenterText");}},
            new HashMap(){{put("key", "saveToGallery"); put("label", "Save to Camera Roll");}},
            new HashMap(){{put("key", "toggleData"); put("label", "Toggle Data");}}
    );


    public PieChartViewController(String nibNameOrNil, NSBundle nibBundleOrNil) {
        super(nibNameOrNil, nibBundleOrNil);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        this.setTitle("Pie Bar Chart");


        setupPieChartView(chartView);

        chartView.setDelegate(this);

        ChartLegend l = chartView.getLegend();
        l.setHorizontalAlignment(ChartLegendHorizontalAlignment.Right);
        l.setVerticalAlignment(ChartLegendVerticalAlignment.Top);
        l.setOrientation(ChartLegendOrientation.Vertical);
        l.setDrawInside(false);
        l.setXEntrySpace(7.0);
        l.setYEntrySpace(0.0);
        l.setYOffset(0.0);

        // entry label styling
        chartView.setEntryLabelColor(UIColor.white());
        chartView.setEntryLabelFont(UIFont.getFont("HelveticaNeue-Light", 12.f));

        sliderX.setValue(4.0f);
        sliderY.setValue(100.0f);
        slidersValueChanged(null);

        chartView.animateXAxis(1.4, ChartEasingOption.EaseOutBack);
    }


    private void updateChartData() {
        setDataCount((int) sliderX.getValue(), sliderY.getValue());
    }


    private void setDataCount(int count,  double range) {
        double mult = range;

        NSMutableArray<ChartDataEntry> values = new NSMutableArray<>();
        for (int i = 0; i < count; i++) {
            double value = new Random().nextInt((int) mult) + mult / 5;
            values.add(new PieChartDataEntry(value, parties[i % parties.length], UIImage.getImage("icon")));
        }

        PieChartDataSet dataSet = new PieChartDataSet(values, "Election Results");
        dataSet.setDrawIconsEnabled(false);
        dataSet.setSliceSpace(2.0);
        dataSet.setIconsOffset(new CGPoint(0, 40));

        // add a lot of colors

        NSMutableArray<UIColor> colors = new NSMutableArray<>();
        colors.addAll(ChartColorTemplates.vordiplom());
        colors.addAll(ChartColorTemplates.joyful());
        colors.addAll(ChartColorTemplates.colorful());
        colors.addAll(ChartColorTemplates.liberty());
        colors.addAll(ChartColorTemplates.pastel());
        colors.add(new UIColor(51/255.f, 181/255.f, 229/255.f, 1.f));

        dataSet.setColors(colors);
        PieChartData data = new PieChartData(new NSArray<>(dataSet));

        NSNumberFormatter pFormatter = new NSNumberFormatter();
        pFormatter.setNumberStyle(NSNumberFormatterStyle.Percent);
        pFormatter.setMaximumFractionDigits(1);
        pFormatter.setMultiplier(NSNumber.valueOf(1.f));
        pFormatter.setPercentSymbol(" %");

        data.setValueFormatter(new ChartDefaultValueFormatter(pFormatter));
        data.setValueFont(UIFont.getFont("HelveticaNeue-Light", 11.f));
        data.setValueTextColor(UIColor.white());

        chartView.setData(data);
        chartView.setHighlightValues(null);
    }

    private void optionTapped(String key) {
        switch (key) {
            case "toggleXValues":
                chartView.setDrawEntryLabelsEnabled(!chartView.isDrawEntryLabelsEnabled());
                chartView.setNeedsDisplay();
                break;

            case "togglePercent":
                chartView.setUsePercentValuesEnabled(!chartView.isUsePercentValuesEnabled());
                chartView.setNeedsDisplay();
                break;

            case "toggleHole":
                chartView.setDrawHoleEnabled(!chartView.isDrawHoleEnabled());
                chartView.setNeedsDisplay();
                break;

            case "drawCenter":
                chartView.setDrawCenterTextEnabled(!chartView.isDrawCenterTextEnabled());
                chartView.setNeedsDisplay();
                break;

            case "animateX":
                chartView.animateWithXAxisDuration(1.4);
                break;

            case "animateY":
                chartView.animateWithYAxisDuration(1.4);
                break;

            case "animateXY":
                chartView.animate(1.4, 1.4);
                break;

            case "spin":
                chartView.spin(2.0, chartView.getRotationAngle(), chartView.getRotationAngle() + 360.f, ChartEasingOption.EaseInCubic);
                break;
        }
    }


    /** @noinspection unchecked*/
    private void setupPieChartView(PieChartView chartView) {
        chartView.setUsePercentValuesEnabled(true);
        chartView.setDrawSlicesUnderHoleEnabled(false);
        chartView.setHoleRadiusPercent(0.58);
        chartView.setTransparentCircleRadiusPercent(0.61);
        chartView.getChartDescription().setEnabled(false);
        chartView.setExtraOffsets(5.f, 10.f, 5.f, 5.f);

        chartView.setDrawCenterTextEnabled(true);

        NSMutableParagraphStyle paragraphStyle = (NSMutableParagraphStyle) NSParagraphStyle.getDefaultParagraphStyle().mutableCopy();
        paragraphStyle.setLineBreakMode(NSLineBreakMode.TruncatingTail);
        paragraphStyle.setAlignment(NSTextAlignment.Center);

        NSMutableAttributedString centerText = new NSMutableAttributedString("Charts\nby Daniel Cohen Gindi");
        centerText.setAttributes(new NSAttributedStringAttributes()
                .setFont(UIFont.getFont("HelveticaNeue-Light", 13.f))
                .setParagraphStyle(paragraphStyle)
                .getDictionary(), new NSRange(0, centerText.length()));
        centerText.setAttributes(new NSAttributedStringAttributes()
                .setFont(UIFont.getFont("HelveticaNeue-Light", 11.f))
                .setForegroundColor(UIColor.gray())
                .getDictionary(), new NSRange(10, centerText.length() - 10));
        centerText.setAttributes(new NSAttributedStringAttributes()
                .setFont(UIFont.getFont("HelveticaNeue-LightItalic", 11.f))
                .setForegroundColor(new UIColor(51/255.f, 181/255.f, 229/255.f, 1.f))
                .getDictionary(), new NSRange(centerText.length() - 19, 19));
        chartView.setCenterAttributedText(centerText);

        chartView.setDrawHoleEnabled(true);
        chartView.setRotationAngle(0.0);
        chartView.setRotationEnabled(true);
        chartView.setHighlightPerTapEnabled(true);

        ChartLegend l = chartView.getLegend();
        l.setHorizontalAlignment(ChartLegendHorizontalAlignment.Right);
        l.setVerticalAlignment(ChartLegendVerticalAlignment.Top);
        l.setOrientation(ChartLegendOrientation.Vertical);
        l.setDrawInside(false);
        l.setXEntrySpace(7.0);
        l.setYEntrySpace(0.0);
        l.setYOffset(0.0);
    }


    @IBAction private void slidersValueChanged(UIControl sender) {
        sliderTextX.setText("" + sliderX.getValue());
        sliderTextY.setText("" + sliderY.getValue());

        updateChartData();
    }


    @IBAction private void optionsButtonTapped(UIControl sender) {
        if (optionsTableView != null) {
            optionsTableView.removeFromSuperview();
            optionsTableView = null;
            return;
        }

        optionsTableView = new UITableView();
        optionsTableView.setBackgroundColor(UIColor.fromWhiteAlpha(0.f, 0.9f));
        optionsTableView.setDelegate(new UITableViewDelegateAdapter() {
            @Override
            public double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
                return 40.0;
            }

            @Override
            public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
                tableView.deselectRow(indexPath, true);
                if (optionsTableView != null) {
                    optionsTableView.removeFromSuperview();
                    optionsTableView = null;
                }

                optionTapped(options.get(indexPath.getRow()).get("key"));
            }
        });
        optionsTableView.setDataSource(new UITableViewDataSourceAdapter() {
            @Override
            public long getNumberOfSections(UITableView tableView) {
                return 1;
            }

            @Override
            public long getNumberOfRowsInSection(UITableView tableView, long section) {
                return options.size();
            }

            @Override
            public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
                UITableViewCell cell = tableView.dequeueReusableCell("Cell");
                if (cell == null)
                {
                    cell = new UITableViewCell(UITableViewCellStyle.Default, "Cell");
                    cell.setBackgroundView(null);
                    cell.setBackgroundColor(UIColor.clear());
                    cell.getTextLabel().setTextColor(UIColor.white());
                }

                cell.getTextLabel().setText(options.get(indexPath.getRow()).get("label"));
                return cell;
            }
        });

        optionsTableView.setTranslatesAutoresizingMaskIntoConstraints(false);
        NSMutableArray<NSLayoutConstraint> constraints = new NSMutableArray<>();
        constraints.add(new NSLayoutConstraint(optionsTableView, NSLayoutAttribute.Leading, NSLayoutRelation.Equal, this.getView(), NSLayoutAttribute.Leading, 1.f,  40.f));
        constraints.add(new NSLayoutConstraint(optionsTableView, NSLayoutAttribute.Trailing, NSLayoutRelation.Equal, sender, NSLayoutAttribute.Trailing, 1.f,  0));
        constraints.add(new NSLayoutConstraint(optionsTableView, NSLayoutAttribute.Top, NSLayoutRelation.Equal, sender, NSLayoutAttribute.Bottom, 1.f,  5));

        this.getView().addSubview(optionsTableView);
        this.getView().addConstraints(constraints);

        optionsTableView.addConstraint(new NSLayoutConstraint(optionsTableView, NSLayoutAttribute.Height, NSLayoutRelation.Equal, null, NSLayoutAttribute.Height, 1.f,  220.f));
    }

    @Override
    public void chartValueSelected(ChartViewBase chartView, ChartDataEntry entry, ChartHighlight highlight) {
        System.out.println("chartValueSelected");
    }

    @Override
    public void chartValueNothingSelected(ChartViewBase chartView) {
        System.out.println("chartValueNothingSelected");
    }

    @Override
    public void chartScaled(ChartViewBase chartView, double scaleX, double scaleY) {
        System.out.println("chartScaled");
    }

    @Override
    public void chartTranslated(ChartViewBase chartView, double dX, double dY) {
        System.out.println("chartTranslated");
    }
}
