#!/usr/bin/env bash
set -e

mkdir -p cmake-build
cd cmake-build
cmake -DCMAKE_OSX_ARCHITECTURES=arm64 ..
make
cp libwebrtcvadwrapper.dylib ../lib/native/Darwin-arm64/.
cd ..
