import indigo._

object Assets {
  val imageFiles  = Set[String] ("baddies-heart", "exit", "fangtooth", "goodies-heart", "island1", "island2",
    "island3", "magic-stars" ,"magic-ruby", "monster-growly", "moonshimmer", "other-wall", "path", "socks", "sparklesun", "sweetlight",
    "sunlight", "sword", "treasure-chest", "wall", "boxy_font_small")
  val buttonFiles = Set[String] ()
  val textFiles   = Set[String] ()
  val jsonFiles   = Set[String] ()
  val audioFiles  = Set[String] ("caught", "got-gem", "won")

  def assets (baseUrl: String): Set[AssetType] =
    imageFiles.map   (file => AssetType.Image (AssetName(file), AssetPath(baseUrl + s"assets/$file.png"))) ++
      buttonFiles.map (file => AssetType.Image (AssetName(file), AssetPath(baseUrl + s"assets/$file.png"))) ++
      textFiles.map   (file => AssetType.Text  (AssetName(file), AssetPath(baseUrl + s"assets/$file.txt"))) ++
      audioFiles.map  (file => AssetType.Audio (AssetName(file), AssetPath(baseUrl + s"assets/$file.m4a"))) ++
      jsonFiles.map   (file => AssetType.Text  (AssetName(file), AssetPath(baseUrl + s"assets/$file.json")))

  val materials: Map[String, Material.Textured] =
    imageFiles.map (image => (image, Material.Textured (AssetName (image)))).toMap

  def graphic (asset: String, width: Int, height: Int): Graphic =
    Graphic (0, 0, width, height, 2, materials (asset))

  val island1: Graphic = graphic ("island1", 64, 64)
  val island2: Graphic = graphic ("island2", 64, 64)
  val island3: Graphic = graphic ("island3", 64, 64)
  val path: Graphic = graphic ("path", 64, 64)
  val wall: Graphic = graphic ("other-wall", 64, 64)
  val magicStars: Graphic = graphic ("magic-stars", 128, 64)

  val moonshimmer: Graphic = graphic ("moonshimmer", 64, 64)
  val sparklesun: Graphic = graphic ("sparklesun", 64, 64)
  val sunlight: Graphic = graphic ("sunlight", 64, 64)
  val sweetlight: Graphic = graphic ("sweetlight", 64, 64)
  val monster: Graphic = graphic ("socks", 64, 64)
  val queen: Graphic = graphic ("fangtooth", 64, 64)
  val guard: Graphic = graphic ("monster-growly", 64, 64)
  val gem: Graphic = graphic ("magic-ruby", 64, 64)
  val exit: Graphic = graphic ("exit", 64, 64)
}
