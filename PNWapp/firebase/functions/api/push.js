const functions = require('firebase-functions');
const admin = require('firebase-admin');

console.log('memes');

module.exports = functions.firestore
    .document('alerts/{newAlertId}')
    .onCreate((change, context) => {
        // The topic name can be optionally prefixed with "/topics/".
        console.log("change, context");
        console.log(change, context);
        var data = change.data();
        console.log("data");
        console.log(data);

        var message = {
            data: {
                score: '850',
                time: '2:45'
            },
            topic: data.header
        };

        console.log("before message send");

        // Send a message to devices subscribed to the provided topic.
        admin.messaging().send(message)
            .then((response) => {
                // Response is a message ID string.

                console.log('Successfully sent message:', response);
                return response; //return something idk
            })
            .catch((error) => {
                console.log('Error sending message:', error);
            });

    });