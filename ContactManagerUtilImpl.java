import java.util.Calendar;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
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
    if (m1.getDate().equals(m2.getDate()) && m1.getContacts().equals(m2.getContacts()))
      return true;
    else return false;
  }

  public <T extends Meeting> List<T> dedupeMeetingList(List<T> list) { 
    if (list.size() == 0)
      throw new IllegalArgumentException("Can't dedupe an empty list.");
    if (list == null)
      throw new NullPointerException("Can't dedupe a null list.");
    Set<Meeting> remove = new HashSet<Meeting>();
    for (int i=0; i<list.size()-1; i++) { 
      for (int j=i+1; j<list.size(); j++) { 
        if (meetingsAreDuplicate(list.get(i), list.get(j)))
          if (list.get(i).getId() < list.get(j).getId())
            remove.add(list.get(i));
          else
            remove.add(list.get(j));
      }
    }
    Iterator<Meeting> i = remove.iterator();
    while (i.hasNext())
      list.remove(i.next());
    return list;
  }

  public <T extends Meeting> List<T> sortMeetingList(List<T> list) { 
    return null;
  }

}
