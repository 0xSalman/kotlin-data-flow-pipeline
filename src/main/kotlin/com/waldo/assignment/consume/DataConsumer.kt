package com.waldo.assignment.consume

/**
 * Interface to consume a list of photo data
 * from an external storage/source
 * This list contains information about how to get each photo
 *
 * Note: there could be multiple implementations of this. For example,
 *  1) Consumer that exposes API
 *  2) Consumer that polls
 *  3) Consumer that acts on an event
 * etc
 *
 * @author Muhammad Salman
 */

interface DataConsumer {

  fun consume()
}