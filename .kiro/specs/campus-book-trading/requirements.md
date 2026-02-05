# Requirements Document: Campus Second-Hand Book Trading Platform

## Introduction

The Campus Second-Hand Book Trading Platform (校园二手书交易平台) is a web-based application designed to facilitate the buying and selling of used textbooks among university students. The platform enables students to list books for sale, browse available books, manage their inventory, and complete transactions. The system prioritizes ease of use with sufficient pages for navigation and clickable page transitions, built with Python using SQLite3 as the database backend.

## Glossary

- **Platform**: The Campus Second-Hand Book Trading Platform web application
- **User**: A student registered on the platform who can buy or sell books
- **Seller**: A user who lists books for sale
- **Buyer**: A user who purchases books from sellers
- **Book Listing**: A record of a book available for sale with details like title, author, price, condition
- **Transaction**: A completed or pending purchase of a book
- **Book Condition**: The physical state of a book (New, Like New, Good, Fair, Poor)
- **SQLite3**: Lightweight embedded SQL database used for data persistence
- **Page Navigation**: Clickable links and buttons that transition between different pages

## Requirements

### Requirement 1: User Registration and Authentication

**User Story:** As a student, I want to register an account and log in to the platform, so that I can access my personal book listings and purchase history.

#### Acceptance Criteria

1. WHEN a user accesses the registration page, THE Platform SHALL display a form requesting username, email, password, and student ID
2. WHEN a user submits valid registration data, THE Platform SHALL create a new user account and store credentials in SQLite3
3. WHEN a user enters correct login credentials, THE Platform SHALL authenticate the user and grant access to the dashboard
4. WHEN a user enters incorrect login credentials, THE Platform SHALL display an error message and deny access
5. WHILE a user is logged in, THE Platform SHALL maintain session state across page navigation

### Requirement 2: Book Listing Management

**User Story:** As a seller, I want to create, edit, and delete book listings, so that I can manage my inventory of books for sale.

#### Acceptance Criteria

1. WHEN a seller accesses the "Create Listing" page, THE Platform SHALL display a form for book title, author, ISBN, price, condition, and description
2. WHEN a seller submits a new book listing, THE Platform SHALL store the listing in SQLite3 with the seller's user ID and creation timestamp
3. WHEN a seller views their listings, THE Platform SHALL display all books they have listed with edit and delete options
4. WHEN a seller clicks the edit button, THE Platform SHALL load the listing details and allow modification of all fields
5. WHEN a seller clicks the delete button, THE Platform SHALL remove the listing from the database and display a confirmation message

### Requirement 3: Book Browsing and Search

**User Story:** As a buyer, I want to browse available books and search by title or author, so that I can find books I want to purchase.

#### Acceptance Criteria

1. WHEN a user accesses the "Browse Books" page, THE Platform SHALL display all available book listings with pagination
2. WHEN a user enters a search term in the search box, THE Platform SHALL filter books by title or author matching the search query
3. WHEN a user clicks on a book listing, THE Platform SHALL navigate to a detailed book page showing full information and seller contact details
4. WHEN a user applies category filters, THE Platform SHALL display only books matching the selected criteria
5. WHILE browsing, THE Platform SHALL display book condition, price, and seller rating on each listing card

### Requirement 4: Shopping Cart and Purchase

**User Story:** As a buyer, I want to add books to a shopping cart and complete purchases, so that I can buy multiple books efficiently.

#### Acceptance Criteria

1. WHEN a buyer clicks "Add to Cart" on a book listing, THE Platform SHALL add the book to the user's shopping cart in SQLite3
2. WHEN a buyer accesses the shopping cart page, THE Platform SHALL display all items with quantities, prices, and total cost
3. WHEN a buyer modifies quantities in the cart, THE Platform SHALL update the total price and persist changes to the database
4. WHEN a buyer clicks "Checkout", THE Platform SHALL create a transaction record and update book listing status to sold
5. WHEN a transaction is completed, THE Platform SHALL send confirmation details to both buyer and seller

### Requirement 5: User Dashboard and Profile

**User Story:** As a user, I want to view my profile, purchase history, and sales history, so that I can track my transactions and manage my account.

#### Acceptance Criteria

1. WHEN a user accesses their dashboard, THE Platform SHALL display their profile information, active listings, and recent transactions
2. WHEN a user views their purchase history, THE Platform SHALL show all books they have bought with dates and prices
3. WHEN a user views their sales history, THE Platform SHALL show all books they have sold with dates and prices
4. WHEN a user clicks on a transaction, THE Platform SHALL display detailed information including buyer/seller contact and delivery status
5. WHILE on the dashboard, THE Platform SHALL provide clickable navigation to all major sections of the application

### Requirement 6: Page Navigation and UI

**User Story:** As a user, I want to navigate between pages easily with clickable links and buttons, so that I can access all features of the platform without confusion.

#### Acceptance Criteria

1. WHEN a user is on any page, THE Platform SHALL display a navigation menu with links to Home, Browse, My Listings, Cart, and Profile
2. WHEN a user clicks a navigation link, THE Platform SHALL transition to the corresponding page without errors
3. WHEN a user clicks a button or link, THE Platform SHALL provide visual feedback indicating the action was registered
4. WHEN a user navigates between pages, THE Platform SHALL maintain the user's session and login state
5. WHEN a user accesses the home page, THE Platform SHALL display featured listings, recent books, and quick navigation options

### Requirement 7: Data Persistence with SQLite3

**User Story:** As a system, I need to store and retrieve all user and book data reliably, so that the platform maintains data integrity across sessions.

#### Acceptance Criteria

1. WHEN the application starts, THE Platform SHALL initialize SQLite3 database with required tables for users, books, transactions, and carts
2. WHEN user data is modified, THE Platform SHALL immediately persist changes to the SQLite3 database
3. WHEN the application retrieves data, THE Platform SHALL query SQLite3 and return accurate results within acceptable performance
4. IF a database error occurs, THEN THE Platform SHALL log the error and display a user-friendly message
5. WHILE the application runs, THE Platform SHALL maintain database connections and handle concurrent access safely

### Requirement 8: Responsive Page Layout

**User Story:** As a user, I want the platform to display properly on different screen sizes, so that I can access it from various devices.

#### Acceptance Criteria

1. WHEN a user accesses the platform on a desktop, THE Platform SHALL display a full-width layout with all features visible
2. WHEN a user accesses the platform on a mobile device, THE Platform SHALL display a responsive layout with stacked elements
3. WHEN a user resizes their browser window, THE Platform SHALL adjust the layout dynamically without breaking functionality
4. WHEN a user navigates between pages, THE Platform SHALL maintain consistent styling and layout across all pages
5. WHILE viewing listings, THE Platform SHALL display book information clearly on all screen sizes

