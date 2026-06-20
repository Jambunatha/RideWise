package com.airtribe.ridewise;

import com.airtribe.ridewise.enums.RideStatus;
import com.airtribe.ridewise.enums.VehicleType;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;

import java.util.List;

public class RideWiseTest {
    private RiderService riderService;
    private DriverService driverService;
    private RideService rideService;
    private int testsPassed = 0;
    private int testsFailed = 0;

    public RideWiseTest() {
        this.riderService = new RiderService();
        this.driverService = new DriverService();
        this.rideService = new RideService(
                new NearestDriverStrategy(),
                new DefaultFareStrategy(),
                driverService
        );
    }

    public static void main(String[] args) {
        RideWiseTest test = new RideWiseTest();
        test.runAllTests();
    }

    public void runAllTests() {
        System.out.println("========================================");
        System.out.println("RideWise Unit Tests");
        System.out.println("========================================\n");

        testRiderRegistration();
        testDriverRegistration();
        testAvailableDrivers();
        testRideRequest();
        testFareCalculation();
        testRideCompletion();
        testStrategies();
        testExceptions();

        printTestSummary();
    }

    private void testRiderRegistration() {
        System.out.println("Test 1: Rider Registration");
        try {
            Rider rider = riderService.registerRider("John Doe", "Location A");
            assertCondition(rider != null && rider.getName().equals("John Doe"), "Rider details mismatch");
            assertCondition(rider.getId().startsWith("R"), "Rider id format mismatch");
            testPassed("Rider registered successfully");
        } catch (Exception e) {
            testFailed("Rider registration failed: " + e.getMessage());
        }
    }

    private void testDriverRegistration() {
        System.out.println("\nTest 2: Driver Registration");
        try {
            Driver driver = driverService.registerDriver("Jane Smith", "Location B", VehicleType.CAR);
            assertCondition(driver != null && driver.getName().equals("Jane Smith"), "Driver details mismatch");
            assertCondition(driver.getId().startsWith("D"), "Driver id format mismatch");
            assertCondition(driver.isAvailable(), "New driver must be available");
            testPassed("Driver registered successfully");
        } catch (Exception e) {
            testFailed("Driver registration failed: " + e.getMessage());
        }
    }

    private void testAvailableDrivers() {
        System.out.println("\nTest 3: Available Drivers");
        try {
            driverService.registerDriver("Driver A", "Location C", VehicleType.BIKE);
            driverService.registerDriver("Driver B", "Location D", VehicleType.AUTO);
            List<Driver> availableDrivers = driverService.getAvailableDrivers();
            assertCondition(availableDrivers.size() >= 2, "Expected at least two available drivers");
            testPassed("Available drivers retrieved: " + availableDrivers.size());
        } catch (Exception e) {
            testFailed("Available drivers test failed: " + e.getMessage());
        }
    }

    private void testRideRequest() {
        System.out.println("\nTest 4: Ride Request and Assignment");
        try {
            Rider rider = riderService.registerRider("Test Rider", "Test Location");
            driverService.registerDriver("Test Driver", "Driver Location", VehicleType.CAR);

            Ride ride = rideService.requestRide(rider, 5.0);
            assertCondition(ride != null && ride.getStatus() == RideStatus.REQUESTED, "Ride should start as REQUESTED");

            ride = rideService.assignDriver(ride.getId());
            assertCondition(ride.getStatus() == RideStatus.ASSIGNED, "Ride should be ASSIGNED after matching");
            assertCondition(ride.getDriver() != null, "Assigned ride must have a driver");
            testPassed("Ride requested and driver assigned");
        } catch (Exception e) {
            testFailed("Ride request failed: " + e.getMessage());
        }
    }

    private void testFareCalculation() {
        System.out.println("\nTest 5: Fare Calculation");
        try {
            Rider rider = riderService.registerRider("Fare Test Rider", "Location");
            driverService.registerDriver("Fare Test Driver", "Location", VehicleType.CAR);

            Ride ride = rideService.requestRide(rider, 10.0);
            ride = rideService.assignDriver(ride.getId());

            // Default fare: Base (50) + Distance (10) * Per-km (10) = 150
            double expectedFare = 50 + (10 * 10);
            assertCondition(ride.getFareReceipt().getAmount() == expectedFare, "Fare mismatch for default strategy");
            testPassed("Fare calculated correctly: Rs. " + ride.getFareReceipt().getAmount());
        } catch (Exception e) {
            testFailed("Fare calculation failed: " + e.getMessage());
        }
    }

    private void testRideCompletion() {
        System.out.println("\nTest 6: Ride Completion");
        try {
            Rider rider = riderService.registerRider("Completion Test Rider", "Location");
            Driver driver = driverService.registerDriver("Completion Test Driver", "Location", VehicleType.CAR);
            int initialCompletedRides = driver.getCompletedRides();

            Ride ride = rideService.requestRide(rider, 5.0);
            ride = rideService.assignDriver(ride.getId());
            assertCondition(!driver.isAvailable(), "Driver should be unavailable while ride is active");

            ride = rideService.completeRide(ride.getId());
            assertCondition(ride.getStatus() == RideStatus.COMPLETED, "Ride should be COMPLETED");

            Driver updatedDriver = driverService.getDriverById(driver.getId()).get();
            assertCondition(updatedDriver.isAvailable(), "Driver should be available after completion");
            assertCondition(updatedDriver.getCompletedRides() == initialCompletedRides + 1, "Completed rides counter mismatch");
            testPassed("Ride completed and driver availability updated");
        } catch (Exception e) {
            testFailed("Ride completion failed: " + e.getMessage());
        }
    }

    private void testStrategies() {
        System.out.println("\nTest 7: Ride Matching Strategies");
        try {
            // Test NearestDriverStrategy
            Rider rider = riderService.registerRider("Strategy Test", "Location");
            Driver driver1 = driverService.registerDriver("Driver Nearest", "Location 1", VehicleType.BIKE);
            Driver driver2 = driverService.registerDriver("Driver Far", "Location 2", VehicleType.AUTO);

            NearestDriverStrategy strategy = new NearestDriverStrategy();
            List<Driver> availableDrivers = driverService.getAvailableDrivers();
            Driver selectedDriver = strategy.findDriver(rider, availableDrivers);

            assertCondition(selectedDriver != null, "Nearest strategy should return a driver");
            testPassed("NearestDriverStrategy selected: " + selectedDriver.getName());

            // Test LeastActiveDriverStrategy
            Ride ride = rideService.requestRide(rider, 5.0);
            rideService.assignDriver(ride.getId());
            rideService.completeRide(ride.getId());

            LeastActiveDriverStrategy leastActiveStrategy = new LeastActiveDriverStrategy();
            availableDrivers = driverService.getAvailableDrivers();
            selectedDriver = leastActiveStrategy.findDriver(rider, availableDrivers);
            assertCondition(selectedDriver != null, "Least-active strategy should return a driver");
            testPassed("LeastActiveDriverStrategy selected driver with least rides");

        } catch (Exception e) {
            testFailed("Strategies test failed: " + e.getMessage());
        }
    }

    private void testExceptions() {
        System.out.println("\nTest 8: Exception Handling");
        try {
            // Test NoDriverAvailableException
            Rider rider = riderService.registerRider("Exception Test", "Location");

            try {
                Ride ride = rideService.requestRide(rider, 5.0);
                // Mark all drivers as unavailable
                List<Driver> allDrivers = driverService.getAllDrivers();
                for (Driver driver : allDrivers) {
                    driverService.updateDriverAvailability(driver.getId(), false);
                }

                rideService.assignDriver(ride.getId());
                testFailed("NoDriverAvailableException not thrown");
            } catch (NoDriverAvailableException e) {
                testPassed("NoDriverAvailableException thrown correctly: " + e.getMessage());
            }
        } catch (Exception e) {
            testFailed("Exception handling test failed: " + e.getMessage());
        }
    }

    private void testPassed(String message) {
        System.out.println("  ✓ PASSED: " + message);
        testsPassed++;
    }

    private void testFailed(String message) {
        System.out.println("  ✗ FAILED: " + message);
        testsFailed++;
    }

    private void printTestSummary() {
        System.out.println("\n========================================");
        System.out.println("Test Summary");
        System.out.println("========================================");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("Success Rate: " + (testsPassed * 100 / (testsPassed + testsFailed)) + "%");
        System.out.println("========================================");
        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
