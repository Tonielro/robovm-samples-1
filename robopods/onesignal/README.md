## sample for OneSignal robopod
[link](https://github.com/dkimitsa/robovm-robopods/tree/alt/onesignal)

to run it OneSignal.framework is required. Run fetch-binaries.sh to build it (requires [Carthage](https://github.com/Carthage/Carthage#installing-carthage))
to run with Notification Service Extension:
 - build the framework
 - open ios/src/main/native/OneSignalNotificationServiceExtension.xcodeproj and update signing parameters with valid identity and certificate (any dummy)
 - run build-app-ext.sh which will build universal binary
