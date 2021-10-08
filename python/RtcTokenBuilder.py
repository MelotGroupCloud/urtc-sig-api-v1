#! /usr/bin/python
# coding:utf-8


import hmac
import hashlib
import base64
import zlib
import json
import time
import random


def base64_encode_url(data):
  """ base url encode 实现"""
  base64_data = base64.b64encode(data)
  base64_data_str = bytes.decode(base64_data)
  base64_data_str = base64_data_str.replace('+', '*')
  base64_data_str = base64_data_str.replace('/', '-')
  base64_data_str = base64_data_str.replace('=', '_')
  return base64_data_str


def base64_decode_url(base64_data):
  """ base url decode 实现"""
  base64_data_str = bytes.decode(base64_data)
  base64_data_str = base64_data_str.replace('*', '+')
  base64_data_str = base64_data_str.replace('-', '/')
  base64_data_str = base64_data_str.replace('_', '=')
  raw_data = base64.b64decode(base64_data_str)
  return raw_data


class RtcTokenBuilder:
  __appId = ""
  __secret = ""

  def __init__(self, appId, secret):
    self.__appId = appId
    self.__secret = secret

  def __hmacsha256(self, roomId, userId, nonce, currTime, expire):
    """ 通过固定串进行 hmac 然后 base64 得的 sig 字段的值"""
    raw_content_to_be_signed = self.__appId + ":" + roomId + ":" + userId + ":" + nonce + ":" + str(currTime) + ":" + str(expire)
    return base64.b64encode(hmac.new(self.__secret.encode('utf-8'),
                                      raw_content_to_be_signed.encode('utf-8'),
                                      hashlib.sha256).digest())

  def buildToken(self, roomId, userId, expire):
    """ 用户可以采用默认的有效期生成 sig """
    nonce = str(random.random() * 1000000)
    curr_time = int(time.time())
    m = dict()
    m["ver"] = "1.0"
    m["nonce"] = nonce
    m["time"] = curr_time
    m["expire"] = int(expire)
    m["signature"] = bytes.decode(self.__hmacsha256(roomId, userId, nonce, curr_time, expire))

    raw_sig = json.dumps(m)
    sig_cmpressed = zlib.compress(raw_sig.encode('utf-8'))
    base64_sig = base64_encode_url(sig_cmpressed)
    return base64_sig