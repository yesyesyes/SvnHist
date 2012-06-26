import org.scalatest.FunSuite
import java.util.ArrayList
import scalaz._
import Scalaz._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FormatterTest extends FunSuite {

  val files = """root/dev/src/main/scala/Scala1.scala
root/dev/src/main/scala/Scala2.scala
root/dev/src/main/java/Java1.java
root/dev/src/main/java/Java2.java
root/dev/src/main/scala/package1/Scala3.scala
root/dev/src/test/scala/Scala1Test.scala
root/dev/src/test/scala/Scala2Test.scala
root/dev/src/test/scala/package1/Scala3Test.scala
root/dev/src/test/java/Java1Test.java
root/dev/src/test/java/Java2Test.java
root/dev/src/main/resources/res1.txt
root/dev/src/test/resources/res1test.txt"""

  val resLists = """Node(root,List(Node(dev,List(Node(src,List(Node(test,List(Node(resources,List(Node(res1test.txt,List(E)))), Node(java,List(Node(Java2Test.java,List(E)), Node(Java1Test.java,List(E)))), Node(scala,List(Node(package1,List(Node(Scala3Test.scala,List(E)))), Node(Scala2Test.scala,List(E)), Node(Scala1Test.scala,List(E)))))), Node(main,List(Node(resources,List(Node(res1.txt,List(E)))), Node(scala,List(Node(package1,List(Node(Scala3.scala,List(E)))), Node(Scala2.scala,List(E)), Node(Scala1.scala,List(E)))), Node(java,List(Node(Java2.java,List(E)), Node(Java1.java,List(E))))))))))))"""
  val resXml = <elem><val>root</val><children><elem><val>dev</val><children><elem><val>src</val><children><elem><val>test</val><children><elem><val>resources</val><children><elem><val>res1test.txt</val><children><end></end></children></elem></children></elem><elem><val>java</val><children><elem><val>Java2Test.java</val><children><end></end></children></elem><elem><val>Java1Test.java</val><children><end></end></children></elem></children></elem><elem><val>scala</val><children><elem><val>package1</val><children><elem><val>Scala3Test.scala</val><children><end></end></children></elem></children></elem><elem><val>Scala2Test.scala</val><children><end></end></children></elem><elem><val>Scala1Test.scala</val><children><end></end></children></elem></children></elem></children></elem><elem><val>main</val><children><elem><val>resources</val><children><elem><val>res1.txt</val><children><end></end></children></elem></children></elem><elem><val>scala</val><children><elem><val>package1</val><children><elem><val>Scala3.scala</val><children><end></end></children></elem></children></elem><elem><val>Scala2.scala</val><children><end></end></children></elem><elem><val>Scala1.scala</val><children><end></end></children></elem></children></elem><elem><val>java</val><children><elem><val>Java2.java</val><children><end></end></children></elem><elem><val>Java1.java</val><children><end></end></children></elem></children></elem></children></elem></children></elem></children></elem></children></elem>
  val res = """|-root/dev/src
      |-test
        |-resources/res1test.txt
        |-java
          |-Java2Test.java
          |-Java1Test.java
        |-scala
          |-package1/Scala3Test.scala
          |-Scala2Test.scala
          |-Scala1Test.scala
      |-main
        |-resources/res1.txt
        |-java
          |-Java2.java
          |-Java1.java
        |-scala
          |-package1/Scala3.scala
          |-Scala2.scala
          |-Scala1.scala"""
  import Graph._
  
  val graph = build(files split "\n" map(_.trim()) toList)
  
  test("should format as correct tree") {
    assert(graph.toString() == resLists)
  }

  test("shoud form correct XML") {
    assert(graph.toXML == resXml)
  }
  
  test("pretty printer") {
    println(XmlToString(graph toXML))
  }

}