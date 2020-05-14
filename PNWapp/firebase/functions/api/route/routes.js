const firebase = require("firebase");
const functions = require('firebase-functions');

const admin = require('firebase-admin');
const express = require('express');
const cors = require('cors');
const app = express();

// Automatically allow cross-origin requests
app.use(cors({
    origin: true
}));

admin.initializeApp();
const db = admin.firestore();
const FieldPath = admin.firestore.FieldPath;

// app.get('/alert/byCategory/:category', (request, response) => {

//             app.get('/alert/byUser/:email', async (request, response) => {

//                             app.post("/user/", (request, response) => {

//                                         app.put("/user/:email", (request, response) => {

//                                                     app.get("/user/subscriptions/:email", (request, response) => {

// Expose Express API as a single Cloud Function:
module.exports = functions.https.onRequest(app);