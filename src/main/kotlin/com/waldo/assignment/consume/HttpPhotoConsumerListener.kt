package com.waldo.assignment.consume

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Listener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Listens to an event to trigger fetching of
 * photo using HttpPhotoConsumer
 *
 * This also acts as a retry queue
 *
 * @author Muhammad Salman
 */

@Service
open class HttpPhotoConsumerListener() : Listener {

  val logger = LoggerFactory.getLogger(HttpPhotoConsumerListener::class.java)

  @Autowired
  lateinit var photoConsumer: PhotoConsumer

  override fun onEvent(event: Event) {
    logger.info("get photo event")
    photoConsumer.consume(event)
  }
}