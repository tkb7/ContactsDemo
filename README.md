The app is works using several api such as Google Place Picker, Google Map View and Firebase

The Flow of app is as below:

SIGN IN ACTIVITY
            Does the job of authentication of user
            For right now, oonly google authentication is implemented. Other sign in methods including sign in using mail etc.
                
HOME PAGE: 
            This activity has two buttons that redirects user to add new contact or to see his own list of contacts
            
CONTACTS LIST:
            This activity loads the data from firebase and displays it on the screen
            Each data has onItemClickListener by which we can edit/delete a contact or can see the added location of user in the map view

ADD CONTACT:
            User can add contacts to database using this activity
            There is functionality of place picker from where user can pick place he/she wants to save
            
CONTACT DETAILS:
            This activity is launched when user taps any existing contact from the contact list.
            User can see edit or delete the contact from here
            User can watch the location saved in the contact in the MapView
            
            
The functionality of the each class is written in the comments in the classes.

Database:
          I have used firebase realtime database for this project.
          The format of the data stored is as follows:
              root
                -users:
                    -<any user's unique id>
                        -contacts
                            -<unique contact id>
                                - "name" : <saved name>
                                - "number" : <saved number>
                                - "latitude" : <latitude>
                                - "longitude" : <longitude>
                          
                            -<unique contact id>
                            -<unique contact id>
                            .
                            .
                            .
                            -<unique contact id>
                    -<any user's unique id>
                    .
                    .
                    .
                    -<any user's unique id>

This implementation becomes easy for data access.
Since all the contacts of one user are stored under his unique id, no other user can access it
Also, retrieval becomes easy since we can easily locate any user with uid.

Scope of Improvement:
- UI of the app is primitive. It can be changed to become more user friendly
- For now, only google authentication is used. More authentication modes can be added
