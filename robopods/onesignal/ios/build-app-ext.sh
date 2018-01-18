#/bin/sh
set -e

# few variables
BUILD_DIR=$(pwd)/native-build
BUILD_ROOT=$(pwd)/native-build
CONFIGURATION=Release
TARGET=OneSignalNotificationServiceExtension
PROJECT=src/main/native/OneSignalNotificationServiceExtension.xcodeproj
OUTPUTFOLDER=$(pwd)/libs

# cleanup before build
rm -rf ${BUILD_DIR}
rm -rf ${OUTPUTFOLDER}/${TARGET}.appex/
rm -rf src/main/native/OneSignal.framework

# copy OneSignal.framework
cp -r libs/OneSignal.framework src/main/native/

# Build Device and Simulator versions

xcodebuild -project ${PROJECT} -target ${TARGET} -configuration ${CONFIGURATION} -sdk iphoneos -arch arm64 -arch armv7 -arch armv7s BUILD_DIR="${BUILD_DIR}" BUILD_ROOT="${BUILD_ROOT}"
xcodebuild -project ${PROJECT} -target ${TARGET} -configuration ${CONFIGURATION} -sdk iphonesimulator -arch i386 -arch x86_64 BUILD_DIR="${BUILD_DIR}" BUILD_ROOT="${BUILD_ROOT}"

# make sure the output directory exists
mkdir -p "${OUTPUTFOLDER}/${TARGET}.appex/"

# Create universal binary file using lipo
lipo -create -output "${OUTPUTFOLDER}/${TARGET}.appex/${TARGET}" "${BUILD_DIR}/${CONFIGURATION}-iphoneos/${TARGET}.appex/${TARGET}" "${BUILD_DIR}/${CONFIGURATION}-iphonesimulator/${TARGET}.appex/${TARGET}"

# copy info.plist
cp "${BUILD_DIR}/${CONFIGURATION}-iphoneos/${TARGET}.appex/Info.plist" "${OUTPUTFOLDER}/${TARGET}.appex/"

# resign with empty signature to remove stuff added
codesign -f -s - "${OUTPUTFOLDER}/${TARGET}.appex/"

# build success just issue file to print platforms included
echo ===
echo ===
echo ===
echo ===
echo === Included archs
file "${OUTPUTFOLDER}/${TARGET}.appex/${TARGET}"

# cleanup after build
rm -rf ${BUILD_DIR}

echo ===
echo === OneSignalNotificationServiceExtension.appex was copied to ${OUTPUTFOLDER}/${TARGET}.appex/
echo ===

