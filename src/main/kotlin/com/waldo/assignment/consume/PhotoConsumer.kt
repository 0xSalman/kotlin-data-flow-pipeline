package com.waldo.assignment.consume

import com.waldo.assignment.common.Event

/**
 * Interface to fetch photo from an external storage/source
 * using the provided PhotoSource
 *
 * Note: Due to fact that, this depends on a PhotoSource via Event,
 *  there could be multiple implementations of this. For example,
 *  1) Consumer that exposes API and receives photo
 *  2) Consumer that uses polling and fetches photo from a database
 *  3) Consumer that fetches from an internal file system
 *  4) Consumer that fetches from a url via http
 * etc
 *
 * @author Muhammad Salman
 */

interface PhotoConsumer {

  fun consume(event: Event)
}