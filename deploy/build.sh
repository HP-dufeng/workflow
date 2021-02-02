#!/bin/bash
set -eou pipefail

readonly IMAGE_NAME=${1:-"automation:latest"}
readonly BUILD_ID=${2:-""}

echo "IMAGE_NAME: ${IMAGE_NAME}"
echo "BUILD_ID: ${BUILD_ID}"


# go back to root directory of project
cd "$(dirname "$0")" && cd ../
pwd

echo "Building image: ${IMAGE_NAME} ..."

docker build  --build-arg buildId=${BUILD_ID} -f deploy/Dockerfile -t ${IMAGE_NAME} .
