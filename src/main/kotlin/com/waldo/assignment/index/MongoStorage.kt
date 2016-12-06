package com.waldo.assignment.index

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Producer
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
  @Autowired
  lateinit var producer: Producer
  @Autowired
  lateinit var storageListener: MongoStorageListener

  override fun save(event: Event) {

    logger.debug("{}", event)

    try {

      if (event.photoSource.key == "0188017b-0d90-4cab-9009-bbb74501c3d5.ede96cc7-5500-4b3a-8828-26aabcaa2f4c.jpg") {
        throw RuntimeException("Retry test!")
      }

      mongo.insert(PhotoMetaData(event.photoSource.key, event.photoData!!))
    } catch(e: Exception) {
      logger.error("Failed to save photo metadata", e)
      event.retries++
      producer.publish(event, storageListener)
    }
  }
}