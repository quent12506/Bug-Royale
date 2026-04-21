package ig;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Manal BENAISSA
 */

//Ok ! Ici c'est pour écouter votre clavier ! Quand une touche du clavier est appuyé, un "event" est émis. Vous pouvez traiter l'event en question comme suit :
public class EcouteurClavier implements KeyListener {
    
    private boolean nord;
    private boolean sud;
    private boolean est;
    private boolean ouest;
    
    @Override
    //Ca c'est quand vous appuyez (rapidement) sur une touche. Même bail, vous pouvez récupérer des infos à travers l'event, comme :
    public void keyPressed(KeyEvent evt) {
        
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            this.est=true;
            }
        if (evt.getKeyCode() == evt.VK_LEFT) {
            this.ouest=true;
            }
        if (evt.getKeyCode() == evt.VK_UP) {
            this.nord=true;
            }
        if (evt.getKeyCode() == evt.VK_DOWN) {
            this.sud=true;
            }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        System.out.println("La touche " + event.getKeyCode() + " a été appuyée, le caractère correspondant est " + event.getKeyChar());
        System.out.flush();
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            this.est=false;
            }
        if (evt.getKeyCode() == evt.VK_LEFT) {
            this.ouest=false;
            }
        if (evt.getKeyCode() == evt.VK_UP) {
            this.nord=false;
            }
        if (evt.getKeyCode() == evt.VK_DOWN) {
            this.sud=false;
            }
    }
}
