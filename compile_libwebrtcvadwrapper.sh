#!/usr/bin/env bash
set -e

mkdir -p cmake-build

cd cmake-build
cmake ../
make
cp libwebrtcvadwrapper.dylib ../lib/native/Darwin/.
cd ..
