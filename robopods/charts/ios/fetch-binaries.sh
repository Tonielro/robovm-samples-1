#/bin/sh

# This script fetches and build the Charts SDK framework binary
set -e
mkdir -p libs
rm -f Cartfile
rm -rf Carthage
rm -rf libs/Charts.framework
echo 'github "danielgindi/Charts" == 3.0.4' > Cartfile
carthage update
cp -r Carthage/Build/iOS/Charts.framework libs/
rm -r Carthage
rm -f Cartfile
rm -f Cartfile.resolved
