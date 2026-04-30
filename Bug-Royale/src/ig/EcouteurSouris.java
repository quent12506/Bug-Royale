package ig;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author manal BENAISSA
 */
public class EcouteurSouris implements MouseListener {
    
    private double X;
    private double Y;
    private boolean click;
    

    @Override
    //MouseClicked est la méthode que vous devez compléter si vous avez une action à faire à chaque détection de clic !
    public void mouseClicked(MouseEvent event){
        //"event" vous donne des infos sur là où vous avez cliqué ! Ici, je peux choper les coordonnées X,Y (en pixel...) de là où j'ai cliqué ! :)
        
        X = event.getX();
        Y = event.getY();
        click=true;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public double getX() {
        return X;
    }

    public void setX(double X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    @Override
    //Le clic peut être vu comme la décomposition de deux mouvements : vous pressez, puis pvous relachez. Si vous avez besoin de faire cette distinction, complétez ici !
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }
    
    @Override
    //Vous occupez pas de ces deux là, vous en avez pas besoin ;)
    public void mouseEntered(MouseEvent event) {
        //NOTHING TODO HERE
    }
    
    @Override
    public void mouseExited(MouseEvent event) {
        //NOTHING TODO HERE
    }
}
