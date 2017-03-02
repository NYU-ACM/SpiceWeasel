package edu.nyu.dlts.bdtools



trait MlogClient {
  
  import org.apache.http.util.EntityUtils
  import org.apache.http.auth.{ AuthScope,  UsernamePasswordCredentials }
  import org.apache.http.client.methods.HttpGet
  import org.apache.http.impl.client.{ BasicCredentialsProvider, HttpClientBuilder, CloseableHttpClient }
  import java.util.UUID
  import com.typesafe.config.ConfigFactory
  import edu.nyu.dlts.bdtools.Protocol._
  
  val conf = ConfigFactory.load
  val username = conf.getString("creds.username")
  val password = conf.getString("creds.password")
  val baseURL = conf.getString("host.url")
  val client = getClient

  def shutdown() { client.close }

  def getClient(): CloseableHttpClient = {

    val provider = new BasicCredentialsProvider
    val creds = new UsernamePasswordCredentials(username, password)
    provider.setCredentials(AuthScope.ANY, creds)   
    HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build
  }

  def getCollectionID(collectionCode: String): Map[Int, UUID] = {
    Map(1 -> UUID.randomUUID)
  }


  def getUUID(mo: MlogObject): Option[UUID] = {
    Some(UUID.randomUUID)
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

trait IO extends MlogClient with TxtWriter {

  import java.io.File
  import org.apache.commons.io.FilenameUtils
  import org.apache.commons.io.FileUtils
  import scala.util.matching.Regex
  import edu.nyu.dlts.bdtools.Protocol._


  def iterateFiles(file: File): Unit = {
    
    if(file.isFile) {
      renameCue(file)
      renameRaw(file)
      renameE01(file)
      createMedilogTxt(file)
    }

    if(file.isDirectory) file.listFiles.foreach{i => iterateFiles(i) } 

  }

  def renameCue(file: File): Unit = {
    val cueFile = "\\.cue\\.".r
    val cueTest = cueFile findFirstIn file.getName
    
    cueTest match {
      case Some(x) => {
        print("renaming: " + file.getName)
        val file2 = new File(file.getParentFile, cueFile.replaceAllIn(file.getName, "."))
        //FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }
  }

  def renameRaw(file: File): Unit = {
    val rawFile = "\\.001\\.".r
    val rawTest = rawFile findFirstIn file.getName
    
    rawTest match {
      case Some(x) => {
        print("renaming: " + file.getName)
        val file2 = new File(file.getParentFile, rawFile.replaceAllIn(file.getName, "."))
        //FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }  
  }

  def renameE01(file: File): Unit = {
    val e01File = "\\.E01\\.".r
    val e01Test = e01File findFirstIn file.getName
    e01Test match {
      case Some(x) => {
        print("renaming: " + file.getName)
        val file2 = new File(file.getParentFile, e01File.replaceAllIn(file.getName, "."))
        //FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }
  }

  def createMedilogTxt(file: File): Unit = {
    val imgFile = "E01$|001$|iso$".r
    val filename = file.getName
    val imgTest = imgFile findFirstIn filename
    imgTest match {
      case Some(i) => {
        val nameParts = filename.split("\\.")(0).split("_")
        val mlogObject = new MlogObject(nameParts(0), nameParts(1).toLowerCase + nameParts(2), nameParts(3).toInt)
        println(mlogObject)
      }

      case None =>
    }
  }

}
