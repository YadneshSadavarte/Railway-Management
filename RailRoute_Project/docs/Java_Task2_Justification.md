# Java Task 2 Design Justification (Review 2)

**Course Outcome:** CO2 — *"Choose an appropriate programming solution to reduce complexity"*  
**Subject:** Problem Solving Using OOP with Java (PSOOP — 2310262L)  
**Deliverable:** Review 2 / Task 2 — Inheritance, Abstract Classes, Polymorphism & Keyword Usage

---

## 1. Why Inheritance Was Chosen (Complexity Reduction)
In the RailRoute system, both **Passenger** (frontend commuter) and **Operator** (backend administrator) represent real-world individuals who inherently share fundamental identity attributes (`personId`, `name`, and `contactNumber`). Without inheritance, each class redundantly defines, validates, and manages these identical fields and accessors, multiplying maintenance overhead and increasing defect surface. Factoring these shared attributes and behaviors into a single common superclass (`Person`) directly reduces code complexity through structural reuse, guarantees a single source of truth for personal data, and establishes a clean hierarchical domain model that can easily accommodate future user roles (e.g., Ticket Examiner, Station Master) without duplicating logic.

---

## 2. Keywords Rationale and Appropriateness

Each keyword was deliberately chosen to serve a specific architectural purpose rather than as cosmetic decoration:

1. **`abstract` (Class `Person` & Method `getRole()`):**
   * *Class Level:* An individual in RailRoute must always be a concrete actor (either a `Passenger` booking tickets or an `Operator` managing routes). Declaring `Person` as `abstract` prevents erroneous direct instantiation of an ambiguous person entity.
   * *Method Level:* Declaring `public abstract String getRole()` enforces a contractual obligation on all subclasses to identify their operational role, guaranteeing dynamic polymorphic dispatch across any collection of persons.

2. **`super` (Constructor Invocation):**
   * Subclass constructors (`Passenger` and `Operator`) invoke `super(id, name, contactNumber)` as their first statement. This delegates common state initialization to the parent class, enforcing constructor chaining and avoiding duplicate assignment logic.

3. **`final` (Field `personId`):**
   * Declared as `protected final String personId`. A person’s primary identification key is assigned upon account creation and must remain immutable throughout the object's lifecycle. Marking it `final` enforces referential integrity and thread-safe immutability at the compiler level.

4. **`static` (Field `personCount` & Method `getPersonCount()`):**
   * Declared as `private static int personCount`. This maintains a class-wide shared counter across all subclasses, tracking the cumulative number of `Person` instances created across the system runtime regardless of specific subclass type.

5. **`this` (Constructor/Setter Disambiguation):**
   * Used within `Person`, `Passenger`, and `Operator` to explicitly disambiguate instance variables from constructor and method parameter names (e.g., `this.name = name;`).

---

## 3. Polymorphism in Practice
In `Main.java`, a heterogeneous array `Person[] people = { passenger, operator };` is processed via a single loop. Invoking `person.getRole()` dynamically resolves at runtime to `Passenger.getRole()` or `Operator.getRole()` without `instanceof` checks or conditional branching, visibly demonstrating runtime polymorphism and decoupling client code from concrete subclass implementations.
