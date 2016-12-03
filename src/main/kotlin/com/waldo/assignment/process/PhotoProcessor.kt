package com.waldo.assignment.process

import com.waldo.assignment.common.Event

/**
 * Interface to process incoming photo
 *
 * Note: there could be multiple implementation of this.
 *  Different ways to process the photo and what information to extract
 *
 * @author Muhammad Salman
 */

interface PhotoProcessor {

  fun process(event: Event)
}