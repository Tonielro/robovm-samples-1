#/bin/sh

# This script fetches and build the OneSignal SDK framework binary
set -e
mkdir -p libs
rm -f Cartfile
rm -rf Carthage
rm -rf libs/OneSignal.framework
echo 'github "OneSignal/OneSignal-iOS-SDK" == 2.6.0' > Cartfile
carthage update
cp -r Carthage/Build/iOS/OneSignal.framework libs/
rm -r Carthage
rm -f Cartfile
rm -f Cartfile.resolved
