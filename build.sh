#!/bin/sh

cd $(dirname "$0")

mvn clean package assembly:assembly

docker build -t kyhtanil/server -f Dockerfile.server target
docker build -t kyhtanil/sync -f Dockerfile.sync target
