plugins {
    kotlin("jvm")
    application
}

val modelix_version: String by project

dependencies {
    implementation("org.modelix:model-server-fatjar:$modelix_version")
}

application {
    mainClass.set("org.modelix.model.server.Main")
    applicationDefaultJvmArgs = listOf("-XX:MaxRAMPercentage=85")

    // well this would be nice. if it worked...
    // args = listOf("-inmemory", "-dumpin courses.modelsever.dump")
}

