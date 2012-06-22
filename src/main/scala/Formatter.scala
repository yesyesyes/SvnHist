object Formatter {
  import scala.collection.JavaConversions._
  def format(ls: java.util.List[java.lang.String], us: java.lang.String) = {
    new Formatter().createGraph(ls, us)
  }

  class Formatter {

    import scala.collection.mutable.{ListBuffer, Stack, Buffer}
    import scala.collection.mutable.Stack
    import scala.collection.mutable.Buffer
    import java.io.FileWriter
    import java.io.BufferedWriter
    private var head: Node = null
    private var list: ListBuffer[Node] = ListBuffer()

    def createGraph(paths: Buffer[String], us: String) = {
      for (pathEntry: String <- paths) {
        var cur: Node = head
        for (curStr: String <- splitNotEmpty(pathEntry)) {
          head match {
            case null => head = new Node(curStr, 0, "non")
            cur = head
            case _ =>
              val lastNode: Option[Node] = cur.getLast(curStr)
              if (lastNode == None) {
                val attachedNode: Node = new Node(curStr, cur.level + 1, cur.parent + "/" + curStr)
                cur.children += attachedNode
                cur = attachedNode
              }
              else cur = lastNode.get
          }
        }
      }
      print(us)
    }

    private def splitNotEmpty(pathEntry: String): List[String] = pathEntry.split("/").toList.filterNot(_.equals(""))

    private def print(us: String) = {
      val explored: ListBuffer[Node] = ListBuffer()
      val nodes: Stack[Node] = new Stack()
      nodes.push(head)
      while (nodes.isEmpty == false) {
        val node: Node = nodes.pop()
        if (!explored.contains(node)) {
          list += (node)
          explored += node
          if (node.children.isEmpty == false)
            nodes.pushAll(node.children)
        }
      }
      levelPrint(us)
    }

    private def levelPrint(us: String) = {
      val writer = new BufferedWriter(new FileWriter(us + ".txt"))
      val out = new StringBuilder()
      list = list.sortWith((a, b) => a.parent == b.parent)
      var isPreviousFile = false
      var previousLevel = -1
      for (node: Node <- list) {
        if (isSingle(node))
          out.append("/" + node.value)
        else {
          if (isPreviousFile && isFile(node.value)
            && previousLevel == node.level)
            out.append(", " + node.value)
          else {
            out.append("\n")
            for (i <- 0 until node.level)
              out.append("  ");
            out.append("|-" + node.value);
          }
        }
        isPreviousFile = isFile(node.value)
        previousLevel = node.level
      }
      System.out.print(out.mkString)
      writer.write(out.mkString)
      writer.close()
    }

    private def isFile(name: String) = name.endsWith(".java") || name.endsWith(".xml") || name.endsWith(".sql")

    private def isSingle(node: Node): Boolean =
      if (node.parent != "non") list.filter(_.parent == node.parent.substring(0, node.parent.lastIndexOf("/"))) match {
        case ListBuffer(x) => if (x.children.length == 1) return !isFile(x.children.head.value) else false
      } else false
  }

}
