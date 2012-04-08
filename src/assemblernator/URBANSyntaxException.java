package assemblernator;

/**
 * Class used for reporting errors via Java's exception mechanism.
 * 
 * @author Josh Ventura
 * @date Apr 8, 2012; 11:16:57 AM
 */
public class URBANSyntaxException extends Throwable {
	/** Make Eclipse's compiler shut up */
	private static final long serialVersionUID = 1L;
	/** The index on the line at which this error occurred */
	public int index;

	/**
	 * @param message
	 *            The error to report
	 * @param idx
	 *            The index from which the error is being reported.
	 */
	public URBANSyntaxException(String message, int idx) {
		super(message);
		index = idx;
	}
}