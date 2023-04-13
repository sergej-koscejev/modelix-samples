plugins {
    application
}


dependencies {
    implementation("org.modelix:model-server")
}

application {
    mainClass.set("org.modelix.model.server.Main")

    // only relevant for docker - use Xmx and Xms here instead
    //applicationDefaultJvmArgs = listOf("-XX:MaxRAMPercentage=85")

    // well this would be nice if it was actually supported by the gradle plugin...
    // args = listOf("-inmemory", "-dumpin courses.modelsever.dump")
}

