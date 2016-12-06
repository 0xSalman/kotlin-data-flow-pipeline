package com.waldo.assignment.consume

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Producer
import com.waldo.assignment.process.ExifDataProcessorListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple implementation of PhotoConsumer that fetches
 * a photo from an external source via http.
 * Then it sends an event to next step in the data flow with extracted photo
 *
 * On failure, it sends an event back to HttpPhotoConsumerListener
 * to retry it again later
 *
 * @author Muhammad Salman
 */

@Service
open class HttpPhotoConsumer : PhotoConsumer {

  val logger = LoggerFactory.getLogger(HttpPhotoConsumer::class.java)

  @Autowired
  lateinit var dataProcessorListener: ExifDataProcessorListener
  @Autowired
  lateinit var photoConsumerListener: HttpPhotoConsumerListener
  @Autowired
  lateinit var producer: Producer

  override fun consume(event: Event) {

    logger.debug("{}", event)

    try {

      val conn = URL("${event.photoSource.path}/${event.photoSource.key}").openConnection() as HttpURLConnection
      conn.requestMethod = "GET"

      if (conn.responseCode != 200) {
        RuntimeException("Failed to fetch photo. HTTP error code: ${conn.responseCode}")
      }

      event.photo = conn.inputStream
      event.addNextStep(dataProcessorListener)

      producer.publish(event, dataProcessorListener)
    } catch(e: Exception) {
      logger.error("Failed to consume photo", e)
      // retry it again
      event.retries++
      producer.publish(event, photoConsumerListener)
    }
  }
}
