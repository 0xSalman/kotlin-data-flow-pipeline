package com.waldo.assignment.common

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.HashMap

/**
 * Note: these could be in their own files
 *
 * Simple classes for publishing events and listening to them
 *
 * @author Muhammad Salman
 */

interface Listener {
  var name: String
  fun onEvent(event: Event)
}

/**
 * There could be multiple implementations of this producer
 * For example, send an event to an external queue or API etc
 *
 * Each event is published asynchronously to prevent blocking
 */
interface Producer {
  @Async
  fun publish(event: Event, listener: Listener? = null)
}

/**
 * A simple producer that emits an event to in-memory listener
 */
@Service
open class SimpleProducer() : Producer {

  override fun publish(event: Event, listener: Listener?) {
    listener!!.onEvent(event)
  }
}

class Event(
  var name: String,
  var photoSource: PhotoSource,
  var photo: InputStream? = null,
  var photoData: MutableMap<String, Any>? = null,
  var retries: Int = 0,
  var steps: MutableMap<String, Listener> = HashMap(),
  var retryPrevStep: Boolean = false) {

  fun addNextStep(step: Listener): Event {
    return addStep(step.name, step)
  }

  fun addPrevStep(step: Listener): Event {
    return addStep(step.name, step)
  }

  private fun addStep(key: String, step: Listener): Event {
    if (steps[key] == null) {
      steps[key] = step
    }
    return this
  }

  override fun toString(): String {
    return "Event[name=$name,photoKey=${photoSource.key}]"
  }
}

