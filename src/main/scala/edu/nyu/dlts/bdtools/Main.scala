package edu.nyu.dlts.bdtools

import java.io.File

trait IO extends MlogClient with TxtWriter {
  def iterateFiles(file: File): Unit = {
    if(file.isFile) fileTest(file)
    if(file.isDirectory) file.listFiles.foreach{i => iterateFiles(i) } 
  }	

  def shutdown(): Unit = {
    client.close
  }

  def fileTest(file: File): Unit = {
    import org.apache.commons.io.FilenameUtils
    import org.apache.commons.io.FileUtils
    import scala.util.matching.Regex

    val fName = file.getName

    //match and delete cue files
    val cueFile  = "\\.cue$".r
    val test1 = cueFile findFirstIn fName
    
    test1 match {
      case Some(x) => {
        println("deleting " + file.getName)
        file.delete()
      }
      
      case None =>
    }

    //match and rename files with ".cue."
    val cue = "\\.cue\\.".r
    val test2 = cue findFirstIn fName
    test2 match {
      case Some(x) => {
  	print("renaming: " + fName)
  	val file2 = new File(file.getParentFile, cue.replaceAllIn(fName, "."))
  	FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }

    //match and rename files with ".001."
    val raw = "\\.001\\.".r
    val test3 = raw findFirstIn fName
    test3 match {
      case Some(x) => {
  	print("renaming: " + fName)
  	val file2 = new File(file.getParentFile, raw.replaceAllIn(fName, "."))
  	FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }

    //match and rename files with ".e01."
    val e01 = "\\.E01\\.".r
    val test4 = e01 findFirstIn fName
    test4 match {
      case Some(x) => {
  	print("renaming: " + fName)
  	val file2 = new File(file.getParentFile, e01.replaceAllIn(fName, "."))
  	FileUtils.moveFile(file, file2)
        println(" to: " + file2.getName)
      }
      case None =>
    }

    //create a medialog file for E01s and 001s
    val img = "E01$|001$|iso$".r
    val test5 = img findFirstIn fName
    test5 match {
      case Some(x) => { 
        val base = FilenameUtils.getBaseName(fName).toLowerCase
        val parent = file.getParentFile.getAbsoluteFile
        val mlog = new File(parent, "medialog.txt")
        
        !mlog.exists match {
          case true => {
            getUUID(base) match {
              case Some(uuid) => {
                writeTxt(uuid, mlog)
                println(s"medialog.txt file generated for $fName")
              }
              case None => println(s"medialog.txt file could not be created for: $fName")
            }
          }
          case false => println(s"medialog.txt exists for: $fName")
        }
      }
      case None =>
    }
  }
}


object Cleaner extends App with IO {
  val root = new File(args(0))
  iterateFiles(root)
  shutdown
}
