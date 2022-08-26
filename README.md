<p align="center">
  <img src="https://raw.githubusercontent.com/vtramo/NaTour/042ceae34cd5618f56cf340d8278eece9ab82e5e/images/Group%2011%20(2).svg"/>
</p>

<H2>What is NaTour?</H2>
NaTour is a complex and distributed system aimed at offering a modern social network to hiking lovers. The system consists of a secure, high-performance and scalable back-end (built with Spring Boot and cloud-based technologies) and a mobile client (Android) through which users can take advantage of the system's features in an intuitive, fast and pleasant way.

<H2>What are the system's functionalities?</H2>

<ul>
  <li>
    A user can register/authenticate (even with third-party authentication e.g. Google and Facebook). This functionality was realized using <a    href="https://www.keycloak.org/">Keycloak</a> (Open Source Identity and Access Management).
    <br><br>
    <img src="https://github.com/vtramo/NaTour/blob/main/images/login.gif?raw=true" height="500"/>
    <img src="https://github.com/vtramo/NaTour/blob/main/images/register.jpeg?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    An authenticated user can view all trails uploaded to the system.
    <br><br>
    <img src="https://github.com/vtramo/NaTour/blob/main/images/home.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    An authenticated user can enter new trails into the platform. A trail is characterized by a name, a duration, a level of difficulty, a starting point, a description (optional), and a geographic trail (optional) that represents it on a map. The geographic trail must be either manually entered (by interacting with an interactive map) or via file in standard GPX format.
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/route_creation.gif?raw=true" height="500"/>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/route_creation_gpx.gif?raw=true" height="500"/>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/route_creation_tracking_map.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    It is possible to display a detail screen for each trail. This screen shows all known information about the trail and displays its route on an interactive map. In addition, the detail screen shows user reviews and uploaded photos. The user can also show their location (if the mobile device supports GPS location) on the map.
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_details.gif?raw=true" height="500"/>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_details_gps.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    A user can download the trail in GPX format, or the summary information in PDF format.
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_download_gpx.gif?raw=true" height="500"/>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_download_pdf.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    A user can add or remove trails from their list of favorites.
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_favorites.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    A user can upload photos of a trail. If the photo has a geographic location saved in the metadata, a marker corresponding to the photo can be displayed on the map to show where it was taken on the trail. All photographs, before being uploaded, are examined to verify the presence or absence of illegal content (this feature was created using the <a href="https://docs.aws.amazon.com/rekognition/latest/dg/what-is.html">AWS Rekognition</a> cloud service).
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_photo_gps.gif?raw=true" height="500"/>
  </li>
  
  <br>
  
  <li>
    A user can add a review to a trail.
    <br><br>
    <image src="https://github.com/vtramo/NaTour/blob/main/images/trail_add_review.gif?raw=true" height="500"/>
  </li>
  
  <br>
</ul>

<H2>Swagger doc</H2>
https://app.swaggerhub.com/apis/vtramo/NaTourAPI/v0
