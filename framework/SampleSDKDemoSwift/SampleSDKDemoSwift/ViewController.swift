//
//  ViewController.swift
//  SampleSDKDemoSwift
//

import UIKit
import SampleSDK

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // testing RoboVM framework
        var sdk:SampleSDK!;
        sdk = SampleSDKInstance();
        NSLog("RoboVM version %@", sdk.roboVmVersion());

        var calc:Calculator;
        calc = sdk.newCalculator();
        calc.add(123);
        calc.sub(23);
        NSLog("%d",calc.result());
        
        sdk.webServer().start();
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

