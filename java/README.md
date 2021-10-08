## 使用

```java
import com.melotyun.RtcTokenBuilder;

RtcTokenBuilder builder = new RtcTokenBuilder("your appId", "your secret");

System.out.print(builder.buildToken("roomId", "userId", 300));
```