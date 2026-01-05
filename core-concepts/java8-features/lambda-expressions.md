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

