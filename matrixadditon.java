import java.util.Scanner;
public class matrixadditon 
{
    public static void main(String[] args) 
    {
        Scanner s=new Scanner(System.in);
        int a[][],b[][],c[][];
        System.out.println("Enter the array size");
        int n=s.nextInt();
        a = new int[n][n];
        b = new int[n][n];
        c = new int[n][n];
        System.out.println("Enter the elements of first matrix");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                a[i][j]=s.nextInt();
            }
        }
        System.out.println("Enter the elements of second matrix");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                b[i][j]=s.nextInt();
            }
        }
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                c[i][j]=a[i][j]+b[i][j];
            }
        }
        System.out.println("The addition of both matrix is :");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                System.out.print(c[i][j]+" ");
            }
            System.out.println();
        }
        s.close();
    }
}
