#!/bin/sh

if sudo docker images | grep -q eureka-server-registry; then
      # Stop and remove any existing container if it exists
      if sudo docker ps -a | grep -q eureka-server-registry; then
          sudo docker stop eureka-server-registry || true
          sudo docker rm eureka-server-registry || true

      # Remove the Docker image
      sudo docker rmi ${DOCKER_HUB_USERNAME}/eureka-server-registry || true
     fi
fi

if sudo docker images | grep -q email-marketing-gateway; then
      # Stop and remove any existing container if it exists
      if sudo docker ps -a | grep -q email-marketing-gateway; then
          sudo docker stop email-marketing-gateway || true
          sudo docker rm email-marketing-gateway || true

      # Remove the Docker image
      sudo docker rmi ${DOCKER_HUB_USERNAME}/email-marketing-gateway || true
     fi
fi

if sudo docker images | grep -q core-service; then
      # Stop and remove any existing container if it exists
      if sudo docker ps -a | grep -q core-service; then
          sudo docker stop core-service || true
          sudo docker rm core-service || true

      # Remove the Docker image
      sudo docker rmi ${DOCKER_HUB_USERNAME}/core-service || true
     fi
fi

if sudo docker images | grep -q auth-service; then
      # Stop and remove any existing container if it exists
      if sudo docker ps -a | grep -q auth-service; then
          sudo docker stop /auth-service || true
          sudo docker rm auth-service || true

      # Remove the Docker image
      sudo docker rmi ${DOCKER_HUB_USERNAME}/auth-service || true
     fi
fi

if sudo docker images | grep -q email-marketing-front; then
      # Stop and remove any existing container if it exists
      if sudo docker ps -a | grep -q email-marketing-front; then
          sudo docker stop email-marketing-front || true
          sudo docker rm email-marketing-front || true

      # Remove the Docker image
      sudo docker rmi ${DOCKER_HUB_USERNAME}/email-marketing-front || true
     fi
fi