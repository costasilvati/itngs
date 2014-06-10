
import itngs_mvc.control.MainController;
import itngs_mvc.view.MainFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juliana
 */
public class ITNGS_MVC {
    public static void main(String[] args) {
        MainFrame framePrincipal = new MainFrame();
        MainController mc = new MainController(framePrincipal);
        framePrincipal.setVisible(true);
    }
    
}
