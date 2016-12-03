package com.waldo.assignment.index

import com.waldo.assignment.common.Event

/**
 * Interface to store/index incoming photo data
 *
 * Note: there could be multiple implementation of this.
 *  Different ways to store in different storage systems
 *
 * @author Muhammad Salman
 */

interface Storage {

  fun save(event: Event)
}