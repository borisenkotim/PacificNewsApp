const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const cors = require('cors');
const app = express();

// Automatically allow cross-origin requests
app.use(cors({
    origin: true
}));

//in the future, we would want to split this up into multiple files to make it more flexible and scalable

admin.initializeApp();
const db = admin.firestore();

app.get('/alert/byCategory/:category', (request, response) => {
    var responseObject = [];

    db.collection("alerts")
        .orderBy("date", "asc")
        .where("category", "==", request.params.category)
        .get()
        .then((querySnapshot) => {
            querySnapshot.forEach((doc) => {
                responseObject.push(doc.data());
            });

            db.collection("alerts")
                .orderBy("date", "asc")
                .where("location", "==", request.params.category)
                .get()
                .then((querySnapshot) => {
                    querySnapshot.forEach((doc) => {
                        var data = doc.data();
                        console.log(data);
                        responseObject.push(data);
                    });
                    return response.send(responseObject);
                }).catch((error) => {
                    console.log("Error getting documents: ", error);
                });

            return responseObject;
        })
        .catch((error) => {
            console.log("Error getting documents: ", error);
        });
});

app.get('/alert/byUser/:email', async (request, response) => {
    var email = request.params.email;
    var responseObject = [];

    // object we can use to sort by the user's subscriptions
    var subscriptions = await db.collection("users")
        .where("email", "==", email).get()
        .then((querySnapshot) => {
            var subsList = [];

            querySnapshot.forEach((userObject) => {
                var data = userObject.data();
                var subs = data.subscriptions;


                subs.forEach((sub) => {
                    if (sub.priority > 0) {
                        subsList.push(sub.category);
                    }
                });
            })

            return subsList;
        }).catch((error) => {
            console.log("Error getting documents: ", error);
        })

    db.collection("alerts")
        .orderBy("date", "asc")
        .where("category", "in", subscriptions)
        .get()
        .then((querySnapshot) => {
            querySnapshot.forEach((doc) => {
                var data = doc.data();
                console.log(data);
                responseObject.push(data);
            });

            db.collection("alerts")
                .orderBy("date", "asc")
                .where("location", "in", subscriptions)
                .get()
                .then((querySnapshot) => {
                    querySnapshot.forEach((doc) => {
                        var data = doc.data();
                        console.log(data);
                        responseObject.push(data);
                    });
                    return response.send(responseObject);
                }).catch((error) => {
                    console.log("Error getting documents: ", error);
                });

            return responseObject;

        }).catch((error) => {
            console.log("Error getting documents: ", error);
        });
});

// we will need a post request route to add a new user
// put request to update what the user is subscribed to
// get request to get what the user is subscribed to

//event handler for adding a new user to the database
app.post("/user/", (request, response) => {
    var body = request.body;
    var newUser = {}; //we can cut out all the the other data besides the stuff we want

    //ghetto data validation since we only have two fields here. Not checking the information inside the subscriptions array.
    if (body.email !== undefined && body.subscriptions !== undefined) {
        newUser.email = body.email;
        newUser.subscriptions = body.subscriptions;
    } else {
        return response.send({
            error: "No email/subscriptions provided"
        });
    }

    db.collection("users")
        .where("email", "==", newUser.email).get()
        .then((querySnapshot) => {
            if (!querySnapshot.empty) { //if the response list is not empty, then that email is already in the database
                return response.send({
                    error: "email is already registered!"
                });
            } else {
                return db.collection("users").add(newUser).then((result) => {
                    return response.send(result);
                }).catch((error) => {
                    console.log("error adding new user: ", error);
                });
            }
        }).catch((error) => {
            console.log("Error getting documents: ", error);
        })
});

app.put("/user/:email", async (request, response) => {
    var body = request.body;
    var newSubs = {};
    var email = request.params.email;

    console.log(email);

    //check to make sure there is a subscriptions field for the data we will update.
    if (body.subscriptions !== undefined && email !== undefined) {
        newSubs.subscriptions = body.subscriptions;
    } else {
        return response.send({
            error: "No subscriptions provided"
        });
    }

    // var user = await 
    db.collection("users").where("email", "==", email).get().then((querySnapshot) => {
        console.log(querySnapshot.docs);
        return querySnapshot.forEach((doc) => {
            console.log("doc " + doc);
            console.log(doc);

            // doc.id
            // db.

            return db.doc("/users/" + doc.id).update(newSubs).then(() => {
                return response.send("subscriptions updated");
            }).catch((error) => {
                console.log("error updating subscriptions for user: ", error);
            });
        });
    }).catch((error) => {
        console.log("error getting key for user: ", error);
    });

    // console.log(user);

    // user.update(newSubs).then(() => {
    //     return response.send("subscriptions updated");
    // }).catch((error) => {
    //     console.log("error updating subscriptions for user: ", error);
    // });

});

app.get("/user/subscriptions/:email", (request, response) => {
    var email = request.params.email;

    db.collection("users").where("email", "==", email).get().then((querySnapshot) => {
        var responseObject = [];
        querySnapshot.forEach((doc) => {
            responseObject.push(doc.data());
        });

        return response.send(responseObject);

    }).catch((error) => {
        console.log("error getting subscriptions for user: ", error);
    });
});

// Expose Express API as a single Cloud Function:
module.exports = functions.https.onRequest(app);