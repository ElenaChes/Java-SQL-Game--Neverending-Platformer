package main_pkg;

public class Queue <T> {
    private  int MAXVECTOR = 500;
    private T[] vector;
    private int front;
    private int rear;

    public  Queue(){
        vector=(T[]) new Object[MAXVECTOR];
        front = MAXVECTOR - 1;
        rear = front;
    }
    
    public void Insert(T x){
        if (rear == (MAXVECTOR - 1))
            rear = 0;
        else
            rear++;
        
        if (rear == front)
        Screen1.Error(Screen1.LevPanel, "Queue Full.");
        else
            vector[rear] = x;
    }
    
    public T Remove(){
        T result;
        if (rear == front)
            Screen1.Error(Screen1.LevPanel, "Queue Empty.");
        else {
            if (front == (MAXVECTOR - 1))
                front = 0;
            else
                front++;
            result = vector[front];
            return result;
        }
      return null;
    }
    
    public T GetItem(int i){
        T result;
        int index=front+i;
            if (index>=MAXVECTOR-1)
                index=index-MAXVECTOR;
        if (rear == front)
        	Screen1.Error(Screen1.LevPanel, "Queue Empty.");
        else {
            result = vector[index+1];
            return result;
        }
      return null;
    }
    
    public boolean IsEmpty(){
        if (rear == front)
            return true;
        return false;
    } 
}