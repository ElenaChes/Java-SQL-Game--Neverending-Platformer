/*
 * @author Elena Chestnov
 */
package main_pkg;
    import java.io.*;
    import java.nio.file.*;
    import javax.swing.JLabel;
    import java.time.LocalTime;

public class LoadLevel extends Background {
/*  Flags and variables  */    
    static public Queue <Tile> level;
    static public Queue <Tile> BackupLevel;
    static public String file="";
    static public String creator="";
    static public String lb="";
    static private Results [] table=new Results [5];
    static private int difficulty;
/*  Classes  */    
    Background bg=new Background();
    
/*  Builders  */     
    public LoadLevel(int amount, JLabel [] labels, Queue list){
        super(amount, labels, list);
        super.CTile=new Tile(1,1,false,0,0,true);
        super.CHeight=1;
        super.shift=0;
    }
    public LoadLevel(){}
    
/*  Level verification:  */      
    public boolean IsValidLevel(){ 
        boolean flag=true;
        if (IsValidHash()){
            try{
                File readfile = new File(System.getProperty( "user.dir" )+"\\levels\\"+file);
                BufferedReader br = new BufferedReader(new FileReader(readfile));
                String st;
                int ID=0;
                int SLTrap=0;
                int CHeight=1;
                st=br.readLine();
                if (st.charAt(0)=='%')
                    difficulty=Integer.parseInt(st.substring(1));
                else
                    flag=false;
                while((st=br.readLine()) != null&&flag&&st.charAt(0)!='@'){ 
                    ID=Integer.parseInt(st);
                    if (ID!=19){    // if didn't reach the end
                        if(bg.IsTrap(ID)){
                            if (SLTrap>0)
                                flag=false;
                            else
                                SLTrap=difficulty;
                        }
                        else
                            SLTrap--;
                
                        if (!bg.IsDynamic(ID)){
                            if(bg.StartElevation(ID)!=CHeight)
                                flag=false;
                            else
                                CHeight=bg.RealElevation(ID);
                        }
                    }
                }
                creator=st.substring(1);
                lb="";
                int i=0;
                while((st=br.readLine()) != null&&i<5){
                    lb+=st;
                    i++;
                    if(i<5)
                        lb+="\n";
                }
                if (i==5&&st.charAt(0)!='&')
                    flag=false;
                else {
                    createTable(lb);
                    if(!IsValidTable())
                        flag=false;
                    else
                        Screen2.update(creator, table, difficulty);
                }
                br.close();
            }
            catch (FileNotFoundException ex) {
		Screen1.Error(Screen1.Scr1Panel,"Problem in loading file "+file);
            }
            catch (IOException ex) {
		ex.printStackTrace();
            }
        }
        else
            return false;
        return flag; 
    }
    
 /*  Hash verification  */      
    public boolean IsValidHash(){ 
        String st="";
        try{
            st = new String ( Files.readAllBytes( Paths.get(System.getProperty( "user.dir" )+"\\levels\\"+file)));
            String[] parts = st.split("&"); 
            parts[0]=parts[0].substring(0,parts[0].lastIndexOf("\n"));
            parts[1]=parts[1].substring(parts[1].lastIndexOf("\n") + 1);
            int oldHash=Integer.parseInt(parts[1]);
            int newHash=7;
            for (int i = 0; i < parts[0].length(); i++) 
                newHash = newHash*31 + parts[0].charAt(i);
            if(newHash==oldHash)
                return true;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            Screen1.Error(Screen1.Scr1Panel,"File has been corrupted or changed.");
        }
        catch (NoSuchFileException ex) {
                Screen1.Error(Screen1.Scr1Panel, "File must be in folder 'levels'.");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;  
    }
    
    static public boolean IsValidTable(){
        for (int i=0;i<4;i++)    
            if(!(table[i].getTime()).isBefore(table[i+1].getTime())
                    &&!((table[i].getTime()).equals(table[i+1].getTime()))&&(table[i+1].getTime()!=LocalTime.of(0,0,0))){
                table[i].PrintLine();
                table[i+1].PrintLine();
                return false;
            }
        return true;
   }    
    
/*  Loading level into Queue "level" */      
    public void LoadFile () {
        try{
            File readfile = new File(System.getProperty( "user.dir" )+"\\levels\\"+file);
            BufferedReader br = new BufferedReader(new FileReader(readfile));
            String st;
            Tile temp;
            int ID;
            level=new Queue <Tile>();
            BackupLevel=new Queue <Tile>();
            st=br.readLine();
            difficulty=Integer.parseInt(st.substring(1));
            while((st=br.readLine()) != null&&st.charAt(0)!='@'){
                ID=Integer.parseInt(st);
                if (ID!=19){
                    int elevation=super.CHeight;
                    temp=super.CreateTile(ID,elevation,false);  
                    if (!temp.IsDynamic())
                        super.CHeight=super.RealElevation(ID);
                    level.Insert(temp);
                    BackupLevel.Insert(temp);
                }
                else{
                    level.Insert(new Tile (ID,CHeight,false,0,19,true));
                    BackupLevel.Insert(new Tile (ID,CHeight,false,0,19,true));
                }
            }
            br.close();
            for (int i=0;i<amount;i++)
                level.Insert(new Tile(1,CHeight,false,0,0,true));
        }
    	catch (FileNotFoundException ex) {
            Screen1.Error(Screen1.Scr1Panel,"File has been corrupted or changed.");
        }
	catch (IOException ex) {
            ex.printStackTrace();
	}
    }
    
/*  Create leaderboard table  */      
    static public void createTable(String lb){
        String[] line = lb.split("\n"); 
        for (int i=0;i<5;i++){    
            String[] part = line[i].split("@"); 
            String[] nums = part[0].split("#"); 
                int minutes=Integer.valueOf(nums[0]);
                int seconds=Integer.parseInt(nums[1]);
                table[i]=new Results(part[1],LocalTime.of(0,minutes,seconds)); 
        }
   }  
    
/*  Running the level:  */      
/*  Shift Screen for smoother transition  */  
    @Override
    protected void ShiftScreen(){
        if (level.IsEmpty()!=true){
            Tile temp;
            if (super.shift>=super.width/super.amount){ // When done shifting
                super.shift=0;
                super.list.Remove();  // Remove oldest integer in "list" 
                temp=level.Remove();
                super.list.Insert(temp); // Inserting next tile
                super.Update();
            }
            for (int i=0;i<amount+1;i++){
                temp=list.GetItem(i);
                if (temp.IsDynamic()){
                    int elevation =temp.getHeight();
                    labels[i].setLocation(width*i/amount-shift,-(elevation-1)*height*16/108);  
                }
                else
                    labels[i].setLocation(width*i/amount-shift,0);
            }
            super.shift+=(super.width/super.amount)/10;
        }
    }
    
/*  Add new result to leaderboard in level file  */      
    static public int insertNew(String player, LocalTime resultTime){
        int line=-1;
        for (int i=0;i<5&&line==-1;i++){
            if(table[i].getTime()==LocalTime.of(0,0,0)||resultTime.isBefore(table[i].getTime())){
                line=i;
                for (i=4;i>line;i--)
                    table[i]=table[i-1];
                Results newline=new Results(player,resultTime);
                table[line]=newline;
            }
        }
        return line;
    }

/*  Updating existing level file  */       
    static public void add(String player, LocalTime resultTime){
        int line=insertNew(player, resultTime);
            if (line!=-1){
                Tile temp;
                String str="%"+difficulty+"\n";
                Queue <Tile> q2=new <Tile> Queue();
                while(!BackupLevel.IsEmpty()) {
                    temp=BackupLevel.Remove();
                    str+=temp.getTile();
                    str+="\n";
                    q2.Insert(temp);
                }
                while(!q2.IsEmpty()){
                    BackupLevel.Insert(q2.Remove());
                }
                str+="@"+creator;   
                for (int i=0;i<5;i++)
                    str+="\n"+(table[i].getTime()).getMinute()+"#"+(table[i].getTime()).getSecond()+"@"+table[i].getPlayer();
            str+="\n&\n"+CreateLevel.stringToHash(str); 
            try{
                File Cfile = new File(System.getProperty( "user.dir" )+"\\levels\\"+file);
                Cfile.delete();
                BufferedWriter bw = new BufferedWriter( new FileWriter(Cfile));
                Cfile.setWritable(true);
                bw.write(str);
                Cfile.setWritable(false);
                bw.close();
            }
            catch (NoSuchFileException ex) {
		Screen1.Error(Screen1.Scr1Panel,"File must be in folder 'levels'.");
            }
            catch (FileNotFoundException ex) {
		Screen1.Error(Screen1.Scr1Panel,"Problem in loading file."+file);
            }
            catch (IOException ex){
                ex.printStackTrace();
            }  
        }  
    } 
}