package thesnake;
import javax.swing.JFrame;

public class aStarFrame extends JFrame{
    aStarFrame(){
        this.add(new aStarPanel());
	this.setTitle("Snake");
	this.setResizable(false);
	this.pack();
	this.setVisible(true);
	this.setLocationRelativeTo(null);
    }
}
