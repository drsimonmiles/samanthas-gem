import indigo._
import indigo.scenes.{Lens, Scene, SceneEvent, SceneName}
import Assets._
import Settings._

object SelectionScene extends Scene[ReferenceData, Model, ViewModel] {
  type SceneModel = Model
  type SceneViewModel = ViewModel
  def name: SceneName = SceneName ("SelectionScene")
  def modelLens: Lens[Model, Model] = Lens.keepLatest[Model]
  def viewModelLens: Lens[ViewModel, ViewModel] = Lens.keepLatest[ViewModel]
  def eventFilters: EventFilters = EventFilters.AllowAll
  def subSystems: Set[SubSystem] = Set ()

  def updateModel (context: FrameContext[ReferenceData], model: Model): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyDown (Key.KEY_1) =>
      Outcome (model.copy (playerImage = selectionImages (0), seenInstructions = true))
        .addGlobalEvents (SceneEvent.JumpTo (nextScene (model)))
    case KeyboardEvent.KeyDown (Key.KEY_2) =>
      Outcome (model.copy (playerImage = selectionImages (1), seenInstructions = true))
        .addGlobalEvents (SceneEvent.JumpTo (nextScene (model)))
    case KeyboardEvent.KeyDown (Key.KEY_3) =>
      Outcome (model.copy (playerImage = selectionImages (2), seenInstructions = true))
        .addGlobalEvents (SceneEvent.JumpTo (nextScene (model)))
    case KeyboardEvent.KeyDown (Key.KEY_4) =>
      Outcome (model.copy (playerImage = selectionImages (3), seenInstructions = true))
        .addGlobalEvents (SceneEvent.JumpTo (nextScene (model)))
    case _ => Outcome (model)
  }

  def nextScene (model: Model): SceneName =
    if (model.seenInstructions) LevelScene.name else InstructionsScene.name

  def updateViewModel (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): GlobalEvent => Outcome[ViewModel] =
    _ => Outcome (viewModel)

  def present (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): Outcome[SceneUpdateFragment] = {
    val characters: List[Graphic] = (0 until 4).toList.map {
      index => selectionImages (index).moveTo (selectionImageX, selectionY (index)) }
    val names: List[Text] = (0 until 4).toList.map {
      index => Text (selectionTexts (index), selectionNameX, selectionY (index) + 10, 1, Font.fontKey) }
    val decor: List[Graphic] = (for (col <- 0 until 4; row <- 0 until 6) yield {
      magicStars.moveTo (col * 128 + 94, row * 64)
    }).toList
    Outcome (SceneUpdateFragment.empty
      .addGameLayerNodes (decor)
      .addGameLayerNodes (characters)
      .addGameLayerNodes (names)
      .addGameLayerNodes (Text (selectionTitleText, selectionTitleX, selectionTitleY, 1, Font.fontKey)))
  }
}
