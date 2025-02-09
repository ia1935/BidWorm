# Bidworm - Student-Exclusive Marketplace

Bidworm is a secure and verified marketplace designed exclusively for students to safely buy, sell, and meet on campus. While it currently supports only FSU students, the platform is built to be a student-exclusive marketplace that will eventually expand to include other universities.

## Features

- **Student Verification**: Only verified students can buy and sell on the platform.
- **Secure Transactions**: Payment is processed only upon the completion of a meetup.
- **Seller-Buyer Communication**: Allows communication between buyers and sellers regarding product details and transaction information within the app.
- **Meetups**: Each transaction is associated with a specific location and time for safe in-person exchanges.
- **Offer Management**: Users can create offers, accept or reject offers, and track the status of their transactions (`pending`, `confirmed`, `completed`).
- **Strike System**: Users can be reported, and repeated offenders face consequences with a 3-strikes-and-out system.
- **Ban/Removal System**: Admins can remove or ban users who violate platform rules.

## Installation

To run Bidworm locally, follow these steps:

1. Clone the repository:
   ```bash
    git clone https://github.com/your-username/bidworm.git

2. Navigate to the project directory:
    ```bash
    cd bidworm
    ```
3. Install dependencies for backend (Spring-Boot)
    ```bash
    ./mvnw install
    ```
4. Configure your environment variables
   - Configure database credentials, 
   email settings, and any other necessary configuration in the
   application.properties file.
   - Sensitive information (keys,passwords,etc.) go in 
   env.properties file in the same directory as
   application.properties


5. Start the server:
    ```bash
    ./mvnw spring-boot:run
    ```

Usage
- User Registration: Users must sign up using their student email to begin the verification process.
- Creating Offers: Users can create new offers for items they want to sell, including setting price, description, and meetup location.
- Accepting Offers: Users can browse and accept offers from other students.
- Transaction Status: After meeting, users can mark their transaction as completed, and the payment will be processed.

# License:
## This project is licensed under the MIT License.