const functions = require('@google-cloud/functions-framework')
const nodemailer = require('nodemailer');
const {SecretManagerServiceClient} = require('@google-cloud/secret-manager');
require('dotenv').config();

const SECRET = {
    MAIL_USERNAME: 'mail-username',
    MAIL_PASSWORD: 'mail-password',
    OAUTH_CLIENTID: 'oauth-client-id',
    OAUTH_CLIENT_SECRET: 'oauth-client-secret',
    OAUTH_REFRESH_TOKEN: 'oauth-refresh-token',
    OAUTH_ACCESS_TOKEN: 'oauth-access-token',
}

const buildSecretName = keyName => {
    const project = process.env.APP_PROJECT_ID;
    return `projects/${project}/secrets/${keyName}/versions/latest`;
};

const accessSecret = async keyName => {
    const client = new SecretManagerServiceClient();
    const name = buildSecretName(keyName);

    const [version] = await client.accessSecretVersion({
        name,
    });

    return version.payload.data.toString();
};

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
    const user = accessSecret(SECRET.MAIL_USERNAME);
    const pass = accessSecret(SECRET.MAIL_PASSWORD);
    const clientId = accessSecret(SECRET.OAUTH_CLIENTID);
    const clientSecret = accessSecret(SECRET.OAUTH_CLIENT_SECRET);
    const refreshToken = accessSecret(SECRET.OAUTH_REFRESH_TOKEN);
    const accessToken = accessSecret(SECRET.OAUTH_ACCESS_TOKEN);

    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            type: 'OAuth2',
            user: user,//process.env.APP_MAIL_USERNAME,
            pass: pass,//process.env.APP_MAIL_PASSWORD,
            clientId: clientId,//process.env.APP_OAUTH_CLIENTID,
            clientSecret: clientSecret,//process.env.APP_OAUTH_CLIENT_SECRET,
            refreshToken: refreshToken,//process.env.APP_OAUTH_REFRESH_TOKEN,
            accessToken: accessToken//process.env.APP_OAUTH_ACCESS_TOKEN
        }
    });

    const emailOptions = {
        from: user,//process.env.APP_MAIL_USERNAME,
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