/*
 *   @author Elena Chestnov
 */
package main_pkg;

public class Tile {
    private String key;
    private int id;
    private int end_height;
    private boolean trap;
    private int allowed_after;
    private int type;
    private boolean dynamic;
    private boolean set;
    private int setCount;

    public Tile(String key, int id, int height, boolean trap, int allowed, int type, boolean dynm, boolean set, int setCount){    //for CreateLevel
        this.key = key;
        this.id = id;
        this.end_height = height;
        this.trap = trap;
        this.allowed_after = allowed;
        this.type = type;
        this.dynamic = dynm;
        this.set = set;
        this.setCount=setCount;
    }
    
    public Tile(Tile CTile, int setCount){    //for CreateLevel, creates tile with new "set count"
        this.key = CTile.getKey();
        this.id = CTile.getTile();
        this.end_height = CTile.getHeight();
        this.trap = CTile.IsTrap();
        this.allowed_after = CTile.getAllowed();
        this.type = CTile.getType();
        this.dynamic = CTile.IsDynamic();
        this.set = CTile.IsSet();
        this.setCount=setCount;
    }
    
    public Tile(int id, int height, boolean trap, int allowed, int type, boolean dynm){ //for Background/LoadLevel
        this.key = "";
        this.id = id;
        this.end_height = height;
        this.trap = trap;
        this.allowed_after = allowed;
        this.type = type;
        this.dynamic = dynm;
        this.set = false;
        this.setCount=0;
    }
    
    @Override
    public String toString(){
        return key;
    }
    
    protected String getKey(){
        return key;
    }
    
    protected int getTile(){
        return id;
    }
    
    protected int getHeight(){
        return end_height;
    }
    
    protected boolean IsTrap(){
        return trap;
    }
    
    protected int getAllowed(){
        return allowed_after;
    }
    
    protected int getType(){
        return type;
    }
    
    protected boolean IsDynamic(){
        return dynamic;
    }
    
    protected boolean IsSet(){
        return set;
    }
    
    protected int getSetCount(){
        return setCount;
    }
    
    protected void PrintTile(){
        System.out.println("************************");
        System.out.println("ID "+this.id);
        System.out.println("Height "+this.end_height);
        System.out.println("Trap "+this.trap);
        System.out.println("Allowed "+this.allowed_after);
        System.out.println("Type "+this.type);
        System.out.println("Dynamic "+this.dynamic);
        System.out.println("SetCount "+this.setCount);
        System.out.println("************************");
    }
}
