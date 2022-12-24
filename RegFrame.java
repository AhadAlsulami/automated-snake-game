package thesnake;
import javax.swing.JFrame;

public class RegFrame extends JFrame{
    RegFrame(){
        this.add(new RegPanel());
	this.setTitle("Snake");
	this.setResizable(false);
	this.pack();
	this.setVisible(true);
	this.setLocationRelativeTo(null);
    }
}
