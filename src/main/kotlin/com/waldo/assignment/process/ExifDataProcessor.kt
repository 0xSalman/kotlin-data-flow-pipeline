package com.waldo.assignment.process

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifDirectoryBase
import com.waldo.assignment.common.Event
import com.waldo.assignment.common.Producer
import com.waldo.assignment.index.MongoStorageListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.HashMap

/**
 * A simple implementation of PhotoProcessor that extracts the EXIF data
 * from an incoming photo using ImageMetadataReader library.
 * Then it sends an event to next step in the data flow
 * containing the extracted information
 *
 * On failure, it sends an event back to ExifDataProcessorListener
 * to retry it again later
 *
 * @author Muhammad Salman
 */

@Service
open class ExifDataProcessor : PhotoProcessor {

  val logger = LoggerFactory.getLogger(ExifDataProcessor::class.java)

  @Autowired
  lateinit var storageListener: MongoStorageListener
  @Autowired
  lateinit var producer: Producer

  override fun process(event: Event) {

    try {
      val metadata = ImageMetadataReader.readMetadata(event.photo!!)
      event.photo!!.close()
      val photoData = HashMap<String, Any>()

      metadata.directories
        .filter { it is ExifDirectoryBase }
        .flatMap { it.tags }
        .forEach {
          photoData.put(it.tagName.replace("\\s+".toRegex(), ""), it.description)
        }

      producer.publish(Event("mongoStorage", event.photoSource, event.photo, photoData), storageListener)
    } catch(e: Exception) {
      logger.error("Failed to extract EXIF data from photo", e)
    }
  }
}