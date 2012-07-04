import org.scalatest.FunSuite
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
  val resXml = <elem><val>root</val><children><elem><val>dev</val><children><elem><val>src</val><children><elem><val>test</val><children><elem><val>resources</val><children><elem><val>res1test.txt</val></elem></children></elem><elem><val>java</val><children><elem><val>Java2Test.java</val></elem><elem><val>Java1Test.java</val></elem></children></elem><elem><val>scala</val><children><elem><val>package1</val><children><elem><val>Scala3Test.scala</val></elem></children></elem><elem><val>Scala2Test.scala</val></elem><elem><val>Scala1Test.scala</val></elem></children></elem></children></elem><elem><val>main</val><children><elem><val>resources</val><children><elem><val>res1.txt</val></elem></children></elem><elem><val>scala</val><children><elem><val>package1</val><children><elem><val>Scala3.scala</val></elem></children></elem><elem><val>Scala2.scala</val></elem><elem><val>Scala1.scala</val></elem></children></elem><elem><val>java</val><children><elem><val>Java2.java</val></elem><elem><val>Java1.java</val></elem></children></elem></children></elem></children></elem></children></elem></children></elem>
  val resStr = "|-root\n  |-dev\n    |-src\n      |-test\n        |-resources\n          |-res1test.txt\n        |-java\n          |-Java2Test.java\n          |-Java1Test.java\n        |-scala\n          |-package1\n            |-Scala3Test.scala\n          |-Scala2Test.scala\n          |-Scala1Test.scala\n      |-main\n        |-resources\n          |-res1.txt\n        |-scala\n          |-package1\n            |-Scala3.scala\n          |-Scala2.scala\n          |-Scala1.scala\n        |-java\n          |-Java2.java\n          |-Java1.java\n"
  
  import Graph._
  
  val graph = build(files split "\n" map(_.trim()) toList, "/")
  
  test("should format as correct tree") {
    assert(graph.toString() == resLists)
  }

  test("should form correct XML") {
    assert(graph.toXML.get == resXml)
  }
  
  test("should format as tree in string") {
    assert(resStr === mkString(graph))
  }
  
  test("show final result as str") {
    println(mkString(graph))
  }
  
}