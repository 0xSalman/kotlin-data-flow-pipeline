package com.waldo.assignment.process

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Listener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Listens to an event to trigger processing of
 * photo using ExifDataProcessor
 *
 * This also acts as a retry queue
 *
 * @author Muhammad Salman
 */

@Service
open class ExifDataProcessorListener : Listener {

  val logger = LoggerFactory.getLogger(ExifDataProcessorListener::class.java)

  @Autowired
  lateinit var photoProcessor: PhotoProcessor

  override fun onEvent(event: Event) {
    logger.info("process photo event")
    photoProcessor.process(event)
  }
}