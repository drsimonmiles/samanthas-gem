import indigo.Graphic
import Assets._

object Settings {
  val cellSize = 64
  val gridWidth = 9
  val gridHeight = 6

  val touchingRadius = 0.16

  def floor (row: Int, column: Int): Graphic =
    if (row == 2 && (column == 2 || column == 6)) Assets.island1
    else if (row == 2 && (column == 3 || column == 5)) Assets.island2
    else if (row == 2 && column == 4) Assets.island3
    else if ((column == 1 || column == 7) && (row == 1 || row == 2)) Assets.wall
    else if (row == 4 && column == 4) Assets.wall
    else Assets.path

  def accessible (x: Double, y: Double): Boolean =
    x >= 0.0 && x <= (gridWidth - 1) && y >= 0.0 && y <= (gridHeight - 1)

  def touching (x1: Double, y1: Double, x2: Double, y2: Double): Boolean =
    Math.sqrt ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) <= touchingRadius

  def inTrap (x: Double, y: Double): Boolean =
    touching (x, y, guardX - 1.0, guardY)

  def place (graphic: Graphic, x: Double, y: Double): Graphic =
    graphic.moveTo ((x * cellSize).toInt, (y * cellSize).toInt)

  val playerSpeed = 1.0
  val playerStartX = 0.0
  val playerStartY = 0.0
  val monsterSpeed = 1.0
  val monster1X = 2.0
  val monster1StartY = 1.0
  val monster1MinY = 0.0
  val monster1MaxY = 5.0
  val monster2StartX = 2.0
  val monster2MinX = 2.0
  val monster2MaxX = 6.0
  val monster2Y = 1.0
  val queenX = 4.0
  val queenY = 3.0
  val guardX = 7.0
  val guardY = 5.0
  val guardSpeed = 3.0
  val gemX = 4.0
  val gemY = 1.0
  val exitX = 8.0
  val exitY = 5.0

  val titleGemX = 7.0
  val titleGemSpeed = 2.0
  val titleGemMinY = 0.0
  val titleGemMaxY = 5.0

  val selectionTitleX = 10
  val selectionTitleY = 10
  val selectionImageX = 20
  val selectionNameX = 20 + 64 + 20
  val selectionY = List (10 + 20, 10 + 20 + 64 + 20, 10 + 20 + 64 + 20 + 64 + 20, 10 + 20 + 64 + 20 + 64 + 20 + 64 + 20)
  val selectionTitleText = "Choose character by typing number"
  val selectionTexts = List ("1. Moonshimmer", "2. Sparklesun", "3. Sunlight", "4. Sweetlight")
  val selectionImages = List (moonshimmer, sparklesun, sunlight, sweetlight)
  val borderX = 200

  val magnificationLevel = 2
  val viewportWidth = cellSize * gridWidth * magnificationLevel
  val viewportHeight = cellSize * gridHeight * magnificationLevel

}
