#!/usr/bin/env bash
set -e

mkdir -p cmake-build
cd cmake-build
cmake -DCMAKE_OSX_ARCHITECTURES=x86_64 ..
make
cp libwebrtcvadwrapper.dylib ../lib/native/darwin-x86-64/.
cd ..
