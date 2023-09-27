# AndroidSpecificDataFetching_App

# Overview:

This Android app captures and displays various device-related data at regular intervals.
Data includes Timestamp, Capture Count, Frequency, Connectivity, Battery Charging, Battery Charge Percentage, and Location.
# Features:

* Timestamp: Displays the date and time of data capture at the top.
* Capture Count: Tracks the number of data refreshes within the current session. Session ends when the app is killed.
* Frequency (min): Allows users to set the frequency of automatic data and photo capture in minutes. Editable by tapping the data field.
* Connectivity: Indicates Internet connectivity status - ON or OFF.
* Battery Charging: Displays the battery charging status - ON or OFF.
* Battery Charge: Shows the percentage value of battery charge.
* Location: Displays the longitude and latitude of the captured location.
* Manual Data Refresh: A button allows users to manually refresh all data captured automatically.
* Data Storage and Firebase:All captured data is stored in Firebase, ensuring data retention without overwriting existing data in Firebase Storage or Firestore Database.
* Locally stored data: Data is retained even if the device restarts when network connectivity is unavailable.
* Low Battery Notification: A notification is sent to the mobile device if the battery charge falls below 20%.
