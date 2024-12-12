import java.util.Scanner;

public class luckynumber2 
{
    
    public static void main(String[] args) 
    {
        Scanner s = new Scanner(System.in);
        int n;
        System.out.println("Enter a 4 digit number");
        n = s.nextInt();
        if(((n/1000)%10)+((n/100)%10)==((n/10)%10)+(n%10))
        {
            System.out.println("The number is a lucky number");
        }
        else
        {
            System.out.println("The number is not a lucky number");
        }
        s.close();
    }
}
