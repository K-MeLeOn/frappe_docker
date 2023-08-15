job("Build and push Docker") {
    host("Build artifacts and a Docker image") {
        dockerBuildPush {
            // Docker context, by default, project root
            // context = "docker"
          	platform = "linux/arm64"
            // path to Dockerfile relative to project root
            // if 'file' is not specified, Docker will look for it in 'context'/Dockerfile
            file = "image/production/ContainerfileCustom"
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