
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.NoSuchElementException;


public class AVLTree<K extends Comparable<K>, T>{

        class AVLNode<key extends Comparable<key>, T> {
                public key key;
                public T data;
                AVLNode<key,T> parent;
                AVLNode<key,T> left;
                AVLNode<key,T> right;
                int balancefact; 

                   
                    public AVLNode() {
                            this.key = null;  
                            this.data = null;
                            this.parent = this.left = this.right = null;
                            this.balancefact = 0;
                    }

                    public AVLNode(key key, T data) {
                            this.key = key  ;  
                            this.data = data;
                            this.parent = this.left = this.right = null;
                            this.balancefact = 0;
                    }

                    public AVLNode(key key, T data, AVLNode<key,T> p, AVLNode<key,T> l, AVLNode<key,T> r){
                            this.key = key  ;  
                            this.data = data;
                            left = l;
                            right = r;
                            parent = p;
                            balancefact =0;
                    }

                    public AVLNode<key,T> getLeft()
                    {
                        return left;
                    }

                    public AVLNode<key,T> getRight()
                    {
                        return right;
                    }

                    public T getData()
                    {
                        return data;
                    }
                    
                    @Override
                    public String toString() {
                        return "AVL Node{" + "key=" + key + ", data =" + data + '}';
                    }
            }

       
        private AVLNode<K,T> root;
        private AVLNode<K,T>  current;
        private int count;
        
        public AVLTree() {
                root = current = null;
                count = 0;
        }

        public boolean empty() {
            return root == null;
        }

        public int size() {
            return count;
        }

        public void clear()
        {
            root = current = null;
            count = 0;
        }

        public T retrieve()
        {
            T data =null;
            if (current != null)
                data = current.data;
            return data;
        }

        public void update(T e)
        {
            if (current != null)
                current.data = e;
        }

        private T searchTreeHelper(AVLNode<K,T> node, K key) {
                   // Place your code here\\
                    if (node == null)
                        return null;
                    else if (node.key.compareTo(key) == 0) 
                    {
                        current = node;
                        return node.data;
                    } 
                    else if (node.key.compareTo(key) >0)
                         return searchTreeHelper(node.left, key);
                    else
                         return searchTreeHelper(node.right, key);
        }
        
        private void updateBalance(AVLNode<K,T> node) {
                if (node.balancefact < -1 || node.balancefact > 1) {
                        rebalance(node);
                        return;
                }

                if (node.parent != null) {
                        if (node == node.parent.left) {
                                node.parent.balancefact -= 1;
                        } 

                        if (node == node.parent.right) {
                                node.parent.balancefact += 1;
                        }

                        if (node.parent.balancefact != 0) {
                                updateBalance(node.parent);
                        }
                }
        }

        void rebalance(AVLNode<K,T> node) {
                if (node.balancefact > 0) {
                        if (node.right.balancefact < 0) {
                                rightRotate(node.right);
                                leftRotate(node);
                        } else {
                                leftRotate(node);
                        }
                } else if (node.balancefact < 0) {
                        if (node.left.balancefact > 0) {
                                leftRotate(node.left);
                                rightRotate(node);
                        } else {
                                rightRotate(node);
                        }
                }
        }

        public boolean find(K key) {
                T data = searchTreeHelper(this.root, key);
                if ( data != null)
                    return true;
                return false;
        }

        void leftRotate(AVLNode<K,T> x) {
            AVLNode<K,T> y = x.right;
            x.right = y.left;
            if (y.left != null) {
                    y.left.parent = x;
            }
            
            y.parent = x.parent;
            if (x.parent == null) {
                    this.root = y;
            } else if (x == x.parent.left) {
                    x.parent.left = y;
            } else {
                    x.parent.right = y;
            }
            y.left = x;
            x.parent = y;

            // update the balance factor
            x.balancefact = x.balancefact - 1 - Math.max(0, y.balancefact);
            y.balancefact = y.balancefact - 1 + Math.min(0, x.balancefact);
        }
        
        void rightRotate(AVLNode<K,T> x) {
                AVLNode<K,T> y = x.left;
                x.left = y.right;
                if (y.right != null) {
                        y.right.parent = x;
                }
                y.parent = x.parent;
                if (x.parent == null) {
                        this.root = y;
                } else if (x == x.parent.right) {
                        x.parent.right = y;
                } else {
                        x.parent.left = y;
                }
                y.right = x;
                x.parent = y;

                x.balancefact = x.balancefact + 1 - Math.min(0, y.balancefact);
                y.balancefact = y.balancefact + 1 + Math.max(0, x.balancefact);
        }

        
        public boolean insert(K key, T data) {
            AVLNode<K,T> node = new AVLNode<K,T>(key, data);

            AVLNode<K,T> p = null;
            AVLNode<K,T> current = this.root;

            while (current != null) {
                    p = current;
                    if (node.key.compareTo(current.key) ==0) {
                            return false;
                    }else if (node.key.compareTo(current.key) <0 ) {
                            current = current.left;
                    } else {
                            current = current.right;
                    }
            }

            node.parent = p;
            if (p == null) {
                    root = node;
                    current = node;
            } else if (node.key.compareTo(p.key) < 0 ) {
                    p.left = node;
            } else {
                    p.right = node;
            }
            count ++;

            updateBalance(node);
            return true;        
        }
        
    public boolean removeKey(K key) {
        K k1 = key;
        AVLNode<K,T>  p = root;
        AVLNode<K,T>  q = null;

        while (p != null) 
        {
                if (k1.compareTo(p.key) <0)
                {
                    q =p;
                    p = p.left;
                } 
                else if (k1.compareTo(p.key) >0)
                {    
                    q = p;
                    p = p.right;
                }
                else 
                {
                   
                    if ((p.left != null) && (p.right != null)) 
                    { 
                      
                        AVLNode<K,T> min = p.right;
                        q = p;
                        while (min.left != null) 
                        {
                            q = min;
                            min = min.left;
                        }
                        p.key = min.key;
                        p.data = min.data;
                        k1 = min.key;
                        p = min;
                    }
                   
                    if (p.left != null) 
                    { 
                        p = p.left;
                    } 
                    else 
                    { 
                        p = p.right;
                    }
                    if (q == null)
                    { 
                        root = p;
                        this.updateBalance(p);
                    } 
                    else 
                    {
                        if (k1.compareTo(q.key) <0)
                            q.left = p;
                        else 
                            q.right = p;
                        this.updateBalance(q);
                    }
                    count--;
                    current = root;
                    return true;    
            } 
        }  
        return false;
    }

    @Override
    public String toString() {
        String str = inOrdersTraversal(root );
        str = str.replace(str.substring(str.length()-2), "");
        return "{" + str + "}";
    }

    private String inOrdersTraversal(AVLNode<K, T>  node)
    {
        if (node == null)
            return "" ;
        return (inOrdersTraversal(node.left ) + " "
        + node.data + "; "        
        + inOrdersTraversal(node.right));
    }

    public void printKeys()
    {
        private_printKeys(root);
    }
    
    private void private_printKeys(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printKeys(node.left);
        System.out.println(node.key);
        private_printKeys(node.right);
        
    }

    public void printKeys_Data()
    {
        private_printKeys_Data(root);
    }
    
    private void private_printKeys_Data(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printKeys_Data(node.left);
        System.out.print(node.key);
        System.out.println(node.data);
        System.out.println("");
        private_printKeys_Data(node.right);    
    }

    public void printData()
    {
        private_printData(root);
    }
    
    private void private_printData(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printData(node.left);
        System.out.println(node.data);
        System.out.println("");
        private_printData(node.right);     
    }

    public LinkedList<T>  inOrdertraverseData() {
        LinkedList<T> data = new LinkedList<T>();
        private_inOrdertraverseData( root , data);
        return data;
    }

    private void  private_inOrdertraverseData(AVLNode<K, T>  node, LinkedList<T> data)
    {
        if (node == null)
            return ;
        private_inOrdertraverseData(node.left, data );
        data.insert(node.data);
        private_inOrdertraverseData(node.right, data);
    }

    public LinkedList <T> intervalSearch(K k1, K k2)
    {
        LinkedList <T> q = new LinkedList <T>();
        if (root != null)
           rec_intervalSearch (k1 , k2, root , q);
        return q;
    }
    
    private void rec_intervalSearch (K k1, K k2, AVLNode <K, T> p , LinkedList <T> q)
    {
        if (p == null)
            return;
        else
        {
                rec_intervalSearch (k1, k2 , p.left , q);
                if (( p.key.compareTo(k1) >= 0 ) && (p.key.compareTo(k2) <= 0 ))
                    q.insert(p.data);
                rec_intervalSearch (k1, k2 , p.right , q);
            
         }
    }   

    public AVLTree <Date,T> intervalSearchDate(LocalDate k1, LocalDate k2)
    {
        AVLTree <Date,T> q = new AVLTree <Date,T>();
        if (root != null)
            if ( root.data instanceof Order )
               rec_intervalSearchDate (k1 , k2, root , q);
        return q;
    }
    
    private void rec_intervalSearchDate (LocalDate Ldate1, LocalDate Ldate2, AVLNode <K, T> p , AVLTree <Date,T> q)
    {
        if (p == null)
            return;
        else
        {
                rec_intervalSearchDate (Ldate1, Ldate2 , p.left , q);
                if ( p.data instanceof Order )
                    if (((Order) p.data).getDate().isAfter(Ldate1) && ((Order) p.data).getDate().isBefore(Ldate2))
                    {
                        Date date = new Date (((Order) p.data).getDate().getDayOfMonth()
                                        , ((Order) p.data).getDate().getMonthValue()
                                        , ((Order) p.data).getDate().getYear() );
                        q.insert(date , p.data);
                    }
                        
                rec_intervalSearchDate (Ldate1, Ldate2 , p.right , q);
            
         }
    }   

    public void inOrdertraverseOutStock() {
        System.out.println("Products out of stock");
        System.out.println("----------------------");
        if (( root != null) && ( root.data instanceof Product))
            private_inOrdertraverseOutStock( root);
    }

    private void  private_inOrdertraverseOutStock(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_inOrdertraverseOutStock(node.left);
        if (node.data instanceof Product);
            if ( ((Product)node.data).getStock() == 0)
                System.out.println(node.data);
        private_inOrdertraverseOutStock(node.right);
    }

    public LinkedList <T> intervalSearchprice(double k1, double k2)
    {
        LinkedList <T> q = new LinkedList <T>();
        if (( root != null) && ( root.data instanceof Product))
           rec_intervalSearchprice (k1 , k2, root , q);
        return q;
    }
    
    private void rec_intervalSearchprice (double k1, double k2, AVLNode <K, T> p , LinkedList <T> q)
    {
        if (p == null)
            return;
        else
        {
                rec_intervalSearchprice (k1, k2 , p.left , q);
                if (p.data instanceof Product) 
                if ((( (Product) p.data).getPrice() >= k1 ) && ((( Product) p.data).getPrice() <=k2))
                    q.insert(p.data);
                rec_intervalSearchprice (k1, k2 , p.right , q);
            
         }
    }   

    public void LoadToFile ( String fileName)
    {
        
        try {
                FileWriter myWriter = new FileWriter(fileName);
                private_LoadToFile ( myWriter, root);
                myWriter.close();

        } catch(IOException ex){
                     System.out.println(ex.getMessage());
        } 
    }
    private void private_LoadToFile ( FileWriter myWriter,  AVLNode <K, T>  node) throws IOException
    {
        if ( node == null)
            return;
        private_LoadToFile ( myWriter,  node.left);
        if (node.data instanceof Customer)
            myWriter.write(((Customer) node.data).getDataToFile() +"\n");
        if (node.data instanceof Order)
            myWriter.write(((Order) node.data).getDataToFile() +"\n");
        if (node.data instanceof Product)
            myWriter.write(((Product) node.data).getDataToFile() +"\n");
        private_LoadToFile ( myWriter,  node.right);   
    }    
}