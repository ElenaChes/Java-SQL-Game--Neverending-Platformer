/*
 * @author Elena Chestnov
 */
package main_pkg;
/*  Visuals  */
    import javax.swing.*;
    import java.awt.Point;
/*  Other  */
    import java.sql.*;
    import java.awt.event.*;
    import java.util.Random;

public class Background {
/*  Flags and variables  */    
    protected int amount;
    protected int width=Screen1.width;
    protected int height=Screen1.height;
    protected int difficulty;
    
    protected Tile CTile;    // Curent tile
    private int SLTrap; // Since Last Trap
    protected int CHeight;  // Current height
    protected int shift;    // screen shifting
    protected String anim="";   // currently animated tile
    private Images [] bg=new Images[19];
/*  Pointers  */     
    protected JLabel [] labels;
    protected Queue <Tile> list;
    
/*  Builders  */     
    public Background(int amount, JLabel [] labels, Queue list){
        this.amount=amount;
        this.labels = labels;
        this.list = list;
        this.CTile=new Tile(1,1,false,0,0,true);
        CHeight=1;
        SLTrap=0;
        shift=0;
        for (int i=0;i<amount+1;i++){
            list.Insert(CTile);
        }
    }
    public Background(){}

/*  Timers  */ 
    private Timer tm = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShiftScreen();
                }
        });
    private Timer animate;
    
/*  Access to timers  */       
    protected void start(){
        tm.start();
        AnimateBG();
    }
    protected void stop(){
        tm.stop();
    }   
    
/*  Preloads images for less lag  */    
    protected void LoadImg(int width, int height){
        String str;
        this.width=width;
        this.height=height;
        for (int i=0;i<19;i++){
            str= System.getProperty( "user.dir" )+"/res/"+(i+1)+".png";
            bg[i]=new Images(str,width/amount+1, height,i+1);
        }
    }
    
    protected void CpQueue(Queue <Tile> q2){   // Pasting a temporary Queue back into "list"
        while(!q2.IsEmpty()){
            list.Insert(q2.Remove());
        }
    }
    
/*  Update screen according to "list"  */  
    protected void Update(){
        Tile temp;
        Queue <Tile> q2=new <Tile> Queue();
        ImageIcon imageIcon=new ImageIcon();
        for (int i=0;i<amount+1;i++){
            temp=(list.Remove());
            q2.Insert(temp);
            int pic=temp.getTile();
            imageIcon=bg[pic-1].getIcon();
            labels[i].setIcon(imageIcon);
            }
        CpQueue(q2);
    }
    
/*  Shift Screen for smooth transition  */  
    protected void ShiftScreen(){
        Tile temp;
        if (shift>=width/amount){   // When done shifting
                shift=0;
                NextTile(MaxOptions());
                Update();
        }
        for (int i=0;i<amount+1;i++){
                temp=list.GetItem(i);
            if (temp.IsDynamic()){    //not a transition tile
                int elevation=temp.getHeight();
                Point point = new Point();
                point.setLocation(width*i/amount-shift,-(elevation-1)*height*16/108);
                labels[i].setLocation(point);
            }
            else
                labels[i].setLocation(width*i/amount-shift,0);
        }
        shift+=(width/amount)/10;
    }
    
/*  Animated tiles  */      
    protected void AnimateBG (){   // Switches between 3 images
        Tile temp;
        ImageIcon imageIcon=new ImageIcon();
            switch (anim) {
                case "":    anim="_2";
                            break;
                case "_2":  anim="_3";
                            break;  
                default:    anim="";
                            break;
        } 
            for (int i=0;i<amount+1;i++){
                temp=list.GetItem(i);
            int type =temp.getType();
                if (type>10&&type<19){
                    int pic=type;
                    if (anim=="")
                        imageIcon=bg[pic-1].getIcon();
                    if (anim=="_2")
                        imageIcon=bg[pic-1].getIcon2();
                    else if (anim=="_3")
                        imageIcon=bg[pic-1].getIcon3();
                    labels[i].setIcon(imageIcon);
                }
            }
        animate = new Timer(125, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnimateBG();
                }
        });
        animate.setRepeats( false );
        animate.start();
    }
    
/*  Maximum of option avalivable for next tile  */    
    private int MaxOptions(){
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="";
            if (SLTrap>0){   // can't choose a trap
                query="SELECT count (*) AS total FROM Background WHERE Category="+CTile.getAllowed()+" AND Trap=False AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";   // command 
            }   
            else    // can choose a trap
                query="SELECT count (*) AS total FROM Background WHERE Category="+CTile.getAllowed()+" AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";  // command
            ResultSet rs = s.executeQuery(query);
            rs.next(); 
            int options=rs.getInt("total");
            s.close();  // Close the statement
            conn.close();   // Close the database.
            return options;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
        }
        return 0;
    }
    
/*  Choosing random tile from the options avalible  */    
    private void NextTile(int options){
        if (options!=0){
            try{
                Random rand = new Random();
                int i=(rand.nextInt(options));  // random int according to max options
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 
                Connection conn = DriverManager.getConnection(dbURL); 
                Statement s = conn.createStatement();
                String query="";
                if (SLTrap>0)   // can't choose a trap
                    query="SELECT * FROM Background WHERE Category="+CTile.getAllowed()+" AND Trap=False AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";    // command 
                else    // can choose a trap
                    query="SELECT * FROM Background WHERE Category="+CTile.getAllowed()+" AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";   // command
                ResultSet rs = s.executeQuery(query);
                rs.next();
                while (i>0){    // getting to the needed random line
                    rs.next();
                    i--;
                }
                int ID=rs.getInt("ID");
                boolean trap=rs.getBoolean("Trap");
                int allowed=rs.getInt("Allowed_After");
                int type=rs.getInt("Type");
                boolean dynamic=rs.getBoolean("Dynamic");
                
                int elevation=CHeight;
                if (!dynamic)
                   CHeight=rs.getInt("End_Height");
                SLTrap--;
                s.close();  // Close the statement
                conn.close();   // Close the database.
                list.Remove();  // Remove oldest integer in "list" 
                CTile=new Tile(ID, elevation, trap, allowed, type, dynamic);
                list.Insert(CTile);
                 if(trap&&(allowed==0||allowed==5)) // is the current tile failable?
                    SLTrap=difficulty;
            }
            
            catch(SQLException ex){
                ex.printStackTrace();
            }
            catch(ClassNotFoundException cnfex) {
                Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
            }
        }
        else
        Screen1.Error(Screen1.LevPanel,"Problem in loading options");
    } 
    
/*  Procedures used by create level/load level  */    
    static protected Tile CreateTile(int id, int elevation, boolean full){  
            try{
                Tile result=null;
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 
                Connection conn = DriverManager.getConnection(dbURL); 
                Statement s = conn.createStatement();
                String query="";
                query="SELECT * FROM Background WHERE ID="+id+";";    //command 
                ResultSet rs = s.executeQuery(query);
                rs.next();
                int ID=rs.getInt("ID");
                boolean trap=rs.getBoolean("Trap");
                int allowed=rs.getInt("Allowed_After");
                int type=rs.getInt("Type");
                boolean dynamic=rs.getBoolean("Dynamic");
                if (!full)
                    result=new Tile(ID, elevation, trap, allowed, type, dynamic);
                else{
                    boolean set=rs.getBoolean("Set");
                    result=new Tile(null, ID, elevation, trap, allowed, type, dynamic, set, 0);
                }
                s.close();  // Close the statement
                conn.close();   // Close the database.
                return result;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex) {
                Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
            }
        return null;
    }
    
    public boolean IsTrap(int id){
        boolean flag=false;
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="SELECT * FROM Background WHERE ID="+id; //command
            ResultSet rs = s.executeQuery(query);
            rs.next(); 
            boolean trap=rs.getBoolean("Trap");
            int type=rs.getInt("Type");
            int allowed=rs.getInt("Allowed_After");
            if(trap&&(allowed==0||type==9))
                flag=true;
            s.close();  // Close the statement
            conn.close(); // Close the database.
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
        }
        return flag;
    }  
    
    public boolean IsDynamic(int id){
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="SELECT * FROM Background WHERE ID="+id; //command
            ResultSet rs = s.executeQuery(query);
            rs.next(); 
            boolean dynamic=rs.getBoolean("Dynamic");
            s.close();  // Close the statement
            conn.close(); // Close the database.
            return dynamic;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
        }
        return false;
    } 
    
    static protected int StartElevation(int id){
            try{
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 
                Connection conn = DriverManager.getConnection(dbURL); 
                Statement s = conn.createStatement();
                String query="";
                query="SELECT * FROM Background WHERE ID="+id+";";    // command 
                ResultSet rs = s.executeQuery(query);
                rs.next();
                int result=rs.getInt("Start_Height");
                s.close();  // Close the statement
                conn.close();   // Close the database.
                return result;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex) {
                Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
            }
        return -1;
    }  
    
    static protected int RealElevation(int id){
            try{
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 
                Connection conn = DriverManager.getConnection(dbURL); 
                Statement s = conn.createStatement();
                String query="";
                query="SELECT * FROM Background WHERE ID="+id+";";    // command 
                ResultSet rs = s.executeQuery(query);
                rs.next();
                int result=rs.getInt("End_Height");
                s.close();  // Close the statement
                conn.close();   // Close the database.
                return result;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex) {
                Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                    + "registering MS Access JDBC driver");
            }
        return -1;
    }   
}