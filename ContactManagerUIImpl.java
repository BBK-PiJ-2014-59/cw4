import java.util.Scanner;
import java.io.File;

public class ContactManagerUIImpl implements ContactManagerUI { 

  private static final String textfile = "contacts.txt";

  public void display(ContactManager cm) { 
    if (cm == null) throw new NullPointerException();
    Scanner input = new Scanner(System.in);
    System.out.println(
        "\n" +
        "Select an option: \n" +
        "  1) Add contact\n" +
        "  2) Exit\n "
    );
     
    int selection = input.nextInt();
    input.nextLine();
    switch (selection) {
    case 1:
      // Add contact 
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
    if (textfile == null) throw new NullPointerException();
    ContactManager result;
    File f = new File(textfile);
    if(f.exists() && !f.isDirectory()) {
      result = new ContactManagerImpl(textfile);
    } else {
      result = new ContactManagerImpl();
    }
    return result;
  }

  public String prompt(String prompt) { 
    return null;
  }

  public static void main(String[] args) { 
    ContactManagerUI cmui = new ContactManagerUIImpl(); 
    ContactManager cm = cmui.launch(textfile);
    while (true)
      cmui.display(cm);
  }
}
