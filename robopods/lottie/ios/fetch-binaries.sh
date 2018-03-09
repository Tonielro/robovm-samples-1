#/bin/sh

# This script fetches and build the Lottie SDK framework binary
set -e
mkdir -p libs
rm -f Cartfile
rm -rf Carthage
rm -rf libs/Lottie.framework
echo 'github "airbnb/lottie-ios" "2.5.0"' > Cartfile
carthage update --platform ios --no-use-binaries
cp -r Carthage/Build/iOS/Lottie.framework libs/
rm -r Carthage
rm -f Cartfile
rm -f Cartfile.resolved
