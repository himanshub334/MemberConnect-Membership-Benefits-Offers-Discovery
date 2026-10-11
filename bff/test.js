import test from 'node:test';
import assert from 'node:assert/strict';
test('demo token is not a production JWT', () => {
  const token = process.env.DEMO_BEARER_TOKEN || 'local-demo-token';
  assert.equal(token, 'local-demo-token');
  assert.equal(token.split('.').length, 1);
});
