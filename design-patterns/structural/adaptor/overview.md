# Adaptor Design Pattern

## Overview
> Adaptor design pattern acts as a bridge between two incompatible interfaces allowing them to work together. It is especially useful while integrating with legacy systems or third party libraries.

## Class Diagram
![Adaptor Pattern](https://media.geeksforgeeks.org/wp-content/uploads/20250905161148734950/Adapter-Design-Pattern.webp)

- Client wants to use a Target interface (it calls Request()).
- Adaptee already has useful functionality, but its method (SpecificRequest()) doesn’t match the Target interface.
- Adapter acts as a bridge: it implements the Target interface (Request()), but inside, it calls the Adaptee’s SpecificRequest().
- This allows the Client to use the Adaptee without changing its code.

## Benefits
- Promotes code reuse without modification
- Adaption logic is different, making dependent class to focus on core logic
- Decouples system implementation, with easing modification and swaps

## Trad-offs
- Adds complexity and maintaining effort
- Adds performance overhead due to underlying indirection

## References
- [GeeksForGeeks](https://www.geeksforgeeks.org/system-design/adapter-pattern/)