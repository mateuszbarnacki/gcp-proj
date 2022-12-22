const functions = require('@google-cloud/functions-framework')
const nodemailer = require('nodemailer');
require('dotenv').config();

functions.cloudEvent('confirmationHandler', cloudEvent => {
    const base64Message = cloudEvent.data.message.data;

    if (base64Message) {
        const emailData = Buffer.from(base64Message, 'base64').toString().split(';');
        console.log('Sending email message');
        sendConfirmationEmail(emailData);
    } else {
        console.error('Error while receiving data from PubSub');
    }
});

function sendConfirmationEmail(emailData) {
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            type: 'OAuth2',
            user: process.env.MAIL_USERNAME,
            pass: process.env.MAIL_PASSWORD,
            clientId: process.env.OAUTH_CLIENTID,
            clientSecret: process.env.OAUTH_CLIENT_SECRET,
            refreshToken: process.env.OAUTH_REFRESH_TOKEN,
            accessToken: process.env.OAUTH_ACRESS_TOKEN
        }
    });

    const emailOptions = {
        from: process.env.MAIL_USERNAME,
        to: emailData[0],
        subject: 'Creation of new ToDo item: ' + emailData[1],
        text: emailData[2]
    };

    transporter.sendMail(emailOptions, function(error, data) {
        if (error) {
            console.error('Could not send email: ' + error);
        } else {
            console.log('Email sent');
        }
    });
}