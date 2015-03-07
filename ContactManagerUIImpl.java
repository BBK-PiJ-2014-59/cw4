import java.util.Scanner;
import java.io.File;

public class ContactManagerUIImpl implements ContactManagerUI { 

  private static final String textfile = "contacts.txt";

  public void display(ContactManager cm) { 
    if (cm == null) throw new NullPointerException();
    String mainMenuPrompt = "Select an option: \n" +
                            "  1) Add contact\n" +
                            "  2) Exit\n ";
    int selection = 1;
    //input.nextLine();
    switch (selection) {
    case 1:
      // Add contact 
      String namePrompt = "Enter contact's name (required):"; 
      String name = promptString(namePrompt);
      String notesPrompt = "Enter notes about contact (optional):"; 
      break;
    case 2:
      // Exit
      break;
    default:
      System.out.println("Invalid selection.");
      break;
    }
  }


  public ContactManager launch(String textfile) { 
    if (textfile == null)
      throw new NullPointerException();
    ContactManager result;
    File f = new File(textfile);
    if(f.exists() && !f.isDirectory()) {
      result = new ContactManagerImpl(textfile);
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
    return 0;
  }

  public static void main(String[] args) { 
    ContactManagerUI cmui = new ContactManagerUIImpl(); 
    ContactManager cm = cmui.launch(textfile);
    while (true)
      cmui.display(cm);
  }
}
