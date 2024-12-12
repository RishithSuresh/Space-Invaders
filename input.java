import java.util.Scanner;
public class input 
{
    public static void main(String[] args) 
    {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Please enter something: ");
    String userInput = scanner.nextLine();
    System.out.println("You entered: " + userInput);
    scanner.close();
  }
}

