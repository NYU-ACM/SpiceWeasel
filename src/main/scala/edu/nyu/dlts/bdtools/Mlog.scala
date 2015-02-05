package edu.nyu.dlts.bdtools



trait MlogClient {
  import com.typesafe.config.ConfigFactory
  import org.apache.http.util.EntityUtils
  import org.apache.http.auth.{ AuthScope,  UsernamePasswordCredentials }
  import org.apache.http.client.methods.HttpGet
  import org.apache.http.impl.client.{ BasicCredentialsProvider, HttpClientBuilder }
  import java.util.UUID
  
  val conf = ConfigFactory.load
  val provider = new BasicCredentialsProvider
  val creds = new UsernamePasswordCredentials(conf.getString("creds.username"), conf.getString("creds.password"))
  provider.setCredentials(AuthScope.ANY, creds)
  val client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build

  def getUUID(id: String): Option[UUID] = {
      val get = new HttpGet(s"http://ml-nyudl.herokuapp.com/mlog_entries/$id/text")
      val response = client.execute(get)
      val entity = response.getEntity
      val code = EntityUtils.toString(entity)
      EntityUtils.consume(entity)
      
      if(code.equals("no resource")) {
        None 
      } else { 
        Some(UUID.fromString(code))
      }
  }
}

trait TxtWriter {
  import java.io.{ File, FileWriter }
  import java.util.UUID

  def writeTxt(code: UUID, target: File): Unit = {

    target.createNewFile
    val writer = new FileWriter(target)
    writer.append("version: 0.1\n")
    writer.append(s"url: https://ml-nyudl.herokuapp.com/mlog_entries/$code\n")
    writer.flush
    writer.close

  }
}
