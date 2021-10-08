## 使用

```python
#! /usr/bin/python
# coding:utf-8

from RtcTokenBuilder import RtcTokenBuilder

def main():
  api = RtcTokenBuilder("your appId", 'your app secret')
  sig = api.buildToken("roomId", "userId", 300)
  print(sig)

if __name__ == "__main__":
  main()
```