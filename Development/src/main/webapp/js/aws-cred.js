const BUCKET_NAME = 'cs5500-chat-app-prod';
const BUCKET_REGION = 'us-east-2';
const IDENTITY_POOL_ID = 'us-east-2:edaa21be-e6a9-4c5f-91b4-5e2747b6a54d';

AWS.config.update({
  region: BUCKET_REGION,
  credentials: new AWS.CognitoIdentityCredentials({
  IdentityPoolId: IDENTITY_POOL_ID
  })
});

var s3 = new AWS.S3({
  apiVersion: '2006-03-01',
  params: {Bucket: BUCKET_NAME}
});