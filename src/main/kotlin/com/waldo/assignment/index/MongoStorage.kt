package com.waldo.assignment.index

import com.waldo.assignment.common.Event
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Service

/**
 * A simple implementation of Storage that stores incoming photo data
 * in in-memory mongo db
 *
 * On failure, it sends an event back to MongoStorageListener
 * to retry it again later
 *
 * @author Muhammad Salman
 */

@Service
open class MongoStorage : Storage {

  val logger = LoggerFactory.getLogger(MongoStorage::class.java)

  @Autowired
  lateinit var mongo: MongoOperations

  override fun save(event: Event) {

    logger.debug("{}", event)

    try {
      mongo.insert(PhotoMetaData(event.photoSource.key, event.photoData!!))
    } catch(e: Exception) {
      logger.error("Failed to save photo metadata", e)
    }
  }
}