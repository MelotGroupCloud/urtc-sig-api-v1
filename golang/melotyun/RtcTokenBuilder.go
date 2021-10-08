package melotyun

import (
	"bytes"
	"compress/zlib"
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"math/rand"
	"strconv"
	"time"
)

func BuildToken(appId string, secret string, roomId string, userId string, expire int) (string, error) {
	nonceInt := rand.Int()
	nonce := strconv.Itoa(nonceInt)
	currTime := time.Now().Unix()
	var sigDoc map[string]interface{}
	sigDoc = make(map[string]interface{})
	sigDoc["ver"] = "1.0"
	sigDoc["nonce"] = nonce
	sigDoc["time"] = currTime
	sigDoc["expire"] = expire
	sigDoc["signature"] = hmacsha256(appId, secret, roomId, userId, nonce, currTime, expire)

	data, err := json.Marshal(sigDoc)
	if err != nil {
		return "", err
	}

	var b bytes.Buffer
	w := zlib.NewWriter(&b)
	w.Write(data)
	w.Close()
	return base64urlEncode(b.Bytes()), nil
}

func hmacsha256(appId string, secret string, roomId string, userId string, nonce string, currTime int64, expire int) string {
	var contentToBeSigned string
	contentToBeSigned = appId + ":" + roomId + ":" + userId + ":" + nonce + ":" + strconv.FormatInt(currTime, 10) + ":" + strconv.Itoa(expire)

	h := hmac.New(sha256.New, []byte(secret))
	h.Write([]byte(contentToBeSigned))
	return base64.StdEncoding.EncodeToString(h.Sum(nil))
}