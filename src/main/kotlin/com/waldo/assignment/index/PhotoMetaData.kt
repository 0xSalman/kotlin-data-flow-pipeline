package com.waldo.assignment.index

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Muhammad Salman
 */

@Document(collection = "photos")
data class PhotoMetaData(
  @Id var key: String,
  var metaData: Map<String, Any>)