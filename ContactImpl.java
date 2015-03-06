public class ContactImpl implements Contact { 
  private final int id; // uniqueness to be managed by ContactManager
  //static int nextId = 100;
  private final String name;
  private String notes;

  public ContactImpl(String name, String notes, int id) {  
    if ((name == null) || (notes == null)) { 
      throw new NullPointerException("Neither contact name nor notes can be null.");
    }
    this.notes = notes;
    this.name = name;
    //id = nextId++; 
    this.id = id; 
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
