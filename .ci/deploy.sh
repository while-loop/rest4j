#!/usr/bin/env bash

echo $GPG_SECRET_KEYS | base64 --decode | $GPG_EXECUTABLE --import
echo $GPG_OWNERTRUST | base64 --decode | $GPG_EXECUTABLE --import-ownertrust
mvn -s .ci/settings.xml deploy -DskipTests -B -P release
