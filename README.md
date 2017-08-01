# QuickCover
Buisness scheduling app made for CUNY Hackathon 2017!

## Team Members 
* Frank Tancredi
* Kevin Alvarez
* Nikita Dmitriev
* Hector Liang Chan
* Alexander Hart

## What it does
Quick Cover allows A "boss" designated user to post a weekly schedule to the workers in a server. Each shift a worker has for the week can be sent to the database and distributed to other users as a "CoverAlert", allowing the first user who accepts the shift to be given it in a newly revised version of the schedule.

## Screenshots
<img src="https://raw.githubusercontent.com/AlvarezKevin/QuickCover/master/Screenshots/QuickCoverMainSS.png" alt="Sign in" width=200 height=400/><img src="https://raw.githubusercontent.com/AlvarezKevin/QuickCover/master/Screenshots/ScheduleCalendar.png" alt="View Schedule" width=200 height=400/><img src="https://raw.githubusercontent.com/AlvarezKevin/QuickCover/master/Screenshots/MoreInfoScreenshot.png" alt="More Information" width=200 height=400/><img src="https://raw.githubusercontent.com/AlvarezKevin/QuickCover/master/Screenshots/AppealScreenshot.png" alt="Appeal Requests" width=200 height=400/>

## Challenges we ran into
Originally we used Node-RED and a NoSQL database called Cloudant to store and retrieve data for users. We soon discovered, however, that the precise way we handled our constantly changing data did not lend itself well to the rigid way JSON documents are edited in Cloudant. This eventually led us to scrap Node-Red and Cloudant and host our data on an SQL database instead provided by a non-profit site.

## Accomplishments that we're proud of
When Node-RED and Cloudant weren't working it was tempting for us to continue trying to force the data to work with us through increasingly convoluted methods. We're proud that instead submitting to our sunk cost and continuing something that wasn't working, we switched up our ideas and were able to get a SQL server up and running quickly and start meaningful work again instead of wasting the night at a standstill.

## What we learned
While most of the team had prior Java experience, none of us had ever posted or gotten data from a database using http request. A few members of the team had also never worked with JSON data before. Now at the closing of Hackathon, we can confidently incorporate those elements into future projects with no fear and a good understanding of how to use them.
