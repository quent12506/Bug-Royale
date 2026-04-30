package ig;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Manal BENAISSA
 */

public class EcouteurClavier implements KeyListener {
    
    private boolean nord;
    private boolean sud;
    private boolean est;
    private boolean ouest;
    private boolean fermeConnection=false;
    
    //getter et setter
    public boolean isNord() {
        return nord;
    }

    public boolean isSud() {
        return sud;
    }

    public boolean isEst() {
        return est;
    }

    public boolean isOuest() {
        return ouest;
    }

    public boolean isFermeConnection() {
        return fermeConnection;
    }

    public void setFermeConnection(boolean fermeConnection) {
        this.fermeConnection = fermeConnection;
    }
    
    @Override//Ca c'est quand vous appuyez (rapidement) sur une touche. Même bail, vous pouvez récupérer des infos à travers l'event, comme :
    public void keyPressed(KeyEvent evt) { //detection d'une touche pressée, stockage de l'info dans les attributs de la classe
        
        if (evt.getKeyCode() == evt.VK_RIGHT) {
            this.est=true;
            }
        if (evt.getKeyCode() == evt.VK_K) {
            this.fermeConnection=true;
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
    public void keyTyped(KeyEvent event) { //Toutes les touches non définies sont notifiées dans le terminal lorsque pressée
        System.out.println("La touche " + event.getKeyCode() + " a été appuyée, le caractère correspondant est " + event.getKeyChar());
        System.out.flush();
    }

    @Override
    public void keyReleased(KeyEvent evt) { //detection d'une touche relachée, stockage de l'info dans les attributs de la classe
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
