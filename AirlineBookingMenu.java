import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class Flight {
    private String flightNumber;
    private String destination;

    public Flight(String flightNumber, String destination) {
        this.flightNumber = flightNumber;
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Flight{" + "flightNumber='" + flightNumber + '\'' + ", destination='" + destination + '\'' + '}';
    }
}

class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Passenger{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}

class Booking<T extends Flight, U extends Passenger> {
    private T flight;
    private U passenger;

    public Booking(T flight, U passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }

    public T getFlight() {
        return flight;
    }

    public U getPassenger() {
        return passenger;
    }

    @Override
    public String toString() {
        return "Booking{" + "flight=" + flight + ", passenger=" + passenger + '}';
    }
}

enum MenuOption {
    PRINT_BOOKINGS(1),
    PRINT_FLIGHT_DETAILS(2),
    PRINT_ADULT_PASSENGERS(3),
    GET_DEFAULT_FLIGHT(4),
    CREATE_BOOKING(5),
    EXIT(6);

    private final int option;

    MenuOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }

    public static MenuOption fromOption(int option) {
        for (MenuOption menuOption : values()) {
            if (menuOption.getOption() == option) {
                return menuOption;
            }
        }
        return null;
    }
}

public class AirlineBookingMenu {
    public static void main(String[] args) {
        List<Booking<Flight, Passenger>> bookings = new ArrayList<>();

        Consumer<Booking<Flight, Passenger>> printBookingDetails = booking -> System.out.println("Booking: " + booking);
        Function<Booking<Flight, Passenger>, String> getFlightDetails = booking -> booking.getFlight().toString();
        Predicate<Passenger> isAdult = passenger -> passenger.getAge() >= 18;
        Supplier<Flight> defaultFlightSupplier = () -> new Flight("DF123", "Default Destination");

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu:");
            for (MenuOption option : MenuOption.values()) {
                System.out.println(option.getOption() + ". " + option.name().replace('_', ' '));
            }
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            MenuOption selectedOption = MenuOption.fromOption(choice);
            if (selectedOption == null) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (selectedOption) {
                case PRINT_BOOKINGS:
                    bookings.forEach(printBookingDetails);
                    break;
                case PRINT_FLIGHT_DETAILS:
                    bookings.stream().map(getFlightDetails).forEach(System.out::println);
                    break;
                case PRINT_ADULT_PASSENGERS:
                    bookings.stream()
                            .map(Booking::getPassenger)
                            .filter(isAdult)
                            .forEach(passenger -> System.out.println("Adult Passenger: " + passenger));
                    break;
                case GET_DEFAULT_FLIGHT:
                    Flight defaultFlight = defaultFlightSupplier.get();
                    System.out.println("Default Flight: " + defaultFlight);
                    break;
                case CREATE_BOOKING:
                    System.out.print("Enter flight number: ");
                    String flightNumber = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();
                    Flight flight = new Flight(flightNumber, destination);

                    System.out.print("Enter passenger name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter passenger age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); 
                    Passenger passenger = new Passenger(name, age);

                    Booking<Flight, Passenger> newBooking = new Booking<>(flight, passenger);
                    bookings.add(newBooking);
                    System.out.println("New booking created: " + newBooking);
                    break;
                case EXIT:
                    System.out.println("Exiting...");
                    break;
            }
            System.out.println();
        } while (choice != MenuOption.EXIT.getOption());

        scanner.close();
    }
}