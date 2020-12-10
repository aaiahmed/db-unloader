docker build -t db-unloader .
docker login aahmed.jfrog.io
docker tag db-unloader aahmed.jfrog.io/docker/db-unloader
docker push aahmed.jfrog.io/docker/db-unloader
