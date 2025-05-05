// Product abstract class
abstract class Product {
    public abstract void display();
}

// Concrete Product A
class ConcreteProductA extends Product {
    @Override
    public void display() {
        System.out.println("Diplaying Concrete Product A");
    }
}

// Concrete Product B
class ConcreteProductB extends Product {
    @Override
    public void display() {
        System.out.println("Diplaying Concrete Product B");
    }
}

// Creator abstract class
abstract class Creator {
    public abstract Product factoryMethod();
}

// Concreate Creator for Product A
class ConcreteCreatorA extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductA();
    }
}

// Concreate Creator for Product B
class ConcreteCreatorB extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductB();
    }
}

// Client code
public class FactoryMethodImpl1 {
    public static void main(String[] args) {
        Creator creatorA = new ConcreteCreatorA();
        Creator creatorB = new ConcreteCreatorB();

        Product productA = creatorA.factoryMethod();
        Product productB = creatorB.factoryMethod();
        productA.display();
        productB.display();
    }
}