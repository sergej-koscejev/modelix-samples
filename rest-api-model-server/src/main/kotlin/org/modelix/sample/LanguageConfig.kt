package org.modelix.sample

import University.Schedule.api.gen.University_Schedule
import University.Schedule.api.gen.jetbrains_mps_lang_core
import University.Schedule.api.gen.org_modelix_model_repositoryconcepts
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import org.modelix.model.api.INodeReference
import org.modelix.model.api.PNodeReference
import org.modelix.model.lazy.INodeReferenceSerializer
import org.modelix.mps.apigen.runtime.MPSLanguageRegistry
import org.modelix.mps.apigen.runtime.MPSNodeReferenceDeserializer
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

/**
 * This class configures access to models using the api-gen generated classes. The required configuration is part
 * of the Quarkus application initialization using Quarkus event listeners.
 */
@ApplicationScoped
class LanguageConfig {

    fun configure(@Observes event: StartupEvent) {
        MPSLanguageRegistry.register(jetbrains_mps_lang_core.INSTANCE)
        MPSLanguageRegistry.register(University_Schedule.INSTANCE)
        MPSLanguageRegistry.register(org_modelix_model_repositoryconcepts.INSTANCE)

        // Add our custom serializer for the REST representation of nodes
        INodeReferenceSerializer.register(serializer)
        // This serializer has to be registered to for being able to resolve node references inside a model
        INodeReferenceSerializer.register(MPSNodeReferenceDeserializer.Companion)
    }

    /**
     * Unregisters node serializers. This is useful for the Quarkus dev mode, where otherwise serializers might pile up
     * in the singleton backing field.
     */
    fun destroy(@Observes event: ShutdownEvent) {
        INodeReferenceSerializer.unregister(serializer)
        INodeReferenceSerializer.unregister(MPSNodeReferenceDeserializer.Companion)
    }

    companion object {
        val serializer = NodeSerializer()

        /**
         * A custom node serializer used in the REST API.
         */
        class NodeSerializer : INodeReferenceSerializer {
            private val prefix = "courses--"
            override fun deserialize(serialized: String): INodeReference? {
                if (!serialized.startsWith(prefix)) return null

                val (id, branch) = serialized.substring(prefix.length).split("_")
                return PNodeReference(id.toLong(), branch)
            }

            override fun serialize(ref: INodeReference): String? {
                if (ref !is PNodeReference) return null

                return "$prefix${ref.id}_${ref.branchId}"
            }
        }
    }

}