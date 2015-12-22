#!/usr/bin/env bash
ab -p bidreq.json -T application/json -c 10 -n 2000 http://localhost:9090/bidrequest
ab -p winnotice.json -T application/json -c 10 -n 2000 http://localhost:9090/winnotice

#json content: {"body":
# {"id":"req-0",
#  "imp":[{"id":"imp-0","bidfloor":20,"bidfloorcur":"JPY"}],
#   "site":{"id":"GZCVlVEklesTBofbJXgXAqDZQ.jp",
#   "name":"VwTKwNYArwoi",
#   "page":"http://GZCVlVEklesTBofbJXgXAqDZQ.jp/umhFBGeiPHocWSMBdf"},
#   "device":{"ua":"iOS","devicetype":1},
#   "user":{"id":"user_1"},
#   "test":1,
#   "at":2,
#   "tmax":1000
#  },
#"result":[false,false,false,false,false]
#}
