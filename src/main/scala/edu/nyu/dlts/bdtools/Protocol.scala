package edu.nyu.dlts.bdtools

object Protocol {
  case class MlogObject(repository: String, collectionCode: String, mediaId: Int)
}