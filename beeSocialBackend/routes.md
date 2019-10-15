# Documentation for routes
## Contents:
- [signup](##signup)
- [login](##login)
- [get user's profile](##getprofile)
- [update user's profile](##updateprofile)
- [post a new event](##postevent)
- [get event by date](##geteventdate)
___

## signup
- post
- for signup
> http://localhost:8888/api/users/signup
```json
{
	"username": "test1@gmail.com",
	"password": "test1",
	"firstname": "testfirst", //optional
	"lastname": "testlast" //optional
}
// return
{
    "success": true,
    "status": "Registration Successful!"
}
```
___
## login
- post
- for login
> http://localhost:8888/api/users/login
```json
{
	"username": "test1@gmail.com",  // must be email format
	"password": "test1"
}
//return
{
    "success": true,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZGE0YjE4ZDIyMWZhN2M5NjNhYzkwMWMiLCJpYXQiOjE1NzEwNzQ2MTIsImV4cCI6MTU3MTA3ODIxMn0.stHQPcoexgViieuuwgnjQ44Y9ZAUL9M3tSJ7ay7ZVmk",
    "status": "You are successfully logged in!",
    "id": "5da4b18d221fa7c963ac901c"
}
```
___

## getprofile
- get
- get profile using userId
> http://localhost:8888/api/profile/:userId

```json
// return
{
    "firstname": "testfirst", 
    "lastname": "testlast",
    "sex": "",
    "description": "",
    "postedEvent": [],
    "_id": "5da4b18d221fa7c963ac901c",
    "username": "test1@gmail.com",
    "__v": 0
}
```
___

## updateprofile
- put
- update profile information
> http://localhost:8888/api/profile/:userId
```json
{
	"firstname": "changefirst", // must be alphabet
	"lastname": "changelast",   // must be alphabet
	"sex": "M",                 // optional
	"description": "blablablabla" //optional
}
// return
{
    "firstname": "changefirst",
    "lastname": "changelast",
    "sex": "M",
    "description": "blablablabla",
    "postedEvent": [],
    "_id": "5da4b18d221fa7c963ac901c",
    "username": "test1@gmail.com",
    "__v": 0
}
```
___

## postevent
- post
- post a new event
> http://localhost:8888/api/events
- must logged in
```json
{
	"name": "Dinner at Manhattan", // required
	"createdBy": "5da4bbfee68b2dd22ef5630c",  
	"location": {               // required
		"type": "Point",
		"coordinates": [40.7128, 73.9712]    // longitude, latitude
	},
	"time": "December 17, 1995 03:24:00",  // optional
	"description": "final event"            // optional
}

// return the user profile
{
    "firstname": "testfirst",
    "lastname": "testlast",
    "sex": "",
    "description": "",
    "postedEvent": [
        "5da4bef2635654d5c48790d0",  // list of event that he/she created
        "5da4bf55a57bb7d60ae7f8be",
        "5da4bfb69b8a94d65d0c2f0e"
    ],
    "_id": "5da4bbfee68b2dd22ef5630c",
    "username": "test2@gmail.com",
    "__v": 3
}
```
___

## geteventdate
- using date
- specifying the date in the query param
- return all the events end at later date
- date is optional, if without date, will return the events end at later than current
> http://localhost:8888/api/events?date=2019-10-10
```json
// return
[
    {
        "interested": [],
        "time": "2019-12-17T08:24:00.000Z",
        "description": "final event",
        "closed": false,
        "_id": "5da5491f251bf4e2badc156c",
        "name": "latest event",
        "createdBy": "5da4bbfee68b2dd22ef5630c",
        "location": {
            "coordinates": [
                40.7128,
                73.9712
            ],
            "_id": "5da5491f251bf4e2badc156d",
            "type": "Point"
        },
        "createdAt": "2019-10-15T04:20:47.757Z",
        "updatedAt": "2019-10-15T04:20:47.757Z",
        "__v": 0
    }
]
```