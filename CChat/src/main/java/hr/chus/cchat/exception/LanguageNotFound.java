package hr.chus.cchat.exception;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class LanguageNotFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LanguageNotFound(final String p_message) {
        super(p_message);
    }

}
