package com.waldo.assignment.common

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.LinkedList
import java.util.Queue

/**
 * Listens to an exception event and save event in local queue
 *
 * @author Muhammad Salman
 */

@Service
open class ExceptionListener : Listener {

  val logger = LoggerFactory.getLogger(ExceptionListener::class.java)
  override var name = "exception"

  var exceptionQueue: Queue<Event> = LinkedList()

  override fun onEvent(event: Event) {
    logger.warn("exception event")
    exceptionQueue.add(event)
  }
}



