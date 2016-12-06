package com.waldo.assignment.process

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Listener
import com.waldo.assignment.common.Producer
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
  override var name = "exifDataProcessor"
  val prevStepName = "httpPhotoConsumer"
  val exceptionStep = "exception"

  @Autowired
  lateinit var photoProcessor: PhotoProcessor
  @Autowired
  lateinit var producer: Producer

  override fun onEvent(event: Event) {
    logger.info("process photo event")
    /*
     * When event is tried,
     * 1) less than 5 times, proceed to the next step in the data flow
     * 2) 5 times and have not yet retried the previous step, send it back to previous step
     * 3) otherwise send it to exception queue
     */
    if (event.retries < 5) {
      photoProcessor.process(event)
    } else if (event.retries == 5 && !event.retryPrevStep) {
      event.retries = 0
      event.retryPrevStep = true
      producer.publish(event, event.steps[prevStepName])
    } else {
      producer.publish(event, event.steps[exceptionStep])
    }
  }
}