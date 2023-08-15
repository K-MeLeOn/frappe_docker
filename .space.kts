job("Build and push Docker") {
    host("Build and push the Docker image") {
        shellScript {
            content = """
                # Install latest docker (https://www.docker.com/blog/multi-arch-build-what-about-travis)
                sudo rm -rf /var/lib/apt/lists/*
                curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add -
                sudo add-apt-repository \"deb [arch=arm64] https://download.docker.com/linux/debian $(lsb_release -cs) edge\"
                sudo apt-get update
                sudo apt-get -y -o Dpkg::Options::=\"--force-confnew\" install docker-ce
                mkdir -vp ~/.docker/cli-plugins/
                curl --silent -L \"https://github.com/docker/buildx/releases/download/v0.11.2/buildx-v0.11.2.linux-arm64\" > ~/.docker/cli-plugins/docker-buildx
                chmod a+x ~/.docker/cli-plugins/docker-buildx
                docker buildx create --use
                docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
                docker buildx inspect --bootstrap
            """
        }
        dockerBuildPush {
            // Docker context, by default, project root
            // context = "docker"
          	platform = "linux/arm64"
            extraArgsForBuildCommand = listOf("--network host")
            // path to Dockerfile relative to project root
            // if 'file' is not specified, Docker will look for it in 'context'/Dockerfile
            file = "images/production/ContainerfileCustom"
          	labels["vendor"] = "frappe"
          	labels["arch"] = "arm64"

            val spaceRepo = "nl-c.registry.jetbrains.space/p/main/arm64/erpnext"
            // image tags for 'docker push'
            tags {
                +"$spaceRepo:1.0.${"$"}JB_SPACE_EXECUTION_NUMBER"
                +"$spaceRepo:latest"
            }
        }
    }
}