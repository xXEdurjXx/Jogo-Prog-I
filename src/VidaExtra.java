import jplay.Sprite;
import jplay.Sound;

public class VidaExtra extends Sprite {
	
	private final Sound somVida;

	public VidaExtra(){
		super("images/1-Up.png",1);
		somVida = new Sound("sons/1up.wav");
		somVida.setRepeat(false);
	}
	public void playSomVida(){
		somVida.play();
	}
}
