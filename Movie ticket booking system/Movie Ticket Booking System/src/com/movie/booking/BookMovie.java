package com.movie.booking;

import java.io.*;
import java.util.Scanner;

public class BookMovie {
    private Scanner sc = new Scanner(System.in);
    private Node[] movieBookings = new Node[1000]; // Assuming Movie-ID <= 999
    private final String ADMIN_PASSWORD = "admin";

    public BookMovie() {
        for (int i = 0; i < movieBookings.length; i++) {
            movieBookings[i] = new Node();
        }
    }

    // Clears the console (works in most terminals)
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Handle exception or ignore
        }
    }

    // Displays the first page with options
    public void firstPage() {
        while (true) {
            clearConsole();
            System.out.println("\t---------WELCOME---------");
            System.out.println("\t1. Admin Profile");
            System.out.println("\t2. SignUp");
            System.out.println("\t3. Login");
            System.out.print("\tEnter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    adminProfile();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    login();
                    break;
                default:
                    System.out.println("Invalid choice. Press Enter to try again.");
                    sc.nextLine();
                    break;
            }
        }
    }

    // Handles admin functionalities
    private void adminProfile() {
        clearConsole();
        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        if (password.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin logged in successfully. Press Enter to continue.");
            sc.nextLine();
            adminMenu();
        } else {
            System.out.println("Incorrect password. Press Enter to return to the main menu.");
            sc.nextLine();
        }
    }

    // Admin menu for managing movies
    private void adminMenu() {
        while (true) {
            clearConsole();
            System.out.println("----- Admin Menu -----");
            System.out.println("1. Insert Movie");
            System.out.println("2. View Movies");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    insertMovie();
                    break;
                case 2:
                    displayMovies();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Press Enter to try again.");
                    sc.nextLine();
                    break;
            }
        }
    }

    // Inserts a new movie into movies.txt
    private void insertMovie() {
        clearConsole();
        System.out.print("Enter Movie Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Movie-ID: ");
        int id = getIntInput();

        if (id <= 0 || id >= movieBookings.length) {
            System.out.println("Invalid Movie-ID. Press Enter to try again.");
            sc.nextLine();
            return;
        }

        // Check if Movie-ID already exists
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 1 && Integer.parseInt(parts[0]) == id) {
                    System.out.println("Movie-ID already exists. Press Enter to try again.");
                    sc.nextLine();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist, proceed to create it
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append the new movie to movies.txt
        try (FileWriter fw = new FileWriter("movies.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(id + "\t" + name);
            System.out.println("Movie inserted successfully. Press Enter to continue.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error inserting movie. Press Enter to continue.");
        }
        sc.nextLine();
    }

    // Displays all movies from movies.txt
    private void displayMovies() {
        clearConsole();
        System.out.println("----- List of Movies -----");
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            boolean hasMovies = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasMovies = true;
            }
            if (!hasMovies) {
                System.out.println("No movies available.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No movies available.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Press Enter to continue.");
        sc.nextLine();
    }

    // Handles user sign-up
    private void signUp() {
        clearConsole();
        System.out.println("\t---------SIGN-UP---------");
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter your Email-ID: ");
        String email = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        String filename = email + ".txt";

        // Check if user already exists
        File userFile = new File(filename);
        if (userFile.exists()) {
            System.out.println("User already exists. Press Enter to try logging in.");
            sc.nextLine();
            return;
        }

        // Create user file
        try (FileWriter fw = new FileWriter(filename);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(email);
            out.println(password);
            out.println(name);
            System.out.println("Successfully Registered. Press Enter to continue.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during registration. Press Enter to continue.");
        }
        sc.nextLine();
    }

    // Handles user login
    private void login() {
        clearConsole();
        System.out.println("\t---------LOGIN---------");
        System.out.print("Enter your Email-ID: ");
        String email = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        String filename = email + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String em = reader.readLine();
            String ps = reader.readLine();
            String name = reader.readLine();

            if (email.equals(em) && password.equals(ps)) {
                System.out.println("Successfully Logged In. Press Enter to continue.");
                sc.nextLine();
                book(email);
            } else {
                System.out.println("Invalid credentials. Press Enter to return to the main menu.");
                sc.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("User not found. Press Enter to try signing up.");
            sc.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during login. Press Enter to continue.");
            sc.nextLine();
        }
    }

    // Handles booking tickets for a user
    private void book(String email) {
        while (true) {
            clearConsole();
            System.out.println("\t---------BOOK MY SHOW---------");
            System.out.println("\t1. Show Movies");
            System.out.println("\t2. Book Ticket");
            System.out.println("\t3. Cancel Ticket");
            System.out.println("\t4. Logout");
            System.out.print("\tEnter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    displayMovies();
                    break;
                case 2:
                    bookTicket(email);
                    break;
                case 3:
                    cancelTicket(email);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Press Enter to try again.");
                    sc.nextLine();
                    break;
            }
        }
    }

    // Books a ticket for a specific movie
    private void bookTicket(String email) {
        clearConsole();
        System.out.print("Enter Movie-ID to book: ");
        int id = getIntInput();

        if (id <= 0 || id >= movieBookings.length) {
            System.out.println("Invalid Movie-ID. Press Enter to try again.");
            sc.nextLine();
            return;
        }

        // Check if movie exists
        boolean movieExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 1 && Integer.parseInt(parts[0]) == id) {
                    movieExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!movieExists) {
            System.out.println("Movie-ID does not exist. Press Enter to try again.");
            sc.nextLine();
            return;
        }

        System.out.print("Enter number of seats to book: ");
        int n = getIntInput();

        for (int i = 0; i < n; i++) {
            System.out.println("\nBooking seat " + (i + 1) + " of " + n);
            movieBookings[id].bookSeat();
        }

        // Save booking information to user's file
        try (FileWriter fw = new FileWriter(email + ".txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("Movie-ID: " + id + ", Seats Booked: " + n);
            System.out.println("Tickets booked successfully. Press Enter to continue.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error booking tickets. Press Enter to continue.");
        }
        sc.nextLine();
    }

    // Cancels a ticket for a user
    private void cancelTicket(String email) {
        clearConsole();
        System.out.print("Enter Movie-ID to cancel: ");
        int id = getIntInput();

        if (id <= 0 || id >= movieBookings.length) {
            System.out.println("Invalid Movie-ID. Press Enter to try again.");
            sc.nextLine();
            return;
        }

        // For simplicity, resetting all seats for the movie
        // You can enhance this to cancel specific seats based on user
        movieBookings[id].resetSeats();
        System.out.println("All bookings for Movie-ID " + id + " have been cancelled.");
        System.out.println("Press Enter to continue.");
        sc.nextLine();
    }

    // Utility method to get integer input with validation
    private int getIntInput() {
        while (true) {
            try {
                int num = sc.nextInt();
                sc.nextLine(); // Consume newline
                return num;
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.nextLine(); // Clear invalid input
            }
        }
    }
}

