## Lambda expressions

- These are functional style code representing anonymous functions, allowing defining behaviours without creating separate classes.
- These can be passed as parameters, assigned to variable resulting in cleaner and readable code.

```aiignore

interface Add {
    int addition(int a, int b);
}

public class Utility {

    public static void main(String[] args){
        
        Add add = (a,b) -> a+b;
        System.out.println("Sum: " + add.addition(10, 20));
    }
}

```

- Functional interface with default method, default methods were introduced to have backward compatibility so that new interfaces can have new code without changing existing code

```aiignore
interface FuncInterface {
    
    void abstractFun(int x);
    default void normalFun(){
        System.out.println("Hello");
    }
}

public class Utility {
    
    public static void main(String[] args){
        
        FuncInterface obj = (int x) -> System.out.println(2 * x);
        fobj.abstractFun(5);
    }
}
```
