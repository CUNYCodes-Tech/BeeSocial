const express = require('express');
const eventRouter = express.Router();
const bodyParser = require('body-parser');
eventRouter.use(bodyParser.json());
var User = require('../models/user');
var Event = require('../models/event');
var authenticate = require('../middleware/auth');
var jwt = require('jsonwebtoken');


// find all the event by location
eventRouter.get('/events/location', (req, res, next) => {
    // get the user location from the query param
    // find all the events within that location
    const long = req.query.longitude;
    const lati = req.query.latitude;
    const minD = req.query.minDistance;
    const maxD = req.query.maxDistance;
    const d = new Date();
    Event.find({
        location: {
            $near: {
                $geometry: { type: "Point", coordinates: [long, lati] },
                $minDistance: minD,
                $maxDistance: maxD
            }
        },
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// find all event by name
eventRouter.get('/events/name', (req, res, next) => {
    const name = req.query.name;
    const d = new Date();
    Event.find({
        name: {
            $regex: name,
            $options: 'i'
        },
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// find event by date
eventRouter.get('/events', (req, res, next) => {
    // get the user location from the query param
    // find all the events within that location
    const d = req.query.date ? new Date(req.query.date) : new Date()
    Event.find({
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// create and event using post
eventRouter.post('/events', authenticate.verifyUser, (req, res, next) => {
    let newEvent = req.body;
    // newEvent.time = new Date(req.body.time);
    Event.create(newEvent)
        .then((event) => {
            //console.log(event);
            User.findById(event.createdBy)
                .then((user) => {
                    console.log(event._id);
                    user.postedEvent.push(event._id);
                    return user.save()
                })
                .then((user) => {
                    console.log(user);
                    res.statusCode = 200;
                    res.setHeader('Content-Type', 'application/json');
                    res.json(user);
                })
                .catch((err) => {
                    res.statusCode = 500;
                    res.setHeader('Content-Type', 'application/json');
                    res.json({ err: err });
                })
        })
        // .then((user) => {
        //     console.log(event._id);
        //     user.postedEvent.push(event._id);
        //     return user.save()
        // })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })
});

// update event
eventRouter.put('/events/update/:eventId', authenticate.verifyUser, (req, res, next) => {
    // get the neccessary information and update
    const id = req.params.eventId;
    const update = req.body;
    if (update.hasOwnProperty('_id')) {
        // cannot update the id
        res.statusCode = 500;
        res.setHeader('Content-Type', 'application/json');
        res.json({ err: { "status": 500, "success": false, "message": "Cannot update the id" } });
    }
    if (update.hasOwnProperty('createdBy')) {
        // cannot update the author
        res.statusCode = 500;
        res.setHeader('Content-Type', 'application/json');
        res.json({ err: { "status": 500, "success": false, "message": "Cannot update the author" } });
    }
    if (update.hasOwnProperty('interested')) {
        // cannot update person who is interest
        res.statusCode = 500;
        res.setHeader('Content-Type', 'application/json');
        res.json({ err: { "status": 500, "success": false, "message": "Cannot update the interested person" } });
    }
    Event.findByIdAndUpdate(id, update, { new: true })
        .then((event) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(event);
        }).catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        });
});

// show interest event
eventRouter.put('/events/addInterest/:eventId', authenticate.verifyUser, (req, res, next) => {
    // find the event, push the userid
    var currentUser = req.headers;
    const token = currentUser.authorization.split(" ")[1];
    let userId = jwt.decode(token)._id;
    const eventId = req.params.eventId;
    Event.findById(eventId)
        .then((event) => {
            let interested = event.interested;
            let newInterested = [];
            let exist = false;
            for (let i = 0; i < interested.length; i++) {
                if (interested[i] === userId) {
                    exist = true;
                    // send respond
                    res.statusCode = 500;
                    res.setHeader('Content-Type', 'application/json');
                    res.json({ "status": 500, "success": false, "message": "You are already in the list" });
                }else {
                    newInterested.push(interested[i]);
                }
            }
            if(!exist) {
                newInterested.push(userId);
            }
            const update = {
                interested: newInterested
            }
            return Event.findByIdAndUpdate(eventId, update, {new: true});
        }).then((event) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(event);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })
});
// withdraw interest event
eventRouter.put('/events/removeInterest/:eventId', authenticate.verifyUser, (req, res, next) => {
    // find the event update the interest
    let currentUser = req.headers;
    const token = currentUser.authorization.split(" ")[1];
    let userId = jwt.decode(token)._id;
    const eventId = req.params.eventId;
    Event.findById(eventId)
        .then((event) => {
            let interested = event.interested;
            let newInterested = [];
            let removed = false;
            for (let i = 0; i < interested.length; i++) {
                if (interested[i] == userId) {
                    removed = true;
                }else {
                    newInterested.push(interested[i]);
                }
            }
            if(!removed) {
                // send respond
                res.statusCode = 500;
                res.setHeader('Content-Type', 'application/json');
                res.json({ "status": 500, "success": false, "message": "You are not even on the list" });
            }
            const update = {
                interested: newInterested
            }
            return Event.findByIdAndUpdate(eventId, update, {new: true});
        }).then((event) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(event);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })
});


// delete event
eventRouter.delete('/events', authenticate.verifyUser, (req, res, next) => {
    Event.findByIdAndRemove(req.body.eventId)
        .then((event) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(event);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});


module.exports = eventRouter;