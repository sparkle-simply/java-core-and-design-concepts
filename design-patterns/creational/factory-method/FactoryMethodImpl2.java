// Product interface
interface Product {
    public void display();
}

// Concrete Product A
class ConcreteProductA implements Product {
    @Override
    public void display() {
        System.out.println("Diplaying Concrete Product A");
    }
}

// Concrete Product B
class ConcreteProductB implements Product {
    @Override
    public void display() {
        System.out.println("Diplaying Concrete Product B");
    }
}

// Creator interface
interface Creator {
    public Product factoryMethod();
}

// Concreate Creator for Product A
class ConcreteCreatorA implements Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductA();
    }
}

// Concreate Creator for Product B
class ConcreteCreatorB implements Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductB();
    }
}

// Client code
public class FactoryMethodImpl2 {
    public static void main(String[] args) {
        Creator creatorA = new ConcreteCreatorA();
        Creator creatorB = new ConcreteCreatorB();

        Product productA = creatorA.factoryMethod();
        Product productB = creatorB.factoryMethod();
        productA.display();
        productB.display();
    }
}