import controller.ConvertController;
import view.MainFrame;

/**
 * Created by Jakob on 03-Oct-16.
 */
public class Program {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        ConvertController convertController = new ConvertController(mainFrame.getConvertButton(), mainFrame.getGeoJSONTextArea(), mainFrame.getResultTextArea());
    }
}
