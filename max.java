import java.util.Scanner;

public class max
{
    public static void main(String [] args) 
    {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the first number");
        int a = s.nextInt();
        System.out.println("Enter the second number");
        int b = s.nextInt();
        
        if (a>b)
        {
            System.out.println("The first number is greater");
        }
        else
        {
            System.out.println("The second number is greater");
        }
        s.close();
    }
}
