## 使用

```javascript
const RtcTokenBuilder = require('./RtcTokenBuilder');

const builder = new RtcTokenBuilder('your appId', 'your secret');

console.log(builder.buildToken('roomId', 'userId', 300));
```