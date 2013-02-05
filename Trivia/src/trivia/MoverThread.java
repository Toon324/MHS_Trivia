/**
 * 
 */
package trivia;

/**
 * @author Cody
 *
 */
public class MoverThread implements Runnable {

	private Actor actor;
	private int ms;
	
	public MoverThread(Actor a, int ms) {
		actor = a;
		this.ms = ms;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		actor.move(ms);
	}

}
