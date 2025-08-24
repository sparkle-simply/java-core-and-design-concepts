# SOLID Principles

**SOLID**  represents five core principles of object-oriented programming (OOP) design, which aim to make code more maintainable, flexible, and easier to understand.

## Table of Contents
- Single Responsibility Principle
- Open/Closed Principle
- Liskov Substitution Principle
- Interface Segregation Principle
- Dependency Inversion Principle
- Benefits

## Single Responsibility Principle
> A class should have one, and only one, reason to change.

```java
// ✅ Good Example
class UserRepository {
    void saveUser(User user) { /* ... */ }
}

class EmailService {
    void sendEmail(User user) { /* ... */ }
}

class ReportingService {
    void generateReport(User user) { /* ... */ }
}

// ❌ Bad Example
class UserManager {
    void saveUser(User user) { /* ... */ }
    void sendEmail(User user) { /* ... */ }
    void generateReport(User user) { /* ... */ }
}

```

## Open/Closed Principle
> Software entities should be open for extension but closed for modification.

```java
// ✅ Good Example
interface PaymentProcessor {
    void processPayment();
}

class CreditPaymentProcessor implements PaymentProcessor {
    public void processPayment() { /* ... */ }
}

class DebitPaymentProcessor implements PaymentProcessor {
    public void processPayment() { /* ... */ }
}

// ❌ Bad Example
class PaymentProcessor {
    void processPayment(String type) {
        if (type.equals("credit")) {
            // process credit payment
        } else if (type.equals("debit")) {
            // process debit payment
        }
    }
}

```

## Liskov Substitution Principle
> Derived classes must be substitutable for their base classes.

```java
// ✅ Good Example
interface Bird {
    void move();
}

class FlyingBird implements Bird {
    public void move() { /* fly */ }
}

class SwimmingBird implements Bird {
    public void move() { /* swim */ }
}

// ❌ Bad Example
class Bird {
    void fly() { /* ... */ }
}

class Penguin extends Bird {  // Penguins can't fly!
    void fly() {
        throw new UnsupportedOperationException();
    }
}

```

## Interface Segregation Principle
> Clients should not be forced to depend on interfaces they do not use.

```java
// ✅ Good Example
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class Human implements Workable, Eatable {
    public void work() { /* ... */ }
    public void eat() { /* ... */ }
}

// ❌ Bad Example
interface Worker {
    void work();
    void eat();
    void sleep();
}

```

## Dependency Inversion Principle
> High-level modules should not depend on low-level modules. Both should depend on abstractions.

```java
// ✅ Good Example
interface Database {
    void save();
}

class EmailService {
    private Database database;

    EmailService(Database database) {
        this.database = database;
    }
}

// ❌ Bad Example
class EmailService {
    MySQLDatabase database = new MySQLDatabase();
    void sendEmail() {
        database.save();
    }
}

```

## Benefits & Best Practices

### Key Benefits

#### Maintainability
- Easier to change and maintain code
- Reduced risk when making changes
- Better organization of code

#### Scalability
- Easier to extend functionality
- Better handling of growing complexity

#### Testability
- Easier to write unit tests
- Better isolation of components

#### Reusability
- More modular code
- Better component isolation
