package com.waldo.assignment

import org.slf4j.LoggerFactory

class Application

fun main(args: Array<String>) {

  val logger = LoggerFactory.getLogger(Application::class.java)

  try {
    logger.debug("hello World!")
  } catch (e: Exception) {
    logger.error(e.message, e)
  }
}
