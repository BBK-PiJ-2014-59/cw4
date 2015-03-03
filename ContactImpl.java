public class ContactImpl implements Contact { 
  private final int id;
  static int nextId = 100;
  private final String name;
  private String note;

  public ContactImpl(String name) { 
    this.name = name;
    id = nextId++; 
  }

  public int getId() { 
    return id;
  }

  public String getName() { 
    return name;
  }

  public String getNotes() { 
    return note;
  }

  public void addNotes(String note) { 
    this.note = note;
  }

}
