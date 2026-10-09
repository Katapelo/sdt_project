1. (0.5p) Identify at least four design patterns that will be utilized in the project.
2. (0.5p) For each selected design pattern, provide a brief explanation justifying its use. This should detail how the pattern solves a specific problem and the advantages it offers over simpler alternatives.
### 1. State Pattern

**Use Case:** Transaction and Account Status Tracking

- **The Problem:** Both Transactions and Accounts have complex lifecycles. A transaction moves from `PENDING` to `SUCCESS` or `REJECTED`. An account moves from `ACTIVE` to `FROZEN` or `CLOSED`. Managing these transitions with nested `if` statements (e.g., `if (status == FROZEN && action == TRANSFER) { ... }`) leads to "spaghetti code" that is extremely difficult to test and prone to security loopholes.
- **The Justification:** The State pattern allows an object to alter its behavior when its internal state changes. We can create specific classes for `FrozenAccountState`, `ActiveAccountState`, and `ClosedAccountState`. The `withdraw()` method would behave differently depending on which state object is currently active. This moves the logic out of a central "God Class" and into small, manageable, and testable state-specific classes, ensuring that a "Closed" account can never accidentally perform a transfer.

### 2. Factory Method Pattern

**Use Case:** Account and Customer Management

- **The Problem:** A digital bank often supports various account types (e.g., `SavingsAccount`, `CheckingAccount`, `PremiumAccount`), each with different interest rates, withdrawal limits, or fee structures. Using a massive `if-else` or `switch` block inside a `createAccount` method to instantiate these classes makes the code brittle and difficult to extend.
- **The Justification:** The Factory Method pattern provides an interface for creating objects but allows subclasses to alter the type of objects that will be created. We can implement an `AccountFactory` that encapsulates the instantiation logic. If the bank decides to launch a "Crypto-linked Account" next month, we only need to add a new class and update the Factory, rather than hunting through the entire codebase to update conditional logic.

### 3. Observer Pattern

**Use Case:** Notifications and Monitoring

- **The Problem:** When a fund transfer occurs, multiple independent modules must react: sending an SMS/Email to the customer, updating the live Admin Dashboard, and appending an entry to the immutable Audit Log. If the core `TransactionManager` is hardcoded to call `EmailService`, `SMSService`, and `AdminDashboard` sequentially, the system becomes dangerously tightly coupled. Adding a new notification channel (like Mobile Push Notifications) would require modifying the core financial logic, introducing risk to the critical funds-transfer flow.
    
- **The Justification:** The Observer pattern allows the `TransactionManager` (the Subject) to maintain a list of "Subscribers" (Observers) via a generic interface. When a transaction completes, the Subject simply broadcasts a state change. The `EmailNotificationService` and `AuditLogService` listen and react independently. This decoupling ensures we can safely attach or remove auxiliary services without ever altering the heavily-tested core ledger code.
    
- **The Scalability Challenge (Solving Latency & Reliability in Banking):** While the classic in-memory Observer pattern solves code coupling, it introduces latency (the main thread blocks while waiting for the email server) and reliability risks (if an observer crashes, a critical audit log might be skipped). To resolve this in a modern digital bank, the pattern is evolved into a distributed architecture:
    
    - **Asynchronous Message Brokers (Pub/Sub):** Instead of calling `observer.update()` synchronously in the same thread, the `TransactionManager` acts as a Publisher. It pushes a lightweight event payload (e.g., `FundsTransferredEvent` or `AccountFrozenEvent`) to a secure external broker like Kafka or RabbitMQ. This allows the transaction thread to return a "Transfer Successful" response to the user's banking app instantly, eliminating UI latency while background microservices handle the emails and dashboards.
        
    - **The Transactional Outbox Pattern:** In banking, financial consistency is paramount. To prevent the "Dual Write Problem" (where the database updates the account balance, but the network fails before reaching the message broker), the event payload is inserted into a dedicated `event_outbox` table _within the exact same ACID database transaction_ as the balance deduction. A separate relay process safely reads this outbox table and pushes the events to the broker, guaranteeing at-least-once delivery of the notification.
        
    - **Acknowledgments and Dead Letter Queues (DLQ):** To ensure no transaction alert or audit log is ever lost, brokers require an explicit Acknowledgment (ACK) from the consumer (e.g., the SMS Service). If the third-party SMS API is temporarily down, the broker retries the message. If the failure persists, the message is routed to a Dead Letter Queue (DLQ) so bank engineers can inspect and manually replay the notification, ensuring strict regulatory compliance and flawless communication.
### 4. Command Pattern

**Use Case:** Transaction Management (Funds Transfers)

- **The Problem:** A transfer is not just a simple mathematical subtraction and addition; it is a discrete operation that requires logging, validation, and potentially "undoing" (rollback) if a failure occurs mid-process. If we simply use a method call like `transfer(from, to, amount)`, it becomes difficult to track the specific intent of the request, queue it for later processing, or implement a transaction history.
- **The Justification:** By encapsulating each transfer request as a `TransferCommand` object, we can store these objects in a `TransactionHistory` list. This allows the system to easily implement features like "Revert Transaction" (by calling an `undo()` method) and provides a structured way to handle asynchronous processing (queuing commands to be executed when resources are available). This is much more powerful than a simple function call, which lacks the metadata and state needed for audit trails.

3. (1 p) Design a single, comprehensive UML class diagram for the entire project. To highlight the pattern, you can use a note in UML, connected to all the classes involved in that pattern. Try to have a single design class diagram, do not treat the design patterns separately, instead they should be integrated in the entire app design.
![[Class_Diagram.png]]

4.  (0.75p) Create two detailed UML sequence diagrams. These diagrams should illustrate the main use cases of the application.
![[Sequence1.png]]

![[Sequence2.png]]

