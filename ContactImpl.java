import java.io.Serializable;

public class ContactImpl implements Contact, Serializable { 
  private static final int firstId = 100;
  private static int nextId = firstId;
  private final int id;
  private final String name;
  private String notes;

  public ContactImpl(String name, String notes) {  
    if ((name == null) || (notes == null)) { 
      throw new NullPointerException("Neither contact name nor notes can be null.");
    }
    this.notes = notes;
    this.name = name;
    this.id = nextId++; 
  }

  public int getId() { 
    return id;
  }

  public String getName() { 
    return name;
  }

  public String getNotes() { 
    return notes;
  }

  public void addNotes(String notes) { 
    this.notes = notes;
  }

}
