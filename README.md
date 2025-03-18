# PriceCam
This is a simple Android app for calculating the exact price per quantity unit basing on store's pricetags. 

>[!NOTE]
>All calculations are made in real-time and offline, the only source that is needed for the app is camera preview.

### Mechanics
The app uses Google MLKit TextRecognition to scan the targeted pricetag, retrieve text from it and start analyzing algorithm every 1,2 seconds.  
Then, 2 stages of analyzing follows:
- first, the app searches for the largest element in received image and assumes it as price, additionally checking if it matches pattern of 2 to 4 digits (price less than 10 and larger than 9999 is extremely rare in rouble zone).
- second, the app searches for Regex matches in every line of scanned text, assuming that a store's pricetag have predictable patterns like "200g" or "1,5kg" and this will be the quantity of the product.

After a successful scan, the app unites 2 results (price and quantity) and calculates price per quantity unit, either liter or kilogram (depending of scan result) and shows the rounded result to user on screen.

### Technology
- Google MLKit TextRecognition
- CameraX
- Jetpack Compose
- MVVM

### Video
