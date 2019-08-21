package main_pkg;

public class Stack <T> {
    private T[] arr;
    private int sp;

    public Stack(){
        arr=(T[]) new Object[500];
        sp=-1;
    }
    
    public void Push(T item){
	if(sp>99)
            Screen1.Error(Screen1.LevPanel, "Stack is full.");
        else
            arr[++sp]=item;
    }
    
    public T Pop(){
	return arr[sp--];
    }
    
    public boolean IsEmpty(){
	if(sp==-1)
            return true;
	return false;
    }
    
    public T Top(){
	return arr[sp];
    }
}
