#!/bin/bash
source "$BUILD_UTILS_SH" || { echo "cannot load buildutils"; exit 1; }
source "$AND_TOOLCHAIN_FILE" ||  exit_with_message "cannot load andtoolchain";

#dirs
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
BUILD_DIR="$SCRIPT_DIR/build"

echo "Building $BUILD_DIR for $TARGET_ARCH_ABI"

case $TARGET_ARCH_ABI in
  "armeabi-v7a")
    TARGET="arm-linux-android"
    ;;
  "arm64-v8a")
    TARGET="arm-64-linux-android"
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
./configure --prefix="$BUILD_DIR" --host="$TARGET" --enable-static=yes --enable-shared=no &&
make clean && make && make install &&
{ cp -f -v "$BUILD_DIR/lib/"*.a "$JNI_LIB_ABI_DIR" || exit_with_message "cannot cp lib" &&
cp -f -v -R "$BUILD_DIR/include" "$JNI_DIR/iconv" || exit_with_message "cannot cp include"; }
