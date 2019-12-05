var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var passportLocalMongoose = require('passport-local-mongoose');


var User = new Schema({
    firstname: {
        type: String,
        default: 'Please add your first name'
    },
    lastname: {
        type: String,
        default: 'Please add your last name'
    },
    favoriteFood: {
      type: String,
      default: 'Please add your favorite food'
    },
    age: {
      type: Number
    },
    sex: {
      type: String,
      default: "Please add your sex"
    },
    description: {
      type: String,
      default: "Please give a short description about yourself"
    },
    postedEvent: [{
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Event'
    }]
});
User.plugin(passportLocalMongoose)

module.exports = mongoose.model('User', User);