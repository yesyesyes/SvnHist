import java.util.{ Collection => JCollection }
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.tmatesoft.svn.core.SVNURL
import scala.collection.Iterable
import org.tmatesoft.svn.core.SVNLogEntry
import org.tmatesoft.svn.core.SVNNodeKind
import scala.collection.JavaConversions._
import java.io.PrintWriter
import java.io.File

object Main {
  import Graph._
  import scala.xml._

  def main(args: Array[String]) {
	def isSet(index: Int, arg: String) = args.length >= index && args(index) == arg
    args match {
      case Array(url, path, login, password, msg, _*) =>
        val ls = SvnUtils getPathList(url, path, login, password, msg, isSet(5, "-filter"))
        writeToFile(ls, msg, true)
      case _ => println("Not enough arguments should be: repoUrl path login password msg [-filter]")
    }  
  }

  def writeToFile(ls: List[String], msg: String, writeXml: Boolean, sep: String = "/") {
    val tree = build(ls, sep)
    if (writeXml)
      writeTo(msg + ".xml", tree.toXML match {
        case None => "Nothing found"
        case Some(x) => XmlToString(x)
      })
    writeTo(msg + ".txt", mkString(tree))
  }

  private def writeTo(file: String, text: String) {
	println("Writing to " + file + "...")
    val writer = new PrintWriter(new File(file))
    writer write text
    writer close ()
  }

  object SvnUtils {

    def getPathList(repoUrl: String, repoPath: String, name: String,
      pass: String, filterMsg: String, filterDeleted: Boolean): List[String] = {
      var repo = connectRepo(repoUrl + "/" + repoPath, name, pass)
      println("Retrieving history...")
      val filtered = filterWithCommitMsg(getLogs(repo), filterMsg)
      println("Verifying that files exist in head revision...")
      if (filterDeleted) {
        repo = connectRepo(repoUrl, name, pass)
        extrExist(filtered, repo).toList
      } else filtered.toList
    }

    private def connectRepo(path: String, name: String, pass: String): SVNRepository = {
      DAVRepositoryFactory setup ()
      val repository = SVNRepositoryFactory create (SVNURL parseURIEncoded path)
      repository setAuthenticationManager (SVNWCUtil createDefaultAuthenticationManager (name, pass))
      repository
    }

    def filterWithCommitMsg(logs: Set[SVNLogEntry], msg: String): Set[String] = {
      def getPath(e: SVNLogEntry): Set[String] = 
        if (e getMessage () contains msg) e getChangedPaths () keySet () toSet else Set[String]()
      logs.foldLeft(Set[String]()) { (x, y) => x ++ getPath(y) }
    }

    def extrExist(logs: Set[String], repo: SVNRepository): Set[String] = {
      def isExist(path: String) = repo.checkPath(path, -1) != SVNNodeKind.NONE
      logs filter (isExist)
    }

    def getLogs(repo: SVNRepository): Set[SVNLogEntry] = {
      val jcoll = (repo log (Array[String](), null, 0, -1, true, true)).asInstanceOf[JCollection[AnyRef]]
      jcoll map (_.asInstanceOf[SVNLogEntry]) toSet
    }
  }
}