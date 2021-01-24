import indigo._
import indigo.scenes.{Scene, SceneName}
import scala.scalajs.js.annotation.JSExportTopLevel
import Assets.sunlight
import Settings._

@JSExportTopLevel("IndigoGame")
object Gem extends IndigoGame[GameViewport, ReferenceData, Model, ViewModel] {
  def eventFilters: EventFilters = EventFilters.AllowAll

  def initialModel (startupData: ReferenceData): Outcome[Model] =
    Outcome (Model (seenInstructions = false, sunlight, playerStartX, playerStartY, gotGem = false,
      monster1StartY, monsterSpeed, monster2StartX, monsterSpeed, 0.0, 0.0))

  def boot(flags: Map[String, String]): Outcome[BootResult[GameViewport]] = {
    val assetPath: String = flags.getOrElse ("baseUrl", "")
    val config = GameConfig (
      viewport = GameViewport (Settings.viewportWidth, Settings.viewportHeight),
      frameRate = 60,
      clearColor = RGBA.White,
      magnification = Settings.magnificationLevel
    )

    Outcome (BootResult (config, config.viewport)
      .withAssets (Assets.assets (assetPath))
      .withFonts (Font.fontInfo))
  }

  def scenes (bootData: GameViewport): NonEmptyList[Scene[ReferenceData, Model, ViewModel]] =
    NonEmptyList (SelectionScene, InstructionsScene, LevelScene)

  def initialScene (bootData: GameViewport): Option[SceneName] =
    Some (SelectionScene.name)

  /** Load and decode the level specs on startup. */
  def setup (bootData: GameViewport,
             assetCollection: AssetCollection,
             dice: Dice): Outcome[Startup[ReferenceData]] = {
    Outcome (Startup.Success (ReferenceData ()))
  }

  def initialViewModel (startupData: ReferenceData, model: Model): Outcome[ViewModel] = {
    Outcome (ViewModel ())
  }

  def updateModel (context: FrameContext[ReferenceData], model: Model): GlobalEvent => Outcome[Model] =
    _ => Outcome (model)

  def updateViewModel (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): GlobalEvent => Outcome[ViewModel] =
    _ => Outcome (viewModel)

  def present (context: FrameContext[ReferenceData], model: Model, viewModel: ViewModel): Outcome[SceneUpdateFragment] =
    Outcome (SceneUpdateFragment.empty)
}
