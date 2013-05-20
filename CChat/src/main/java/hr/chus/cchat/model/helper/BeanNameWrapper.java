package hr.chus.cchat.model.helper;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class BeanNameWrapper {

    private String name;

    public BeanNameWrapper(final String p_name) {
        super();
        name = p_name;
    }
    
    // Getters & setters

    public final String getName() {
        return name;
    }

    public final void setName(final String p_name) {
        name = p_name;
    }

}
