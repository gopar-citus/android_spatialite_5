# For window OS (WSL)
- sudo apt-get install dos2unix
- sudo apt install pkg-config
- sudo apt-get install libicu-le-hb-dev
- sudo apt-get install cmake
- dos2unix gradlew # remove CRLF
- find ./static_libs -type f -exec file "{}" \; | grep ':.*CRLF' | cut -d: -f1 | xargs -I {} sed -i 's/\r$//' "{}" # remove CRLF
- dos2unix ./static_libs/icu73.2/source/runConfigureICU # remove CRLF
- sudo apt-get update
- sudo apt-get install openjdk-17-jdk
- sudo update-alternatives --config java
- export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
- export NDK_HOME=$HOME/android/ndk/21.4.7075529
- export PROJECT_ROOT=/mnt/c/Dev/AndroidStudioWork/android_spatialite_5
- rm -rf ./static_libs/icu73.2/build
- 필요시 빌드된 sqlite3.pc, geos.pc 파일을 ubuntu에 복사 (pkg-config 경로 /lib/pkgconfig 에 복사)

# Pre build
./gradlew preBuild

# Build aar
./gradlew assembleRelease



