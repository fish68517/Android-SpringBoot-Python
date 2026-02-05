# Implementation Plan: Campus Second-Hand Book Trading Platform

## Python Backend Setup

- [x] 1. Set up Python Flask project structure and dependencies


  - Create Flask project with virtual environment
  - Install required packages: Flask, Flask-CORS, SQLAlchemy, python-dotenv
  - Create project directory structure (app, models, routes, services, repositories)
  - _Requirements: 7.1_

- [x] 2. Initialize SQLite3 database and create tables

  - Create database initialization script
  - Define and create users table with fields: id, username, email, password_hash, student_id, created_at, updated_at
  - Define and create books table with fields: id, seller_id, title, author, isbn, price, condition, description, status, created_at, updated_at
  - Define and create cart_items table with fields: id, user_id, book_id, quantity, added_at
  - Define and create transactions table with fields: id, buyer_id, seller_id, book_id, price, status, delivery_address, created_at, updated_at
  - Insert mock data (4-8 records per table) with Chinese simplified text for testing
  - _Requirements: 7.1, 7.2_

- [x] 3. Implement authentication API endpoints

  - Create UserService with register_user, authenticate_user, get_user_by_id methods
  - Create UserRepository for database operations
  - Implement POST /api/auth/register endpoint
  - Implement POST /api/auth/login endpoint with token generation
  - Implement GET /api/auth/verify endpoint for token validation
  - _Requirements: 1.1, 1.2, 1.3, 1.4_

- [x] 4. Implement book listing API endpoints

  - Create BookService with CRUD operations
  - Create BookRepository for database queries
  - Implement GET /api/books endpoint with pagination and filtering
  - Implement GET /api/books/search endpoint for search functionality
  - Implement GET /api/books/<id> endpoint for book details
  - Implement POST /api/books endpoint for creating listings
  - Implement PUT /api/books/<id> endpoint for updating listings
  - Implement DELETE /api/books/<id> endpoint for deleting listings
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2, 3.3_

- [x] 5. Implement shopping cart API endpoints

  - Create CartService with cart operations
  - Create CartRepository for database operations
  - Implement GET /api/cart endpoint to retrieve user's cart
  - Implement POST /api/cart endpoint to add items to cart
  - Implement PUT /api/cart/<item_id> endpoint to update quantities
  - Implement DELETE /api/cart/<item_id> endpoint to remove items
  - Implement DELETE /api/cart endpoint to clear entire cart
  - _Requirements: 4.1, 4.2, 4.3_

- [x] 6. Implement transaction and checkout API endpoints

  - Create TransactionService for transaction operations
  - Create TransactionRepository for database queries
  - Implement POST /api/transactions endpoint for checkout
  - Implement GET /api/transactions/purchases endpoint for purchase history
  - Implement GET /api/transactions/sales endpoint for sales history
  - Implement GET /api/transactions/<id> endpoint for transaction details
  - Implement PUT /api/transactions/<id>/status endpoint for status updates
  - _Requirements: 4.4, 4.5, 5.2, 5.3, 5.4_

- [x] 7. Implement user profile API endpoints

  - Implement GET /api/users/profile endpoint to retrieve user profile
  - Implement PUT /api/users/profile endpoint to update profile information
  - Implement PUT /api/users/password endpoint to change password
  - _Requirements: 5.1, 5.5_

- [x] 8. Add error handling and logging to Python backend

  - Implement global exception handler for API errors
  - Add request/response logging
  - Create custom error response format
  - Add validation for all API inputs
  - _Requirements: 7.3, 7.4_

## Android Frontend Setup

- [x] 9. Set up Android Studio project and dependencies


  - Create new Android project with minimum SDK 21
  - Add Gradle dependencies: Retrofit, OkHttp, Gson, AndroidX libraries, Material Components
  - Create project package structure (activities, fragments, models, api, utils)
  - Configure AndroidManifest.xml with required permissions (INTERNET, READ_EXTERNAL_STORAGE)
  - Set up Material Design 3 theme with color scheme (primary blue, secondary teal, tertiary orange)
  - Configure Material Components library for Material Design UI
  - _Requirements: 6.1, 6.2_

- [x] 10. Create data models and API client interfaces

  - Create User, Book, CartItem, Transaction data classes
  - Create AuthResponse, LoginRequest, RegisterRequest classes
  - Create Retrofit service interfaces: UserApiClient, BookApiClient, CartApiClient, TransactionApiClient
  - Configure Retrofit instance with base URL (localhost:5000)
  - Create API client manager singleton
  - _Requirements: 3.1, 3.2, 3.3_

- [x] 11. Implement authentication screens (Splash, Login, Register)

  - Create SplashActivity with auto-navigation logic and Material Design splash screen
  - Create LoginActivity with Material Design TextInputLayout for email/password input
  - Create RegisterActivity with Material Design form fields (username, email, password, student_id)
  - Implement form validation with Material error messages
  - Add error message display using Material Snackbar for failed authentication
  - Store authentication token in SharedPreferences
  - Apply Material Design 3 theme with proper colors and typography
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5_



- [x] 12. Implement home screen and navigation





  - Create HomeActivity with Material Design bottom navigation menu
  - Add navigation menu items: Browse, Sell, Cart, Profile with Material Icons
  - Implement featured books carousel with Material CardView
  - Add quick search bar with Material SearchView
  - Create Material navigation drawer or bottom navigation bar
  - Implement screen transitions between all main sections with Material transitions
  - Apply Material Design 3 theme with proper spacing and elevation
  - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

- [x] 13. Implement browse books screen with search and filter





  - Create BrowseActivity with Material RecyclerView for book listings
  - Implement BookAdapter with Material CardView for each book item
  - Add search functionality with Material SearchView and API integration
  - Add filter options (condition, price range) with Material Chip components
  - Add sort options (newest, price, rating) with Material menu
  - Implement pagination/infinite scroll with Material ProgressBar loading indicator
  - Add click listener to navigate to book detail screen with Material transitions
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

- [x] 14. Implement book detail screen





  - Create BookDetailActivity to display full book information with Material Design
  - Display book images in Material ImageView with proper aspect ratio
  - Display title, author, price, condition with Material Typography
  - Display seller profile card with Material CardView and rating
  - Implement "Add to Cart" button with Material Button style
  - Show related books section with Material RecyclerView
  - Add reviews/ratings section with Material RatingBar
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 3.1, 3.2, 3.3, 4.1_

- [x] 15. Implement shopping cart screen





  - Create CartActivity with Material RecyclerView for cart items
  - Implement CartAdapter with Material CardView for each cart item
  - Add quantity adjustment controls with Material buttons
  - Add remove item buttons with Material IconButton
  - Calculate and display subtotal, tax, total with Material Typography
  - Implement checkout button with Material Button style
  - Add continue shopping button with Material Button style
  - Show empty cart message with Material empty state design
  - _Requirements: 4.1, 4.2, 4.3_

- [x] 16. Implement checkout and transaction screens





  - Create CheckoutActivity for order confirmation with Material Design
  - Add delivery address input form with Material TextInputLayout
  - Add payment method selection with Material RadioButton group
  - Display order summary with Material CardView
  - Implement place order button with Material Button style
  - Show order success confirmation with Material Dialog
  - Navigate to dashboard after successful checkout with Material transitions
  - _Requirements: 4.4, 4.5_

- [x] 17. Implement create and edit listing screens





  - Create CreateListingActivity with Material Design form
  - Add fields with Material TextInputLayout: title, author, ISBN, price, condition spinner, description
  - Implement image picker with Material design
  - Add form validation with Material error messages
  - Implement submit button with Material Button style
  - Create EditListingActivity with pre-filled form data
  - Add update and delete buttons with Material Button styles
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_

- [x] 18. Implement my listings screen





  - Create MyListingsActivity with Material RecyclerView of user's listings
  - Implement ListingAdapter with Material CardView for each listing
  - Add edit button for each listing with Material IconButton
  - Add delete button with Material IconButton and confirmation dialog
  - Add create new listing button with Material FAB (Floating Action Button)
  - Display status indicator (active, sold) with Material Chip
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 2.3, 2.4, 2.5_

- [x] 19. Implement user dashboard screen





  - Create DashboardActivity with Material Design layout
  - Display user profile summary with Material CardView
  - Display quick stats (number of listings, purchases, sales) with Material cards
  - Show recent transactions with Material RecyclerView
  - Add navigation buttons to purchase history, sales history, profile with Material Button
  - Add logout button with Material Button style
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

- [x] 20. Implement purchase and sales history screens





  - Create PurchaseHistoryActivity with Material RecyclerView of purchases
  - Create SalesHistoryActivity with Material RecyclerView of sales
  - Implement TransactionAdapter with Material CardView for each transaction
  - Display transaction details (book, price, date, seller/buyer info) with Material Typography
  - Display delivery status with Material Chip
  - Add click listener to view full transaction details with Material transitions
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 5.2, 5.3, 5.4_

- [x] 21. Implement user profile screen





  - Create ProfileActivity to display user information with Material Design
  - Add edit profile button with Material Button style
  - Add change password option with Material Button
  - Add account settings with Material Switch components
  - Add logout button with Material Button style
  - Implement profile update functionality with Material TextInputLayout
  - Apply Material Design 3 theme with proper colors and spacing
  - _Requirements: 5.1, 5.5_

- [x] 22. Add error handling and user feedback to Android app









  - Implement global error handler for API failures
  - Add Material Snackbar messages for user feedback
  - Add Material ProgressBar loading indicators for API calls
  - Implement retry logic for failed requests with Material Dialog
  - Add network connectivity check with Material AlertDialog
  - Apply Material Design 3 theme to all dialogs and messages
  - _Requirements: 6.3, 6.4_

- [x] 23. Implement responsive layout for different screen sizes








  - Use Material ConstraintLayout for flexible layouts
  - Add landscape layout variants for key screens with Material design
  - Test on multiple screen sizes (phone 5-6", tablet 7-10")
  - Adjust font sizes and spacing for readability using Material typography scale
  - Apply Material Design 3 spacing guidelines (8dp base unit)
  - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

- [x] 24. Add session management and token persistence





  - Implement SharedPreferences for storing auth tokens
  - Add token refresh logic
  - Implement automatic logout on token expiration
  - Add session state management across screen transitions
  - _Requirements: 1.5, 6.4_

## Testing and Integration

- [ ]* 25. Write unit tests for Python backend services
  - Test UserService methods (register, authenticate, get user)
  - Test BookService methods (CRUD operations, search)
  - Test CartService methods (add, remove, update, calculate total)
  - Test TransactionService methods (create, retrieve)
  - _Requirements: 7.1, 7.2, 7.3_

- [ ]* 26. Write integration tests for Python API endpoints
  - Test authentication flow (register, login, verify)
  - Test book listing flow (create, read, update, delete)
  - Test shopping cart flow (add, update, remove, checkout)
  - Test transaction flow (create, retrieve history)
  - _Requirements: 7.1, 7.2, 7.3_

- [ ]* 27. Write unit tests for Android API clients
  - Test Retrofit service interfaces
  - Test API request/response handling
  - Test error handling and retry logic
  - _Requirements: 3.1, 3.2, 3.3_

- [ ]* 28. Perform end-to-end testing of complete user flows
  - Test user registration and login flow
  - Test book browsing and search flow
  - Test adding books to cart and checkout flow
  - Test creating and managing listings flow
  - Test viewing purchase and sales history
  - _Requirements: 1.1, 2.1, 3.1, 4.1, 5.1, 6.1_

- [ ]* 29. Test responsive layout on multiple devices
  - Test on phone (5-6 inch screens)
  - Test on tablet (7-10 inch screens)
  - Test in landscape and portrait orientations
  - Verify all buttons and inputs are accessible
  - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

## Documentation and Deployment

- [ ]* 30. Create API documentation
  - Document all API endpoints with request/response examples
  - Create setup guide for running Python backend
  - Document database schema
  - _Requirements: 7.1, 7.2_

- [ ]* 31. Create Android app documentation
  - Document app architecture and key classes
  - Create user guide for app features
  - Document setup instructions for Android Studio
  - _Requirements: 6.1, 6.2_

- [ ]* 32. Prepare deployment package
  - Create startup script for Python backend
  - Generate APK for Android app
  - Create README with setup and usage instructions
  - _Requirements: All_

