package gie.yaro


import slogging._


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport



object app extends JSApp with LazyLogging {

  def main(): Unit = {

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")


  }

}

