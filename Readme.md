## Setup

1. Clone the repo flask-bank-backend https://github.com/anpa6841/flask-bank-backend and follow the  setup instructions

2. Launch Android Studio and register a device in the emulator for launching the app. 
   For e.g Android 7.1.1 ("Nougat") | arm64 for Mac M1.

3. In APIClient.java, replace API_SERVER with your flask server's ip address and port.
   
   e.g `https://<ip>:<port>`

   Note: Assign the private IP address instead of 127.0.0.1 as a workaround if Android Studio blocks
         traffic on localhost.

4. Default test user accounts created: [username:sa-1, password:sa-1], [username:sa-2, password:sa-2]

5. E2E Tests Repo: https://github.com/anpa6841/androidTests

Screenshots

### Login

<img src="./screenshots/login.png" alt="drawing" width="300"/>

### List Accounts

<img src="./screenshots/accounts.png" alt="drawing" width="300"/>


### List Transactions

<img src="./screenshots/transactions.png" alt="drawing" width="300"/>

### Paginated View

<img src="./screenshots/paginated_view.png" alt="drawing" width="300" />

### Filter By Date

<img src="./screenshots/filter_by_date.png" alt="drawing" width="300"/>

### Filter By Amount

<img src="./screenshots/filter_by_amount.png" alt="drawing" width="300"/>

### Filter By Keyword

<img src="./screenshots/filter_by_keyword.png" alt="drawing" width="300"/>

### Transfer Funds

<img src="./screenshots/transfer.png" alt="drawing" width="300"/>
