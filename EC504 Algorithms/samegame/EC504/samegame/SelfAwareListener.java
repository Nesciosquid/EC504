package EC504.samegame;

/*
 * Listener for events of interest to SelfAwareJButtons
 */

interface SelfAwareListener {
	/*
	 * Fired by a circle when it's been "rolled over" by the mouse
	 */
	
	public void rollingOver(CircleRolloverEvent e);
}
