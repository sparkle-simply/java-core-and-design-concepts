# Factory Design Pattern

## Overview
> Factory Method defines an interface or abstract class for creating objects and lets subclasses decide which class to instantiate.

## Class Diagram
![Factory Pattern](https://media.geeksforgeeks.org/wp-content/uploads/20240110163911/Key-Component-of-Factory-Method-Design-Pattern-in-Java-768.jpg)

## Benefits
- Decouples object creation from business logic
- Promotes loose coupling through abstraction
- Makes code more maintainable and testable
- Easy to extend with new product types
- Follows Single Responsibility and Open/Closed principles

## Limitations
- Can increase complexity by adding multiple classes and may not require factory for simple objects
- Client code must be aware of concrete subclasses to make accurate factory calls

## References
- [GeeksForGeeks](https://www.geeksforgeeks.org/factory-method-design-pattern-in-java/)