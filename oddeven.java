import java.util.Scanner;

public class oddeven 
{
    public static void main(String [] args) 
    {
        Scanner s = new Scanner(System.in);
        int a;
        System.out.println("Enter the number");
        a = s.nextInt();
        if (a%2==0)
        {
            System.out.println("Entered number is an even number");

        }
        else
        {
            System.out.println("Entered number is an odd number");

        }
        s.close();
    }
}
