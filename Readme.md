# Moon Project

Dev ROOT password = "root"

To run coverage execute this command `mvn jacoco:prepare-agent test install jacoco:report`

# Running the project
To start the project, the environment variables must be added to your system. To make things easier, you can add a .env file in resources with the following variables.
- JWT_SECRET=YOUR_SECRET `Example: 80b629299fcdbf9dabfcdeaaafd668c3f626e3e7d4f180b54cdef46aadbd1dc5`
- JWT_EXPIRATION_TIME=YOUR_EXPIRATION_TIME `Example: 60`

# Role list
- ROOT
- ADMIN
- USER
- LIST_[RESOURCE] `Example: LIST_USER`
- FIND_[RESOURCE] `Example: FIND_USER`
- REGISTER_[RESOURCE] `Example: REGISTER_USER`
- UPDATE_[RESOURCE] `Example: UPDATE_USER`
- DELETE_[RESOURCE] `Example: DELETE_USER`
