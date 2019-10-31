#!/usr/bin/env bash
git ls-files -o
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . --tag "$DOCKER_IMAGE" --build-arg JAR_FILE=./build/libs/dominigeiger-0.0.1-SNAPSHOT.jar
docker push "$DOCKER_IMAGE"