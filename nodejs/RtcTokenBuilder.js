'use strict'

const zlib = require('zlib');
const crypto = require('crypto');

const Base64URL = require('./Base64URL');

class RtcTokenBuilder {

  constructor(appId, secret) {
    this.appId = appId;
    this.secret = secret;
  }

  buildToken(roomId, userId, expire) {
    const time = Date.now() / 1000 | 0;
    const nonce = getNonce(5);
    const signDoc = {
      ver: '1.0',
      nonce,
      time,
      expire
    }

    const signature = crypto.createHmac('sha256', this.secret).update(`${this.appId}:${roomId}:${userId}:${nonce}:${time}:${expire}`).digest('base64');
    signDoc.signature = signature;
    return Base64URL.escape(zlib.deflateSync(Buffer.from(JSON.stringify(signDoc))).toString('base64'));
  }

}

module.exports = RtcTokenBuilder;

function getNonce(chars) {
  const d = []
  for (let i = 0; i < chars; i++) {
    d.push(Math.round(Math.random() * 10))
  }
  return d.join('')
}