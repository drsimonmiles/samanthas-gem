import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.6.0`, millindigo._

object gem extends ScalaJSModule with MillIndigo {
  def scalaVersion   = "2.13.4"
  def scalaJSVersion = "1.3.1"

  val gameAssetsDirectory: os.Path = os.pwd / "assets"
  val showCursor: Boolean          = true
  val title: String                = "Samantha's Gem"

  val magnification = 2
  val windowStartHeight = 384 * magnification
  val windowStartWidth = 576 * magnification

  def ivyDeps = Agg (
    ivy"io.indigoengine::indigo-json-circe::0.6.0",
    ivy"io.indigoengine::indigo::0.6.0"
  )

  def buildGame() = T.command {
    T {
      compile()
      fastOpt()
      indigoBuild()() // Note the double parenthesis!
    }
  }

  def runGame() = T.command {
    T {
      compile()
      fastOpt()
      indigoRun()() // Note the double parenthesis!
    }
  }

  def buildGameFull() = T.command {
    T {
      compile()
      fullOpt()
      indigoBuildFull()() // Note the double parenthesis!
    }
  }

  def runGameFull() = T.command {
    T {
      compile()
      fullOpt()
      indigoRunFull()() // Note the double parenthesis!
    }
  }
}