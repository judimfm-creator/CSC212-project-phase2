import java.io.File;
import java.util.Scanner;

public class customerManager {
    
    public static Scanner input = new Scanner (System.in);
    public static AVLTree<Integer, Customer> customersIDs = new AVLTree<Integer, Customer> ();
    public static AVLTree<String, Customer> customersNames = new AVLTree<String, Customer> ();
   
    public AVLTree<Integer, Customer> getcustomersIDs ( )
    {
        return customersIDs;
    }

    public AVLTree<String, Customer> getcustomersNames ( )
    {
        return customersNames;
    }
   
    public customerManager(String fileName)
    {
            try{
                File docsfile = new File(fileName);
                Scanner read = new Scanner (docsfile);
                String line = read.nextLine();
                
                while(read.hasNext())
                {
                    line = read.nextLine();
                    String [] data = line.split(","); 
                    Customer customer = new Customer(Integer.parseInt(data[0]),data[1], data[2] );
                    customersIDs.insert(customer.customerId, customer);
                    customersNames.insert(customer.name, customer);
                }
                read.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
    }
  
public void RegisterCustomer()
{
    Customer customer = new Customer ();

    System.out.println("Enter customer ID: ");
    customer.setCustomerId(input.nextInt());
    
    while (customersIDs.find(customer.getCustomerId()))
    {
        System.out.println("This ID already exists. Enter a different ID:");
        customer.setCustomerId(input.nextInt());
    }    
    
    System.out.println("Enter Customer Name: ");
    String name = input.nextLine();
    name = input.nextLine();
    customer.setName(name);
    
    System.out.println("Enter Customer Email: ");
    customer.setEmail(input.nextLine());

    customersIDs.insert(customer.customerId, customer);
    customersNames.insert(customer.name, customer);
}
   
public void OrderHistory()
{
        if (customersIDs.empty())
            System.out.println("No customer records found.");
        else
        {
            System.out.println("Enter customer ID: ");
            int customerID = input.nextInt();
            
            if (customersIDs.find(customerID))
            {
                if (customersIDs.retrieve().getOrders().empty())
                    System.out.println("There are no orders for customer ID: " + customersIDs.retrieve().getCustomerId());
                else
                {
                    System.out.println("Order History");
                    System.out.println(customersIDs.retrieve().getOrders());
                }
            }
            else
                System.out.println("Customer ID not found.");
        }
    }
    
    public Customer getCustomerID()
    {
        if (customersIDs.empty())
            System.out.println("No customer records available.");
        else
        {
            System.out.println("Enter customer ID: ");
            int customerID = input.nextInt();

            if (customersIDs.find(customerID))
            {
                System.out.println(customersIDs.retrieve());
                return customersIDs.retrieve();
            }
        }
        
        System.out.println("Customer ID not found.");
        return null;
    }
    
    public void printNamesAlphabetically()
    {
        customersNames.printKeys();
    }
}
