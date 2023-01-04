const supertest = require('supertest');
const functionsFramework = require('@google-cloud/functions-framework/testing')

require('../index');

describe('function_cloudevent_pubsub', () => {
    it('should process a PubSub event', async () => {
        const pubsubEventData = {data: {message: {}}};

        const testEmail = process.env.MAIL_USERNAME_TEST;
        const testTitle = 'Umyć okna event created;';
        const testContent = 'The new todo item \"Umyć okna\" is created. It\'s due date is set to 22.12.2022.';
        const testMessage = testEmail + testTitle + testContent;
        pubsubEventData.data.message = {
            data: Buffer.from(testMessage).toString('base64'),
        };

        const server = functionsFramework.getTestServer('confirmationHandler');
        await supertest(server)
            .post('/')
            .send(pubsubEventData)
            .set('Content-Type', 'application/json')
            .expect(204);
    });
});