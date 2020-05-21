//
//  AppDelegate.swift
//  watchhost
//
//  Created by Demyan Kimitsa on 21/05/2020.
//  Copyright © 2020 Demyan Kimitsa. All rights reserved.
//

import UIKit
import WatchConnectivity

class SessionDelegate : NSObject, WCSessionDelegate {
    var onMessageReceived : ((_ msg: [String : Any]) -> Void) = { _ in  }
    var onLog : ((_ msg: String) -> Void) = { _ in}
    
    func session(_ session: WCSession, activationDidCompleteWith activationState: WCSessionActivationState, error: Error?) {
        onLog("activationDidCompleteWith activationState = \(activationState) error = \(String(describing: error))")
    }

    func sessionDidBecomeInactive(_ session: WCSession) {
        onLog("sessionDidBecomeInactive")
    }
    
    func sessionDidDeactivate(_ session: WCSession) {
        onLog("sessionDidDeactivate")
    }
    
    func session(_ session: WCSession, didReceiveMessage message: [String : Any]) {
        onMessageReceived(message)
    }

    func session(_ session: WCSession, didReceiveMessage message: [String : Any], replyHandler: @escaping ([String : Any]) -> Void) {
        onMessageReceived(message)
        replyHandler(["msg" : "ok"])
    }
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    let wcSessionDelegate = SessionDelegate()

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        // Perform any final initialization of your application.
        assert(WCSession.isSupported())
        WCSession.default.delegate = wcSessionDelegate
        WCSession.default.activate()
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

