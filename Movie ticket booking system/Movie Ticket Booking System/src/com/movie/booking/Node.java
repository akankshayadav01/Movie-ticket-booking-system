package com.movie.booking;

public class Node {
    Node next;
    Node prev;
    int[][] seat = new int[10][7];
    int row, col;
    String name, pass;

    // Constructor initializes all seats to 0 (available)
    public Node() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                seat[i][j] = 0;
            }
        }
    }

    // Resets all seats to 0 (available)
    public void resetSeats() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                seat[i][j] = 0;
            }
        }
    }

    // Displays the seat layout
    public void displaySeats() {
        System.out.println("\nFollowing are the seats:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (seat[i][j] == 0)
                    System.out.print("\t| |");
                else
                    System.out.print("\t|B|");
            }
            System.out.println();
        }
    }

    // Books a seat based on user input
    public void bookSeat() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Enter row (0-9): ");
        this.row = sc.nextInt();
        System.out.print("Enter column (0-6): ");
        this.col = sc.nextInt();

        if (this.row < 0 || this.row >= 10 || this.col < 0 || this.col >= 7) {
            System.out.println("No such seat exists.");
            return;
        }

        if (seat[this.row][this.col] == 1) {
            System.out.println("Seat already booked.");
            return;
        }

        seat[this.row][this.col] = 1;
        System.out.println("Seat booked successfully.");
    }

    // Cancels a booked seat based on user credentials
    public void cancelBooking() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pas = sc.nextLine();

        if (this.name.equals(user) && this.pass.equals(pas)) {
            if (seat[this.row][this.col] == 1) {
                seat[this.row][this.col] = 0;
                System.out.println("Cancellation successful.");
            } else {
                System.out.println("No booking found to cancel.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // Checks out tickets for a user
    public void checkout(String userName) {
        int tickets = 0;
        Node current = this.next;
        while (current != null) {
            if (current.name.equals(userName)) {
                tickets++;
            }
            current = current.next;
        }
        System.out.println("Total tickets for " + userName + ": " + tickets);
    }
}
