#!/usr/bin/env bash
set -e

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
TARGET="$SCRIPTPATH/target_libfvad/"

cd $SCRIPTPATH

if  [ ! -d "libfvad" ]; then
    git clone git@github.com:dpirch/libfvad.git
fi

cd libfvad
autoreconf -i
./configure prefix=$TARGET
make
make install

cd ..
cp "$TARGET/lib/libfvad.0.dylib" lib/native/Darwin
mv lib/native/Darwin/libfvad.0.dylib lib/native/Darwin/libfvad.dylib

rm -r $TARGET