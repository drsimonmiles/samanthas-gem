import Assets._
import Settings._
import indigo._
import indigo.scenes.{Lens, Scene, SceneEvent, SceneName}

object TitleScene extends Scene[ReferenceData, Model, ViewModel] {
  type SceneModel = Model
  type SceneViewModel = ViewModel
  def name: SceneName = SceneName ("TitleScene")
  def modelLens: Lens[Model, Model] = Lens.keepLatest[Model]
  def viewModelLens: Lens[ViewModel, ViewModel] = Lens.keepLatest[ViewModel]
  def eventFilters: EventFilters = EventFilters.AllowAll
  def subSystems: Set[SubSystem] = Set ()

  def updateModel (context: FrameContext[ReferenceData], model: Model): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyDown (Key.SPACE) =>
      Outcome (model).addGlobalEvents (SceneEvent.JumpTo (SelectionScene.name))
    case _ => Outcome (model)
  }

  def updateViewModel (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): GlobalEvent => Outcome[ViewModel] = {
    case FrameTick => Outcome (adjustGemY (viewModel, context.delta))
    case _ => Outcome (viewModel)
  }

  def present (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): Outcome[SceneUpdateFragment] = {
    val tiles = (for (row <- 0 until 8; col <- 0 until 8) yield purple.moveTo (col * cellSize, row * cellSize)).toList
    Outcome (SceneUpdateFragment.empty
      .addGameLayerNodes (tiles)
      .addGameLayerNodes (Text ("S  A  M  A  N  T  H  A  S    G  E  M", 10, 10, 1, Font.fontKey).scaleBy (3, 3))
      .addGameLayerNodes (Text ("Press space to play!", 10, 80, 1, Font.fontKey))
      .addGameLayerNodes (Text ("Design, graphics and sound by Samantha", 10, 100, 1, Font.fontKey))
      .addGameLayerNodes (Text ("Programming by Simon", 10, 120, 1, Font.fontKey))
      .addGameLayerNodes (place (gem, titleGemX, viewModel.titleGemY))
    )
  }

  def adjustGemY (model: ViewModel, delta: Seconds): ViewModel = {
    var newY = model.titleGemY + (delta.value * model.titleGemVelocity)
    var newDirection = model.titleGemVelocity
    if (newY > titleGemMaxY) {
      newY = titleGemMaxY
      newDirection *= -1.0
    } else if (newY < titleGemMinY) {
      newY = titleGemMinY
      newDirection *= -1.0
    }
    model.copy (titleGemY = newY, titleGemVelocity = newDirection)
  }

}
