# For window OS (WSL)
sudo apt-get install dos2unix
dos2unix gradlew
dos2unix static_libs/*.sh
sudo apt-get update
sudo apt-get install openjdk-11-jdk
sudo update-alternatives --config java
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/b

# Pre build
$./gradlew preBuild

# Build aar
$./gradlew assembleRelease



