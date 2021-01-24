import indigo._
import indigo.scenes.{Lens, Scene, SceneEvent, SceneName}
import Assets._
import Settings._

object LevelScene extends Scene[ReferenceData, Model, ViewModel] {
  type SceneModel = Model
  type SceneViewModel = ViewModel
  def name: SceneName = SceneName ("LevelScene")
  def modelLens: Lens[Model, Model] = Lens.keepLatest[Model]
  def viewModelLens: Lens[ViewModel, ViewModel] = Lens.keepLatest[ViewModel]
  def eventFilters: EventFilters = EventFilters.AllowAll
  def subSystems: Set[SubSystem] = Set ()

  def updateModel (context: FrameContext[ReferenceData], model: Model): GlobalEvent => Outcome[Model] = {
    case FrameTick => adjustGuardX (adjustMonster2X (adjustMonster1Y (Outcome (model), context.delta), context.delta), context.delta)
    case KeyboardEvent.KeyDown (Key.DOWN_ARROW) => move (model, 0.0, 1.0)
    case KeyboardEvent.KeyDown (Key.UP_ARROW) => move (model, 0.0, -1.0)
    case KeyboardEvent.KeyDown (Key.LEFT_ARROW) => move (model, -1.0, 0.0)
    case KeyboardEvent.KeyDown (Key.RIGHT_ARROW) => move (model, 1.0, 0.0)
    case KeyboardEvent.KeyDown (Key.ESCAPE) => Outcome (model).addGlobalEvents (SceneEvent.JumpTo (SelectionScene.name))
    case _ => Outcome (model)
  }

  def move (model: Model, dx: Double, dy: Double): Outcome[Model] =
    pickUpGem (leaveExit (moveIfAccessible (Outcome (model), dx, dy)))

  def moveIfAccessible (outcome: Outcome[Model], dx: Double, dy: Double): Outcome[Model] = {
    val newX = outcome.map (_.playerX + dx).getOrElse (gridWidth + 1.0)
    val newY = outcome.map (_.playerY + dy).getOrElse (gridHeight + 1.0)
    val guardXOffset = outcome.map (_.guardXOffset).getOrElse (0.0)
    if (accessible (newX, newY)) {
      if (touching (newX, newY, queenX, queenY) || touching (newX, newY, guardX + guardXOffset, guardY) )
        outcome.map (_.copy (playerX = 0.0, playerY = 0.0, gotGem = false))
          .addGlobalEvents (PlaySound(AssetName ("caught"), Volume.Max))
      else
        outcome.map (_.copy (playerX = newX, playerY = newY))
    } else
      outcome
  }

  def pickUpGem (outcome: Outcome[Model]): Outcome[Model] =
    if (outcome.map (model => !model.gotGem && touching (model.playerX, model.playerY, gemX, gemY)).getOrElse (false))
      outcome.map (_.copy (gotGem = true)).addGlobalEvents (PlaySound(AssetName ("got-gem"), Volume.Max))
    else outcome

  def leaveExit (outcome: Outcome[Model]): Outcome[Model] =
    if (outcome.map (model => model.gotGem && touching (model.playerX, model.playerY, exitX, exitY)).getOrElse (false))
      outcome.map (_.copy (playerX = 0.0, playerY = 0.0, gotGem = false)).addGlobalEvents (PlaySound(AssetName ("won"), Volume.Max))
    else
      outcome

  def adjustMonster1Y (outcome: Outcome[Model], delta: Seconds): Outcome[Model] = {
    var newY = outcome.map (model => model.monster1Y + (delta.value * model.monster1Velocity)).getOrElse (0.0)
    var newDirection = outcome.map (_.monster1Velocity).getOrElse (0.0)
    val playerX = outcome.map (_.playerX).getOrElse (gridWidth + 1.0)
    val playerY = outcome.map (_.playerY).getOrElse (gridHeight + 1.0)
    if (newY > monster1MaxY) {
      newY = monster1MaxY
      newDirection *= -1.0
    } else if (newY < monster1MinY) {
      newY = monster1MinY
      newDirection *= -1.0
    }
    if (touching (monster1X, newY, playerX, playerY))
      outcome.map (_.copy (monster1Y = newY, monster1Velocity = newDirection, playerX = 0.0, playerY = 0.0, gotGem = false))
        .addGlobalEvents (PlaySound(AssetName ("caught"), Volume.Max))
    else
      outcome.map (_.copy (monster1Y = newY, monster1Velocity = newDirection))
  }

  def adjustMonster2X (outcome: Outcome[Model], delta: Seconds): Outcome[Model] = {
    var newX = outcome.map (model => model.monster2X + (delta.value * model.monster2Velocity)).getOrElse (0.0)
    var newDirection = outcome.map (_.monster2Velocity).getOrElse (0.0)
    val playerX = outcome.map (_.playerX).getOrElse (gridWidth + 1.0)
    val playerY = outcome.map (_.playerY).getOrElse (gridHeight + 1.0)
    if (newX > monster2MaxX) {
      newX = monster2MaxX
      newDirection *= -1.0
    } else if (newX < monster2MinX) {
      newX = monster2MinX
      newDirection *= -1.0
    }
    if (touching (newX, monster2Y, playerX, playerY))
      outcome.map (_.copy (monster2X = newX, monster2Velocity = newDirection, playerX = 0.0, playerY = 0.0, gotGem = false))
        .addGlobalEvents (PlaySound(AssetName ("caught"), Volume.Max))
    else
      outcome.map (_.copy (monster2X = newX, monster2Velocity = newDirection))
  }

  def adjustGuardX (outcome: Outcome[Model], delta: Seconds): Outcome[Model] = {
    val newGuardVelocity = outcome.map { model =>
      if (inTrap (model.playerX, model.playerY) && model.guardXOffset >= -1.0) -guardSpeed
      else if (model.guardXOffset < 0.0) guardSpeed
      else 0.0
    }.getOrElse (0.0)
    val newOffsetX = outcome.map (model => model.guardXOffset + (delta.value * model.guardVelocity)).getOrElse (0.0)
    val playerX = outcome.map (_.playerX).getOrElse (gridWidth + 1.0)
    val playerY = outcome.map (_.playerY).getOrElse (gridHeight + 1.0)
    if (touching (guardX + newOffsetX, guardY, playerX, playerY))
      outcome.map (_.copy (guardXOffset = newOffsetX, guardVelocity = newGuardVelocity, playerX = 0.0, playerY = 0.0, gotGem = false))
        .addGlobalEvents (PlaySound(AssetName ("caught"), Volume.Max))
    else
      outcome.map (_.copy (guardXOffset = newOffsetX, guardVelocity = newGuardVelocity))
  }

  def updateViewModel (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): GlobalEvent => Outcome[ViewModel] =
    _ => Outcome (viewModel)

  def place (graphic: Graphic, x: Double, y: Double): Graphic =
    graphic.moveTo ((x * cellSize).toInt, (y * cellSize).toInt)

  def present (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): Outcome[SceneUpdateFragment] =
  Outcome {
    val paths =
      (for (row <- 0 until gridHeight; column <- 0 until gridWidth) yield
        floor (row, column).moveTo (column * cellSize, row * cellSize)).toList
    val items: List[Graphic] = List (
      place (model.playerImage, model.playerX, model.playerY),
      place (monster, monster1X, model.monster1Y),
      place (monster, model.monster2X, monster2Y),
      place (queen, queenX, queenY),
      place (guard, guardX + model.guardXOffset, guardY),
      place (exit, exitX, exitY)
    ) ++ (if (model.gotGem) Nil else List (place (gem, gemX, gemY)))
    SceneUpdateFragment.empty
      .addGameLayerNodes (paths)
      .addGameLayerNodes (items)
  }
}
