package hr.chus.cchat.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.type.TimestampType;

/**
 * 
 * @author Jan Čustović
 *
 */
public class DateTimeType extends TimestampType {

	private static final long serialVersionUID = 1L;

	
	public Date get(ResultSet rs, String name) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(name, Calendar.getInstance(TimeZone.getDefault()));
        if (timestamp == null) return null;
        return new Date(timestamp.getTime()+timestamp.getNanos()/1000000);
   }
	
	@Override
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		Timestamp ts;
		if (value instanceof Timestamp) {
			ts = (Timestamp) value;
		} else {
			Date date = (Date) value;
			ts = new Timestamp((date).getTime());
		}
		st.setTimestamp(index, ts);
	}
    
}
