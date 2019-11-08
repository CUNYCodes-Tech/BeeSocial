# Documentation for routes

## Notes:
- Sometime the port will be at 3000 if you are runnign it locally
___
## Contents:
1. [Signup](#1)
1. [Login](#2)
1. [Get user's profile](#3)
1. [Update user's profile](#4)
1. [Post a new event](#5)
1. [Get event by date](#6)
1. [Get event by name](#7)
1. [Get event by location](#8)
1. [Adding user into interest list](#9)
1. [Remove user from interest list](#10)
1. [Adding user from interest to participant list](#11)
1. [Get list of user himself posted events](#12)
___
###### 1
# 1. Signup
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
###### 2
# 2. Login
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

###### 3
# 3. Get the user's Profile
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

###### 4
# 4. Update the user's profile
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

###### 5
# 5. Post a new event
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

###### 6
# 6. Get lists of event by date
- get
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
___
###### 7
# 7. Get list of events by name
- using name
> http://localhost:8888/api/events/name?name=latest
```json
//return
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
___
###### 8
# 8. Get list of event by location
- searching an event by using location
> http://localhost:8888/api/events?longitude=40.7128&latitude=73.9712
___
###### 9
# 9. Add user into interest list
- put
- need the event id
> http://localhost:3000/api/events/addInterest/5da5491f251bf4e2badc156c

```json
// return the updated event
{
    "interested": [
        "5da4b18d221fa7c963ac901c"
    ],
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
    "updatedAt": "2019-11-04T02:34:31.721Z",
    "__v": 0
}
```
___
###### 10
# 10. Remove user from interest list
- put
- withdraw user from the interest list of the event
- this is done by the user himself
- need the event's id in the route
> http://localhost:3000/api/events/removeInterest/5da5491f251bf4e2badc156c
```json
// return the updated event as well
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
    "updatedAt": "2019-11-04T02:35:49.119Z",
    "__v": 0
}
```
___
###### 11
# 11. Adding user from interest to participant list
- put
- move a person from interest list to the participant list
- taking event's id as the route param
- the person's id that you want to add in the body
> http://localhost:3000/api/events/invite/5da5491f251bf4e2badc156c
```json
{
	"person": "5da4b18d221fa7c963ac901c"
}
```
___
###### 12
# 12. Get list of user himself posted events
- get
- to get all the event posted by myself
> http://localhost:3000/api/events/mine
### Example
- assume this is the person, we see that he has posted two events
```json
{
    "firstname": "changefirst",
    "lastname": "changelast",
    "sex": "M",
    "description": "blablablabla",
    "postedEvent": [
        "5dc5dfbf897f757640bc1089",
        "5dc5e0555bdbda76761a2724"
    ],
    "_id": "5da4b18d221fa7c963ac901c",
    "username": "test1@gmail.com",
    "__v": 2
}
```
- calling this route will return
```json
{
    "firstname": "changefirst",
    "lastname": "changelast",
    "sex": "M",
    "description": "blablablabla",
    "postedEvent": [
        {
            "interested": [],
            "participant": [],
            "time": "2019-12-17T08:24:00.000Z",
            "description": "Lets go for dinner!!",
            "closed": false,
            "_id": "5dc5dfbf897f757640bc1089",
            "name": "Dinner at Manhattan",
            "createdBy": "5da4b18d221fa7c963ac901c",
            "location": {
                "coordinates": [
                    40.7128,
                    73.9712
                ],
                "_id": "5dc5dfbf897f757640bc108a",
                "type": "Point"
            },
            "createdAt": "2019-11-08T21:35:59.345Z",
            "updatedAt": "2019-11-08T21:35:59.345Z",
            "__v": 0
        },
        {
            "interested": [],
            "participant": [],
            "time": "2019-12-17T08:24:00.000Z",
            "description": "Lets go for dinner!!",
            "closed": false,
            "_id": "5dc5e0555bdbda76761a2724",
            "name": "Dinner at Manhattan",
            "location": {
                "coordinates": [
                    40.7128,
                    73.9712
                ],
                "_id": "5dc5e0555bdbda76761a2725",
                "type": "Point"
            },
            "createdBy": "5da4b18d221fa7c963ac901c",
            "createdAt": "2019-11-08T21:38:29.599Z",
            "updatedAt": "2019-11-08T21:38:29.599Z",
            "__v": 0
        }
    ],
    "_id": "5da4b18d221fa7c963ac901c",
    "username": "test1@gmail.com",
    "__v": 2
}
```