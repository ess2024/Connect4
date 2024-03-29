import javafx.scene.image.Image
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}

import java.io.IOException

object Main extends JFXApp3 {
  /**
   * Starts the App.
   */
  override def start(): Unit = {
    System.setProperty("java.awt.headless", "false")
    val fxml = getClass.getResource("resources/mainView.fxml")
    if (fxml == null) {
      throw new IOException("Cannot load resource: AdoptionForm.fxml")
    }
    val root = FXMLView(fxml, NoDependencyResolver)

    stage = new JFXApp3.PrimaryStage {
      title.value = "Game Engine"
      width = 1080
      height = 800
      scene = new Scene(root)
    }
    stage.getIcons.add(new Image("resources/icon.png"))
  }
}