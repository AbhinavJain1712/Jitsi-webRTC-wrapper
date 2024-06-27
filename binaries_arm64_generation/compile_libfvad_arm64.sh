#!/usr/bin/env bash
set -e

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
TARGET="$SCRIPTPATH/target_libfvad/"

# Cross-compiler setup for ARM64 on macOS
export CC="xcrun --sdk macosx clang"
export CFLAGS="-arch arm64 -target arm64-apple-macos11 -std=c11"
export LDFLAGS="-arch arm64 -target arm64-apple-macos11"

cd "$SCRIPTPATH"

# Clone libfvad repository if not already present
if [ ! -d "libfvad" ]; then
    git clone git@github.com:dpirch/libfvad.git
fi

cd libfvad

# Run autoreconf to generate the configure script
autoreconf -i

./configure --prefix="$TARGET" --host=arm-apple-darwin
make CC="$CC" CFLAGS="$CFLAGS" LDFLAGS="$LDFLAGS"
make install

# Move the generated dynamic library to the desired location
mkdir -p lib/native/darwin-arm64
cp "$TARGET/lib/libfvad.0.dylib" lib/native/darwin-arm64/
mv lib/native/darwin-arm64/libfvad.0.dylib lib/native/darwin-arm64/libfvad.dylib

# Clean up the temporary build directory
rm -r "$TARGET"

echo "libfvad.dylib has been successfully built and installed."









