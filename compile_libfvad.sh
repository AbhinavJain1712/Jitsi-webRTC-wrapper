#!/usr/bin/env bash
set -e

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
TARGET="$SCRIPTPATH/target_libfvad/"

# Cross-compiler setup for x86_64 on macOS
export CC="xcrun --sdk macosx clang"
export CFLAGS="-arch x86_64 -target x86_64-apple-macos10.12 -std=c11"
export LDFLAGS="-arch x86_64 -target x86_64-apple-macos10.12"

cd "$SCRIPTPATH"

# Clone libfvad repository if not already present
if [ ! -d "libfvad" ]; then
    git clone git@github.com:dpirch/libfvad.git
fi

cd libfvad

# Run autoreconf to generate the configure script
autoreconf -i

./configure --prefix="$TARGET" --host=x86_64-apple-darwin
make CC="$CC" CFLAGS="$CFLAGS" LDFLAGS="$LDFLAGS"
make install

# Move the generated dynamic library to the desired location
mkdir -p lib/native/darwin-x86-64
cp "$TARGET/lib/libfvad.0.dylib" lib/native/darwin-x86-64/
mv lib/native/darwin-x86-64/libfvad.0.dylib lib/native/darwin-x86-64/libfvad.dylib

# Clean up the temporary build directory
rm -r "$TARGET"

echo "libfvad.dylib has been successfully built and installed."
