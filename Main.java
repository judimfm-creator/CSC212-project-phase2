/**
 *
 * @author Manal Alhihi
 */

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Main {
    
    //=============================================================================
    // global variables
    //=============================================================================
    public static Scanner input = new Scanner (System.in);

    public static productsManager pdata  = new productsManager("prodcuts.csv");
    public static AVLTree<Integer, Product>  products = pdata.getproductsIDs();

    public static customersManager cdata = new customersManager("customers.csv");
    public  static AVLTree<Integer, Customer> customers = cdata.getcustomersIDs();

    public static ordersManager odata = new ordersManager("orders.csv" , cdata.getcustomersIDs());
    public static AVLTree<Integer, Order>  orders = odata.getordersData();

    public static reviewsManager rdata= new reviewsManager("reviews.csv", pdata.getproductsIDs());
    public static LinkedList <Review>  reviews = rdata.getreviewsData();
 
    //=============================================================================
    // Products Menu
    //=============================================================================
    public static void productsMenu()
    {
        int choice;

        System.out.println("\n==============================");
        System.out.println("           Products Menu       ");
        System.out.println("==============================");
        System.out.println("1. Add new product");
        System.out.println("2. Remove product");
        System.out.println("3. Update product (name, price, stock)");
        System.out.println("4. Search By ID");
        System.out.println("5. Search products by name");
        System.out.println("6. List All Products Within a Price Range [minPrice, maxPrice]");
        System.out.println("7. Track all Out-stock products");
        System.out.println("8. Display All Customers Who Reviewed specific product sorted by RATING");
        System.out.println("9. Display All Customers Who Reviewed specific product sorted by CUSTOMER ID");
        System.out.println("10. Print all Products");
        System.out.println("11. Return to Main Menu");
        System.out.print("Enter your choice: ");
        choice = input.nextInt();

        switch (choice)
        {
            case 1:
                pdata.addProduct();
                break;

            case 2:
                pdata.removeProduct();
                break;

            case 3:
                pdata.updateProduct();
                break;

            case 4:
                pdata.searchProducID();
                break;

            case 5:
                pdata.searchProducName();
                break;

            case 6:
            {
                System.out.println("Enter range [minPrice, maxPrice] :");
                System.out.print("minPrice : ");
                double minPrice = input.nextDouble();
                System.out.print("maxPrice : ");
                double maxPrice = input.nextDouble();
                
                while (minPrice >= maxPrice)
                {
                    System.out.println("Re-enter range [minPrice less than maxPrice] :");
                    System.out.print("minPrice : ");
                    minPrice = input.nextDouble();
                    System.out.print("maxPrice : ");
                    maxPrice = input.nextDouble();
                }

                LinkedList<Product> data = pdata.getPriceRange(minPrice, maxPrice);
                if (data.empty())
                    System.out.println("No products in this range.");
                else
                    data.print();
            }
            break;

            case 7:
                pdata.Out_Stock_Products();
                break;

            case 8:
                Customers_Reviewed_product_sort_RATING();
                break;

            case 9:
                Customers_Reviewed_product_sort_CID();
                break;

            case 10:
                pdata.getproductsIDs().printKeys_Data();
                break;

            case 11:
                // just return to main menu
                break;

            default:
                System.out.println("Invalid choice! Returning to Main Menu...");
        }
    }
    
    //=============================================================================
    // Customers reviewed product - sorted by rating
    //=============================================================================
    public static void Customers_Reviewed_product_sort_RATING()
    {
        LinkedPQ<Customer> Rating = new LinkedPQ<Customer> ();
        products = pdata.getproductsIDs();
        
        System.out.print("Enter product ID: ");
        int pID = input.nextInt();
        while (!products.find(pID))
        {
            System.out.println("Product ID not available, re-enter:");
            pID = input.nextInt();
        }

        if (reviews.empty())
        {
            System.out.println("No reviews are available.");
            return;
        }
        
        reviews.findFirst();
        while (!reviews.last())
        {
            if (reviews.retrieve().getProduct() == pID)
                if (customers.find(reviews.retrieve().getCustomer()))
                    Rating.enqueue(customers.retrieve(), reviews.retrieve().getRating());
            reviews.findNext();
        }

        if (reviews.retrieve().getProduct() == pID)
            if (customers.find(reviews.retrieve().getCustomer()))
                Rating.enqueue(customers.retrieve(), reviews.retrieve().getRating());

        System.out.println("\nCustomers who reviewed product " + pID + " sorted by rating:");
        while (Rating.length() > 0)
        {
            PQElement pq = Rating.serve();
            System.out.print(pq.data);
            System.out.println("Rating: " + pq.priority);
            System.out.println();
        }
    }

    //=============================================================================
    // Customers reviewed product - sorted by customer ID
    //=============================================================================
    public static void Customers_Reviewed_product_sort_CID()
    {
        AVLTree <Integer, Customer> CIDs = new AVLTree<Integer, Customer> ();
        
        System.out.print("Enter product ID: ");
        int pID = input.nextInt();
        while (!products.find(pID))
        {
            System.out.println("Product ID not available, re-enter:");
            pID = input.nextInt();
        }

        if (reviews.empty())
        {
            System.out.println("No reviews are available.");
            return;
        }
        
        reviews.findFirst();
        while (!reviews.last())
        {
            if (reviews.retrieve().getProduct() == pID)
                if (customers.find(reviews.retrieve().getCustomer()))
                    CIDs.insert(customers.retrieve().getCustomerId(), customers.retrieve());
            reviews.findNext();
        }

        if (reviews.retrieve().getProduct() == pID)
            if (customers.find(reviews.retrieve().getCustomer()))
                CIDs.insert(customers.retrieve().getCustomerId(), customers.retrieve());
        
        System.out.println("\nCustomers who reviewed product " + pID + " sorted by customer ID:");
        CIDs.printKeys_Data();
    }
    
    //=============================================================================
    // Customers Menu
    //=============================================================================
    public static void CustomersMenu()
    {
        int choice;

        System.out.println("\n==============================");
        System.out.println("          Customers Menu       ");
        System.out.println("==============================");
        System.out.println("1. Register new customer");
        System.out.println("2. Search for customer (IDs)");
        System.out.println("3. Place new order for specific customer");
        System.out.println("4. View order history for specific customer");
        System.out.println("5. List all customers sorted alphabetically");
        System.out.println("6. Extract reviews from a specific customer for all products");
        System.out.println("7. List all customers");
        System.out.println("8. Return to Main Menu");
        System.out.print("Enter your choice: ");
        choice = input.nextInt();

        switch (choice)
        {
            case 1:
                cdata.RegisterCustomer();
                break;
            case 2:
                cdata.getCustomerID();
                break;
            case 3:
                PlaceOrder();
                break;
            case 4:
                cdata.OrderHistory();
                break;
            case 5:
                cdata.printNamesAlphabetically();
                break;
            case 6:
                AllReviewsForCustomer();
                break;
            case 7:
                customers.printKeys_Data();
                break;
            case 8:
                // return
                break;
            default:
                System.out.println("Invalid choice! Returning to Main Menu...");
        }
    }
    
    //=================================================================
    // Return all the reviews for specific customer
    //=================================================================
    public static LinkedList <Review> AllReviewsForCustomer()
    {
        System.out.print("Enter customer ID: ");
        int cid = input.nextInt();
        while(!customers.find(cid))
        {
            System.out.println("Customer ID not available, re-enter:");
            cid = input.nextInt();
        }
        
        // collecting all reviews
        LinkedList <Review> selectedReviews = new LinkedList<Review> ();
        reviews.findFirst();
        while (!reviews.last())
        {
            if (reviews.retrieve().getCustomer() == cid)
                selectedReviews.insert(reviews.retrieve());
            reviews.findNext();
        }
        if (reviews.retrieve().getCustomer() == cid)
            selectedReviews.insert(reviews.retrieve());

        // printing all reviews
        if (selectedReviews.empty())
            System.out.println("No reviews for customer " + cid);
        else
        {
            System.out.println("\nAll reviews for customer (" + cid + "):");
            selectedReviews.findFirst();
            while (!selectedReviews.last())
            {
                System.out.println(selectedReviews.retrieve());
                if (products.find(selectedReviews.retrieve().getProduct()))
                    System.out.println(products.retrieve());
                else
                    System.out.println("No data for Product (deleted / archived).");
                System.out.println();
                selectedReviews.findNext();
            }
            System.out.println(selectedReviews.retrieve());
            if (products.find(selectedReviews.retrieve().getProduct()))
                System.out.println(products.retrieve());
            else
                System.out.println("No data for Product (deleted / archived).");
            System.out.println();
        }
        return selectedReviews;
    }
    
    //=================================================================
    // Orders Menu
    //=================================================================
    public static void OrdersMenu()
    {
        int choice;

        System.out.println("\n==============================");
        System.out.println("            Orders Menu        ");
        System.out.println("==============================");
        System.out.println("1. Place new order");
        System.out.println("2. Cancel order");
        System.out.println("3. Update order status");
        System.out.println("4. Search by ID (log n)");
        System.out.println("5. All orders between two dates");
        System.out.println("6. Return to Main Menu");
        System.out.print("Enter your choice: ");
        choice = input.nextInt();
        
        switch (choice)
        {
            case 1:
                PlaceOrder();
                break;

            case 2:
            {
                System.out.print("Enter order ID: ");
                int oid = input.nextInt();
                odata.cancelOrder(oid);
            }
            break;

            case 3:
            {
                System.out.print("Enter order ID: ");
                int oid = input.nextInt();
                odata.UpdateOrder(oid);
            }
            break;

            case 4:
            {
                System.out.print("Enter order ID: ");
                int oid = input.nextInt();
                if (orders.find(oid))
                    System.out.println(orders.retrieve());
                else
                    System.out.println("No order found with this ID.");
            }
            break;

            case 5:
            {
                System.out.println("Enter first date (dd/MM/yyyy)");
                String date1 = input.next();
                
                System.out.println("Enter second date (dd/MM/yyyy)");
                String date2 = input.next();
                
                AVLTree<Date, Order> o = odata.BetweenTwoDates(date1, date2);
                o.printData();
            }
            break;

            case 6:
                // return
                break;

            default:
                System.out.println("Invalid choice! Returning to Main Menu...");
        }
    }
    
    //=================================================================
    // Place new order
    //=================================================================
    public static void PlaceOrder()
    {
        Order new_order = new Order ();
        int total_price = 0;
        
        System.out.print("Enter order ID: ");
        int oid = input.nextInt();
        while (orders.find(oid))
        {
            System.out.println("Order ID already exists, re-enter:");
            oid = input.nextInt();
        }
        new_order.setOrderId(oid);
        
        System.out.print("Enter customer ID: ");
        int cid = input.nextInt();
        while(!customers.find(cid))
        {
            System.out.println("Customer ID not available, re-enter:");
            cid = input.nextInt();
        }
        new_order.setCustomerRefrence(cid);
        Customer cus = customers.retrieve();
        cus.addOrder(oid);
        customers.update(cus);
        System.out.println("Customer (" + cid + ") placed order (" + oid + ").");

        char answer = 'y';
        while (answer == 'y' || answer == 'Y')
        {
            System.out.print("Enter product ID: ");
            int pid = input.nextInt();
           
            if (products.find(pid))
            {
                if (products.retrieve().getStock() == 0)
                    System.out.println("Product out of stock, try another time.");
                else
                {
                    Product pp = products.retrieve();
                    pp.setStock(pp.getStock() - 1);
                    products.update(pp);
                    new_order.addProduct(pid);
                
                    System.out.println("Product added to order.");
                    total_price += pp.getPrice();
                }
            }
            else
                System.out.println("No such product ID.");
                
            System.out.print("Do you want to continue adding products? (Y/N): ");
            answer = input.next().charAt(0);
        }
        
        new_order.setTotal_price(total_price);
        
        System.out.print("Enter order date (dd/MM/yyyy): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate Ldate = LocalDate.parse(input.next(), formatter);
        new_order.setDate(Ldate);
 
        System.out.print("Enter new status (pending, shipped, delivered): ");
        new_order.setStatus(input.next());
        
        orders.insert(new_order.getOrderId(), new_order);
           
        System.out.println("Order has been added successfully:");
        System.out.println(new_order);
    }
    
    //=================================================================
    // Reviews Menu
    //=================================================================
    public static void ReviewsMenu()
    {
        System.out.println("\n==============================");
        System.out.println("            Reviews Menu       ");
        System.out.println("==============================");
        System.out.println("1. Add review");
        System.out.println("2. Edit review (rating, comment)");
        System.out.println("3. Get average rating for a product");
        System.out.println("4. Show the top 3 highest AVG rated products");
        System.out.println("5. Show the top 3 most reviewed products");
        System.out.println("6. Common products (AVG rating >= 4) between 2 customers");
        System.out.println("7. Return to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();

        switch (choice)
        {
            case 1:
                AddNewReview();
                break;
            case 2:
                rdata.updateReview();
                break;
            case 3:
            {
                System.out.print("Enter product ID to get average rating: ");
                int pid = input.nextInt();

                while (!products.find(pid))
                {
                    System.out.println("Product ID not available, re-enter:");
                    pid = input.nextInt();
                }
                float AVG = avgRating(pid);
                System.out.println("Average Rating for product " + pid + " is " + AVG);
            }
            break;
            case 4:
                top3ProductsRating();
                break;
            case 5:
                top3ProductsReviewd();
                break;
            case 6:
            {
                Customer cid1 = cdata.getCustomerID();
                Customer cid2 = cdata.getCustomerID();
                commonProducts(cid1.getCustomerId(), cid2.getCustomerId());
            }
            break;
            case 7:
                // return
                break;
            default:
                System.out.println("Invalid choice! Returning to Main Menu...");
        }
    }
    
    //=================================================================
    // Add new review
    //=================================================================
    public static void AddNewReview()
    {
        System.out.print("Enter customer ID: ");
        int cID = input.nextInt();
        while (!customers.find(cID))
        {
            System.out.println("Customer ID not available, re-enter:");
            cID = input.nextInt();
        }
        
        System.out.print("Enter product ID: ");
        int pID = input.nextInt();
        while (!products.find(pID))
        {
            System.out.println("Product ID not available, re-enter:");
            pID = input.nextInt();
        }
        
        Review review = rdata.AddReview(cID, pID);
        System.out.println("New Review (" + review.getReviewId() + ") added for product "
                + review.getProduct() + " by customer (" + review.getCustomer()
                + "), Rate (" + review.getRating() + ") with comment: " + review.getComment() + ".");
    }
    
    //=================================================================
    // Get an average rating for product.
    //=================================================================
    public static float avgRating(int pid)
    {
        int counter = 0;
        float rate = 0;
        
        reviews.findFirst();
        while (!reviews.last())
        {
            if (reviews.retrieve().getProduct() == pid)
            {
                counter++;
                rate += reviews.retrieve().getRating();
            }
            reviews.findNext();
        }
        if (reviews.retrieve().getProduct() == pid)
        {
            counter++;
            rate += reviews.retrieve().getRating();
        }
        
        return (counter == 0 ? 0 : (rate / counter));
    }

    //=================================================================
    // Top 3 most reviewed products
    //=================================================================
    public static void top3ProductsReviewd()
    {
        LinkedPQ<Product> top3 = new LinkedPQ<Product> ();

        LinkedList<Product> allProducts = products.inOrdertraverseData();
        
        if (!allProducts.empty())
        {
            allProducts.findFirst();
            for (int i = 0 ; i < allProducts.size() ; i++)
            {
                Product p = allProducts.retrieve();
                
                int ReviewdCounter = 0;
                reviews.findFirst();
                while (!reviews.last())
                {
                    if (reviews.retrieve().getProduct() == p.getProductId())
                        ReviewdCounter++;
                    reviews.findNext();
                }                
                if (reviews.retrieve().getProduct() == p.getProductId())
                    ReviewdCounter++;

                top3.enqueue(p, ReviewdCounter);
                
                allProducts.findNext();
            }
        }
        
        System.out.println("Top 3 products reviewed (from most to least):");
        for (int j = 1 ; j <= 3 && top3.length() > 0 ; j++)
        {
            PQElement<Product> top = top3.serve();
            System.out.println("Product " + j + ": " + top.data.getProductId() 
                    + " " + top.data.getName() + " - number of reviews (" + top.priority + ")");
        }
    }

    //=================================================================
    // Top 3 by average rating
    //=================================================================
    public static void top3ProductsRating()
    {
        LinkedPQ<Product> top3 = new LinkedPQ<Product> ();

        LinkedList<Product> allProducts = products.inOrdertraverseData();
        
        if (!allProducts.empty())
        {
            allProducts.findFirst();
            for (int i = 0 ; i < allProducts.size() ; i++)
            {
                Product p = allProducts.retrieve();
                float AVGrating = avgRating(p.productId);
                top3.enqueue(p, AVGrating);
                
                allProducts.findNext();
            }
        }
        
        System.out.println("Top 3 products by average rating (from most to least):");
        for (int j = 1 ; j <= 3 && top3.length() > 0 ; j++)
        {
            PQElement<Product> top = top3.serve();
            System.out.println("Product " + j + ": " + top.data.getProductId() 
                    + " " + top.data.getName() + " - AVG rate (" + top.priority + ")");
        }
    }
    
    //=================================================================
    // Common products between 2 customers with AVG >= 4
    //=================================================================
    public static void commonProducts(int cid1 , int cid2)
    {
        LinkedList<Integer> pcustomer1 = new LinkedList<Integer> ();
        LinkedList<Integer> pcustomer2 = new LinkedList<Integer> ();
        
        // 1. find all products for customer1 and customer2 that are reviewed
        reviews = rdata.getreviewsData();
        
        if (!reviews.empty())
        {
            reviews.findFirst();
            for (int i = 1; i <= reviews.size(); i++)
            {
                if (reviews.retrieve().getCustomer() == cid1)
                {
                    pcustomer1.findFirst();
                    boolean found1 = false;
                    for (int x = 1; x <= pcustomer1.size(); x++)
                    {
                        if (pcustomer1.retrieve() == reviews.retrieve().getProduct())
                        {
                            found1 = true;
                            break;
                        }
                        pcustomer1.findNext();
                    }
                    pcustomer1.findLast();
                    if (!found1)
                        pcustomer1.insert(reviews.retrieve().getProduct());
                }
                
                if (reviews.retrieve().getCustomer() == cid2)
                {
                    pcustomer2.findFirst();
                    boolean found2 = false;
                    for (int x = 1; x <= pcustomer2.size(); x++)
                    {
                        if (pcustomer2.retrieve() == reviews.retrieve().getProduct())
                        {
                            found2 = true;
                            break;
                        }
                        pcustomer2.findNext();
                    }
                    
                    pcustomer2.findLast();
                    if (!found2)
                        pcustomer2.insert(reviews.retrieve().getProduct());
                }
                reviews.findNext();
            }
            
            // find common products for both lists
            LinkedPQ<Product> AVGrate45 = new LinkedPQ<Product> ();
            if (!pcustomer1.empty() && !pcustomer2.empty())
            {
                pcustomer1.findFirst();
                for (int m = 1; m <= pcustomer1.size(); m++)
                {
                    int pID = pcustomer1.retrieve();
                    pcustomer2.findFirst();
                    for (int n = 1; n <= pcustomer2.size(); n++)
                    {
                        if (pID == pcustomer2.retrieve())
                        {
                            float AVGrating = avgRating(pID);
                            if (AVGrating >= 4)
                            {
                                Product p = pdata.getProductData(pID);
                                AVGrate45.enqueue(p, AVGrating);
                            }
                        }
                        pcustomer2.findNext();
                    }
                    pcustomer1.findNext();
                }                
            
                System.out.println("Common Products with average rating >= 4:");
                while (AVGrate45.length() > 0)
                {
                    PQElement<Product> product_rate = AVGrate45.serve();
                    System.out.println("Product (" + product_rate.data.productId + ") "
                            + product_rate.data.getName() + " with AVG rate " + product_rate.priority);
                    System.out.println(product_rate.data);
                    System.out.println();
                }
            }
            else
                System.out.println("NO COMMON products between the two customers.");
        }
        else
            System.out.println("Reviews not available for all products.");
    }
    
    
    //=================================================================
    // main
    //=================================================================
    public static void main(String[] args) {

        System.out.println("loading data from CVSs files");

        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("             AVL TREE          ");
            System.out.println("==============================");
            System.out.println("1. Products");
            System.out.println("2. Customers");
            System.out.println("3. Orders");
            System.out.println("4. Reviews");
            System.out.println("5. Exist with saving all data to files");
            System.out.println("6. Exist (without saving)");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
                
            switch (choice)
            {
                case 1:
                    productsMenu();
                    break;
                case 2:
                    CustomersMenu();
                    break;
                case 3:
                    OrdersMenu();
                    break;
                case 4:
                    ReviewsMenu();
                    break;
                case 5:
                    System.out.println("Exit with saving all data to files...");
                    customers.LoadToFile("customers.txt");
                    System.out.println("Successfully wrote to customers file.");

                    products.LoadToFile("products.txt");
                    System.out.println("Successfully wrote to products file.");

                    pdata.getproductsArchived().LoadToFile("Archivedproducts.txt");
                    System.out.println("Successfully wrote to archived products file.");
                    
                    reviews.LoadToFile("reviews.txt");
                    System.out.println("Successfully wrote to reviews file.");

                    orders.LoadToFile("orders.txt");
                    System.out.println("Successfully wrote to orders file.");
                    break;
                case 6 :
                    // exit without saving
                    break;
                default:
                    System.out.println("Invalid choice! Try again...");
            }
        } while (choice != 5);
    }
}
       
