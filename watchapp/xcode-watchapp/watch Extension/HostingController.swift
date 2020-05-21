//
//  HostingController.swift
//  watch Extension
//
//  Created by Demyan Kimitsa on 21/05/2020.
//  Copyright © 2020 Demyan Kimitsa. All rights reserved.
//

import WatchKit
import Foundation
import SwiftUI
import WatchConnectivity

class HostingController: WKHostingController<ContentView> {
    let model = ExternalModel(me: "Ping", other: "Pong")

    override var body: ContentView {
        // subscribe for message
        (WCSession.default.delegate as? SessionDelegate)?.onMessageReceived = { msg in
            DispatchQueue.main.async {
                self.model.msgReceived(msg: msg)
            }
        }
        (WCSession.default.delegate as? SessionDelegate)?.onLog = { msg in
            DispatchQueue.main.async {
                self.model.log(msg: msg)
            }
        }

        // action once clicked
        let onClickAction = {
            self.model.onSending()
            WCSession.default.sendMessage(["msg" : self.model.me], replyHandler:{ msg in
                DispatchQueue.main.async {
                    self.model.msgSent()
                }
            }, errorHandler: { err in
                DispatchQueue.main.async {
                    self.model.onError(err: err)
                }
            })
        }
        
        return ContentView(model: model, action: onClickAction)
    }
}
