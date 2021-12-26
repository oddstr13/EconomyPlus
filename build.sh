#!/bin/sh
docker run \
  -it --rm \
  -v ~/.cache/m2:/var/maven/.m2 \
  -v "$(pwd)":/usr/src/mymaven \
  -w /usr/src/mymaven \
  -u 1000 \
  -e MAVEN_CONFIG=/var/maven/.m2 \
  maven mvn \
    -Duser.home=/var/maven \
    -Drevision=$(git describe --tags --always --dirty | grep -oP '^[vV]?\K.*$') \
    clean package
