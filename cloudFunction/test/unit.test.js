require('../index');
const assert = require('assert');
const uuid = require('uuid');
const sinon = require('sinon');

describe('functions_pubsub', () => {

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
        const testEmail = uuid.v4() + ';';
        const testTitle = uuid.v4() + ';';
        const testContent = uuid.v4();
        const testMessage = testEmail + testTitle + testContent;
        const cloudEvent = {
            data: Buffer.from(testMessage).toString('base64'),
            attributes: {}
        };

        require('..').confirmationHandler(cloudEvent);
        assert.ok(console.log.calledWith('Sending email message'));
    });
});