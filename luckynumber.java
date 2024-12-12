import java.util.Scanner;
public class luckynumber 
{
    public static void main(String[] args) 
    {
        Scanner s = new Scanner(System.in);
        int n,s1=0,s2=0,x,y;
        System.out.println("Enter a 4 digit number");
        n = s.nextInt();
        int a=n/100;
        int b=n%100;
        if(n>9999)
        {
            System.out.println("Entered number is greater than a four digit number");
        }
        else
        {
            while (a!=0 && b!=0) 
            {
                x=a%10;
                s1=s1+x;
                a=a/10;
                y=b%10;
                s2=s2+y;
                b=b/10;
            }
            if (s1==s2)
            {
                System.out.println("The number is lucky");
            }
            else
            {
                System.out.println("The number is not lucky");
            }
        }
    s.close();
    }
}
