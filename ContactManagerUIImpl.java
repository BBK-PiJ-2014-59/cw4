import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class ContactManagerUIImpl implements ContactManagerUI { 

  private String filename = "contacts.txt";

  public ContactManagerUIImpl() {}

  public ContactManagerUIImpl(String testfile) { 
    filename = testfile;
    System.out.println("filename: " + filename);
  }

  public void display(ContactManager cm) { 
    if (cm == null) throw new NullPointerException();
    String mainMenuPrompt = "Select an option: \n" +
                            "  1) Add contact\n" +
                            "  2) Exit";
    int selection = promptInt(mainMenuPrompt);
    switch (selection) {
    case 1:
      // Add contact 
      String namePrompt = "Enter contact's name (required):"; 
      String name = promptString(namePrompt);
      String notesPrompt = "Enter notes about contact (optional):"; 
      String notes = promptString(notesPrompt);
      cm.addNewContact(name, notes);
      break;
    case 2:
      // Exit
      System.out.println("Saving and exiting Contact Manager.");
      cm.flush();
      System.exit(0);
    default:
      System.out.println("\n!!! Invalid selection !!!");
      break;
    }
  }

  public ContactManager launch() { 
    System.out.println("LAUNCH");
    if (filename == null)
      throw new NullPointerException();
    ContactManager result = null;
    FileInputStream fis = null;
    ObjectInputStream in = null;
    if (new File(filename).exists()) {
      try { 
        fis = new FileInputStream(filename);
        in = new ObjectInputStream(fis);
        result = (ContactManager) in.readObject();
        in.close();
      } catch (Exception ex) { 
        ex.printStackTrace();
      }
    } else {
      result = new ContactManagerImpl();
    }
    return result;
  }


  public String promptString(String prompt) { 
    if (prompt == null)
      throw new NullPointerException();
    System.out.print( "\n" +
                      prompt + " ");
    Scanner input = new Scanner(System.in);
    String result = input.nextLine();
    return result.replaceAll("\\s+", " ").trim();
  }

  public int promptInt(String prompt) { 
    if (prompt == null)
      throw new NullPointerException();
    System.out.print( "\n" +
                      prompt + 
                      "\n> ");
    Scanner input = new Scanner(System.in);
    int result;
    try { 
      result = input.nextInt();
    } catch (Exception e) { 
      result = -1; 
    }
    return result;
  }

  public static void main(String[] args) { 
    ContactManagerUI cmui = new ContactManagerUIImpl(); 
    ContactManager cm = cmui.launch();
    while (true)
      cmui.display(cm);
  }
}
