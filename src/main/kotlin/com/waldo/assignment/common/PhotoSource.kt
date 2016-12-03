package com.waldo.assignment.common

/**
 * Abstract class to handle the details about
 * what and where of the photo
 *
 * This class could be enhanced and extended to support
 * different types of original sources
 *
 * @author Muhammad Salman
 */

data class PhotoSource(var path: String, var key: String)