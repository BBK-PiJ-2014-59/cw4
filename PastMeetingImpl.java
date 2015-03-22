import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting { 

  public PastMeetingImpl(Calendar date, Set<Contact> contacts, String notes) {
    super(date, contacts, notes);
  }

  // Copy constructor, for converting to PastMeeting:
  public PastMeetingImpl(Meeting m, String notes) { 
    super(m, notes);
  }

  public String getNotes() { 
    return notes;
  }
}
