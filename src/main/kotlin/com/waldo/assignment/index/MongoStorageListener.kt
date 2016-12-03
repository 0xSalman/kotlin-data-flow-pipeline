package com.waldo.assignment.index

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Listener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Listens to an event to trigger indexing/storing of
 * photo data using MongoStorage
 *
 * This also acts as a retry queue
 *
 * @author Muhammad Salman
 */

@Service
open class MongoStorageListener : Listener {

  @Autowired
  lateinit var storage: Storage

  val logger = LoggerFactory.getLogger(MongoStorageListener::class.java)

  override fun onEvent(event: Event) {
    logger.info("photo data storage event")
    storage.save(event)
  }
}