package com.airtribe.ridewise;

import com.airtribe.ridewise.enums.VehicleType;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private RiderService riderService;
    private DriverService driverService;
    private RideService rideService;
    private Scanner scanner;

    public Main() {
        this.riderService = new RiderService();
        this.driverService = new DriverService();
        // Initialize RideService with NearestDriverStrategy and DefaultFareStrategy
        this.rideService = new RideService(new NearestDriverStrategy(), new DefaultFareStrategy(), driverService);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addRider();
                        break;
                    case "2":
                        addDriver();
                        break;
                    case "3":
                        viewAvailableDrivers();
                        break;
                    case "4":
                        requestRide();
                        break;
                    case "5":
                        completeRide();
                        break;
                    case "6":
                        viewRides();
                        break;
                    case "7":
                        running = false;
                        System.out.println("Exiting RideWise. Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n========== RIDEWISE MENU ==========");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View All Rides");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addRider() {
        System.out.print("Enter rider name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter rider location: ");
        String location = scanner.nextLine().trim();

        Rider rider = riderService.registerRider(name, location);
        System.out.println("Rider registered successfully: " + rider);
    }

    private void addDriver() {
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter driver location: ");
        String location = scanner.nextLine().trim();
        System.out.println("Select vehicle type: 1. BIKE, 2. AUTO, 3. CAR");
        System.out.print("Enter choice: ");
        String vehicleChoice = scanner.nextLine().trim();

        VehicleType vehicleType = switch (vehicleChoice) {
            case "1" -> VehicleType.BIKE;
            case "2" -> VehicleType.AUTO;
            case "3" -> VehicleType.CAR;
            default -> {
                System.out.println("Invalid vehicle type. Defaulting to CAR.");
                yield VehicleType.CAR;
            }
        };

        Driver driver = driverService.registerDriver(name, location, vehicleType);
        System.out.println("Driver registered successfully: " + driver);
    }

    private void viewAvailableDrivers() {
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            System.out.println("No drivers available at the moment.");
        } else {
            System.out.println("\n========== AVAILABLE DRIVERS ==========");
            availableDrivers.forEach(System.out::println);
        }
    }

    private void requestRide() {
        System.out.print("Enter rider ID: ");
        String riderId = scanner.nextLine().trim();

        Optional<Rider> riderOpt = riderService.getRiderById(riderId);
        if (riderOpt.isEmpty()) {
            System.out.println("Rider not found.");
            return;
        }

        System.out.print("Enter distance (in km): ");
        try {
            double distance = Double.parseDouble(scanner.nextLine().trim());

            Ride ride = rideService.requestRide(riderOpt.get(), distance);
            System.out.println("Ride requested: " + ride);

            // Try to assign a driver
            ride = rideService.assignDriver(ride.getId());
            System.out.println("Driver assigned to ride: " + ride);
            System.out.println("Fare: Rs. " + ride.getFareReceipt().getAmount());
        } catch (NumberFormatException e) {
            System.out.println("Invalid distance format.");
        } catch (NoDriverAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void completeRide() {
        System.out.print("Enter ride ID: ");
        String rideId = scanner.nextLine().trim();

        try {
            Ride ride = rideService.completeRide(rideId);
            System.out.println("Ride completed: " + ride);
        } catch (NoDriverAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewRides() {
        List<Ride> allRides = rideService.getAllRides();
        if (allRides.isEmpty()) {
            System.out.println("No rides available.");
        } else {
            System.out.println("\n========== ALL RIDES ==========");
            allRides.forEach(ride -> {
                System.out.println(ride);
                System.out.println("---");
            });
        }
    }
}
