package com.waldo.assignment

import com.waldo.assignment.consume.DataConsumer
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync

/**
 * Main class
 *
 * @author Muhammad Salman
 */

@SpringBootApplication
@EnableAsync
open class Application {

  val logger = LoggerFactory.getLogger(Application::class.java)

  @Bean
  open fun init(dataConsumer: DataConsumer) = CommandLineRunner {
    try {
      dataConsumer.consume()
    } catch(e: Exception) {
      logger.error("Failed to consume data", e)
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}