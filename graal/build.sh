#!/bin/bash -eux


native-image \
  --verbose \
  --allow-incomplete-classpath \
  --no-fallback  \
  -jar ../target/odterm-metrics-shaded.jar
