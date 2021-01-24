import indigo._

case class Model (seenInstructions: Boolean,
  playerImage: Graphic, playerX: Double, playerY: Double, gotGem: Boolean,
  monster1Y: Double, monster1Velocity: Double,
  monster2X: Double, monster2Velocity: Double,
  guardXOffset: Double, guardVelocity: Double)
