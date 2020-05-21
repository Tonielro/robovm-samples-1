//
//  ContentView.swift
//  watchhost
//
//  Created by Demyan Kimitsa on 21/05/2020.
//  Copyright © 2020 Demyan Kimitsa. All rights reserved.
//

import SwiftUI
class ExternalModel: ObservableObject {
    var me = ""
    var other = ""
    var sentCnt = 0
    var receivedCnt = 0
    @Published var buttonTitle: String = ""
    @Published var buttonDisabled = false
    @Published var sent: String = ""
    @Published var received: String = ""
    @Published var log: String = ""

    init(me: String, other: String) {
        self.me = me
        self.other = other
        buttonTitle = "\(me) it!"
    }

    func enableButton() -> Void {
       buttonTitle = "\(me) it!"
       buttonDisabled = false
    }
    
    func msgSent() -> Void {
       sentCnt += 1;
       sent = "\(me) sent: \(sentCnt)"
       enableButton()
    }

    func msgReceived(msg: [String:Any]) -> Void {
        receivedCnt += 1
        received = "\(other) received: \(receivedCnt)"
        log = "\(Date()): Received \(String(describing: msg))"
    }
    
    func onError(err: Error?) {
        log = "\(Date()): Error \(String(describing: err))"
        enableButton()
    }
    
    func log(msg: String) {
        log = "\(Date()): \(msg)"
    }

    func onSending() {
        buttonTitle = "Wait, sending!"
        buttonDisabled = true
        log = ""
    }
}


struct ContentView: View {
    @ObservedObject var viewModel: ExternalModel
    var onClick: (() -> Void) = { }
    
    init(model : ExternalModel, action: @escaping (() -> Void)) {
        self.viewModel = model
        self.onClick = action
    }

    var body: some View {
        ScrollView {
            VStack {
                Button(action: onClick) {
                    Text(self.viewModel.buttonTitle)
                    .disabled(self.viewModel.buttonDisabled)
                    .frame(minWidth: 300, minHeight: 100)
                    .foregroundColor(Color.black)
                    .background(Color.yellow)
                    .padding()
                }
                Text(self.viewModel.sent)
                Text(self.viewModel.received)
                Text(self.viewModel.log)
                    .frame(maxWidth: .infinity)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        let model = ExternalModel(me: "Pong", other: "Ping")
        model.msgSent()
        model.msgReceived(msg: ["msg": "hello"])
        return ContentView(model: model, action: {})
    }
}
