#!/usr/bin/env bash
set -e

mkdir -p cmake-build
cd cmake-build
cmake -DCMAKE_OSX_ARCHITECTURES=arm64 ..
make
cp libwebrtcvadwrapper.dylib ../lib/native/darwin-arm64/.
cd ..































for x86-64

#!/usr/bin/env bash
set -e

# Create build directory and navigate into it
mkdir -p cmake-build
cd cmake-build

# Configure the build system for x86_64 architecture
cmake -DCMAKE_OSX_ARCHITECTURES=x86_64 ..

# Build the project
make

# Copy the built library to the appropriate directory
cp libwebrtcvadwrapper.dylib ../lib/native/darwin-x86-64/.

# Navigate back to the original directory
cd ..
