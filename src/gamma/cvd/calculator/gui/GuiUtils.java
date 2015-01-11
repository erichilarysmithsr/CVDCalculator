package gamma.cvd.calculator.gui;

import gamma.cvd.calculator.launcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Jack
 */
public class GuiUtils
{
    
    // Changes Swing style to native Operating System Style
    static void setNativeGuiStyle(JFrame screen)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Centers screen in center of display
    static void centerScreen(JFrame screen)
    {
        screen.setLocationRelativeTo(null);
    }
}
