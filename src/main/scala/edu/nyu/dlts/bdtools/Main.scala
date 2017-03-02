package edu.nyu.dlts.bdtools

import java.io.File
import com.typesafe.config.ConfigFactory
import org.apache.http.impl.client.CloseableHttpClient

object Cleaner extends App with IO with MlogClient {
  
  val root = new File(args(0))

  iterateFiles(root)

  client.close
}
