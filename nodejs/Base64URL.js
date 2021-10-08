'use strict'

const Base64URL = {};

Base64URL.unescape = function unescape(str) {
  return (str + Array(5 - str.length % 4))
      .replace(/_/g, '=')
      .replace(/\-/g, '/')
      .replace(/\*/g, '+');
}

Base64URL.escape = function escape(str) {
  return str.replace(/\+/g, '*')
      .replace(/\//g, '-')
      .replace(/=/g, '_');
}

Base64URL.encode = function encode(str) {
  return this.escape(Buffer.from(str).toString('base64'));
}

Base64URL.decode = function decode(str) {
  return Buffer.from(this.unescape(str), 'base64').toString();
}

module.exports = Base64URL;