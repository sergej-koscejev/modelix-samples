package org.modelix.sample.restapimodelserver

import University.Schedule.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.util.collections.*
import org.modelix.metamodel.typed
import org.modelix.model.api.IBranchListener
import org.modelix.model.api.ITree
import org.modelix.model.api.ITreeChangeVisitor
import org.modelix.model.api.PNodeAdapter
import org.modelix.model.area.PArea
import org.modelix.model.client.ReplicatedRepository
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.websocket.OnClose
import javax.websocket.OnError
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/updates")
@ApplicationScoped
class UpdateSocket(private val repo: ReplicatedRepository, private val mapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(UpdateSocket::class.java)

    init {
        repo.branch.addListener(object : IBranchListener {
            override fun treeChanged(oldTree: ITree?, newTree: ITree) {
                logger.info("Processing repo change")

                if (oldTree == null) {
                    logger.info("Ignoring changes because no old tree exited")
                    return
                }

                newTree.visitChanges(oldTree, object : ITreeChangeVisitor {
                    override fun childrenChanged(nodeId: Long, role: String?) {
                        logger.debug("Children of node changed. [nodeId={}, role={}]", nodeId, role)
                        handleChange(nodeId)
                    }

                    override fun containmentChanged(nodeId: Long) {
                        logger.debug("Containment of node changed. [nodeId={}]", nodeId)
                        handleChange(nodeId)
                    }

                    override fun propertyChanged(nodeId: Long, role: String) {
                        logger.debug("Property of node changed. [nodeId={}, role={}]", nodeId, role)
                        handleChange(nodeId)
                    }

                    override fun referenceChanged(nodeId: Long, role: String) {
                        logger.debug("Reference of node changed. [nodeId={}, role={}]", nodeId, role)
                        handleChange(nodeId)
                    }

                })
            }
        })
    }

    private fun handleChange(nodeId: Long) {
        val area = PArea(repo.branch)
        val node = PNodeAdapter(nodeId, repo.branch)
        area.executeRead {
            try {
                when (node.typed()) {
                    is N_Room -> broadcast(ChangeNotification(WhatChanged.ROOM, node.typed<N_Room>().toJson()))
                    is N_Rooms -> broadcast(ChangeNotification(WhatChanged.ROOM_LIST, node.typed<N_Rooms>().rooms.toList().toJson()))
                    is N_Lecture -> broadcast(ChangeNotification(WhatChanged.LECTURE, node.typed<N_Lecture>().toJson()))
                    is N_Courses -> broadcast(ChangeNotification(WhatChanged.LECTURE_LIST, node.typed<N_Courses>().lectures.toList().toJson()))
                    else -> logger.warn("Could not handle change")
                }
            } catch (e: RuntimeException){
                // see https://github.com/modelix/modelix.core/issues/31
                logger.warn("Ignoring change due to invalid model: "+ e.message)
            }
        }
    }

    private val sessions = ConcurrentSet<Session>()

    private fun broadcast(data: Any) {
        logger.info("Broadcasting to all sessions. [data={}]", data)
        val mapped = mapper.writeValueAsString(data)
        sessions.forEach { session ->
            logger.debug("Broadcasting to session. [session={}, mapped={}]", session, mapped)
            session.asyncRemote.sendObject(mapped)
        }
    }

    @OnOpen
    fun onOpen(session: Session) {
        logger.debug("Opening new session. [session={}]", session)
        sessions.add(session);
    }

    @OnClose
    fun onClose(session: Session) {
        logger.debug("Closing session regularly. [session={}]", session)
        sessions.remove(session)
    }

    @OnError
    fun onError(session: Session, throwable: Throwable) {
        logger.warn("Closing session after error. [session={}, throwable={}]", session, throwable, throwable)
        sessions.remove(session)
    }

}
