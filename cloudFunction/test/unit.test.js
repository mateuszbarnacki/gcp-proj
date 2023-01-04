const {getFunction} = require('@google-cloud/functions-framework/testing');
require('../index');

describe('functions_cloudevent_pubsub', () => {
    const assert = require('assert');
    const uuid = require('uuid');
    const sinon = require('sinon');

    const stubConsole = () => {
        sinon.stub(console, 'error');
        sinon.stub(console, 'log');
    };

    const restoreConsole = () => {
        console.log.restore();
        console.error.restore();
    };

    beforeEach(stubConsole);
    afterEach(restoreConsole);

    it('should print confirmation log before sending email message', () => {
        const cloudEvent = {data: {message: {}}};

        const testEmail = uuid.v4() + ';';
        const testTitle = uuid.v4() + ';';
        const testContent = uuid.v4();
        const testMessage = testEmail + testTitle + testContent;
        cloudEvent.data.message = {
            data: Buffer.from(testMessage).toString('base64'),
        };

        const confirmationHandler = getFunction('confirmationHandler');
        confirmationHandler(cloudEvent);
        assert.ok(console.log.calledWith('Sending email message'));
    });

    it('should print error log', () => {
        const cloudEvent = {data: {message: {}}};

        const confirmationHandler = getFunction('confirmationHandler');
        confirmationHandler(cloudEvent);
        assert.ok(console.error.calledWith('Error while receiving data from PubSub'));
    });
});