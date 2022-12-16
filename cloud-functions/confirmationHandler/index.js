const {PubSub} = require('@google-cloud/pubsub');
const nodemailer = require('nodemailer');

async function confirmationHandler(projectId = 'smiling-topic-371018',
                                   topicName = 'confirmation-topic',
                                   subscriptionName = 'confirmation-sub') {
    const pubsub = new PubSub();

    const [topic] = await pubsub.createTopic(topicName);
    const [subscription] = await topic.createSubscription(subscriptionName);

    subscription.on('message', message => {
        const emailData = message.data;
        sendConfirmationEmail(emailData);
    });

    subscription.on('error', error => {
        console.error('Received error: ', error);
    });
}

function sendConfirmationEmail(emailData) {
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: '',
            pass: ''
        }
    });

    const emailOptions = {
        from: 'gcp.proj.email@gmail.com',
        to: emailData.address,
        subject: 'Creation of new ToDo item: ' + emailData.title,
        text: emailData.content
    };

    transporter.sendMail(emailOptions, (error, info) => {
        if (error) {
            console.error('Could not send email: ' + error);
        } else {
            console.log('Email sent');
        }
    });
}

module.exports.confirmirmationHandler = confirmationHandler;