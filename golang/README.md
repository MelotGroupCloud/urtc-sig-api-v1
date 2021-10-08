## 使用

```golang
package main

import (
	"golang/melotyun"
	"fmt"
)

const (
	appId = "your appId"
	secret = "your app secret"
)

func main() {
	token, err := melotyun.BuildToken(appId, secret, "roomId", "userId", 300)
	if err != nil {
		fmt.Println(err.Error())
	} else {
		fmt.Println(token)
	}
}
```