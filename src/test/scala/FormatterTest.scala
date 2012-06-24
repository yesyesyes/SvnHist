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
  
  test("should format as tree") {
  }
  
  
}