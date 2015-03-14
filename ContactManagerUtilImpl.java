import java.util.Calendar;

public class ContactManagerUtilImpl implements ContactManagerUtil { 

  public boolean isFuture(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.after(now);
  }

  public boolean isPast(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.before(now);
  }
}