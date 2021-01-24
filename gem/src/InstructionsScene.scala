import Assets._
import Settings._
import indigo._
import indigo.scenes.{Lens, Scene, SceneEvent, SceneName}

object InstructionsScene extends Scene[ReferenceData, Model, ViewModel] {
  type SceneModel = Model
  type SceneViewModel = ViewModel
  def name: SceneName = SceneName ("InstructionsScene")
  def modelLens: Lens[Model, Model] = Lens.keepLatest[Model]
  def viewModelLens: Lens[ViewModel, ViewModel] = Lens.keepLatest[ViewModel]
  def eventFilters: EventFilters = EventFilters.AllowAll
  def subSystems: Set[SubSystem] = Set ()

  def updateModel (context: FrameContext[ReferenceData], model: Model): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyDown (Key.SPACE) =>
      Outcome (model).addGlobalEvents (SceneEvent.JumpTo (LevelScene.name))
    case KeyboardEvent.KeyDown (Key.ESCAPE) =>
      Outcome (model).addGlobalEvents (SceneEvent.JumpTo (SelectionScene.name))
    case _ => Outcome (model)
  }

  def updateViewModel (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): GlobalEvent => Outcome[ViewModel] =
    _ => Outcome (viewModel)

  def present (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): Outcome[SceneUpdateFragment] = {
    val tiles = (for (row <- 0 until 4; col <- 0 until 8) yield island1.moveTo (col * cellSize, row * cellSize)).toList
    Outcome (SceneUpdateFragment.empty
      .addGameLayerNodes (tiles)
      .addGameLayerNodes (Text ("Collect the gem and leave by the exit", 10, 10, 1, Font.fontKey))
      .addGameLayerNodes (Text ("without getting caught by monsters", 10, 30, 1, Font.fontKey))
      .addGameLayerNodes (Text ("The exit guard will chase you to the left", 10, 50, 1, Font.fontKey))
      .addGameLayerNodes (Text ("   if you get too close!", 10, 70, 1, Font.fontKey))
      .addGameLayerNodes (Text ("Use arrow keys to move", 10, 90, 1, Font.fontKey))
      .addGameLayerNodes (Text ("ESC to select a new character", 10, 110, 1, Font.fontKey))
      .addGameLayerNodes (Text ("Press space to play!", 10, 150, 1, Font.fontKey))
    )
  }
}
