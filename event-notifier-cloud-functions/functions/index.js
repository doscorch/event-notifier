const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();
exports.notifyNewMessage = functions.firestore
    .document('eventInstances/{eventInstance}')
    .onCreate((docSnapshot, context) => {
        const message = docSnapshot.data();
        const name = message['name'];
        const details = message['details'];
        const payload = {
            notification: {
                title: name + " occured",
                body: details,
                clickAction: "NotificationHandler"
            },
            data: {
                name: message['name'],
                details: message['details'],
                date: message['date'].toString()
            }
        }
        return admin.firestore().collection('events').where('name', '==', name).get().then(eventsSnapshot => {
            if (!eventsSnapshot.empty) {
                if (eventsSnapshot.docs[0].get('subscribed') === true) {
                    return admin.firestore().collection('users').get().then(snapshot => {
                        if (!snapshot.empty) {
                            const registrationTokens = [];
                            snapshot.forEach(doc => registrationTokens.push(doc.get('registrationToken')))
                            return admin.messaging().sendToDevice(registrationTokens, payload)
                        }
                        return;
                    })
                }
                return;
            }
            return;
        })
    })