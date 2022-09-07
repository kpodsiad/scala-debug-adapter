package ch.epfl.scala.debugadapter.internal.stepfilter

import java.lang.reflect.Method
import ch.epfl.scala.debugadapter.Logger
import com.sun.jdi
import scala.util.Try
import ch.epfl.scala.debugadapter.DebuggeeRunner
import java.util.function.Consumer
import java.nio.file.Path
import java.lang.reflect.InvocationTargetException
import ch.epfl.scala.debugadapter.Java8
import ch.epfl.scala.debugadapter.Java9OrAbove

class Scala3StepFilter(
    bridge: Any,
    skipMethod: Method,
    logger: Logger,
    testMode: Boolean
) extends ScalaStepFilter {
  override protected def skipScalaMethod(method: jdi.Method): Boolean =
    try {
      skipMethod.invoke(bridge, method).asInstanceOf[Boolean]
    } catch {
      case e: InvocationTargetException => throw e.getCause
    }

}

object Scala3StepFilter {
  def tryLoad(
      runner: DebuggeeRunner,
      logger: Logger,
      testMode: Boolean
  ): Option[Scala3StepFilter] = {
    for {
      classLoader <- runner.stepFilterClassLoader
      stepFilterTry = Try {
        val className =
          "ch.epfl.scala.debugadapter.internal.stepfilter.ScalaStepFilterBridge"
        val cls = classLoader.loadClass(className)
        val ctr = cls.getConstructor(
          classOf[Array[Path]],
          classOf[Consumer[String]],
          classOf[Boolean]
        )
        // TASTy Query needs the javaRuntimeJars
        val javaRuntimeJars = runner.javaRuntime.toSeq.flatMap {
          case Java8(javaHome, classJars, sourceZip) => classJars
          case Java9OrAbove(javaHome, fsJar, sourceZip) => Seq.empty
        }
        val debuggeeClasspath = runner.classPath.toArray ++ javaRuntimeJars
        val warnLogger: Consumer[String] = msg => logger.warn(msg)
        val bridge = ctr.newInstance(
          debuggeeClasspath,
          warnLogger,
          testMode: java.lang.Boolean
        )
        val skipMethod = cls.getMethods.find(m => m.getName == "skipMethod").get
        new Scala3StepFilter(bridge, skipMethod, logger, testMode)
      }
      stepFilter <- stepFilterTry.toOption
    } yield stepFilter
  }
}
