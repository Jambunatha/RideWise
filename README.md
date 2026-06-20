# RideWise - Modular Ride-Sharing System

## Overview
RideWise is a simplified, console-based ride-sharing system (Uber/Ola style) demonstrating high-quality Low-Level Design (LLD) and design principles. The system showcases proper application of SOLID principles, design patterns, and clean code architecture.

## Project Structure
```
src/
├── com/airtribe/ridewise/
│   ├── Main.java                          # Entry point & Console UI
│   ├── model/                              # Domain entities
│   │   ├── Rider.java
│   │   ├── Driver.java
│   │   ├── Ride.java
│   │   └── FareReceipt.java
│   ├── enums/
│   │   ├── RideStatus.java               # Enum: REQUESTED, ASSIGNED, COMPLETED, CANCELLED
│   │   └── VehicleType.java              # Enum: BIKE, AUTO, CAR
│   ├── strategy/                           # Strategy Pattern implementation
│   │   ├── RideMatchingStrategy.java      # Interface
│   │   ├── NearestDriverStrategy.java     # Implementation
│   │   ├── LeastActiveDriverStrategy.java # Implementation
│   │   ├── FareStrategy.java              # Interface
│   │   ├── DefaultFareStrategy.java       # Implementation
│   │   └── PeakHourFareStrategy.java      # Implementation
│   ├── service/                            # Business Logic Layer
│   │   ├── RiderService.java
│   │   ├── DriverService.java
│   │   └── RideService.java
│   ├── exception/                          # Custom Exceptions
│   │   └── NoDriverAvailableException.java
│   └── util/                               # Utility Classes
│       └── IdGenerator.java
│   └── RideWiseTest.java                   # Unit Tests
docs/
├── Requirements.md
├── Class_Model.md
├── SOLID_Reflection.md
└── Object_Relationships.md

```

## Key Features

### 1. Rider Management
- Register riders with name and location
- Retrieve rider information
- Update rider location

### 2. Driver Management
- Register drivers with vehicle type
- Track driver availability
- Monitor completed rides
- Update driver location

### 3. Ride Management
- Request rides with distance
- Automatic driver assignment using strategies
- Fare calculation using strategies
- Track ride status through lifecycle
- Complete or cancel rides

### 4. Ride Status Lifecycle
```
REQUESTED → ASSIGNED → COMPLETED
         ↘ CANCELLED ↗
```

## Design Principles Implemented

### SOLID Principles

#### Single Responsibility Principle (SRP)
- Each service class has a single, well-defined responsibility
- `RiderService` handles only rider operations
- `DriverService` handles only driver operations
- `RideService` manages ride lifecycle

#### Open/Closed Principle (OCP)
- New strategies can be added without modifying existing code
- Strategy interfaces allow extension: `RideMatchingStrategy`, `FareStrategy`
- Main.java can easily switch between different strategies

#### Liskov Substitution Principle (LSP)
- All `RideMatchingStrategy` implementations are interchangeable
- All `FareStrategy` implementations are interchangeable
- Strategies can be swapped at runtime

#### Interface Segregation Principle (ISP)
- Small, focused interfaces: `RideMatchingStrategy`, `FareStrategy`
- Classes implement only the methods they need
- Clear contracts for each interface

#### Dependency Inversion Principle (DIP)
- `RideService` depends on interfaces, not concrete classes
- Strategies are injected through constructor
- `DriverService` is injected into `RideService`

### Design Patterns

#### Strategy Pattern
- **Ride Matching**: Choose between different driver selection algorithms
    - `NearestDriverStrategy`: Select the first available driver
    - `LeastActiveDriverStrategy`: Select driver with fewest completed rides

- **Fare Calculation**: Choose between different pricing models
    - `DefaultFareStrategy`: Base fare + per-km charge
    - `PeakHourFareStrategy`: Applies surge multiplier during peak hours

#### Composition over Inheritance
- `Ride` composes `Rider` and `Driver` (not inheritance)
- `Ride` composes `FareReceipt`
- Services collaborate through composition

### Other Principles

#### Law of Demeter
- Services communicate directly with collaborators
- No deep method chains or unnecessary coupling
- Each class only knows about its immediate dependencies

#### DRY (Don't Repeat Yourself)
- Centralized ID generation in `IdGenerator`
- Shared business logic in service layer
- Strategy logic encapsulated in separate classes

#### KISS (Keep It Simple, Stupid)
- Simple entity modeling
- Clear, straightforward implementations
- Minimal code, maximum clarity

#### YAGNI (You Aren't Gonna Need It)
- MVP focus: only features that are specified
- No premature optimization
- No unnecessary abstractions

## Features Demonstration

### Fare Calculation
**Default Strategy:**
```
Fare = Base Fare (50) + (Distance × Per-km Charge (10))
For 10 km: 50 + (10 × 10) = Rs. 150
For 5 km: 50 + (5 × 10) = Rs. 100
```

**Peak Hour Strategy:**
```
Applies 1.5x multiplier during peak hours (8-10 AM, 6-8 PM)
For 10 km during peak: (50 + 100) × 1.5 = Rs. 225
```

### Driver Matching

**Nearest Driver Strategy:**
- Selects the first available driver
- Simple, efficient approach

**Least Active Driver Strategy:**
- Selects driver with minimum completed rides
- Ensures fair distribution of work

## Quick Start

### Prerequisites
- **Java 17+** installed
- Any IDE: IntelliJ IDEA, VS Code, Eclipse, or any text editor

### Setup & Run

1. **Install Java 17** from [Azul](https://www.azul.com/downloads/)

2. **Open the project** in your IDE:
  - IntelliJ: File → Open → select `RideWise` folder
  - VS Code: File → Open Folder → select `RideWise` folder
  - Eclipse: File → Open Projects from File System → select `RideWise`

3. **Run Main.java:**
  - Open `src/com/airtribe/RideWise/Main.java`
  - Click Run button or press Ctrl+F5

## Console Menu Options

```
========== RIDEWISE MENU ==========
1. Add Rider          - Register new rider
2. Add Driver         - Register new driver
3. View Available Drivers - List all available drivers
4. Request Ride       - Book a ride with automatic driver assignment
5. Complete Ride      - Mark ride as completed
6. View All Rides     - Display all rides with status
7. Exit               - Quit application
```

## Usage Example

```
========== RIDEWISE MENU ==========
Enter your choice: 1
Enter rider name: Alice
Enter rider location: Downtown
Rider registered successfully: Rider{id='R1000', name='Alice', location='Downtown'}

========== RIDEWISE MENU ==========
Enter your choice: 2
Enter driver name: Driver1
Enter driver location: Midtown
Select vehicle type: 1. BIKE, 2. AUTO, 3. CAR
Enter choice: 3
Driver registered successfully: Driver{id='D2000', name='Driver1', currentLocation='Midtown', available=true, vehicleType=CAR, completedRides=0}

========== RIDEWISE MENU ==========
Enter your choice: 4
Enter rider ID: R1000
Enter distance (in km): 10
Driver assigned to ride: Ride{id='RD3000', rider=Rider{id='R1000', name='Alice', location='Downtown'}, driver=Driver{id='D2000', name='Driver1', currentLocation='Midtown', available=false, vehicleType=CAR, completedRides=0}, distance=10.0, status=ASSIGNED, fareReceipt=FareReceipt{rideId='RD3000', amount=150.0, ...}}
Fare: Rs. 150.0
```

## Architecture Highlights

### Separation of Concerns
- **Model Layer**: Pure domain entities
- **Strategy Layer**: Algorithm implementations
- **Service Layer**: Business logic and orchestration
- **UI Layer**: Console menu in Main.java

### Dependency Management
- All dependencies are explicitly declared
- Constructor injection for strategies and services
- No global state or singletons

### Extensibility
- Add new ride matching strategies by implementing `RideMatchingStrategy`
- Add new fare strategies by implementing `FareStrategy`
- Services remain unchanged when adding new strategies

### Testability
- Services are independently testable
- Strategies can be tested in isolation
- Mock-friendly dependency injection

## Future Enhancements

- Database persistence (remove in-memory storage)
- Real GPS-based distance calculation
- Rating system for drivers/riders
- Payment integration
- Real-time notifications
- Advanced matching algorithms (machine learning)
- Multiple language support
- Mobile application

## Author
Jambunatha Koni

