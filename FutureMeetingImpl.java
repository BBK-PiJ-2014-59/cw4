import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting { 

  public FutureMeetingImpl(Calendar date, Set<Contact> contacts) { 
    super(date, contacts);
  }

}
