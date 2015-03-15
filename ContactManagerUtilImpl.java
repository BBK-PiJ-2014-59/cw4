import java.util.Calendar;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class ContactManagerUtilImpl implements ContactManagerUtil, Serializable { 

  public boolean isFuture(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.after(now);
  }

  public boolean isPast(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.before(now);
  }

  public boolean areSameDay(Calendar date1, Calendar date2) { 
    int year1 = date1.get(Calendar.YEAR);
    int month1 = date1.get(Calendar.MONTH);
    int day1 = date1.get(Calendar.DAY_OF_MONTH);

    //System.out.println("year1: " + year1 + " month1: " + month1 + " day1: " + day1); 

    int year2 = date2.get(Calendar.YEAR);
    int month2 = date2.get(Calendar.MONTH);
    int day2 = date2.get(Calendar.DAY_OF_MONTH);

    //System.out.println("year2: " + year2 + " month2: " + month2 + " day2: " + day2); 

    
    return (Arrays.deepEquals(new Integer[]{year1,month1,day1}, (new Integer[]{year2,month2,day2})));
  }

  public boolean meetingsAreDuplicate(Meeting m1, Meeting m2) { 
    return true;
  }

  public List<Meeting> dedupeMeetingList(List<Meeting> list) { 
    return null;
  }

}
