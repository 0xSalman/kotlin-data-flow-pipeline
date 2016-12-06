package com.waldo.assignment.consume

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Listener
import com.waldo.assignment.common.Producer
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
  override var name = "httpPhotoConsumer"
  val prevStepName = "exception"

  @Autowired
  lateinit var photoConsumer: PhotoConsumer
  @Autowired
  lateinit var producer: Producer

  override fun onEvent(event: Event) {
    logger.info("get photo event")
    /*
      * When event is tried,
      * 1) 5 times or more, send it back to previous step in the data flow (exception in this case)
      * 2) otherwise proceed to the new step
     */
    if (event.retries >= 5) {
      producer.publish(event, event.steps[prevStepName])
    } else {
      photoConsumer.consume(event)
    }
  }
}