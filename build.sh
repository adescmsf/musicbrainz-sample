#!/bin/bash

#CONST VALUES
apk_name=MBSample
apk_extension=.apk
apk_folder=build/apk/

#BUILD APK
chmod +x gradlew
./gradlew clean
./gradlew assembleRelease

#CREATE FILENAME
apk_final_path=${apk_folder}${apk_name}${apk_extension}

#COPY FILE
mkdir -p ${apk_folder}
apk_origin_path=$(find . -type f -name '*apk' | tail -1)
cp ${apk_origin_path} ${apk_final_path}

#VOILA