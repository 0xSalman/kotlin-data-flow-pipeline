package com.waldo.assignment.consume

import com.waldo.assignment.common.Event
import com.waldo.assignment.common.ExceptionListener
import com.waldo.assignment.common.PhotoSource
import com.waldo.assignment.common.Producer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * A simple implementation of DataConsumer that fetches
 * a list of photo data from amazon S3 storage via http.
 * Then it iterates over the list and sends an event to
 * next step in the data flow with information
 * about how to fetch/process the photo
 *
 * @author Muhammad Salman
 */

@Service
open class AmazonHttpDataConsumer : DataConsumer {

  val logger = LoggerFactory.getLogger(AmazonHttpDataConsumer::class.java)
  val sourceUrl = "http://s3.amazonaws.com/waldo-recruiting"

  @Autowired
  lateinit var photoConsumerListener: HttpPhotoConsumerListener
  @Autowired
  lateinit var exceptionListener: ExceptionListener
  @Autowired
  lateinit var producer: Producer

  override fun consume() {

    try {
      val conn = URL(sourceUrl).openConnection() as HttpURLConnection
      conn.requestMethod = "GET"

      if (conn.responseCode != 200) {
        RuntimeException("Failed to get bucket list. HTTP error code: ${conn.responseCode}")
      }

      // extract xml data
      val xmlData = conn.inputStream.bufferedReader().use { it.readText() }
      conn.disconnect()
      val result = JAXB.unmarshal(StringReader(xmlData), BucketResult::class.java)

      // iterate over the list and emit photo consumer event
      result.contents?.forEach { photoInfo ->
        if (photoInfo.key != null) {
          val photoSource = PhotoSource(sourceUrl, photoInfo.key)
          val event = Event(photoConsumerListener.name, photoSource)
            .addPrevStep(exceptionListener)
            .addNextStep(photoConsumerListener)
          if (event.photoSource.key == "0188017b-0d90-4cab-9009-bbb74501c3d5.ede96cc7-5500-4b3a-8828-26aabcaa2f4c.jpg") {
            producer.publish(event, photoConsumerListener)
          }
        }
      }
    } catch(e: Exception) {
      // FIXME should try it again at later time
      logger.error("Failed to consume s3 bucket list", e)
    }
  }
}

/**
 * Note: these could be in their own files
 *
 * Classes to map to fields of incoming xml input
 * Easier to de-serialize xml using JAXB
 *
 * @author Muhammad Salman
 */

@XmlRootElement(name = "ListBucketResult")
class BucketResult() {
  @XmlElement(name = "Name") val name: String? = null
  @XmlElement(name = "MaxKeys") val maxKeys: Int? = null
  @XmlElement(name = "Contents") val contents: List<PhotoInfo>? = null
}

class PhotoInfo() {
  @XmlElement(name = "Key") val key: String? = null
  @XmlElement(name = "ETag") val eTag: String? = null
  @XmlElement(name = "Size") val size: Int? = null
  @XmlElement(name = "LastModified") val lastModified: Date? = null
}
