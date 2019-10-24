var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var passportLocalMongoose = require('passport-local-mongoose');


var User = new Schema({
    firstname: {
        type: String,
        default: ''
    },
    lastname: {
        type: String,
        default: ''
    },
    age: {
      type: Number
    },
    sex: {
      type: String,
      default: ""
    },
    description: {
      type: String,
      default: ""
    },
    postedEvent: [{
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Event'
    }]
});
User.plugin(passportLocalMongoose)

module.exports = mongoose.model('User', User);