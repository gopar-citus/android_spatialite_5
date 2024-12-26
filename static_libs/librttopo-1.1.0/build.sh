#!/bin/bash
source "$BUILD_UTILS_SH" || { echo "cannot load buildutils"; exit 1; }
source "$AND_TOOLCHAIN_FILE" ||  exit_with_message "cannot load andtoolchain";

[ -z "$GEOS_INSTALL_DIR" ] && exit_with_message "Error: GEOS_INSTALL_DIR environment variable is not set."
GEOS_INCLUDE_DIR="$GEOS_INSTALL_DIR/include"
GEOS_LIB_DIR="$GEOS_INSTALL_DIR/lib"
GEOS_BIN_DIR="$GEOS_INSTALL_DIR/bin"

#dirs
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
BUILD_DIR="$SCRIPT_DIR/build"
echo "Building $BUILD_DIR for $TARGET_ARCH_ABI"

case $TARGET_ARCH_ABI in
  "armeabi-v7a")
    TARGET="armv7a-linux-androideabi"
    ;;
  "arm64-v8a")
    TARGET="aarch64-linux-android"
    ;;
  "x86")
    TARGET="i686-linux-android"
    ;;
  "x86_64")
    TARGET="x86_64-linux-android"
    ;;
  *)
    exit_with_message "Unsupported TARGET_ARCH_ABI: '$TARGET_ARCH_ABI'"
    ;;
esac

cd "$SCRIPT_DIR" ||  exit_with_message "cannot cd to $SCRIPT_DIR";


#cleanup
rm -rf "$BUILD_DIR" || exit_with_message "cannot cleanup build dir";
mkdir -pv "$BUILD_DIR"  || exit_with_message "cannot make dirs";
echo "build dir is $BUILD_DIR";
chmod +x configure;
./autogen.sh
LDFLAGS="-L$GEOS_LIB_DIR -lgeos_c -llog" \
CFLAGS="$CFLAGS -I$GEOS_INCLUDE_DIR" \
ac_cv_search_GEOSContext_setErrorMessageHandler_r=yes \
./configure \
    --host="$TARGET" \
    --prefix="$BUILD_DIR" \
    --enable-shared=NO \
    --enable-static=YES \
    --with-geosconfig="$GEOS_BIN_DIR/geos-config" \
    && make clean && make && make install && 
{ cp -f -v "$BUILD_DIR/lib/"*.a "$JNI_LIB_ABI_DIR" || exit_with_message "cannot cp lib" &&
cp -f -v -R "$BUILD_DIR"/include "$JNI_DIR/rttopo" || exit_with_message "cannot cp include"; }